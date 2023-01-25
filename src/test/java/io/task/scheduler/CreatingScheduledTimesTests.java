package io.task.scheduler;

import io.task.scheduler.domain.ScheduledTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class CreatingScheduledTimesTests {

    private SchedulerService schedulerService;

    @BeforeEach
    void setUp() {
        this.schedulerService = new SchedulerService();
    }

    @Test
    void createCorrectScheduledTimeFromValidInputDecimal() {

        String[] validInput = {"12:05", "5"};
        int inputLineNumber = 1;
        LocalTime expectedTime = LocalTime.of(12, 5);
        List<DayOfWeek> expectedDays = new ArrayList<>();
        expectedDays.add(DayOfWeek.MONDAY);
        expectedDays.add(DayOfWeek.WEDNESDAY);
        ScheduledTime expectedOutput = new ScheduledTime(expectedTime, expectedDays);
        ScheduledTime testedOutput = schedulerService.createScheduledTime(validInput, inputLineNumber);

        Assertions.assertEquals(expectedOutput, testedOutput);

    }

    @Test
    void createCorrectScheduledTimeFromValidInputHexadecimal() {

        String[] validInput = {"12:05", "0x5"};
        int inputLineNumber = 1;
        ScheduledTime testedOutput = schedulerService.createScheduledTime(validInput, inputLineNumber);

        LocalTime expectedTime = LocalTime.of(12, 5);
        List<DayOfWeek> expectedDays = new ArrayList<>();
        expectedDays.add(DayOfWeek.MONDAY);
        expectedDays.add(DayOfWeek.WEDNESDAY);
        ScheduledTime expectedOutput = new ScheduledTime(expectedTime, expectedDays);

        Assertions.assertEquals(expectedOutput, testedOutput);

    }

    @Test
    void returnNullWhenInvalidTimeInput() {

        String[] invalidInput = {"12:77", "0x5"};
        int inputLineNumber = 1;

        ScheduledTime testedOutput = schedulerService.createScheduledTime(invalidInput, inputLineNumber);

        Assertions.assertNull(testedOutput);

    }

    @Test
    void returnNullWhenInvalidDaysInput() {

        String[] invalidInput1 = {"12:05", "0x9B"};
        int inputLineNumber = 1;

        ScheduledTime testedOutput = schedulerService.createScheduledTime(invalidInput1, inputLineNumber);

        Assertions.assertNull(testedOutput);

    }

}
