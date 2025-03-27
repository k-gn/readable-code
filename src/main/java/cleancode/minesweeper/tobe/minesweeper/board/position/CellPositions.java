package cleancode.minesweeper.tobe.minesweeper.board.position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cleancode.minesweeper.tobe.minesweeper.board.cell.Cell;

public class CellPositions {

	private final List<CellPosition> positions;

	private CellPositions(List<CellPosition> positions) {
		this.positions = positions;
	}

	public static CellPositions of(List<CellPosition> positions) {
		return new CellPositions(positions);
	}

	public static CellPositions from(Cell[][] board) {
		List<CellPosition> cellPositions = new ArrayList<>();

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				CellPosition cellPosition = CellPosition.of(row, col);
				cellPositions.add(cellPosition);
			}
		}

		return of(cellPositions);
	}

	public List<CellPosition> getPositions() {
		return new ArrayList<>(positions);
	}

	public List<CellPosition> extractRandomPositions(int landMineCount) {
		List<CellPosition> cellPositions = new ArrayList<>(positions);
		Collections.shuffle(cellPositions);
		return cellPositions.subList(0, landMineCount);
	}

	public List<CellPosition> subtract(List<CellPosition> positionListToSubtract) {
		List<CellPosition> cellPositions = new ArrayList<>(positions);
		CellPositions positionsToSubtract = CellPositions.of(positionListToSubtract);
		return cellPositions.stream()
			.filter(positionsToSubtract::doesNotContain)
			.toList();
	}

	private boolean doesNotContain(CellPosition position) {
		return !positions.contains(position);
	}
}
