package ru.nsu.ryzhneva.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.HashSet;
import java.util.Set;

import ru.nsu.ryzhneva.CommandExecutor;

/**
 * Сервис для взаимодействия с системой контроля версий Git. 
 * Предоставляет методы для работы с репозиториями студентов: их скачивания
 * и анализа коммитов для расчета активности.
 */
public class GitService {
    private final CommandExecutor executor;

    /**
     * Конструтор.
     *
     * @param executor выполняет консольные команды
     */
    public GitService(CommandExecutor executor) {
        this.executor = executor;
    }

    /**
     * Клонирует удаленный репозиторий из GitHub.
     * Если директория уже существует, считает клонирование успешным.
     *
     * @param repoUrl ссылка на репозиторий
     *
     * @param targetDir директория, куда репозиторий будет склонирован
     *
     * @return true - клонирование завершено успешно
     */
    public boolean clone(String repoUrl, File targetDir) {
        if (targetDir.exists()) {
            return true;
        }

        return executor.execute(targetDir.getParentFile(),
                "git", "clone", repoUrl, targetDir.getName());
    }

    /**
     * Вычисляет процент активности студента.
     * Студент считается активным на протяжении недели,
     * если он сделал хотя бы один коммит.
     *
     * @param repoDir директория с репозиторием
     *
     * @return коэффициент активности: от 0.0 до 1.0
     */
    public double calculateActivityPercentage(File repoDir) {
        return calculateActivityPercentage(repoDir, null, null);
    }

    /**
     * Вычисляет процент активности студента в рамках заданного учебного окна.
     * Неделя считается активной, если в ней был хотя бы один коммит.
     *
     * @param repoDir директория с репозиторием
     * @param start начало окна (включительно) или {@code null}
     * @param end конец окна (включительно) или {@code null}
     * @return коэффициент активности: от 0.0 до 1.0
     */
    public double calculateActivityPercentage(File repoDir, LocalDate start, LocalDate end) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "git", "log", "--pretty=format:%cd", "--date=short");
            pb.directory(repoDir);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            Set<String> weeksWithCommits = new HashSet<>();
            LocalDate firstCommit = null;
            LocalDate lastCommit = null;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    LocalDate date = LocalDate.parse(line);
                    if (firstCommit == null || date.isBefore(firstCommit)) {
                        firstCommit = date;
                    }
                    if (lastCommit == null || date.isAfter(lastCommit)) {
                        lastCommit = date;
                    }

                    if (start != null && date.isBefore(start)) {
                        continue;
                    }
                    if (end != null && date.isAfter(end)) {
                        continue;
                    }
                    
                    int weekYear = date.get(IsoFields.WEEK_BASED_YEAR);
                    int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                    weeksWithCommits.add(weekYear + "-" + week);
                } catch (DateTimeParseException e) {
                    System.err.println("Предупреждение: невозможно распарсить "
                            + "дату коммита '" + line + "' в директории " + repoDir.getName());
                }
            }
            process.waitFor();

            if (firstCommit == null) {
                return 0.0;
            }

            LocalDate startDate = start != null ? start : firstCommit;
            LocalDate endDate = end != null ? end : lastCommit;

            if (endDate.isBefore(startDate)) {
                return 0.0;
            }

            long weeksTotal = ChronoUnit.WEEKS.between(startDate, endDate) + 1;
            if (weeksTotal <= 0) {
                weeksTotal = 1;
            }

            return Math.min(1.0, (double) weeksWithCommits.size() / weeksTotal);
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Возвращает дату первого коммита, после которого задачу можно считать "сданной".
     *
     * @param repoDir директория локального git-репозитория
     * @param taskDirName имя директории задачи
     *
     * @return дата потенциальной "сдачи" или {@code null}
     */
    public LocalDate getFirstCommitWhenBuildBecameOk(File repoDir, String taskDirName) {
        try {
            String markerPath = taskDirName + "/build.gradle";
            ProcessBuilder pb = new ProcessBuilder(
                    "git", "log", "--reverse",
                    "--pretty=format:%cd", "--date=short", "--", markerPath);
            pb.directory(repoDir);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                process.waitFor();
                if (line == null || line.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(line.trim());
            }
        } catch (Exception e) {
            return null;
        }
    }
}
