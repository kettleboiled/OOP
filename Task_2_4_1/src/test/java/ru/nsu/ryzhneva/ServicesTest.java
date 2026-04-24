package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.domain.CourseConfig;
import ru.nsu.ryzhneva.domain.Student;
import ru.nsu.ryzhneva.domain.Task;
import ru.nsu.ryzhneva.results.StudentResult;
import ru.nsu.ryzhneva.results.TaskResult;

/**
 * Tests for system services.
 */
public class ServicesTest {

    @Test
    void testCommandExecutor() {
        CommandExecutor executor = new CommandExecutor();
        boolean res = executor.execute(new File("."), "echo", "test");
        assertTrue(res);
    }

    @Test
    void testCommandExecutorNonExistingCommandReturnsFalse() {
        CommandExecutor executor = new CommandExecutor();
        boolean res = executor.execute(new File("."), "definitely_not_a_command_12345");
        assertFalse(res);
    }

    @Test
    void testGitServiceCloneDry() {
        CommandExecutor executor = new CommandExecutor() {
            @Override
            public boolean execute(File dir, String... cmd) {
                return true;
            }
        };
        GitService git = new GitService(executor);
        File dummyDir = new File("build/dummy_target_" + System.currentTimeMillis());
        boolean success = git.clone("http://example.com/repo", dummyDir);
        assertTrue(success);
    }

    @Test
    void testGitServiceCloneTargetDirExistsSkipsExecutor() throws Exception {
        final boolean[] called = {false};
        CommandExecutor executor = new CommandExecutor() {
            @Override
            public boolean execute(File dir, String... cmd) {
                called[0] = true;
                return false;
            }
        };
        GitService git = new GitService(executor);

        Path existing = Files.createTempDirectory("repo_exists");
        assertTrue(git.clone("http://example.com/repo", existing.toFile()));
        assertFalse(called[0]);
    }

    @Test
    void testGradleRunnerCallsGradlewAndMakesItExecutable() throws Exception {
        Path projectDir = Files.createTempDirectory("gradlerunner");
        Path gradlew = projectDir.resolve("gradlew");
        Files.writeString(gradlew, "#!/bin/sh\necho ok\n");
        assertTrue(gradlew.toFile().setExecutable(false));

        final StringBuilder invoked = new StringBuilder();
        CommandExecutor executor = new CommandExecutor() {
            @Override
            public boolean execute(File dir, String... cmd) {
                invoked.append(String.join(" ", cmd));
                return true;
            }
        };
        GradleRunner runner = new GradleRunner(executor);
        assertTrue(runner.compile(projectDir.toFile()));
        assertTrue(gradlew.toFile().canExecute());
        assertTrue(invoked.toString().contains("./gradlew"));
        assertTrue(invoked.toString().contains("classes"));
    }

    @Test
    void testGradleRunnerCallsExpectedTasks() throws Exception {
        Path projectDir = Files.createTempDirectory("gradlerunner_tasks");
        
        final List<String> commands = new ArrayList<>();
        CommandExecutor executor = new CommandExecutor() {
            @Override
            public boolean execute(File dir, String... cmd) {
                commands.add(String.join(" ", cmd));
                return true;
            }
        };

        GradleRunner runner = new GradleRunner(executor);
        assertTrue(runner.compile(projectDir.toFile()));
        assertTrue(runner.checkStyle(projectDir.toFile()));
        assertTrue(runner.runTests(projectDir.toFile()));
        assertTrue(runner.generateJavadoc(projectDir.toFile()));

        assertTrue(commands.contains("./gradlew classes"));
        assertTrue(commands.contains("./gradlew checkstyleMain"));
        assertTrue(commands.contains("./gradlew test"));
        assertTrue(commands.contains("./gradlew javadoc"));
    }

    @Test
    void testHtmlReportGeneratorEmpty() {
        HtmlReportGenerator gen = new HtmlReportGenerator();
        List<StudentResult> results = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
        CourseConfig config = new CourseConfig();
        config.setGradeCriteria(8, 6, 4);

        assertDoesNotThrow(() -> gen.generateReport(results, tasks, config));
    }

    @Test
    void testHtmlReportGeneratorGradesBranches() throws Exception {
        CourseConfig config = new CourseConfig();
        config.setGradeCriteria(8, 6, 4);

        Task task = new Task();
        task.setId("Task_2_3_1");
        task.setName("Змейка");
        task.setMaxPoints(10);

        Student s1 = new Student();
        s1.setGithubUsername("kettleboiled");
        s1.setFullName("Ryzhneva Anastasia Victorovna");
        s1.setRepositoryUrl("https://github.com/kettleboiled/OOP");
        StudentResult r1 = new StudentResult(s1);
        r1.setActivityPercentage(1.0);
        TaskResult tr1 = new TaskResult();
        tr1.totalPoints = 9.0;
        r1.addTaskResult(task.getId(), tr1);

        Student s2 = new Student();
        s2.setGithubUsername("kettleboiled2");
        s2.setFullName("Someone Else");
        s2.setRepositoryUrl("https://github.com/kettleboiled/OOP");
        StudentResult r2 = new StudentResult(s2);
        r2.setActivityPercentage(0.5);
        TaskResult tr2 = new TaskResult();
        tr2.totalPoints = 0.0;
        r2.addTaskResult(task.getId(), tr2);

        Path report = Path.of("report.html");
        Files.deleteIfExists(report);
        try {
            HtmlReportGenerator gen = new HtmlReportGenerator();
            gen.generateReport(List.of(r1, r2), List.of(task), config);
            assertTrue(Files.exists(report));
            String text = Files.readString(report);
            assertNotNull(text);
            assertTrue(text.contains("Отлично"));
            assertTrue(text.contains("50%</td><td>-</td>"));
        } finally {
            Files.deleteIfExists(report);
        }
    }
}
