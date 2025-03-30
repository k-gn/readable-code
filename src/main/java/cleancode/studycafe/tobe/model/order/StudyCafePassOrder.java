package cleancode.studycafe.tobe.model.order;

import java.util.Optional;

import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafeSeatPass;

public class StudyCafePassOrder {

	private final StudyCafeSeatPass seatPass;
	private final StudyCafeLockerPass lockerPass;

	private StudyCafePassOrder(
		StudyCafeSeatPass seatPass,
		StudyCafeLockerPass lockerPass
	) {
		this.seatPass = seatPass;
		this.lockerPass = lockerPass;
	}

	public static StudyCafePassOrder of(
		StudyCafeSeatPass seatPass,
		StudyCafeLockerPass lockerPass
	) {
		return new StudyCafePassOrder(seatPass, lockerPass);
	}

	public StudyCafeSeatPass getSeatPass() {
		return seatPass;
	}

	public Optional<StudyCafeLockerPass> getLockerPass() {
		return Optional.ofNullable(lockerPass);
	}

	public int getDiscountPrice() {
		return seatPass.getDiscountPrice();
	}

	public int getTotalPrice() {
		int lockerPassPrice = lockerPass != null ? lockerPass.getPrice() : 0;
		int totalPassPrice = seatPass.getPrice() + lockerPassPrice;
		return  totalPassPrice - getDiscountPrice();
	}
}
