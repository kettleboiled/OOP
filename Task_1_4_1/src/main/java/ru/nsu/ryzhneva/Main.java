package ru.nsu.ryzhneva;

import ru.nsu.ryzhneva.gradebook.Discipline;
import ru.nsu.ryzhneva.gradebook.Semester;
import ru.nsu.ryzhneva.gradebook.Student;
import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;
import ru.nsu.ryzhneva.gradebook.typesandgrades.TypeOfControl;

import java.time.LocalDate;

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
        System.out.println("Student: Ivanov Ivan, Semester: "
                + student.getCurrentSemester());
        Semester sem1 = new Semester(1);
        Discipline math = new Discipline("Math Analysis",
                TypeOfControl.EXAM, Grade.UNSATISFACTORY);

        sem1.addDiscipline(math);
        sem1.addDiscipline(new Discipline("History",
                TypeOfControl.CREDIT, Grade.PASS));
        sem1.addDiscipline(new Discipline("Programming",
                TypeOfControl.DIFF_CREDIT, Grade.EXCELLENT));

        student.getGradeBook().addSemester(sem1);

        System.out.println("\n--- Before Retake ---");
        System.out.printf("Average Score: %.2f%n", student.getGradeBook().getAverageScore());
        System.out.println("Stipend: " + (student.hasIncreasedScholarship() ? "Yes" : "No"));

        math.retake(Grade.GOOD, LocalDate.now().plusDays(7));

        System.out.println("\n--- After Retake (Math -> GOOD) ---");
        System.out.printf("Average Score: %.2f%n", student.getGradeBook().getAverageScore());
        System.out.println("Stipend: " + (student.hasIncreasedScholarship() ? "Yes" : "No"));

        math.retake(Grade.EXCELLENT, LocalDate.now().plusDays(14));
        System.out.println("\n--- After Second Retake (Math -> EXCELLENT) ---");
        System.out.printf("Average Score: %.2f%n", student.getGradeBook().getAverageScore());
        System.out.println("Stipend: " + (student.hasIncreasedScholarship() ? "Yes" : "No"));

        student.moveToNextSemester();
        System.out.println("\n=== Moved to Semester: "
                + student.getCurrentSemester() + " ===");

        Semester sem2 = new Semester(2);
        sem2.addDiscipline(new Discipline("Discrete Math",
                TypeOfControl.EXAM, Grade.EXCELLENT));
        sem2.addDiscipline(new Discipline("Imperative Programming",
                TypeOfControl.EXAM, Grade.EXCELLENT));
        sem2.addDiscipline(new Discipline("PE",
                TypeOfControl.CREDIT, Grade.PASS));

        student.getGradeBook().addSemester(sem2);

        System.out.printf("Total Average Score: %.2f%n",
                student.getGradeBook().getAverageScore());

        student.setIsBudget(false);
        System.out.println("Transfer to budget (from paid)? "
                + (student.transferToBudget() ? "Yes" : "No"));

        Semester sem8 = new Semester(8);
        sem8.addDiscipline(new Discipline("Final Thesis",
                TypeOfControl.THESIS_DEFENSE, Grade.EXCELLENT));
        student.getGradeBook().addSemester(sem8);

        System.out.println("Is it possible to get Red Diploma? "
                + (student.getGradeBook().canGetRedDiploma() ? "Yes" : "No"));
    }
}