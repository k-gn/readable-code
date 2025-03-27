package cleancode.minesweeper.tobe.io.sign;

import cleancode.minesweeper.tobe.cell.CellSnapshot;
import cleancode.minesweeper.tobe.cell.CellSnapshotStatus;

public class NumberCellSignProvider implements CellSignProvidable {

	@Override
	public boolean supports(CellSnapshot snapshot) {
		return snapshot.isSameStatus(CellSnapshotStatus.NUMBER);
	}

	@Override
	public String provide(CellSnapshot snapshot) {
		return String.valueOf(snapshot.getNearByLandMineCount());
	}
}
