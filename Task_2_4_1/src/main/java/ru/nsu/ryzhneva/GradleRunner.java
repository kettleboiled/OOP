package ru.nsu.ryzhneva;

import java.io.File;

/**
 * Реализация {@link BuildRunner} для проектов, использующих систему сборки Gradle.
 * Все задачи (компиляция, проверка стиля, тестирование и генерация документации)
 * выполняются через локальный выполняемый файл оболочки (gradlew).
 */
public class GradleRunner implements BuildRunner {
    private final CommandExecutor executor;

    public GradleRunner(CommandExecutor executor) {
        this.executor = executor;
    }

    /**
     * Вспомогательный метод для решения проблемы "Permission denied" (error 13).
     * Выдает скачанному файлу gradlew права на исполнение.
     */
    private void ensureExecutable(File dir) {
        File gradlew = new File(dir, "gradlew");
        if (gradlew.exists()) {
            gradlew.setExecutable(true);
        }
    }

    @Override
    public boolean compile(File dir) {
        ensureExecutable(dir);
        return executor.execute(dir, "./gradlew", "classes");
    }

    @Override
    public boolean checkStyle(File dir) {
        ensureExecutable(dir);
        return executor.execute(dir, "./gradlew", "checkstyleMain");
    }

    @Override
    public boolean runTests(File dir) {
        ensureExecutable(dir);
        return executor.execute(dir, "./gradlew", "test");
    }

    @Override
    public boolean generateJavadoc(File dir) {
        ensureExecutable(dir);
        return executor.execute(dir, "./gradlew", "javadoc");
    }
}
