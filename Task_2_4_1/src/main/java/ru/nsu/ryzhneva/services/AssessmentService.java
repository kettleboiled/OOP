package ru.nsu.ryzhneva.services;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.nsu.ryzhneva.runner.BuildRunner;
import ru.nsu.ryzhneva.HtmlReportGenerator;
import ru.nsu.ryzhneva.domain.CourseConfig;
import ru.nsu.ryzhneva.domain.Student;
import ru.nsu.ryzhneva.domain.Task;
import ru.nsu.ryzhneva.results.StudentResult;
import ru.nsu.ryzhneva.results.TargetResolver;
import ru.nsu.ryzhneva.results.TaskResult;

/**
 * Основной класс системы проверки задач.
 * Оркестрирует процесс: скачивание репозиториев студентов, подсчет активности, 
 * сборка проектов, запуск тестов, парсинг их результатов и формирование HTML-отчета.
 */
public class AssessmentService {

    private final GitService gitService;
    private final BuildRunner buildRunner;
    private final TargetResolver resolver;
    private final File workingDirectory;
    private final CourseConfig config;
    private final HtmlReportGenerator reportGenerator;

    /**
     * Конструктор.
     *
     * @param resolver переводит строковые значения данных
     *     в объекты соответствующего типа
     *
     * @param gitService работа с гитом
     *
     * @param buildRunner раннер для сборки и тестирования задачи
     *
     * @param workingDirectory рабочая директория
     *
     * @param config конфигурация
     */
    public AssessmentService(TargetResolver resolver,
                             GitService gitService,
                             BuildRunner buildRunner,
                             File workingDirectory,
                             CourseConfig config,
                             HtmlReportGenerator reportGenerator) {
        this.resolver = resolver;
        this.gitService = gitService;
        this.buildRunner = buildRunner;
        this.workingDirectory = workingDirectory;
        this.config = config;
        this.reportGenerator = reportGenerator;
    }

    /**
     * Основной метод запуска системы оценивания.
     * Резолвит студентов и задачи, последовательно применяет к ним BuildRunner.
     * В конце генерирует итоговый HTML отчет.
     */
    public void run() {
        List<Student> students = resolver.resolveStudents();
        List<Task> tasks = resolver.resolveTasks();

        List<StudentResult> courseResults = new ArrayList<>();

        for (Student student : students) {
            StudentResult studentResult = new StudentResult(student);
            File studentDir = new File(workingDirectory, student.getGithubUsername());

            if (!gitService.clone(student.getRepositoryUrl(), studentDir)) {
                courseResults.add(studentResult);
                continue;
            }

            double activity = gitService.calculateActivityPercentage(studentDir);
            studentResult.setActivityPercentage(activity);

            for (Task task : tasks) {
                TaskResult taskResult = new TaskResult();
                File taskDir = new File(studentDir, task.getId());
                if (!taskDir.exists()) {
                    studentResult.addTaskResult(task.getId(), taskResult);
                    continue;
                }

                if (buildRunner.compile(taskDir)) {
                    taskResult.compiled = true;

                    if (buildRunner.generateJavadoc(taskDir)) {
                        taskResult.docsGenerated = true;
                    }

                    if (buildRunner.checkStyle(taskDir)) {
                        taskResult.styleOk = true;
                    }

                    buildRunner.runTests(taskDir);
                    parseTestResults(taskDir, taskResult);

                    int totalTests = taskResult.testsPassed + taskResult.testsFailed
                            + taskResult.testsSkipped;
                    if (totalTests > 0) {
                        double fraction = (double) taskResult.testsPassed / totalTests;
                        double points = Math.round(task.getMaxPoints() * fraction);
                        points += taskResult.bonusPoints;

                        LocalDate submitDate = gitService.getFirstCommitWhenBuildBecameOk(
                                studentDir, task.getId());
                        if (submitDate != null) {
                            if (task.getHardDeadline() != null
                                    && submitDate.isAfter(task.getHardDeadline())) {
                                points = 0.0;
                            } else if (task.getSoftDeadline() != null
                                    && submitDate.isAfter(task.getSoftDeadline())) {
                                points = Math.max(0.0, points - 0.5);
                            }
                        }

                        taskResult.totalPoints = points;
                    }
                }
                studentResult.addTaskResult(task.getId(), taskResult);
            }
            courseResults.add(studentResult);
        }
        reportGenerator.generateReport(courseResults, tasks, config);
    }

    /**
     * Анализирует XML-результаты запущенных JUnit-тестов (директория build/test-results/test/).
     * Записывает количество успешных/проваленных/пропущенных тестов в объект {@link TaskResult}.
     * 
     *
     * @param taskDir директория задачи с собранным проектом и выполненными тестами
     *
     * @param taskResult модель результатов проверки, куда записывается статистика выполнения тестов
     */
    private void parseTestResults(File taskDir, TaskResult taskResult) {
        File testResultsDir = new File(taskDir, "build/test-results/test/");
        if (!testResultsDir.exists() || !testResultsDir.isDirectory()) {
            return;
        }

        File[] xmlFiles = testResultsDir.listFiles((dir, name) -> name.endsWith(".xml"));
        if (xmlFiles == null) {
            return;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            for (File xmlFile : xmlFiles) {
                Document doc = builder.parse(xmlFile);
                NodeList testSuites = doc.getElementsByTagName("testsuite");
                
                for (int i = 0; i < testSuites.getLength(); i++) {
                    Element suite = (Element) testSuites.item(i);
                    int tests = Integer.parseInt(suite.getAttribute("tests"));
                    int failures = Integer.parseInt(suite.getAttribute("failures"));
                    int skipped = Integer.parseInt(suite.getAttribute("skipped"));
                    int errors = Integer.parseInt(suite.getAttribute("errors"));
                    
                    taskResult.testsPassed += (tests - failures - skipped - errors);
                    taskResult.testsFailed += (failures + errors);
                    taskResult.testsSkipped += skipped;
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing XML report: " + e.getMessage());
        }
    }
}
