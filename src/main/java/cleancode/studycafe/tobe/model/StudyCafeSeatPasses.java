package cleancode.studycafe.tobe.model;

import java.util.List;

public class StudyCafeSeatPasses {

	private final List<StudyCafeSeatPass> passes;

	private StudyCafeSeatPasses(List<StudyCafeSeatPass> passes) {
		this.passes = passes;
	}

	public static StudyCafeSeatPasses of(List<StudyCafeSeatPass> passes) {
		return new StudyCafeSeatPasses(passes);
	}

	public List<StudyCafeSeatPass> findPassBy(StudyCafePassType passType) {
		return this.passes.stream()
			.filter(studyCafePass -> studyCafePass.isSamePassType(passType))
			.toList();
	}
}
