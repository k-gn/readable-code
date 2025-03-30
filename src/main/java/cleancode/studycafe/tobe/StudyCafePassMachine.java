package cleancode.studycafe.tobe;

import java.util.List;
import java.util.Optional;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.io.StudyCafeIOHandler;
import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafeLockerPasses;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;
import cleancode.studycafe.tobe.model.StudyCafePasses;

public class StudyCafePassMachine {

	private final StudyCafeIOHandler ioHandler = new StudyCafeIOHandler();
	private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

	public void run() {
		try {
			ioHandler.showWelcomeMessage();
			ioHandler.showAnnouncement();

			StudyCafePass selectedPass = selectPass();

			Optional<StudyCafeLockerPass> optionalLockerPass = selectLockerPass(selectedPass);

			optionalLockerPass.ifPresentOrElse(
				lockerPass -> ioHandler.showPassOrderSummary(selectedPass, lockerPass),
				() -> ioHandler.showPassOrderSummary(selectedPass)
			);

		} catch (AppException e) {
			ioHandler.showSimpleMessage(e.getMessage());
		} catch (Exception e) {
			ioHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
		}
	}

	private StudyCafePass selectPass() {
		StudyCafePassType passType = ioHandler.askPassTypeSelecting();
		List<StudyCafePass> passCandidates = findPassCandidatesBy(passType);

		return ioHandler.askPassSelecting(passCandidates);
	}

	private List<StudyCafePass> findPassCandidatesBy(StudyCafePassType passType) {
		StudyCafePasses allPasses = studyCafeFileHandler.readStudyCafePasses();
		return allPasses.findPassBy(passType);
	}

	private Optional<StudyCafeLockerPass> selectLockerPass(StudyCafePass selectedPass) {
		if (selectedPass.canNotUseLocker()) {
			return Optional.empty();
		}

		Optional<StudyCafeLockerPass> lockerPassCandidate = findLockerPassCandidatesBy(selectedPass);

		if (lockerPassCandidate.isPresent()) {
			StudyCafeLockerPass lockerPass = lockerPassCandidate.get();
			boolean isLockerSelected = ioHandler.askLockerPass(lockerPass);
			if (isLockerSelected) {
				return Optional.of(lockerPass);
			}
		}

		return Optional.empty();
	}

	private Optional<StudyCafeLockerPass> findLockerPassCandidatesBy(StudyCafePass selectedPass) {
		StudyCafeLockerPasses allLockerPasses = studyCafeFileHandler.readLockerPasses();
		return allLockerPasses.findLockerPassBy(selectedPass);
	}

}
