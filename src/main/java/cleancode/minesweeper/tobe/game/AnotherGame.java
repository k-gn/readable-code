package cleancode.minesweeper.tobe.game;

public class AnotherGame implements Game {

	@Override
	public void initialize() {
		// 필요 없는데..? => ISP 위반
	}

	@Override
	public void run() {

	}
}
