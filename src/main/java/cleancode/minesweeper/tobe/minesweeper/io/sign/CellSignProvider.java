package cleancode.minesweeper.tobe.minesweeper.io.sign;

import java.util.Arrays;

import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot;
import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshotStatus;

// 다형성으로 인해 많아지는 클래스 구조를 대체할 enum
public enum CellSignProvider implements CellSignProvidable {

	EMPTY(CellSnapshotStatus.EMPTY) {
		@Override
		public String provide(CellSnapshot snapshot) {
			return EMPTY_SIGN;
		}
	},
	FLAG(CellSnapshotStatus.FLAG) {
		@Override
		public String provide(CellSnapshot snapshot) {
			return FLAG_SIGN;
		}
	},
	LAND_MINE(CellSnapshotStatus.LAND_MINE) {
		@Override
		public String provide(CellSnapshot snapshot) {
			return LAND_MINE_SIGN;
		}
	},
	NUMBER(CellSnapshotStatus.NUMBER) {
		@Override
		public String provide(CellSnapshot snapshot) {
			return String.valueOf(snapshot.getNearByLandMineCount());
		}
	},
	UNCHECKED(CellSnapshotStatus.UNCHECKED) {
		@Override
		public String provide(CellSnapshot snapshot) {
			return UNCHECKED_SIGN;
		}
	};

	private static final String FLAG_SIGN = "⚑";
	private static final String EMPTY_SIGN = "■";
	private static final String LAND_MINE_SIGN = "☼";
	private static final String UNCHECKED_SIGN = "□";

	private final CellSnapshotStatus status;

	CellSignProvider(CellSnapshotStatus status) {
		this.status = status;
	}

	@Override
	public boolean supports(CellSnapshot snapshot) {
		return snapshot.isSameStatus(status);
	}

	public static String findCellSignFrom(CellSnapshot snapshot) {
		CellSignProvider provider = findBy(snapshot);
		return provider.provide(snapshot);
	}

	private static CellSignProvider findBy(CellSnapshot snapshot) {
		return Arrays.stream(values())
			.filter(provide -> provide.supports(snapshot))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("확인할 수 없는 셀 입니다."));
	}
}
