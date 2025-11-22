package ru.nsu.ryzhneva;

import ru.nsu.ryzhneva.gradebook.DisciplineData;
import ru.nsu.ryzhneva.gradebook.Grade;
import ru.nsu.ryzhneva.gradebook.GradeBook;
import ru.nsu.ryzhneva.gradebook.TypeOfControl;

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
        GradeBook myGradeBook = new GradeBook();

        System.out.println("=== GradeBook ===");

        myGradeBook.grades.add(new DisciplineData("Mathematics analise",
                1, TypeOfControl.EXAM, Grade.GOOD));
        myGradeBook.grades.add(new DisciplineData("History",
                1, TypeOfControl.CREDIT, Grade.PASS));
        myGradeBook.grades.add(new DisciplineData("Programming",
                1, TypeOfControl.DIFF_CREDIT, Grade.EXCELLENT));

        myGradeBook.grades.add(new DisciplineData("Discrete mathematics",
                2, TypeOfControl.EXAM, Grade.EXCELLENT));
        myGradeBook.grades.add(new DisciplineData("Imperative programming",
                2, TypeOfControl.EXAM, Grade.EXCELLENT));
        myGradeBook.grades.add(new DisciplineData("Physical culture",
                2, TypeOfControl.CREDIT, Grade.PASS));

        System.out.printf("1. Average Score: %.2f%n", myGradeBook.getAverageScore());

        System.out.println("2. Transfer to budget? "
                + (myGradeBook.transferToBudget() ? "Yes" : "No"));

        System.out.println("3. Is it possible to get PGAS? "
                + (myGradeBook.canGetPGAS() ? "Yes" : "No"));

        System.out.println("Diploma defense");
        myGradeBook.grades.add(new DisciplineData("ВКР",
                8, TypeOfControl.THESIS_DEFENSE, Grade.EXCELLENT));

        System.out.println("4. Is it possible to get RedDiploma? "
                + (myGradeBook.canGetRedDiploma() ? "Yes" : "No"));
    }
}