package cleancode.minesweeper.tobe.minesweeper.io;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import cleancode.minesweeper.tobe.minesweeper.board.GameBoard;
import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.exception.GameException;
import cleancode.minesweeper.tobe.minesweeper.io.sign.CellSignProvider;

public class ConsoleOutputHandler implements OutputHandler {

	// public final CellSignFinder cellSignFinder = new CellSignFinder();

	@Override
	public void showGameStartComments() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("지뢰찾기 게임 시작!");
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	@Override
	public void showBoard(GameBoard board) {
		String alphabets = generateColAlphabets(board);

		System.out.println("   " + alphabets);
		for (int row = 0; row < board.getRowSize(); row++) {
			System.out.printf("%2d  ", row + 1);
			for (int col = 0; col < board.getColSize(); col++) {
				CellPosition cellPosition = CellPosition.of(row, col);

				// cell 이 자신을 그리는 것 보다 여기서 데이터를 받아 그려주는게 더 관심사에 맞음
				CellSnapshot cellSnapshot = board.getSnapshot(cellPosition);
				// String cellSign = cellSignFinder.findCellSignFrom(cellSnapshot);
				String cellSign = CellSignProvider.findCellSignFrom(cellSnapshot);

				System.out.print(cellSign + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private String generateColAlphabets(GameBoard board) {
		List<String> alphabets = IntStream.range(0, board.getColSize())
			.mapToObj(index -> (char)('a' + index))
			.map(Objects::toString)
			.toList();

		return String.join(" ", alphabets);
	}

	@Override
	public void showGameWinningComment() {
		System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
	}

	@Override
	public void showGameLosingComment() {
		System.out.println("지뢰를 밟았습니다. GAME OVER!");
	}

	@Override
	public void showCommentForSelectingCell() {
		System.out.println("선택할 좌표를 입력하세요. (예: a1)");
	}

	@Override
	public void showCommentForUserAction() {
		System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
	}

	@Override
	public void showExceptionMessage(GameException e) {
		System.out.println(e.getMessage());
	}

	@Override
	public void showSimpleMessage(String message) {
		System.out.println(message);
	}
}
