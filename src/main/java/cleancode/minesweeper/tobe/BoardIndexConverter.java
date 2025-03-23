package cleancode.minesweeper.tobe;

// 인덱스 변환 책임
public class BoardIndexConverter {

	public static final char BASE_CHAR_FOR_COL = 'a';

	public int getSelectedRowIndex(String cellInput) {
		String cellInputRow = cellInput.substring(1);
		return convertRowFrom(cellInputRow);
	}

	public int getSelectedColIndex(String cellInput) {
		char cellInputCol = cellInput.charAt(0);
		return convertColFrom(cellInputCol);
	}

	private int convertRowFrom(String cellInputRow) {
		int rowIndex = Integer.parseInt(cellInputRow) - 1;
		if (rowIndex < 0) {
			throw new GameException("잘못된 입력입니다.");
		}
		return rowIndex;
	}

	private int convertColFrom(char cellInputCol) {
		int colIndex = cellInputCol - BASE_CHAR_FOR_COL;
		if (colIndex < 0) {
			throw new GameException("잘못된 입력입니다.");
		}
		return colIndex;
	}
}

