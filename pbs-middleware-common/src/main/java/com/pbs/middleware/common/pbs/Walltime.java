package com.pbs.middleware.common.pbs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Walltime {

    private static final String WALLTIME_DELIMITER = ":";

    private static final Integer HOURS_IN_WEEK = 168;
    private static final Integer HOURS_IN_DAY = 24;

    private Integer weeks = 0;
    private Integer days = 0;
    private Integer hours = 0;
    private Integer minutes = 0;
    private Integer seconds = 0;

    public static Walltime from(String walltime) {
        return new Walltime(walltime);
    }

    private Walltime(String wallTime) {
        if (wallTime == null) {
            throw new IllegalArgumentException("Walltime should not be null!");
        }
        if (wallTime.isEmpty() || wallTime.isBlank()) {
            throw new IllegalArgumentException("Walltime can not be empty string!");
        }
        try {
            String[] time = wallTime.split(WALLTIME_DELIMITER);
            if (time.length != 3) {
                throw new IllegalArgumentException("Walltime should be in format <hours>:<minutes>:<seconds>");
            }
            this.seconds = Integer.parseInt(time[2]);
            this.minutes = Integer.parseInt(time[1]);

            Integer hours = Integer.parseInt(time[0]);
            this.weeks = hours / HOURS_IN_WEEK;
            hours = hours % HOURS_IN_WEEK;
            this.days = hours / HOURS_IN_DAY;
            this.hours = hours % HOURS_IN_DAY;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Walltime should be in format <hours>:<minutes>:<seconds> and should contain only numbers!");
        }

        if (weeks < 0) {
            throw new IllegalArgumentException("Number of weeks must be positive number");
        }
        if (days < 0) {
            throw new IllegalArgumentException("Number of days must be positive number");
        }
        if (hours < 0) {
            throw new IllegalArgumentException("Number of hours must be positive number");
        }
        if (minutes < 0) {
            throw new IllegalArgumentException("Number of minutes must be positive number");
        }
        if (seconds < 0) {
            throw new IllegalArgumentException("Number of seconds must be positive number");
        }

    }

    @Override
    public String toString() {
        return (weeks * HOURS_IN_WEEK) + (days * HOURS_IN_DAY) + hours + ":" + minutes + ":" + seconds;
    }

}
