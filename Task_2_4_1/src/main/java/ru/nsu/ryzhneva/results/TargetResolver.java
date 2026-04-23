package ru.nsu.ryzhneva.results;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.ryzhneva.domain.CourseConfig;
import ru.nsu.ryzhneva.domain.Group;
import ru.nsu.ryzhneva.domain.Student;
import ru.nsu.ryzhneva.domain.Task;

/**
 * Транслирует списки "абстрактных строк", заданных
 * в конфигурации проверки (никнеймы студентов или названия групп),
 * в реальные списки Java-объектов {@link Student} и {@link Task}.
 */
public class TargetResolver {
    private final CourseConfig config;

    /**
     * Конструктор.
     *
     * @param config конфигурация
     */
    public TargetResolver(CourseConfig config) {
        this.config = config;
    }

    /**
     * Извлекает плоский список студентов, чьи работы требуется проверить.
     * Анализирует: если строка представляет собой группу, то извлекаются
     * все студенты этой группы. Если это никнейм на Github, то находится только он.
     *
     * @return финальный список всех студентов,
     * чьи репозитории необходимо склонировать
     */
    public List<Student> resolveStudents() {
        List<Student> result = new ArrayList<>();

        List<String> identifiers = config.getCheckAssignment().getTargetIdentifiers();
        if (identifiers == null) {
            return result;
        }

        for (String id : identifiers) {
            for (Group group : config.getGroups()) {
                if (group.getName().equals(id)) {
                    result.addAll(group.getStudents());
                } else {
                    for (Student student : group.getStudents()) {
                        if (student.getGithubUsername().equals(id) && !result.contains(student)) {
                            result.add(student);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Извлекает список задач, которые ожидаются к проверке у выбранных студентов.
     * Ищет полное описание  каждой задачи из пула.
     *
     * @return список извлекаемых задач с учетом их метаинформации
     */
    public List<Task> resolveTasks() {
        List<Task> result = new ArrayList<>();

        List<String> taskIds = config.getCheckAssignment().getTaskIds();
        if (taskIds == null) {
            return result;
        }

        for (String id : taskIds) {
            for (Task task : config.getTasks()) {
                if (task.getId().equals(id)) {
                    result.add(task);
                    break;
                }
            }
        }
        return result;
    }
}
