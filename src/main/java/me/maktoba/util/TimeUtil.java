package me.maktoba.util;

import java.time.Duration;

/**
 * Time helper class which is used to format song durations
 * for a few music commands.
 */
public class TimeUtil {

    /**
     * Formats a nice String of the song duration to return to user.
     * @param duration - The duration of the song, unformatted.
     * @return - A formatted duration of the song
     */
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