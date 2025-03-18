package cleancode.minesweeper.tobe;

// 의도한 예외
public class AppException extends RuntimeException {

	public AppException(String message) {
		super(message);
	}
}
