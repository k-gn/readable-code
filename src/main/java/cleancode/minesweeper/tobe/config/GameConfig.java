package cleancode.minesweeper.tobe.config;

import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;

// Minesweeper 의 생성자 추가를 방지하기 위한 설정 묶음 객체 + 설정 정보를 관리하기 위함
public class GameConfig {

	private final GameLevel gameLevel;
	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;

	public GameConfig(
		GameLevel gameLevel,
		InputHandler inputHandler,
		OutputHandler outputHandler
	) {
		this.gameLevel = gameLevel;
		this.inputHandler = inputHandler;
		this.outputHandler = outputHandler;
	}

	public GameLevel getGameLevel() {
		return gameLevel;
	}

	public InputHandler getInputHandler() {
		return inputHandler;
	}

	public OutputHandler getOutputHandler() {
		return outputHandler;
	}
}
