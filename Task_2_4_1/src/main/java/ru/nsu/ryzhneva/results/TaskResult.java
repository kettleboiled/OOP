package ru.nsu.ryzhneva.results;

/**
 * Объект-контейнер DTO, хранящий результаты выполнения определенной
 * задачи для одного студента.
 */
public class TaskResult {
    public boolean compiled = false;
    public boolean docsGenerated = false;
    public boolean styleOk = false;

    public int testsPassed = 0;
    public int testsFailed = 0;
    public int testsSkipped = 0;

    public double bonusPoints = 0.0;
    public double totalPoints = 0.0;

    /**
     * Форматирует строку пройденных/проваленных тестов для генерации отчета HTML.
     * Если не скомпилировалось или тестов не было, возвращает 0/0/0.
     * 
     *
     * @return формат: "{успешные}/{упавшие}/{пропущенные}"
     */
    public String getTestsString() {
        if (!compiled || (testsPassed == 0 && testsFailed == 0 && testsSkipped == 0)) {
            return "0/0/0";
        }
        return testsPassed + "/" + testsFailed + "/" + testsSkipped;
    }
}
