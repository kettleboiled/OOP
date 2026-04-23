package ru.nsu.ryzhneva.domain;

/**
 * Объектная модель студента на курсе.
 * Сохраняет идентификационную информацию:
 * имя, логин GitHub и ссылку на репозиторий с задачками.
 */
public class Student {
    private String githubUsername;
    private String fullName;
    private String repositoryUrl;

    /**
     * @return логин студента на площадке GitHub
     */
    public String getGithubUsername() {
        return githubUsername;
    }

    /**
     * Устанавливает GitHub-логин данного студента.
     * @param githubUsername логин
     */
    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    /**
     * @return полное имя (ФИО) студента, используемое при формировании отчетов
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Устанавливает ФИО студента.
     * @param fullName полное имя студента
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return ссылка на git-репозиторий студента для проверки задач
     */
    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    /**
     * Устанавливает ссылку на репозиторий.
     * @param repositoryUrl URL репозитория студента
     */
    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    @Override
    public String toString() {
        return "Student{" +
                "githubUsername='" + githubUsername + '\'' +
                ", fullName='" + fullName + '\'' +
                ", repositoryUrl='" + repositoryUrl + '\'' +
                '}';
    }
}
