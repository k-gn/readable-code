package cleancode.minesweeper.tobe;

// getter, setter 지양

public class LagacyCell {

	public static final String FLAG_SIGN = "⚑";
	public static final String LAND_MINE_SIGN = "☼";
	public static final String UNCHECKED_SIGN = "□";
	public static final String EMPTY_SIGN = "■";

	private boolean isOpened;
	private boolean isFlagged;
	private boolean isLandMine;
	private int nearByLandMineCount;

	/*
		속성 : 근처 지뢰 숫자, 지뢰 여부
		상태 : 깃발 유무, 열림/닫힘, 사용자 확인

	    * 도메인 지식은 발견하는 것이다.
		* 이전엔 몰랐던 도메인 지식 습득!
		  - 열렸다/닫혔다 개념은 사용자가 체크했다는 개념과 다르다.
		  - 게임 종료 조건 : 모든 셀이 열려있거나, 닫혀있지만 깃발로 확인된 경우
		  - 단순히 열렸다/닫혔다로 게임을 종료 시킬 수 있는게 아닌 사용자의 체크 유무로 종료시킬 수 있다.
		    - 기존엔 BOARD 배열에서 sign 을 갈아끼우는 형태
		      -> cell 이라는 정보를 담을 객체가 생김
		      -> 사용자의 행위에 따라 cell 의 상태를 변화시키는 방향으로 변경
	 */

	public LagacyCell(
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

	public static LagacyCell of(
		int nearByLandMineCount,
		boolean isLandMine,
		boolean isFlagged,
		boolean isOpened
	) {
		return new LagacyCell(nearByLandMineCount, isLandMine, isFlagged, isOpened);
	}

	public static LagacyCell create() {
		return of(0, false, false, false);
	}

	public void flag() {
		this.isFlagged = true;
	}

	public void open() {
		this.isOpened = true;
	}

	public void turnOnLandMine() {
		this.isLandMine = true;
	}

	public boolean isLandMine() {
		return isLandMine;
	}

	public boolean isOpened() {
		return isOpened;
	}

	public boolean isChecked() {
		return isFlagged || isOpened;
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
