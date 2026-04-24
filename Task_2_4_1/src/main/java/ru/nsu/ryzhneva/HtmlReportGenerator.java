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
     * @param tasks список проверяемых задач (лабораторных)
     *
     * @param config конфигурация курса для получения порогов оценивания (отлично, хорошо и т.д.)
     */
    public void generateReport(List<StudentResult> results, List<Task> tasks, CourseConfig config) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n")
                .append("<title>OOP Checker Report</title>\n");
        html.append("<style>\n");
        html.append("body { font-family: monospace; }\n");
        html.append("table { border-collapse: collapse; width: 100%; margin-bottom: 30px; }\n");
        html.append("th, td { border: 1px solid black; padding: 8px; text-align: center; }\n");
        html.append(".left-align { text-align: left; }\n");
        html.append(".header-row { background-color: #f2f2f2; }\n");
        html.append("</style>\n</head>\n<body>\n");

        html.append("<h3>Отчет о проверке задач ООП</h3>\n");

        for (Task task : tasks) {
            html.append("<table>\n");
            html.append("<tr class=\"header-row\"><th colspan=\"7\" class=\"left-align\">")
                    .append("Лабораторная ")
                    .append(task.getId()).append(" (").append(task.getName())
                    .append(")</th></tr>\n");

            html.append("<tr><th>Студент</th><th>Сборка</th><th>Документация</th><th>")
                    .append("Style guide</th><th>Тесты</th><th>Доп. балл</th><th>")
                    .append("Общий балл</th></tr>\n");

            for (StudentResult studentRes : results) {
                TaskResult tr = studentRes.getTaskResults()
                        .getOrDefault(task.getId(), new TaskResult());

                html.append("<tr>")
                        .append("<td class=\"left-align\">")
                        .append(studentRes.getStudent().getFullName()).append("</td>")
                        .append("<td>").append(tr.compiled ? "+" : "-").append("</td>")
                        .append("<td>").append(tr.docsGenerated ? "+" : "-").append("</td>")
                        .append("<td>").append(tr.styleOk ? "+" : "-").append("</td>")
                        .append("<td>").append(tr.getTestsString()).append("</td>")
                        .append("<td>").append(formatPoints(tr.bonusPoints)).append("</td>")
                        .append("<td>").append(formatPoints(tr.totalPoints)).append("</td>")
                        .append("</tr>\n");
            }
            html.append("</table>\n");
        }

        html.append("<table>\n");
        int colspan = tasks.size() + 4;
        html.append("<tr class=\"header-row\"><th colspan=\"").append(colspan)
                .append("\" class=\"left-align\">Общая статистика</th></tr>\n");

        html.append("<tr><th>Студент</th>");
        for (Task task : tasks) {
            html.append("<th>").append(task.getId()).append("</th>");
        }
        html.append("<th>Сумма</th><th>Активность</th><th>Оценка</th></tr>\n");

        for (StudentResult studentRes : results) {
            html.append("<tr><td class=\"left-align\">")
                    .append(studentRes.getStudent().getFullName()).append("</td>");

            double totalSum = 0.0;
            for (Task task : tasks) {
                TaskResult tr = studentRes.getTaskResults()
                        .getOrDefault(task.getId(), new TaskResult());
                html.append("<td>").append(formatPoints(tr.totalPoints)).append("</td>");
                totalSum += tr.totalPoints;
            }

            String activityStr = (int) (studentRes.getActivityPercentage() * 100) + "%";
            String finalGrade = "-";
            
            if (totalSum > 0.0) {
                double score = totalSum * studentRes.getActivityPercentage();
                if (score >= config.getScoreExcellent()) {
                    finalGrade = "Отлично";
                } else if (score >= config.getScoreGood()) {
                    finalGrade = "Хорошо";
                } else if (score >= config.getScoreSatisfactory()) {
                    finalGrade = "Удовл";
                } else {
                    finalGrade = "Неуд";
                }
            }

            html.append("<td>").append(formatPoints(totalSum)).append("</td>")
                    .append("<td>").append(activityStr).append("</td>")
                    .append("<td>").append(finalGrade).append("</td>")
                    .append("</tr>\n");
        }
        html.append("</table>\n");

        html.append("</body>\n</html>");

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
}

