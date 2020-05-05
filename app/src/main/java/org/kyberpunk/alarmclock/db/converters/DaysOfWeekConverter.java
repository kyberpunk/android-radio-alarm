package org.kyberpunk.alarmclock.db.converters;

import androidx.room.TypeConverter;

import java.util.Calendar;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DaysOfWeekConverter {
    @TypeConverter
    public static Set<Integer> toDaysOfWeek(byte daysOfWeekByte) {
        return IntStream.range(1, 8)
                .filter(d -> flagSet(daysOfWeekByte, d))
                .boxed()
                .collect(Collectors.toSet());
    }

    @TypeConverter
    public static byte fromDaysOfWeek(Set<Integer> daysOfWeek){
        byte daysOfWeekByte = 0;
        for (int d : daysOfWeek) {
            daysOfWeekByte |= (byte)(1 << d);
        }
        return daysOfWeekByte;
    }

    private static boolean flagSet(byte flags, int exponent) {
        boolean result = (flags & (byte)(1 << exponent)) != 0;
        return result;
    }
}
