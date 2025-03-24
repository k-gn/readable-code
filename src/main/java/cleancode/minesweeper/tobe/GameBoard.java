package cleancode.minesweeper.tobe;

import java.util.List;

import cleancode.minesweeper.tobe.cell.Cell;
import cleancode.minesweeper.tobe.cell.Cells;
import cleancode.minesweeper.tobe.cell.EmptyCell;
import cleancode.minesweeper.tobe.cell.LandMineCell;
import cleancode.minesweeper.tobe.cell.NumberCell;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.position.CellPositions;
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
		CellPositions cellPositions = CellPositions.from(board);

		initializeEmptyCells(cellPositions);

		List<CellPosition> landMineCellPositions = cellPositions.extractRandomPositions(landMineCount);
		initializeLandMineCells(landMineCellPositions);

		List<CellPosition> numberPositionsCandidates = cellPositions.subtract(landMineCellPositions);
		initializeNumberCells(numberPositionsCandidates);
	}

	private void initializeNumberCells(List<CellPosition> numberPositionsCandidates) {
		for (CellPosition candidatePosition : numberPositionsCandidates) {
			int count = countNearByLandMines(candidatePosition);
			if (count != 0) {
				updateCellAt(candidatePosition, new NumberCell(count));
			}
		}
	}

	private void initializeLandMineCells(List<CellPosition> landMineCellPositions) {
		for (CellPosition cellPosition : landMineCellPositions) {
			updateCellAt(cellPosition, new LandMineCell());
		}
	}

	private void initializeEmptyCells(CellPositions cellPositions) {
		List<CellPosition> allPositions = cellPositions.getPositions();
		for (CellPosition cellPosition : allPositions) {
			updateCellAt(cellPosition, new EmptyCell());
		}
	}

	private void updateCellAt(CellPosition cellPosition, Cell cell) {
		board[cellPosition.getRowIndex()][cellPosition.getColIndex()] = cell;
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
		Cells cells = Cells.from(board);
		return cells.isAllChecked();
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
