package ru.nsu.ryzhneva.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Корневой объект доменной модели, генерируемый DSL-скриптом.
 * Хранит полную информацию о курсе: список задач {@link Task}, групп {@link Group}, 
 * контрольных точек {@link Checkpoint}, критериев перевода баллов в оценки и
 * непосредственно текущего задания на проверку {@link CheckAssignment}.
 */
public class CourseConfig {
    private List<Task> tasks = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<Checkpoint> checkpoints = new ArrayList<>();
    private CheckAssignment checkAssignment = new CheckAssignment();

    private int scoreExcellent;
    private int scoreGood;
    private int scoreSatisfactory;

    /**
     * @return полный список задач
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Регистрация задачи.
     * @param task настроенный объект {@link Task}
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * @return полный список групп в текущей конфигурации
     */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * Добавляет академическую группу.
     * @param group сконфигурированная группа {@link Group}
     */
    public void addGroup(Group group) {
        this.groups.add(group);
    }

    /**
     * @return список контрольных точек курса
     */
    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    /**
     * Добавление контрольной точки (дедлайна или рубежного контроля).
     * @param checkpoint объект {@link Checkpoint}
     */
    public void addCheckpoint(Checkpoint checkpoint) {
        this.checkpoints.add(checkpoint);
    }

    /**
     * Конфигурирует критерии оценки, задавая нижние пороги накопленных баллов
     * (с учетом активности), необходимых для получения той или иной оценки.
     *
     * @param exc минимальный балл для оценки "Отлично"
     * @param good минимальный балл для оценки "Хорошо"
     * @param sat минимальный балл для оценки "Удовлетворительно"
     */
    public void setGradeCriteria(int exc, int good, int sat) {
        this.scoreExcellent = exc;
        this.scoreGood = good;
        this.scoreSatisfactory = sat;
    }

    /**
     * @return нижний балльный порог оценки "Отлично"
     */
    public int getScoreExcellent() {
        return scoreExcellent;
    }

    /**
     * @return нижний балльный порог оценки "Хорошо"
     */
    public int getScoreGood() {
        return scoreGood;
    }

    /**
     * @return нижний балльный порог оценки "Удовлетворительно"
     */
    public int getScoreSatisfactory() {
        return scoreSatisfactory;
    }

    /**
     * Возвращает настройки конкретного сеанса проверки.
     * Задается в DSL-регламенте через блок {check}.
     *
     * @return объект {@link CheckAssignment}
     */
    public CheckAssignment getCheckAssignment() {
        return checkAssignment;
    }

    /**
     * Привязывает настройки сеанса проверки.
     * @param checkAssignment Задание проверки
     */
    public void setCheckAssignment(CheckAssignment checkAssignment) {
        this.checkAssignment = checkAssignment;
    }

}
