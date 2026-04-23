package ru.nsu.ryzhneva;

import groovy.lang.GroovyShell;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.nsu.ryzhneva.domain.CourseConfig;
import ru.nsu.ryzhneva.dsl.CourseDSLScript;
import ru.nsu.ryzhneva.results.TargetResolver;

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

        if (!configFile.exists()) {
            System.err.println("Файл конфигурации не найден: " + configFile.getAbsolutePath());
            System.exit(1);
        }

        CourseConfig config = parseConfig(configFile);

        if (config != null) {
            System.out.println("Конфигурация успешно загружена!");
            System.out.println("Количество групп: " + config.getGroups().size());
            System.out.println("Количество задач: " + config.getTasks().size());

            System.out.println("Запуск процесса проверки...");

            File workspaceDir = new File(System.getProperty("user.dir"), "check_workspace");
            if (!workspaceDir.exists()) {
                workspaceDir.mkdirs();
            }

            CommandExecutor commandExecutor = new CommandExecutor();
            GitService gitService = new GitService(commandExecutor);
            BuildRunner buildRunner = new GradleRunner(commandExecutor);
            TargetResolver resolver = new TargetResolver(config);

            AssessmentService engine = new AssessmentService(resolver,
                    gitService, buildRunner, workspaceDir, config);
            engine.run();
        }
    }

    /**
     * Парсит Groovy DSL-файл конфигурации курса, используя {@link GroovyShell} 
     * и базовый скрипт {@link CourseDSLScript}.
     *
     * @param configFile файл конфигурации
     *
     * @return объект {@link CourseConfig}, заполненный данными из DSL, или null в случае ошибки
     */
    private static CourseConfig parseConfig(File configFile) {
        CompilerConfiguration compilerConfig = new CompilerConfiguration();
        compilerConfig.setScriptBaseClass(CourseDSLScript.class.getName());
        GroovyShell shell = new GroovyShell(compilerConfig);

        try {
            CourseDSLScript script = (CourseDSLScript) shell.parse(configFile);
            script.run();
            return script.getConfig();
        } catch (Exception e) {
            System.err.println("Ошибка при разборе DSL-скрипта: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}