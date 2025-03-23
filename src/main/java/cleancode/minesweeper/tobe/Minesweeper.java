package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.game.GameInitializable;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;
import cleancode.minesweeper.tobe.position.CellPosition;

public class Minesweeper implements GameRunnable, GameInitializable {

	// GameBoard 로 캡슐화한 순간 Minesweeper 입장에선 2중 배열 존재를 모른다.
	private final GameBoard gameBoard;

	// 입출력은 별개의 책임으로 분리
	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;
	private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();

	public Minesweeper(
		GameLevel gameLevel,
		InputHandler inputHandler,
		OutputHandler outputHandler
	) {
		this.gameBoard = new GameBoard(gameLevel);
		this.inputHandler = inputHandler;
		this.outputHandler = outputHandler;
	}

	private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

	@Override
	public void initialize() {
		gameBoard.initializeGame();
	}

	@Override
	public void run() {
		outputHandler.showGameStartComments();

		while (true) {
			try {
				outputHandler.showBoard(gameBoard);

				if (doseUserWinTheGame()) {
					outputHandler.showGameWinningComment();
					break;
				}
				if (doesUserLoseTheGame()) {
					outputHandler.showGameLosingComment();
					break;
				}

				CellPosition cellPosition = getCellInputFromUser();
				String userActionInput = getUserActionInputFromUser();
				actOnCell(cellPosition, userActionInput);
			} catch (GameException e) {
				outputHandler.showExceptionMessage(e);
			} catch (Exception e) {
				outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
				break;
			}
		}
	}


	private void actOnCell(
		CellPosition cellPosition,
		String userActionInput
	) {
		if (doesUserChooseToPlantFlag(userActionInput)) {
			gameBoard.flagAt(cellPosition);
			checkIfGameIsOver();
			return;
		}

		if (doesUserChooseToOpenCell(userActionInput)) {
			if (gameBoard.isLandMineCellAt(cellPosition)) {
				gameBoard.openAt(cellPosition);
				changeGameStatusToLose();
				return;
			}

			gameBoard.openSurroundedCells(cellPosition);
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
		outputHandler.showCommentForUserAction();
		return inputHandler.getUserInput();
	}

	private CellPosition getCellInputFromUser() {
		outputHandler.showCommentForSelectingCell();
		CellPosition cellPosition = inputHandler.getCellPositionFromUser();
		if (gameBoard.isInvalidCellPosition(cellPosition)) {
			throw new IllegalArgumentException("잘못된 좌표를 선택하셨습니다.");
		}
		return cellPosition;
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
