package ru.nsu.ryzhneva;

import java.io.File;

import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.nsu.ryzhneva.domain.CourseConfig;
import ru.nsu.ryzhneva.dsl.CourseDSLScript;

/**
 * Парсер DSL-конфигурации курса.
 * Инкапсулирует создание {@link GroovyShell} и чтение {@code config.dsl}.
 */
public class ParseCourseConfig {
    private final File configFile;

    /**
     * Конструктор.
     *
     * @param configFile конфигурационный файл
     */
    public ParseCourseConfig(File configFile) {
        this.configFile = configFile;
    }

    /**
     * Парсит DSL-файл конфигурации курса.
     *
     * @return объект {@link CourseConfig}, заполненный данными из DSL
     * @throws ConfigParseException исключение
     */
    public CourseConfig parse() throws ConfigParseException {
        if (configFile == null) {
            throw new ConfigParseException("Config file is null");
        }
        if (!configFile.exists()) {
            throw new ConfigParseException(
                    "Файл конфигурации не найден: "
                            + configFile.getAbsolutePath()
            );
        }

        CompilerConfiguration compilerConfig = new CompilerConfiguration();
        compilerConfig.setScriptBaseClass(CourseDSLScript.class.getName());
        GroovyShell shell = new GroovyShell(compilerConfig);

        try {
            CourseDSLScript script = (CourseDSLScript) shell.parse(configFile);
            script.run();
            return script.getConfig();
        } catch (Exception e) {
            throw new ConfigParseException("Ошибка при разборе DSL-скрипта", e);
        }
    }
}
