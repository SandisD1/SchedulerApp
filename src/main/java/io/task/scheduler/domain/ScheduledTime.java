package io.task.scheduler.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@EqualsAndHashCode
public class ScheduledTime {
    private final LocalTime time;
    private final List<DayOfWeek> days;

    public ScheduledTime(LocalTime time, List<DayOfWeek> days) {
        this.time = time;
        this.days = days;
    }
}
