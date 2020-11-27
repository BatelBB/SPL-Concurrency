package bgu.spl.mics.application.passiveObjects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class EwokTest {

    private Ewok testEwok;

    @Before
    public void setUp() throws Exception {
        testEwok = new Ewok();
    }

    @Test
    public void testAcquire() {
        assertTrue(testEwok.available);
        testEwok.acquire();
        assertFalse(testEwok.available);
    }

    @Test
    public void testRelease() {
        assertFalse(testEwok.available);
        testEwok.release();
        assertTrue(testEwok.available);
    }
}