package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import ru.nsu.ryzhneva.results.StudentResult;
import ru.nsu.ryzhneva.results.TargetResolver;
import ru.nsu.ryzhneva.runner.BuildRunner;
import ru.nsu.ryzhneva.services.AssessmentService;
import ru.nsu.ryzhneva.services.GitService;

/**
 * Unit-тесты для {@link AssessmentService}.
 * В тестах внешние действия (git/gradle) подменяются фейковыми реализациями,
 * а результаты JUnit подготавливаются как XML-файлы во временной директории.
 */
public class AssessmentServiceTest {

    /**
     * Генератор данных, переданных в HTML-отчет.
     */
    private static class CapturingHtmlReportGenerator extends HtmlReportGenerator {

        private List<StudentResult> capturedResults;
        private List<Task> capturedTasks;
        private CourseConfig capturedConfig;

        @Override
        public void generateReport(List<StudentResult> results, List<Task> tasks, CourseConfig config) {
            this.capturedResults = results;
            this.capturedTasks = tasks;
            this.capturedConfig = config;
        }
    }

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
        Files.writeString(
                xml,
                "<testsuite tests=\"5\" failures=\"1\" skipped=\"1\" errors=\"1\" name=\"x\"></testsuite>"
        );

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

        CapturingHtmlReportGenerator capturingGenerator = new CapturingHtmlReportGenerator();

        AssessmentService service = new AssessmentService(
                resolver,
                gitService,
                buildRunner,
                workDir.toFile(),
                config,
                capturingGenerator
        );
        service.run();

        assertNotNull(capturingGenerator.capturedResults);
        assertNotNull(capturingGenerator.capturedTasks);
        assertNotNull(capturingGenerator.capturedConfig);

        assertEquals(1, capturingGenerator.capturedResults.size());
        StudentResult sr = capturingGenerator.capturedResults.getFirst();
        assertEquals(1.0, sr.getActivityPercentage(), 1e-9);

        assertTrue(sr.getTaskResults().containsKey("Task_2_3_1"));
        double points = sr.getTaskResults().get("Task_2_3_1").totalPoints;
        assertEquals(3.5, points, 1e-9);

        assertEquals(8, config.getScoreExcellent());
        assertEquals(6, config.getScoreGood());
        assertEquals(4, config.getScoreSatisfactory());
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

        CapturingHtmlReportGenerator capturingGenerator = new CapturingHtmlReportGenerator();

        AssessmentService service = new AssessmentService(
                resolver,
                gitService,
                buildRunner,
                workDir.toFile(),
                config,
                capturingGenerator
        );
        service.run();

        assertNotNull(capturingGenerator.capturedResults);
        assertEquals(1, capturingGenerator.capturedResults.size());

        StudentResult sr = capturingGenerator.capturedResults.getFirst();
        assertTrue(sr.getTaskResults().containsKey("Task_2_3_1"));

        double points = sr.getTaskResults().get("Task_2_3_1").totalPoints;
        assertEquals(0.0, points, 1e-9);
    }
}
