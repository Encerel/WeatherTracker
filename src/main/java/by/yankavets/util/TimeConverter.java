package by.yankavets.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

@UtilityClass
public class TimeConverter {

    public LocalTime convertFromUTC(long secondsSinceUnixEpoch) {
        Instant instant = Instant.ofEpochSecond(secondsSinceUnixEpoch);
        return instant.atZone(ZoneId.systemDefault()).toLocalTime();
    }
}
