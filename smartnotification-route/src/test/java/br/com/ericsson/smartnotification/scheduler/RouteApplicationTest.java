package br.com.ericsson.smartnotification.scheduler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit RouteValidateExpirationComponentTest for simple App.
 */
public class RouteApplicationTest 
    extends TestCase
{
    /**
     * Create the RouteValidateExpirationComponentTest case
     *
     * @param testName name of the RouteValidateExpirationComponentTest case
     */
    public RouteApplicationTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( RouteApplicationTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
