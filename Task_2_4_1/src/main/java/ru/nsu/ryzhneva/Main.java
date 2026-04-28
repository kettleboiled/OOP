package ru.nsu.ryzhneva;

import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import ru.nsu.ryzhneva.domain.CourseConfig;
import ru.nsu.ryzhneva.results.TargetResolver;
import ru.nsu.ryzhneva.runner.BuildRunner;
import ru.nsu.ryzhneva.runner.GradleRunner;
import ru.nsu.ryzhneva.services.AssessmentService;
import ru.nsu.ryzhneva.services.GitService;

/**
 * Главный класс и точка входа в приложение (автоматическая проверка задач по ООП).
 * Инициализирует консольные потоки, загружает DSL-конфигурационный файл,
 * разрешает требуемые зависимости из него и запускает {@link AssessmentService}.
 */
public class Main {

    /**
     * Точка входа.
     *
     * @param args аргументы
     */
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        String configPath = args.length > 0 ? args[0] : "config.dsl";
        File configFile = new File(configPath);

        CourseConfig config;
        try {
            config = new ParseCourseConfig(configFile).parse();
        } catch (ConfigParseException e) {
            System.err.println("Не удалось загрузить конфигурацию курса.");
            System.err.println(e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Причина: " + e.getCause().getMessage());
            }
            return;
        }

        System.out.println("Конфигурация успешно загружена!");
        System.out.println("Количество групп: " + config.getGroups().size());
        System.out.println("Количество задач: " + config.getTasks().size());

        System.out.println("Запуск процесса проверки...");

        File workspaceDir = new File(System.getProperty("user.dir"), "check_workspace");
        if (!workspaceDir.exists()) {
            boolean created = workspaceDir.mkdirs();
            if (!created) {
                System.err.println(
                        "Не удалось создать рабочую директорию: "
                                + workspaceDir.getAbsolutePath()
                );
                return;
            }
        }

        CommandExecutor commandExecutor = new CommandExecutor();
        GitService gitService = new GitService(commandExecutor);
        BuildRunner buildRunner = new GradleRunner(commandExecutor);
        TargetResolver resolver = new TargetResolver(config);
        HtmlReportGenerator reportGenerator = new HtmlReportGenerator();

        AssessmentService engine = new AssessmentService(
                resolver,
                gitService,
                buildRunner,
                workspaceDir,
                config,
                reportGenerator
        );
        engine.run();
    }
}