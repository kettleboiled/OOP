package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.domain.CourseConfig;
import ru.nsu.ryzhneva.domain.Group;
import ru.nsu.ryzhneva.domain.Student;
import ru.nsu.ryzhneva.domain.Task;
import ru.nsu.ryzhneva.results.TargetResolver;

/**
 * Unit-тесты для {@link AssessmentService}.
 * В тестах внешние действия (git/gradle) подменяются фейковыми реализациями,
 * а результаты JUnit подготавливаются как XML-файлы во временной директории.
 */
public class AssessmentServiceTest {

    @Test
    void runParsesJUnitXmlAndComputesPoints() throws Exception {
        CourseConfig config = new CourseConfig();
        config.setGradeCriteria(8, 6, 4);

        Task t = new Task();
        t.setId("Task_2_3_1");
        t.setName("Змейка");
        t.setMaxPoints(10);
        t.setSoftDeadline("2026-04-04");
        t.setHardDeadline("2026-04-13");
        config.addTask(t);

        Student s = new Student();
        s.setGithubUsername("kettleboiled");
        s.setFullName("Ryzhneva Anastasia Victorovna");
        s.setRepositoryUrl("https://github.com/kettleboiled/OOP");
        Group g = new Group("24216");
        g.addStudent(s);
        config.addGroup(g);

        Path workDir = Files.createTempDirectory("assessment");
        File studentRepoDir = workDir.resolve("kettleboiled").toFile();
        assertTrue(studentRepoDir.mkdirs());

        File taskDir = new File(studentRepoDir, "Task_2_3_1");
        assertTrue(taskDir.mkdirs());

        File testResultsDir = new File(taskDir, "build/test-results/test");
        assertTrue(testResultsDir.mkdirs());
        Path xml = testResultsDir.toPath().resolve("TEST-sample.xml");
        Files.writeString(xml,
                "<testsuite tests=\"5\" failures=\"1\" " +
                        "skipped=\"1\" errors=\"1\" name=\"x\"></testsuite>");

        GitService gitService = new GitService(new CommandExecutor()) {
            @Override
            public boolean clone(String repoUrl, File targetDir) {
                return true;
            }

            @Override
            public LocalDate getFirstCommitWhenBuildBecameOk(File repoDir, String taskDirName) {
                return LocalDate.of(2026, 4, 10);
            }

            @Override
            public double calculateActivityPercentage(File repoDir) {
                return 1.0;
            }
        };

        BuildRunner buildRunner = new BuildRunner() {
            @Override
            public boolean compile(File dir) {
                return true;
            }

            @Override
            public boolean checkStyle(File dir) {
                return true;
            }

            @Override
            public boolean runTests(File dir) {
                return true;
            }

            @Override
            public boolean generateJavadoc(File dir) {
                return true;
            }
        };

        TargetResolver resolver = new TargetResolver(config) {
            @Override
            public List<Student> resolveStudents() {
                return List.of(s);
            }

            @Override
            public List<Task> resolveTasks() {
                return List.of(t);
            }
        };

        Path report = Path.of("report.html");
        Files.deleteIfExists(report);
        try {
            AssessmentService service =
                    new AssessmentService(resolver, gitService, buildRunner, workDir.toFile(), config);
            service.run();

            assertTrue(Files.exists(report));
            String html = Files.readString(report);
            assertNotNull(html);
            assertTrue(html.contains("Отчет о проверке задач"));
            assertTrue(html.contains(">3.5<"));
            assertTrue(html.contains("100%</td><td>Неуд</td>"));
        } finally {
            Files.deleteIfExists(report);
        }
    }

    @Test
    void runDoesNotComputePointsWhenNoTestReports() throws Exception {
        CourseConfig config = new CourseConfig();

        Task t = new Task();
        t.setId("Task_2_3_1");
        t.setName("Змейка");
        t.setMaxPoints(10);
        config.addTask(t);

        Student s = new Student();
        s.setGithubUsername("kettleboiled");
        s.setFullName("Ryzhneva Anastasia Victorovna");
        s.setRepositoryUrl("https://github.com/kettleboiled/OOP");
        Group g = new Group("24216");
        g.addStudent(s);
        config.addGroup(g);

        Path workDir = Files.createTempDirectory("assessment_notests");
        File studentRepoDir = workDir.resolve("kettleboiled").toFile();
        assertTrue(studentRepoDir.mkdirs());
        File taskDir = new File(studentRepoDir, "Task_2_3_1");
        assertTrue(taskDir.mkdirs());

        GitService gitService = new GitService(new CommandExecutor()) {
            @Override
            public boolean clone(String repoUrl, File targetDir) {
                return true;
            }

            @Override
            public LocalDate getFirstCommitWhenBuildBecameOk(File repoDir, String taskDirName) {
                return null;
            }

            @Override
            public double calculateActivityPercentage(File repoDir) {
                return 1.0;
            }
        };

        BuildRunner buildRunner = new BuildRunner() {
            @Override
            public boolean compile(File dir) {
                return true;
            }

            @Override
            public boolean checkStyle(File dir) {
                return true;
            }

            @Override
            public boolean runTests(File dir) {
                return true;
            }

            @Override
            public boolean generateJavadoc(File dir) {
                return true;
            }
        };

        TargetResolver resolver = new TargetResolver(config) {
            @Override
            public List<Student> resolveStudents() {
                return List.of(s);
            }

            @Override
            public List<Task> resolveTasks() {
                return List.of(t);
            }
        };

        Path report = Path.of("report.html");
        Files.deleteIfExists(report);
        try {
            AssessmentService service =
                    new AssessmentService(resolver, gitService, buildRunner, workDir.toFile(), config);
            service.run();
            assertTrue(Files.exists(report));
            String html = Files.readString(report);
            assertTrue(html.contains(">0<"));
        } finally {
            Files.deleteIfExists(report);
        }
    }
}


