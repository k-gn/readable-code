package cleancode.minesweeper.tobe.minesweeper.io.sign;

import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot;
import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshotStatus;

public class FlagCellSignProvider implements CellSignProvidable {

	private static final String FLAG_SIGN = "⚑";

	@Override
	public boolean supports(CellSnapshot snapshot) {
		return snapshot.isSameStatus(CellSnapshotStatus.FLAG);
	}

	@Override
	public String provide(CellSnapshot snapshot) {
		return FLAG_SIGN;
	}
}
