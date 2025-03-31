package cleancode.studycafe.tobe;

import java.util.List;
import java.util.Optional;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.StudyCafeIOHandler;
import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafeLockerPasses;
import cleancode.studycafe.tobe.model.StudyCafePassType;
import cleancode.studycafe.tobe.model.StudyCafeSeatPass;
import cleancode.studycafe.tobe.model.StudyCafeSeatPasses;
import cleancode.studycafe.tobe.model.order.StudyCafePassOrder;
import cleancode.studycafe.tobe.provider.LockerPassProvider;
import cleancode.studycafe.tobe.provider.SeatPassProvider;

public class StudyCafePassMachine {

	private final StudyCafeIOHandler ioHandler = new StudyCafeIOHandler();
	private final SeatPassProvider seatPassProvider;
	private final LockerPassProvider lockerPassProvider;

	public StudyCafePassMachine(
		SeatPassProvider seatPassProvider,
		LockerPassProvider lockerPassProvider
	) {
		this.seatPassProvider = seatPassProvider;
		this.lockerPassProvider = lockerPassProvider;
	}

	public void run() {
		try {
			ioHandler.showWelcomeMessage();
			ioHandler.showAnnouncement();

			StudyCafeSeatPass selectedPass = selectPass();
			Optional<StudyCafeLockerPass> optionalLockerPass = selectLockerPass(selectedPass);

			StudyCafePassOrder passOrder = StudyCafePassOrder.of(
				selectedPass,
				optionalLockerPass.orElse(null)
			);

			ioHandler.showPassOrderSummary(passOrder);
		} catch (AppException e) {
			ioHandler.showSimpleMessage(e.getMessage());
		} catch (Exception e) {
			ioHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
		}
	}

	private StudyCafeSeatPass selectPass() {
		StudyCafePassType passType = ioHandler.askPassTypeSelecting();
		List<StudyCafeSeatPass> passCandidates = findPassCandidatesBy(passType);

		return ioHandler.askPassSelecting(passCandidates);
	}

	private List<StudyCafeSeatPass> findPassCandidatesBy(StudyCafePassType passType) {
		StudyCafeSeatPasses allPasses = seatPassProvider.getSeatPasses();
		return allPasses.findPassBy(passType);
	}

	private Optional<StudyCafeLockerPass> selectLockerPass(StudyCafeSeatPass selectedPass) {
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

	private Optional<StudyCafeLockerPass> findLockerPassCandidatesBy(StudyCafeSeatPass selectedPass) {
		StudyCafeLockerPasses allLockerPasses = lockerPassProvider.getLockerPasses();
		return allLockerPasses.findLockerPassBy(selectedPass);
	}

}
