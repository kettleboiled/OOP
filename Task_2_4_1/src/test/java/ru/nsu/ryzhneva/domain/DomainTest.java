package ru.nsu.ryzhneva.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Тесты domain.
 */
public class DomainTest {

    @Test
    void testCheckpoint() {
        Checkpoint cp = new Checkpoint();
        cp.setName("Midterm");
        cp.setDate("2024-11-15");
        
        assertEquals("Midterm", cp.getName());
        assertEquals(LocalDate.of(2024, 11, 15), cp.getDate());
    }

    @Test
    void testStudentAndGroup() {
        Student s = new Student();
        s.setFullName("Ryzhneva Anastasia Victorovna");
        s.setGithubUsername("kettleboiled");
        s.setRepositoryUrl("https://github.com/kettleboiled/OOP");

        assertEquals("Ryzhneva Anastasia Victorovna", s.getFullName());
        assertEquals("kettleboiled", s.getGithubUsername());
        
        Group g = new Group("24216");
        g.addStudent(s);
        
        assertEquals("24216", g.getName());
        assertEquals(1, g.getStudents().size());
        assertEquals(s, g.getStudents().get(0));
    }

    @Test
    void testTask() {
        Task t = new Task();
        t.setId("Task_2_3_1");
        t.setName("Змейка");
        t.setMaxPoints(1);
        t.setSoftDeadline("2026-04-04");
        t.setHardDeadline("2026-04-13");

        assertEquals("Task_2_3_1", t.getId());
        assertEquals("Змейка", t.getName());
        assertEquals(1, t.getMaxPoints());
        assertEquals(LocalDate.of(2026, 4, 4), t.getSoftDeadline());
        assertEquals(LocalDate.of(2026, 4, 13), t.getHardDeadline());
    }

    @Test
    void testCourseConfigAndCheckAssignment() {
        CourseConfig config = new CourseConfig();
        config.setGradeCriteria(8, 6, 4);

        assertEquals(8, config.getScoreExcellent());
        assertEquals(6, config.getScoreGood());
        assertEquals(4, config.getScoreSatisfactory());

        CheckAssignment ca = new CheckAssignment();
        ca.addTask("Task_2_3_1");
        ca.addTarget("24216");

        config.setCheckAssignment(ca);

        assertNotNull(config.getCheckAssignment());
        assertTrue(config.getCheckAssignment().getTaskIds().contains("Task_2_3_1"));
        assertTrue(config.getCheckAssignment().getTargetIdentifiers().contains("24216"));
    }
}
