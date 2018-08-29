package br.com.ericsson.smartnotification.util.test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.utils.DateUtil;

public class DateUtilTest {
    
    @Test
    public void convertStringToDate() throws ApplicationException {
        int day = 12;
        int month = 10;
        int year = 2018;
                
        Calendar date = ((Calendar)Calendar.getInstance().clone());
        date.setTime(DateUtil.parseToDate(year + "-" + month + "-" + day));
        
        Calendar dateTest = ((Calendar)Calendar.getInstance().clone());
        dateTest.set(Calendar.YEAR, year);
        dateTest.set(Calendar.MONTH, month -1);
        dateTest.set(Calendar.DAY_OF_MONTH, day);
        
        assertEquals(date.get(Calendar.YEAR), dateTest.get(Calendar.YEAR));
        assertEquals(date.get(Calendar.MONTH), dateTest.get(Calendar.MONTH));
        assertEquals(date.get(Calendar.DAY_OF_MONTH), dateTest.get(Calendar.DAY_OF_MONTH));
    }
    
    @Test
    public void convertStringToDateTime() throws ApplicationException {
        int day = 12;
        int month = 10;
        int year = 2018;
        int h = 12;
        int m = 10;
        int s = 54;
                
        Calendar date = ((Calendar)Calendar.getInstance().clone());
        date.setTime(DateUtil.parseToDateTime(year + "-" + month + "-" + day + " " + h + ":" + m + ":" + s));
        
        Calendar dateTest = ((Calendar)Calendar.getInstance().clone());
        dateTest.set(Calendar.YEAR, year);
        dateTest.set(Calendar.MONTH, month -1);
        dateTest.set(Calendar.DAY_OF_MONTH, day);
        dateTest.set(Calendar.HOUR_OF_DAY, h);
        dateTest.set(Calendar.MINUTE, m);
        dateTest.set(Calendar.SECOND, s);
        
        assertEquals(date.get(Calendar.YEAR), dateTest.get(Calendar.YEAR));
        assertEquals(date.get(Calendar.MONTH), dateTest.get(Calendar.MONTH));
        assertEquals(date.get(Calendar.DAY_OF_MONTH), dateTest.get(Calendar.DAY_OF_MONTH));
        assertEquals(date.get(Calendar.HOUR_OF_DAY), dateTest.get(Calendar.HOUR_OF_DAY));
        assertEquals(date.get(Calendar.MINUTE), dateTest.get(Calendar.MINUTE));
        assertEquals(date.get(Calendar.SECOND), dateTest.get(Calendar.SECOND));
    }

}
