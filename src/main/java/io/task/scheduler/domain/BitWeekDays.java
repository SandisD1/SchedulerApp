package io.task.scheduler.domain;

import lombok.Getter;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Getter
public enum BitWeekDays {
    MONDAY_BIT(1, 1),
    TUESDAY_BIT(2, 2),
    WEDNESDAY_BIT(4, 3),
    THURSDAY_BIT(8, 4),
    FRIDAY_BIT(16, 5),
    SATURDAY_BIT(32, 6),
    SUNDAY_BIT(64, 7);

    private final int bitValue;
    private final int dayNumber;

    BitWeekDays(int bitValue, int dayNumber) {
        this.bitValue = bitValue;
        this.dayNumber = dayNumber;
    }

    public static List<DayOfWeek> getDaysOfWeekFromBitValues(int originalBitMask) {
        List<DayOfWeek> returnValues = new ArrayList<>();
        if (originalBitMask > 127) {
            throw new IllegalArgumentException();
        }
        int bitMask = originalBitMask;

        for (BitWeekDays val : BitWeekDays.values()) {
            if ((val.bitValue & bitMask) == val.bitValue) {
                bitMask -= val.bitValue;

                DayOfWeek day = DayOfWeek.of(val.dayNumber);

                returnValues.add(day);
            }
        }

        return returnValues;
    }
}
