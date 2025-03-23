package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import cleancode.minesweeper.tobe.cell.Cell;
import cleancode.minesweeper.tobe.cell.EmptyCell;
import cleancode.minesweeper.tobe.cell.LandMineCell;
import cleancode.minesweeper.tobe.cell.NumberCell;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.position.RelativePosition;

public class GameBoard {

	private final Cell[][] board;
	private final int landMineCount;

	// OCP 적용 - 난이도를 쉽게 변경할 수 있다.
	public GameBoard(GameLevel gameLevel) {
		int rowSize = gameLevel.getRowSize();
		int colSize = gameLevel.getColSize();
		board = new Cell[rowSize][colSize];

		landMineCount = gameLevel.getLandMineCount();
	}

	public void initializeGame() {
		int rowSize = getRowSize();
		int colSize = getColSize();

		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				board[row][col] = new EmptyCell();
			}
		}

		for (int i = 0; i < landMineCount; i++) {
			int landMineCol = new Random().nextInt(colSize);
			int landMineRow = new Random().nextInt(rowSize);
			board[landMineRow][landMineCol] = new LandMineCell();
		}

		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				CellPosition cellPosition = CellPosition.of(row, col);
				if (isLandMineCellAt(cellPosition)) {
					continue;
				}
				int count = countNearByLandMines(cellPosition);
				if (count == 0) {
					continue;
				}
				board[row][col] = new NumberCell(count);
			}
		}
	}

	public int countNearByLandMines(CellPosition cellPosition) {
		long count = calculateSurroundedPositions(cellPosition, getRowSize(), getColSize())
			.stream()
			.filter(this::isLandMineCellAt)
			.count();

		return (int) count;
	}

	public boolean isLandMineCellAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.isLandMine();
	}

	public void flagAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.flag();
	}

	public String getSign(CellPosition cellPosition) {
		return findCell(cellPosition).getSign();
	}

	private Cell findCell(CellPosition cellPosition) {
		return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
	}

	public int getRowSize() {
		return board.length;
	}

	public int getColSize() {
		return board[0].length;
	}

	public void openAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.open();
	}

	private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.hasLandMineCount();
	}

	private boolean isOpenedCell(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.isOpened();
	}

	public boolean isAllCellChecked() {
		return Arrays.stream(board)
			.flatMap(Arrays::stream) // 2중 배열 -> 배열로 변환
			.allMatch(Cell::isChecked);
	}

	public void openSurroundedCells(CellPosition cellPosition) {
		if (isOpenedCell(cellPosition)) {
			return;
		}
		if (isLandMineCellAt(cellPosition)) {
			return;
		}

		openAt(cellPosition);

		if (doesCellHaveLandMineCount(cellPosition)) {
			return;
		}

		List<CellPosition> surroundedPositions = calculateSurroundedPositions(cellPosition, getRowSize(), getColSize());
		surroundedPositions.forEach(this::openSurroundedCells);
	}

	public boolean isInvalidCellPosition(CellPosition cellPosition) {
		int rowSize = getRowSize();
		int colSize = getColSize();

		return cellPosition.isRowIndexMoreThanOrEqual(rowSize) ||
		       cellPosition.isColIndexMoreThanOrEqual(colSize);
	}

	private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowSize, int colSize) {
		return RelativePosition.SURROUNDED_POSITIONS.stream()
			.filter(cellPosition::canCalculatePositionBy)
			.map(cellPosition::calculatePositionBy)
			.filter(position -> position.isRowIndexLessThan(rowSize))
			.filter(position -> position.isColIndexLessThan(colSize))
			.toList();
	}
}
