package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.domain.CourseConfig;
import ru.nsu.ryzhneva.domain.Task;
import ru.nsu.ryzhneva.results.StudentResult;

public class ServicesTest {

    @Test
    void testCommandExecutor() {
        CommandExecutor executor = new CommandExecutor();
        boolean res = executor.execute(new File("."), "echo", "test");
        assertTrue(res);
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
    void testHtmlReportGeneratorEmpty() {
        HtmlReportGenerator gen = new HtmlReportGenerator();
        List<StudentResult> results = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
        CourseConfig config = new CourseConfig();
        config.setGradeCriteria(8, 6, 4);

        assertDoesNotThrow(() -> gen.generateReport(results, tasks, config));
    }
}

