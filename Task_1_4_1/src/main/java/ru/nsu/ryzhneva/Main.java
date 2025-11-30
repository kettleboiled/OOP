package ru.nsu.ryzhneva;

import ru.nsu.ryzhneva.gradebook.Discipline;
import ru.nsu.ryzhneva.gradebook.Semester;
import ru.nsu.ryzhneva.gradebook.Student;
import java.time.LocalDate;
import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;
import ru.nsu.ryzhneva.gradebook.typesandgrades.TypeOfControl;

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
        System.out.println("Student created: " + "Ivanov Ivan");

        System.out.println("\n--- Semester 1 Start ---");
        Semester sem1 = new Semester(1);

        Discipline math = new Discipline(
                "Math Analysis", TypeOfControl.EXAM, Grade.UNSATISFACTORY);
        sem1.addDiscipline(math);

        sem1.addDiscipline(new Discipline(
                "History", TypeOfControl.CREDIT, Grade.PASS));
        sem1.addDiscipline(new Discipline(
                "Programming", TypeOfControl.DIFF_CREDIT, Grade.EXCELLENT));

        student.getGradeBook().addSemester(sem1);

        System.out.println("Current Math Grade: " + math.getGrade());
        System.out.printf("Average Score: %.2f%n",
                student.getGradeBook().getAverageScore());
        System.out.println("Is increased stipend possible? "
                + (student.hasIncreasedScholarship() ? "Yes" : "No"));

        System.out.println("\n... Retaking Math (getting GOOD) ...");
        math.retake(Grade.GOOD, LocalDate.now().plusDays(7));

        System.out.printf("Average Score: %.2f%n",
                student.getGradeBook().getAverageScore());
        System.out.println("Is increased stipend possible? "
                + (student.hasIncreasedScholarship() ? "Yes" : "No"));

        System.out.println("\n... Retaking Math again (getting EXCELLENT) ...");
        math.retake(Grade.EXCELLENT, LocalDate.now().plusDays(14));

        System.out.printf("Average Score: %.2f%n",
                student.getGradeBook().getAverageScore());
        System.out.println("Is increased stipend possible? "
                + (student.hasIncreasedScholarship() ? "Yes" : "No"));

        student.moveToNextSemester();
        System.out.println("\n--- Moved to Semester: "
                + student.getCurrentSemester() + " ---");

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
        System.out.println("Current status: Paid education");

        boolean canTransfer = student.transferToBudget();
        System.out.println("Transfer to budget possible? "
                + (canTransfer ? "Yes" : "No"));

        System.out.println("\n--- Diploma Defense ---");
        Semester sem8 = new Semester(8);
        sem8.addDiscipline(new Discipline("Final Thesis",
                TypeOfControl.THESIS_DEFENSE, Grade.EXCELLENT));
        student.getGradeBook().addSemester(sem8);

        System.out.println("Is Red Diploma possible? "
                + (student.getGradeBook().canGetRedDiploma() ? "Yes" : "No"));
    }
}