package br.com.ericsson.smartnotification.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;

public final class DateUtil {
    
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    private DateUtil() {
    }
    
    public static Date parseToDate(String value) throws ApplicationException {
        try {
            return new SimpleDateFormat(PATTERN_DATE).parse(value);
        } catch (ParseException e) {
            throw new ApplicationException(String.format("Erro na conversão para data do valor %s", value));
        }
    }
    
    public static String parseDateToString(Date date) {
        return new SimpleDateFormat(PATTERN_DATE).format(date);
    }
    
    public static String parseDateTimeToString(Date date) {
        return new SimpleDateFormat(PATTERN_DATE + " " + PATTERN_TIME).format(date);
    }
    
    public static Date parseToDateTime(String value) throws ApplicationException {
        try {
            return new SimpleDateFormat(PATTERN_DATE + " " + PATTERN_TIME).parse(value);
        } catch (ParseException e) {
            throw new ApplicationException(String.format("Erro na conversão para data do valor %s", value));
        }
    }
    
    
    public static DayOfWeek getDayOfWeek(Date date) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return dateTime.getDayOfWeek();
    }

}
