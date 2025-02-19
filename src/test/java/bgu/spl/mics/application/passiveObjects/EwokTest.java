package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class EwokTest {
    private Ewok testEwok;
    @BeforeEach
    void setUp() {
        testEwok = new Ewok(1);
    }

    @Test
    void testAcquire() {
        assertTrue(testEwok.available);
        testEwok.acquire();
        assertFalse(testEwok.available);
    }

    @Test
    void testRelease() {
        assertTrue(testEwok.available);
        testEwok.acquire();
        assertFalse(testEwok.available);
        testEwok.release();
        assertTrue(testEwok.available);
    }
}