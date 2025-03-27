package cleancode.minesweeper.tobe.minesweeper.io.sign;

import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot;
import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshotStatus;

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
