package cleancode.minesweeper.tobe.minesweeper.board;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Stack;

import cleancode.minesweeper.tobe.minesweeper.board.cell.Cell;
import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot;
import cleancode.minesweeper.tobe.minesweeper.board.cell.Cells;
import cleancode.minesweeper.tobe.minesweeper.board.cell.EmptyCell;
import cleancode.minesweeper.tobe.minesweeper.board.cell.LandMineCell;
import cleancode.minesweeper.tobe.minesweeper.board.cell.NumberCell;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPositions;
import cleancode.minesweeper.tobe.minesweeper.board.position.RelativePosition;
import cleancode.minesweeper.tobe.minesweeper.gamelevel.GameLevel;

// 게임 진행에 대한 책임 (실질적인 지뢰찾기 도메인 로직)
public class GameBoard {

	private final Cell[][] board;
	private final int landMineCount;
	private GameStatus gameStatus;

	// OCP 적용 - 난이도를 쉽게 변경할 수 있다.
	public GameBoard(GameLevel gameLevel) {
		int rowSize = gameLevel.getRowSize();
		int colSize = gameLevel.getColSize();
		board = new Cell[rowSize][colSize];

		landMineCount = gameLevel.getLandMineCount();
		initializeGameStatus();
	}

	public void initializeGame() {
		initializeGameStatus();
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

	private void initializeGameStatus() {
		gameStatus = GameStatus.IN_PROGRESS;
	}

	private void initializeEmptyCells(CellPositions cellPositions) {
		List<CellPosition> allPositions = cellPositions.getPositions();
		for (CellPosition cellPosition : allPositions) {
			updateCellAt(cellPosition, new EmptyCell());
		}
	}

	private void updateCellAt(
		CellPosition cellPosition,
		Cell cell
	) {
		board[cellPosition.getRowIndex()][cellPosition.getColIndex()] = cell;
	}

	private int countNearByLandMines(CellPosition cellPosition) {
		long count = calculateSurroundedPositions(cellPosition, getRowSize(), getColSize())
			.stream()
			.filter(this::isLandMineCellAt)
			.count();

		return (int)count;
	}

	private boolean isLandMineCellAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.isLandMine();
	}

	public void flagAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.flag();

		checkIfGameIsOver();
	}

	public int getRowSize() {
		return board.length;
	}

	public int getColSize() {
		return board[0].length;
	}

	public void openAt(CellPosition cellPosition) {
		if (isLandMineCellAt(cellPosition)) {
			openOneCellAt(cellPosition);
			changeGameStatusToLose();
			return;
		}

		openSurroundedCells(cellPosition);
		checkIfGameIsOver();
	}

	public boolean isInvalidCellPosition(CellPosition cellPosition) {
		int rowSize = getRowSize();
		int colSize = getColSize();

		return cellPosition.isRowIndexMoreThanOrEqual(rowSize) ||
			cellPosition.isColIndexMoreThanOrEqual(colSize);
	}

	public boolean isInProgress() {
		return gameStatus == GameStatus.IN_PROGRESS;
	}

	public boolean isLose() {
		return gameStatus == GameStatus.LOSE;
	}

	public boolean isWin() {
		return gameStatus == GameStatus.WIN;
	}

	public CellSnapshot getSnapshot(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.getSnapshot();
	}

	private void checkIfGameIsOver() {
		if (isAllCellChecked()) {
			changeGameStatusToWin();
		}
	}

	private void changeGameStatusToWin() {
		gameStatus = GameStatus.WIN;
	}

	private Cell findCell(CellPosition cellPosition) {
		return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
	}

	private void openOneCellAt(CellPosition cellPosition) {
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

	private boolean isAllCellChecked() {
		Cells cells = Cells.from(board);
		return cells.isAllChecked();
	}

	private void openSurroundedCells(CellPosition cellPosition) {
		Deque<CellPosition> deque = new ArrayDeque<>();
		deque.push(cellPosition);

		while (!deque.isEmpty()) {
			openAndPushCellAt(deque);
		}
	}

	/*
		많은 스택프레임이 재귀로 인해 쌓여 스택오버플로우 발생
		  - 스레드마다 생기는 스택 영역에는 함수를 호출할 때마다 frame 이 쌓인다.
		  - frame 은 지역변수, 연산을 위한 정보 등을 담고있다.
		  - 스택 영역의 크기가 제한되어 있다.

		=> 필요한 CellPosition 만을 가진 Deque 자료구조를 따로 만들어 해결
		 (Stack 보다 Deque 자료구조가 성능상 좋다.)
	 */
	private void openAndPushCellAt(Deque<CellPosition> deque) {
		CellPosition currentCellPosition = deque.pop();
		if (isOpenedCell(currentCellPosition)) {
			return;
		}
		if (isLandMineCellAt(currentCellPosition)) {
			return;
		}

		openOneCellAt(currentCellPosition);

		if (doesCellHaveLandMineCount(currentCellPosition)) {
			return;
		}

		List<CellPosition> surroundedPositions = calculateSurroundedPositions(currentCellPosition, getRowSize(),
			getColSize());
		surroundedPositions.forEach(deque::push);
	}

	private List<CellPosition> calculateSurroundedPositions(
		CellPosition cellPosition,
		int rowSize,
		int colSize
	) {
		return RelativePosition.SURROUNDED_POSITIONS.stream()
			.filter(cellPosition::canCalculatePositionBy)
			.map(cellPosition::calculatePositionBy)
			.filter(position -> position.isRowIndexLessThan(rowSize))
			.filter(position -> position.isColIndexLessThan(colSize))
			.toList();
	}

	private void changeGameStatusToLose() {
		gameStatus = GameStatus.LOSE;
	}
}
