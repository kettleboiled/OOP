package ru.nsu.ryzhneva;

import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;
import ru.nsu.ryzhneva.gradebook.typesandgrades.TypeOfControl;
import ru.nsu.ryzhneva.gradebook.Discipline;
import ru.nsu.ryzhneva.gradebook.Semester;
import ru.nsu.ryzhneva.gradebook.Student;

/**
 * Класс Main.
 */
public class Main {
    /**
     * Точка входа.
     *
     * @param args аргументы.
     */
    public static void main(String[] args) {
        System.out.println("=== Start System ===");
        Student student = new Student("Ivanov Ivan", true, 12345);
        System.out.println("Student: Ivanov Ivan, Semester: " + student.getCurrentSemester());

        Semester sem1 = new Semester(1);
        sem1.addDiscipline(new Discipline("Math Analysis", TypeOfControl.EXAM, Grade.GOOD)); // 4
        sem1.addDiscipline(new Discipline("History", TypeOfControl.CREDIT, Grade.PASS));
        sem1.addDiscipline(new Discipline("Programming", TypeOfControl.DIFF_CREDIT, Grade.EXCELLENT));

        student.getGradeBook().addSemester(sem1);

        System.out.println("Stipend (Sem 1): " + (student.hasIncreasedScholarship() ? "Yes" : "No"));

        student.moveToNextSemester();
        System.out.println("\nMoved to Semester: " + student.getCurrentSemester());

        Semester sem2 = new Semester(2);
        sem2.addDiscipline(new Discipline("Discrete Math", TypeOfControl.EXAM, Grade.EXCELLENT));
        sem2.addDiscipline(new Discipline("Imperative Programming", TypeOfControl.EXAM, Grade.EXCELLENT));
        sem2.addDiscipline(new Discipline("PE", TypeOfControl.CREDIT, Grade.PASS));

        student.getGradeBook().addSemester(sem2);
        System.out.printf("1. Average Score: %.2f%n", student.getGradeBook().getAverageScore());

        System.out.println("2. Is it possible to get Increased Scholarship (Sem 2)? "
                + (student.hasIncreasedScholarship() ? "Yes" : "No"));

        student.setIsBudget(false);
        System.out.println("3. Transfer to budget (from paid)? "
                + (student.transferToBudget() ? "Yes" : "No"));

        Semester sem8 = new Semester(8);
        sem8.addDiscipline(new Discipline("Final Thesis", TypeOfControl.THESIS_DEFENSE, Grade.EXCELLENT));
        student.getGradeBook().addSemester(sem8);

        System.out.println("4. Is it possible to get Red Diploma? "
                + (student.getGradeBook().canGetRedDiploma() ? "Yes" : "No"));
    }
}