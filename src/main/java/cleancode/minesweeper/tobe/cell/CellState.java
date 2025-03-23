package cleancode.minesweeper.tobe.cell;

public class CellState {

	// 내부에서 해당 필드를 어떻게 변경하든, 오픈 메서드 스팩만 동일하다면 외부에 영향을 전혀 주지 않는 구조 => 조합의 장점k
	private boolean isOpened;
	private boolean isFlagged;

	public CellState(
		boolean isOpened,
		boolean isFlagged
	) {
		this.isOpened = isOpened;
		this.isFlagged = isFlagged;
	}

	public static CellState initialize() {
		return new CellState(false, false);
	}

	public void flag() {
		this.isFlagged = true;
	}

	public void open() {
		this.isOpened = true;
	}

	public boolean isOpened() {
		return isOpened;
	}

	public boolean isChecked() {
		return isFlagged || isOpened;
	}

	public boolean isFlagged() {
		return isFlagged;
	}
}
