package cleancode.minesweeper.tobe.minesweeper.exception;

// 의도한 예외
public class GameException extends RuntimeException {

	public GameException(String message) {
		super(message);
	}
}
