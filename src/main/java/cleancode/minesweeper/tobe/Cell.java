package cleancode.minesweeper.tobe;

// getter, setter 지양

public class Cell {

	public static final String FLAG_SIGN = "⚑";
	public static final String LAND_MINE_SIGN = "☼";
	public static final String UNCHECKED_SIGN = "□";
	public static final String EMPTY_SIGN = "■";

	private boolean isLandMine;
	private boolean isFlagged;
	private boolean isOpened;
	private int nearByLandMineCount;

	/*
		속성 : 근처 지뢰 숫자, 지뢰 여부
		상태 : 깃발 유무, 열림/닫힘, 사용자 확인
	 */

	public Cell(
		int nearByLandMineCount,
		boolean isLandMine,
		boolean isFlagged,
		boolean isOpened
	) {
		this.nearByLandMineCount = nearByLandMineCount;
		this.isLandMine = isLandMine;
		this.isFlagged = isFlagged;
		this.isOpened = isOpened;
	}

	public static Cell of(
		int nearByLandMineCount,
		boolean isLandMine,
		boolean isFlagged,
		boolean isOpened
	) {
		return new Cell(nearByLandMineCount, isLandMine, isFlagged, isOpened);
	}

	public static Cell create() {
		return of(0, false, false, false);
	}

	public void turnOnLandMine() {
		this.isLandMine = true;
	}

	public void flag() {
		this.isFlagged = true;
	}

	public void open() {
		this.isOpened = true;
	}

	public boolean isChecked() {
		return isFlagged || isOpened;
	}

	public boolean isLandMine() {
		return isLandMine;
	}

	public boolean isOpened() {
		return isOpened;
	}

	public boolean hasLandMineCount() {
		return this.nearByLandMineCount != 0;
	}

	public void updateNearByLandMineCount(int count) {
		this.nearByLandMineCount = count;
	}

	public String getSign() {
		if (isOpened) {
			if (isLandMine) {
				return LAND_MINE_SIGN;
			}

			if (hasLandMineCount()) {
				return String.valueOf(nearByLandMineCount);
			}

			return EMPTY_SIGN;
		}

		if (isFlagged) {
			return FLAG_SIGN;
		}

		return UNCHECKED_SIGN;
	}
}
