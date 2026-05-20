package ru.nsu.ryzhneva.results;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.domain.CheckAssignment;
import ru.nsu.ryzhneva.domain.CourseConfig;
import ru.nsu.ryzhneva.domain.Group;
import ru.nsu.ryzhneva.domain.Student;
import ru.nsu.ryzhneva.domain.Task;

/**
 * Набор unit-тестов для пакета {@code ru.nsu.ryzhneva.results}.
 * Проверяет корректность DTO-моделей результатов и работы {@link TargetResolver}.
 */
public class ResultsTest {

    @Test
    void testTaskResult() {
        TaskResult tr = new TaskResult();
        
        assertEquals("0/0/0", tr.getTestsString());
        
        tr.compiled = true;
        tr.testsPassed = 5;
        tr.testsFailed = 2;
        tr.testsSkipped = 1;
        
        assertEquals("5/2/1", tr.getTestsString());
    }

    @Test
    void testStudentResult() {
        Student s = new Student();
        s.setFullName("Petr");
        
        StudentResult sr = new StudentResult(s);
        sr.setActivityPercentage(0.75);
        sr.addTaskResult("Task1", new TaskResult());
        
        assertEquals(0.75, sr.getActivityPercentage());
        assertEquals("Petr", sr.getStudent().getFullName());
        assertTrue(sr.getTaskResults().containsKey("Task1"));
    }

    @Test
    void testTargetResolver() {
        CourseConfig config = new CourseConfig();
        
        Task t1 = new Task();
        t1.setId("Task_2_3_1");
        Task t2 = new Task();
        t2.setId("T2");
        config.addTask(t1);
        config.addTask(t2);

        Student s1 = new Student();
        s1.setGithubUsername("kettleboiled");
        Student s2 = new Student();
        s2.setGithubUsername("s2");
        
        Group g1 = new Group("24216");
        g1.addStudent(s1);
        Group g2 = new Group("G2");
        g2.addStudent(s2);
        
        config.addGroup(g1);
        config.addGroup(g2);
        
        CheckAssignment ca = new CheckAssignment();
        ca.addTask("Task_2_3_1");
        ca.addTarget("24216");
        ca.addTarget("s2");
        config.setCheckAssignment(ca);
        
        TargetResolver resolver = new TargetResolver(config);
        
        List<Task> resolvedTasks = resolver.resolveTasks();
        assertEquals(1, resolvedTasks.size());
        assertEquals("Task_2_3_1", resolvedTasks.get(0).getId());
        
        List<Student> resolvedStudents = resolver.resolveStudents();
        assertEquals(2, resolvedStudents.size());
        assertTrue(resolvedStudents.contains(s1));
        assertTrue(resolvedStudents.contains(s2));
    }
}
