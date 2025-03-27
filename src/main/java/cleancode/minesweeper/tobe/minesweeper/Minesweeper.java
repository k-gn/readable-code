package cleancode.minesweeper.tobe.minesweeper;

import cleancode.minesweeper.tobe.minesweeper.board.GameBoard;
import cleancode.minesweeper.tobe.minesweeper.config.GameConfig;
import cleancode.minesweeper.tobe.minesweeper.exception.GameException;
import cleancode.minesweeper.tobe.game.GameInitializable;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.minesweeper.io.InputHandler;
import cleancode.minesweeper.tobe.minesweeper.io.OutputHandler;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.user.UserAction;

// 게임 실행, 출력, 입력 등을 중간에서 호출하는 책임
public class Minesweeper implements GameRunnable, GameInitializable {

	// GameBoard 로 캡슐화한 순간 Minesweeper 입장에선 2중 배열 존재를 모른다.
	private final GameBoard gameBoard;

	// 입출력은 별개의 책임으로 분리
	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;

	public Minesweeper(GameConfig gameConfig) {
		this.gameBoard = new GameBoard(gameConfig.getGameLevel());
		this.inputHandler = gameConfig.getInputHandler();
		this.outputHandler = gameConfig.getOutputHandler();
	}

	@Override
	public void initialize() {
		gameBoard.initializeGame();
	}

	@Override
	public void run() {
		outputHandler.showGameStartComments();

		while (gameBoard.isInProgress()) {
			try {
				outputHandler.showBoard(gameBoard);

				CellPosition cellPosition = getCellInputFromUser();
				UserAction userAction = getUserActionInputFromUser();
				actOnCell(cellPosition, userAction);
			} catch (GameException e) {
				outputHandler.showExceptionMessage(e);
			} catch (Exception e) {
				outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
				break;
			}
		}

		outputHandler.showBoard(gameBoard);

		if (gameBoard.isWin()) {
			outputHandler.showGameWinningComment();
		}
		if (gameBoard.isLose()) {
			outputHandler.showGameLosingComment();
		}
	}


	private void actOnCell(
		CellPosition cellPosition,
		UserAction userAction
	) {
		if (doesUserChooseToPlantFlag(userAction)) {
			gameBoard.flagAt(cellPosition);
			return;
		}

		if (doesUserChooseToOpenCell(userAction)) {
			gameBoard.openAt(cellPosition);
			return;
		}

		throw new IllegalArgumentException("잘못된 번호를 선택하셨습니다.");
	}

	private boolean doesUserChooseToOpenCell(UserAction userAction) {
		return userAction == UserAction.OPEN;
	}

	private boolean doesUserChooseToPlantFlag(UserAction userAction) {
		return userAction == UserAction.FLAG;
	}

	private UserAction getUserActionInputFromUser() {
		outputHandler.showCommentForUserAction();
		return inputHandler.getUserActionFromUser();
	}

	private CellPosition getCellInputFromUser() {
		outputHandler.showCommentForSelectingCell();
		CellPosition cellPosition = inputHandler.getCellPositionFromUser();
		if (gameBoard.isInvalidCellPosition(cellPosition)) {
			throw new IllegalArgumentException("잘못된 좌표를 선택하셨습니다.");
		}
		return cellPosition;
	}
}
