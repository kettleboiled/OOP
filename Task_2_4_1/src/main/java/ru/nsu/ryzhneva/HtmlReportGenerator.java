package ru.nsu.ryzhneva;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import ru.nsu.ryzhneva.domain.CourseConfig;
import ru.nsu.ryzhneva.domain.Task;
import ru.nsu.ryzhneva.results.StudentResult;
import ru.nsu.ryzhneva.results.TaskResult;

/**
 * Генератор HTML-отчета о проверках студенческих работ.
 * Формирует визуальное представление статистики выполненных заданий, 
 * баллов, активности и итоговой оценки в виде таблицы.
 */
public class HtmlReportGenerator {

    private static final String HTML_HEAD = """
            <!DOCTYPE html>
            <html>
            <head>
            <meta charset="UTF-8">
            <title>OOP Checker Report</title>
            <style>
            body { font-family: monospace; }
            table { border-collapse: collapse; width: 100%; margin-bottom: 30px; }
            th, td { border: 1px solid black; padding: 8px; text-align: center; }
            .left-align { text-align: left; }
            .header-row { background-color: #f2f2f2; }
            </style>
            </head>
            <body>

            <h3>Отчет о проверке задач ООП</h3>
            """;

    private static final String HTML_FOOT = """
            </body>
            </html>
            """;

    private static final String TASK_TABLE_TEMPLATE = """
            <table>
            <tr class="header-row"><th colspan="7" class="left-align">Лабораторная %s (%s)</th></tr>
            <tr>
              <th>Студент</th>
              <th>Сборка</th>
              <th>Документация</th>
              <th>Style guide</th>
              <th>Тесты</th>
              <th>Доп. балл</th>
              <th>Общий балл</th>
            </tr>
            %s
            </table>
            """;

    private static final String TASK_ROW_TEMPLATE = """
            <tr>
              <td class="left-align">%s</td>
              <td>%s</td>
              <td>%s</td>
              <td>%s</td>
              <td>%s</td>
              <td>%s</td>
              <td>%s</td>
            </tr>
            """;

    private static final String SUMMARY_TABLE_TEMPLATE = """
            <table>
            <tr class="header-row"><th colspan="%d" class="left-align">Общая статистика</th></tr>
            <tr>
              <th>Студент</th>
            %s
              <th>Сумма</th>
              <th>Активность</th>
              <th>Оценка</th>
            </tr>
            %s
            </table>
            """;

    private static final String SUMMARY_TASK_HEADER_TEMPLATE = """
              <th>%s</th>
            """;

    private static final String SUMMARY_ROW_TEMPLATE = """
            <tr>
              <td class="left-align">%s</td>
            %s
              <td>%s</td>
              <td>%s</td>
              <td>%s</td>
            </tr>
            """;

    private static String formatPoints(double points) {
        if (points == Math.rint(points)) {
            return String.valueOf((int) points);
        }
        return String.valueOf(points);
    }

    /**
     * Генерирует HTML-файл на основе собранных результатов и конфигурации курса.
     * Отчет сохраняется в файл report.html.
     * 
     *
     * @param results список результатов студентов по проверяемому курсу
     *
     * @param tasks список проверяемых задач
     *
     * @param config конфигурация курса для получения порогов оценивания
     */
    public void generateReport(List<StudentResult> results,
                               List<Task> tasks,
                               CourseConfig config) {
        StringBuilder html = new StringBuilder();
        html.append(HTML_HEAD);

        for (Task task : tasks) {
            String rows = renderTaskRows(results, task);
            html.append(TASK_TABLE_TEMPLATE.formatted(task.getId(), task.getName(), rows));
        }

        html.append(renderSummaryTable(results, tasks, config));
        html.append(HTML_FOOT);

        String finalHtml = html.toString();

        System.out.println("\n--- HTML ОТЧЕТ ---");
        System.out.println(finalHtml);

        try {
            Files.writeString(
                    Path.of("report.html"),
                    finalHtml
            );
            System.out.println("\nОтчет успешно сохранен в файл: report.html");
        } catch (IOException e) {
            System.err.println("Не удалось сохранить файл отчета: " + e.getMessage());
        }
    }

    private static String renderTaskRows(List<StudentResult> results, Task task) {
        StringBuilder rows = new StringBuilder();
        for (StudentResult studentRes : results) {
            TaskResult tr = studentRes
                    .getTaskResults()
                    .getOrDefault(task.getId(), new TaskResult());
            rows.append(renderTaskRow(studentRes, tr));
        }
        return rows.toString();
    }

    private static String renderTaskRow(StudentResult studentRes, TaskResult tr) {
        return TASK_ROW_TEMPLATE.formatted(
                studentRes.getStudent().getFullName(),
                tr.compiled ? "+" : "-",
                tr.docsGenerated ? "+" : "-",
                tr.styleOk ? "+" : "-",
                tr.getTestsString(),
                formatPoints(tr.bonusPoints),
                formatPoints(tr.totalPoints)
        );
    }

    private static String renderSummaryTable(List<StudentResult> results,
                                            List<Task> tasks,
                                            CourseConfig config) {
        int colspan = tasks.size() + 4;

        StringBuilder taskHeaders = new StringBuilder();
        for (Task task : tasks) {
            taskHeaders.append(SUMMARY_TASK_HEADER_TEMPLATE.formatted(task.getId()));
        }

        StringBuilder rows = new StringBuilder();
        for (StudentResult studentRes : results) {
            rows.append(renderSummaryRow(studentRes, tasks, config));
        }

        return SUMMARY_TABLE_TEMPLATE.formatted(
                colspan,
                taskHeaders.toString(),
                rows.toString()
        );
    }

    private static String renderSummaryRow(StudentResult studentRes,
                                          List<Task> tasks,
                                          CourseConfig config) {
        double totalSum = 0.0;
        StringBuilder taskCells = new StringBuilder();
        for (Task task : tasks) {
            TaskResult tr = studentRes.getTaskResults().getOrDefault(task.getId(), new TaskResult());
            taskCells.append("<td>").append(formatPoints(tr.totalPoints)).append("</td>\n");
            totalSum += tr.totalPoints;
        }

        String activityStr = (int) (studentRes.getActivityPercentage() * 100) + "%";
        String finalGrade = calculateFinalGrade(
                totalSum,
                studentRes.getActivityPercentage(),
                config
        );

        return SUMMARY_ROW_TEMPLATE.formatted(
                studentRes.getStudent().getFullName(),
                taskCells.toString(),
                formatPoints(totalSum),
                activityStr,
                finalGrade
        );
    }

    private static String calculateFinalGrade(double totalSum,
                                              double activity,
                                              CourseConfig config) {
        if (totalSum <= 0.0) {
            return "-";
        }

        double score = totalSum * activity;
        if (score >= config.getScoreExcellent()) {
            return "Отлично";
        }
        if (score >= config.getScoreGood()) {
            return "Хорошо";
        }
        if (score >= config.getScoreSatisfactory()) {
            return "Удовл";
        }
        return "Неуд";
    }
}

