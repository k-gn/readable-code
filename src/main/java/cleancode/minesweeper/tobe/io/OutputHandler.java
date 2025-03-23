package cleancode.minesweeper.tobe.io;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import cleancode.minesweeper.tobe.GameBoard;
import cleancode.minesweeper.tobe.GameException;

public interface OutputHandler {

	void showGameStartComments();
	void showBoard(GameBoard board);
	void showGameWinningComment();
	void showGameLosingComment();
	void showCommentForSelectingCell();
	void showCommentForUserAction();
	void showExceptionMessage(GameException e);
	void showSimpleMessage(String message);
}
