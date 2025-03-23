package cleancode.minesweeper.tobe.cell;

public abstract class LagacyAbstractCell {

	protected static final String FLAG_SIGN = "⚑";
	protected static final String UNCHECKED_SIGN = "□";

	// 부모의 필드를 자식이 사용하기 위해 protected 설정 => 결합도 증가 => 캡슐화 깨짐
	protected boolean isOpened;
	protected boolean isFlagged;

	public abstract boolean isLandMine();

	public abstract boolean hasLandMineCount();

	public abstract String getSign();

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
}
