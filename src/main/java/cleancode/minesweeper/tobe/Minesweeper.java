package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

public class Minesweeper {

	public static final int BOARD_ROW_SIZE = 8;
	public static final int BOARD_COL_SIZE = 10;

	// GameBoard 로 캡슐화한 순간 Minesweeper 입장에선 2중 배열 존재를 모른다.
	private final GameBoard gameBoard;

	// 입출력은 별개의 책임으로 분리
	private final ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler();
	private final ConsoleOutputHandler consoleOutputHandler = new ConsoleOutputHandler();
	private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();

	public Minesweeper(GameLevel gameLevel) {
		gameBoard = new GameBoard(gameLevel);
	}

	private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

	public void run() {
		consoleOutputHandler.showGameStartComments();
		gameBoard.initializeGame();

		while (true) {
			try {
				consoleOutputHandler.showBoard(gameBoard);

				if (doseUserWinTheGame()) {
					consoleOutputHandler.printGameWinningComment();
					break;
				}
				if (doesUserLoseTheGame()) {
					consoleOutputHandler.printGameLosingComment();
					break;
				}

				String cellInput = getCellInputFromUser();
				String userActionInput = getUserActionInputFromUser();
				actOnCell(cellInput, userActionInput);
			} catch (GameException e) {
				consoleOutputHandler.printExceptionMessage(e);
			} catch (Exception e) {
				consoleOutputHandler.printSimpleMessage("프로그램에 문제가 생겼습니다.");
				e.printStackTrace();
			}
		}
	}


	private void actOnCell(
		String cellInput,
		String userActionInput
	) {
		int selectedColIndex = boardIndexConverter.getSelectedColIndex(cellInput, gameBoard.getColSize());
		int selectedRowIndex = boardIndexConverter.getSelectedRowIndex(cellInput, gameBoard.getRowSize());

		if (doesUserChooseToPlantFlag(userActionInput)) {
			gameBoard.flag(selectedRowIndex, selectedColIndex);
			checkIfGameIsOver();
			return;
		}

		if (doesUserChooseToOpenCell(userActionInput)) {
			if (gameBoard.isLandMineCell(selectedColIndex, selectedRowIndex)) {
				gameBoard.open(selectedRowIndex, selectedColIndex);
				changeGameStatusToLose();
				return;
			}

			gameBoard.openSurroundedCells(selectedRowIndex, selectedColIndex);
			checkIfGameIsOver();
			return;
		}

		throw new IllegalArgumentException("잘못된 번호를 선택하셨습니다.");
	}

	private void changeGameStatusToLose() {
		gameStatus = -1;
	}

	private boolean doesUserChooseToOpenCell(String userActionInput) {
		return userActionInput.equals("1");
	}

	private boolean doesUserChooseToPlantFlag(String userActionInput) {
		return userActionInput.equals("2");
	}

	private String getUserActionInputFromUser() {
		consoleOutputHandler.printCommentForUserAction();
		return consoleInputHandler.getUserInput();
	}

	private String getCellInputFromUser() {
		consoleOutputHandler.printCommentForSelectingCell();
		return consoleInputHandler.getUserInput();
	}

	private boolean doesUserLoseTheGame() {
		return gameStatus == -1;
	}

	private boolean doseUserWinTheGame() {
		return gameStatus == 1;
	}

	// check 라는 이름은 통상적으로 void 를 반환한다.
	private void checkIfGameIsOver() {
		if (gameBoard.isAllCellChecked()) {
			changeGameStatusToWin();
		}
	}

	private void changeGameStatusToWin() {
		gameStatus = 1;
	}
}
