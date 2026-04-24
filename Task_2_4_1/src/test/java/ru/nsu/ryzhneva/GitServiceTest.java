package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Тесты для {@link GitService}.
 *
 * <p>Тесты создают временный git-репозиторий и коммиты локально, поэтому не
 * требуют сети и не зависят от GitHub.
 */
public class GitServiceTest {

    @Test
    void calculateActivityPercentageEmptyRepoReturnsZero() throws Exception {
        Path repo = Files.createTempDirectory("gitrepo_empty");
        exec(repo, "git", "init");

        GitService service = new GitService(new CommandExecutor());
        double activity = service.calculateActivityPercentage(repo.toFile());
        assertEquals(0.0, activity);
    }

    @Test
    void getFirstCommitWhenBuildBecameOkFindsFirstMarkerCommit() throws Exception {
        Path repo = Files.createTempDirectory("gitrepo_marker");
        exec(repo, "git", "init");
        exec(repo, "git", "config", "user.email", "test@example.com");
        exec(repo, "git", "config", "user.name", "Test User");

        Files.writeString(repo.resolve("README.md"), "hello");
        exec(repo, "git", "add", ".");
        exec(repo, "git", "commit", "-m", "init");

        Path taskDir = repo.resolve("Task_2_3_1");
        Files.createDirectories(taskDir);
        Files.writeString(taskDir.resolve("build.gradle"), "// marker");
        exec(repo, "git", "add", ".");
        exec(repo, "git", "commit", "-m", "add marker");

        String expected = capture(repo).trim();
        assertFalse(expected.isEmpty());

        GitService service = new GitService(new CommandExecutor());
        LocalDate date = service.getFirstCommitWhenBuildBecameOk(repo.toFile(), "Task_2_3_1");
        assertNotNull(date);
        assertEquals(LocalDate.parse(expected), date);
    }

    @Test
    void getFirstCommitWhenBuildBecameOkReturnsNullWhenMarkerMissing() throws Exception {
        Path repo = Files.createTempDirectory("gitrepo_nomarker");
        exec(repo, "git", "init");
        exec(repo, "git", "config", "user.email", "test@example.com");
        exec(repo, "git", "config", "user.name", "Test User");

        Files.writeString(repo.resolve("README.md"), "hello");
        exec(repo, "git", "add", ".");
        exec(repo, "git", "commit", "-m", "init");

        GitService service = new GitService(new CommandExecutor());
        assertNull(service.getFirstCommitWhenBuildBecameOk(repo.toFile(), "Task_2_3_1"));
    }

    private static void exec(Path dir, String... cmd) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(dir.toFile());
        pb.redirectErrorStream(true);
        Process p = pb.start();
        if (p.waitFor() != 0) {
            throw new IllegalStateException("Command failed: " + String.join(" ", cmd));
        }
    }

    private static String capture(Path dir) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(
                "git", "log", "--reverse", "-1", "--pretty=format:%cd", "--date=short", "--",
                "Task_2_3_1/build.gradle");
        pb.directory(dir.toFile());
        pb.redirectErrorStream(true);
        Process p = pb.start();
        byte[] bytes = p.getInputStream().readAllBytes();
        if (p.waitFor() != 0) {
            throw new IllegalStateException("Command failed: git log ...");
        }
        return new String(bytes);
    }
}



