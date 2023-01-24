package io.task.scheduler;


import io.task.scheduler.domain.BitWeekDays;
import io.task.scheduler.domain.ScheduledTime;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulerService implements Runnable {

    @Override
    public void run() {
        scheduledTaskExecution();
    }

    public void scheduledTaskExecution() {
        ZoneId lagosId = ZoneId.of("Africa/Lagos");
        LocalDateTime timeInLagos = ZonedDateTime.now().withZoneSameInstant(lagosId).toLocalDateTime();
        String pathToCSV = "input/schedule.csv";
        List<ScheduledTime> scheduledTimes = loadScheduleFromCSV(pathToCSV);
        int tasksExecuted = 0;

        for (ScheduledTime scheduledTime : scheduledTimes) {
            LocalTime executeTaskTime = scheduledTime.getTime();
            if (executeTaskTime.getHour() == timeInLagos.getHour()
                    && executeTaskTime.getMinute() == timeInLagos.getMinute()
                    && scheduledTime.getDays().contains(timeInLagos.getDayOfWeek())) {
                System.out.println("executing scheduled task");
                tasksExecuted += 1;
            }
        }
        if (tasksExecuted == 0) {
            System.out.println("nothing to execute");
        }

    }

    public List<ScheduledTime> loadScheduleFromCSV(String location) {
        List<ScheduledTime> scheduledTimes = new ArrayList<>();

        try (InputStream resource = new ClassPathResource(location).getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(resource))) {
            String line = br.readLine();
            int lineNr = 1;

            while (line != null) {
                String[] receivedInput = line.split(",");
                ScheduledTime newScheduledTime = createScheduledTime(receivedInput, lineNr);
                if (newScheduledTime != null) {
                    scheduledTimes.add(newScheduledTime);
                }
                lineNr += 1;
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return scheduledTimes;

    }

    public ScheduledTime createScheduledTime(String[] input, int lineNr) {
        try {
            LocalTime time = LocalTime.parse(input[0]);
            String daysInput = input[1];
            int bitMask;
            if ((daysInput.length() > 2) && (daysInput.charAt(1) == 'x')) {
                bitMask = Integer.parseInt(daysInput.substring(2), 16);
            } else {
                bitMask = Integer.parseInt(daysInput);
            }

            List<DayOfWeek> days = BitWeekDays.getDaysOfWeekFromBitValues(bitMask);

            return new ScheduledTime(time, days);

        } catch (IllegalArgumentException | DateTimeParseException e) {
            System.out.println("Illegal input values at line " + lineNr);
            return null;
        }
    }

}
