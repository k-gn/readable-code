package cleancode.studycafe.tobe.io;

import java.util.List;
import java.util.Scanner;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.model.StudyCafePassType;
import cleancode.studycafe.tobe.model.StudyCafeSeatPass;

public class InputHandler {

	private static final Scanner SCANNER = new Scanner(System.in);

	public StudyCafePassType getPassTypeSelectingUserAction() {
		String userInput = SCANNER.nextLine();

		return switch (userInput) {
			case "1" -> StudyCafePassType.HOURLY;
			case "2" -> StudyCafePassType.WEEKLY;
			case "3" -> StudyCafePassType.FIXED;
			default -> throw new AppException("잘못된 입력입니다.");
		};
	}

	public StudyCafeSeatPass getSelectPass(List<StudyCafeSeatPass> passes) {
		String userInput = SCANNER.nextLine();
		int selectedIndex = Integer.parseInt(userInput) - 1;
		return passes.get(selectedIndex);
	}

	public boolean getLockerSelection() {
		String userInput = SCANNER.nextLine();
		return "1".equals(userInput);
	}

}
