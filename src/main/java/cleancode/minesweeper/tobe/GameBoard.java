package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;

public class GameBoard {

	private final Cell[][] board;
	private static final int LAND_MINE_COUNT = 10;

	public GameBoard(int rowSize, int colSize) {
		board = new Cell[rowSize][colSize];
	}

	public void initializeGame() {
		int rowSize = getRowSize();
		int colSize = getColSize();

		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				board[row][col] = Cell.create();
			}
		}

		for (int i = 0; i < LAND_MINE_COUNT; i++) {
			int landMineCol = new Random().nextInt(colSize);
			int landMineRow = new Random().nextInt(rowSize);
			Cell landMineCell = findCell(landMineCol, landMineRow);
			landMineCell.turnOnLandMine();
		}

		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				if (isLandMineCell(col, row)) {
					continue;
				}
				int count = countNearByLandMines(row, col);
				Cell cell = findCell(col, row);
				cell.updateNearByLandMineCount(count);
			}
		}
	}

	public int countNearByLandMines(
		int row,
		int col
	) {
		int rowSize = getRowSize();
		int colSize = getColSize();

		int count = 0;
		if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(col - 1, row - 1)) {
			count++;
		}
		if (row - 1 >= 0 && isLandMineCell(col, row - 1)) {
			count++;
		}
		if (row - 1 >= 0 && col + 1 < colSize && isLandMineCell(col + 1, row - 1)) {
			count++;
		}
		if (col - 1 >= 0 && isLandMineCell(col - 1, row)) {
			count++;
		}
		if (col + 1 < colSize && isLandMineCell(col + 1, row)) {
			count++;
		}
		if (row + 1 < rowSize && col - 1 >= 0 && isLandMineCell(col - 1, row + 1)) {
			count++;
		}
		if (row + 1 < rowSize && isLandMineCell(col, row + 1)) {
			count++;
		}
		if (row + 1 < rowSize && col + 1 < colSize && isLandMineCell(col + 1, row + 1)) {
			count++;
		}
		return count;
	}

	public boolean isLandMineCell(
		int selectedColIndex,
		int selectedRowIndex
	) {
		Cell cell = findCell(selectedColIndex, selectedRowIndex);
		return cell.isLandMine();
	}

	public void flag(int rowIndex, int colIndex) {
		Cell cell = findCell(rowIndex, colIndex);
		cell.flag();
	}

	public String getSign(
		int rowIndex,
		int colIndex
	) {
		return findCell(colIndex, rowIndex).getSign();
	}

	private Cell findCell(
		int rowIndex,
		int colIndex
	) {
		return board[rowIndex][colIndex];
	}

	public int getRowSize() {
		return board.length;
	}

	public int getColSize() {
		return board[0].length;
	}

	public void open(
		int rowIndex,
		int colIndex
	) {
		Cell cell = findCell(rowIndex, colIndex);
		cell.open();
	}

	private boolean doesCellHaveLandMineCount(
		int row,
		int col
	) {
		Cell cell = findCell(row, col);
		return cell.hasLandMineCount();
	}

	private boolean isOpenedCell(
		int row,
		int col
	) {
		Cell cell = findCell(row, col);
		return cell.isOpened();
	}

	public boolean isAllCellChecked() {
		return Arrays.stream(board)
			.flatMap(Arrays::stream) // 2중 배열 -> 배열로 변환
			.allMatch(Cell::isChecked);
	}

	public void openSurroundedCells(int row, int col) {
		if (row < 0 || row >= getRowSize() || col < 0 || col >= getColSize()) {
			return;
		}
		if (isOpenedCell(row, col)) {
			return;
		}
		if (isLandMineCell(col, row)) {
			return;
		}

		open(row, col);

		if (doesCellHaveLandMineCount(row, col)) {
			return;
		}

		openSurroundedCells(row - 1, col - 1);
		openSurroundedCells(row - 1, col);
		openSurroundedCells(row - 1, col + 1);
		openSurroundedCells(row, col - 1);
		openSurroundedCells(row, col + 1);
		openSurroundedCells(row + 1, col - 1);
		openSurroundedCells(row + 1, col);
		openSurroundedCells(row + 1, col + 1);
	}
}
