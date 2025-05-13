package me.maktoba.util;

import java.time.Duration;

public class TimeUtil {

    public static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60
        );

        return seconds < 0 ? "-" + positive : positive;
    }

    public static String formatMillis(long millis) {
        return formatDuration(Duration.ofMillis(millis));
    }
}