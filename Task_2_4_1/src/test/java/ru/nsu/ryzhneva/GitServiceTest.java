package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.services.GitService;

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
    void calculateActivityPercentageInWindowCountsOnlySemesterWeeks() throws Exception {
        Path repo = Files.createTempDirectory("gitrepo_activity_window");
        exec(repo, "git", "init");
        exec(repo, "git", "config", "user.email", "test@example.com");
        exec(repo, "git", "config", "user.name", "Test User");

        commitWithDate(repo, "2025-09-01", "c1");
        commitWithDate(repo, "2025-09-02", "c2");
        commitWithDate(repo, "2025-09-09", "c3");
        commitWithDate(repo, "2026-01-10", "c4");

        GitService service = new GitService(new CommandExecutor());
        LocalDate start = LocalDate.parse("2025-09-01");
        LocalDate end = LocalDate.parse("2025-12-31");
        double activity = service.calculateActivityPercentage(repo.toFile(), start, end);

        long weeksTotal = java.time.temporal.ChronoUnit.WEEKS.between(start, end) + 1;
        assertEquals(2.0 / weeksTotal, activity);
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

    private static void commitWithDate(Path repo, String isoDate, String message) throws Exception {
        Path file = repo.resolve("f_" + message + ".txt");
        Files.writeString(file, message);
        exec(repo, "git", "add", ".");

        ProcessBuilder pb = new ProcessBuilder("git", "commit", "-m", message);
        pb.directory(repo.toFile());
        pb.redirectErrorStream(true);
        pb.environment().put("GIT_AUTHOR_DATE", isoDate + "T12:00:00");
        pb.environment().put("GIT_COMMITTER_DATE", isoDate + "T12:00:00");
        Process p = pb.start();
        if (p.waitFor() != 0) {
            throw new IllegalStateException("Commit failed for " + isoDate);
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



