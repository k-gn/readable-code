package cleancode.studycafe.tobe.model.order;

import java.util.Optional;

import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafeSeatPass;

public record StudyCafePassOrder(
	StudyCafeSeatPass seatPass,
	StudyCafeLockerPass lockerPass
) {

	public static StudyCafePassOrder of(
		StudyCafeSeatPass seatPass,
		StudyCafeLockerPass lockerPass
	) {
		return new StudyCafePassOrder(seatPass, lockerPass);
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
		return totalPassPrice - getDiscountPrice();
	}
}
