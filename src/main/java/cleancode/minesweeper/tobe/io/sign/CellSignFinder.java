package cleancode.minesweeper.tobe.io.sign;

import java.util.List;

import cleancode.minesweeper.tobe.cell.CellSnapshot;

public class CellSignFinder {

	public static final List<CellSignProvidable> CELL_SIGN_PROVIDERS = List.of(
		new EmptyCellSignProvider(),
		new FlagCellSignProvider(),
		new NumberCellSignProvider(),
		new LandMineCellSignProvider(),
		new UncheckedCellSignProvider()
	);

	public String findCellSignFrom(CellSnapshot snapshot) {
		// 다형성 활용
		return CELL_SIGN_PROVIDERS.stream()
			.filter(provider -> provider.supports(snapshot))
			.findFirst()
			.map(provider -> provider.provide(snapshot))
			.orElseThrow(() -> new IllegalArgumentException("확인할 수 없는 셀 입니다."));
	}
}
