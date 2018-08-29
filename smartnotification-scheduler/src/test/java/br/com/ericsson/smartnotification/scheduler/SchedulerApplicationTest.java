package br.com.ericsson.smartnotification.scheduler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit SchedulerValidationShippingRestrictionsComponentTest for simple App.
 */
public class SchedulerApplicationTest 
    extends TestCase
{
    /**
     * Create the SchedulerValidationShippingRestrictionsComponentTest case
     *
     * @param testName name of the SchedulerValidationShippingRestrictionsComponentTest case
     */
    public SchedulerApplicationTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( SchedulerApplicationTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
