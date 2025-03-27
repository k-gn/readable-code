package cleancode.minesweeper.tobe.minesweeper.io.sign;

import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot;
import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshotStatus;

public class LandMineCellSignProvider implements CellSignProvidable {

	private static final String LAND_MINE_SIGN = "☼";

	@Override
	public boolean supports(CellSnapshot snapshot) {
		return snapshot.isSameStatus(CellSnapshotStatus.LAND_MINE);
	}

	@Override
	public String provide(CellSnapshot snapshot) {
		return LAND_MINE_SIGN;
	}
}
