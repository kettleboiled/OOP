package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;

class GradeTest {

    @Test
    void testGetScoreValues() {
        assertEquals(5, Grade.EXCELLENT.getScore());
        assertEquals(4, Grade.GOOD.getScore());
        assertEquals(3, Grade.SATISFACTORY.getScore());
        assertEquals(2, Grade.UNSATISFACTORY.getScore());
        assertEquals(1, Grade.PASS.getScore());
        assertEquals(-1, Grade.FAIL.getScore());
    }

    @Test
    void testIsGraded() {
        assertTrue(Grade.EXCELLENT.isGraded());
        assertTrue(Grade.GOOD.isGraded());
        assertTrue(Grade.SATISFACTORY.isGraded());
        assertTrue(Grade.UNSATISFACTORY.isGraded());
        assertFalse(Grade.PASS.isGraded());
        assertFalse(Grade.FAIL.isGraded());
        assertFalse(Grade.UNDEFINED.isGraded());
    }

    @Test
    void testIsBadGrade() {
        assertTrue(Grade.SATISFACTORY.isBadGrade());
        assertTrue(Grade.UNSATISFACTORY.isBadGrade());
        assertTrue(Grade.FAIL.isBadGrade());
        assertFalse(Grade.EXCELLENT.isBadGrade());
        assertFalse(Grade.GOOD.isBadGrade());
        assertFalse(Grade.PASS.isBadGrade());
    }
}