package ru.nsu.ryzhneva;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.gradebook.DisciplineData;
import ru.nsu.ryzhneva.gradebook.Grade;
import ru.nsu.ryzhneva.gradebook.GradeBook;
import ru.nsu.ryzhneva.gradebook.TypeOfControl;

import static org.junit.jupiter.api.Assertions.*;

class GradeBookTest {

    private GradeBook gradeBook;

    @BeforeEach
    void setUp() {
        gradeBook = new GradeBook();
    }

    @Test
    void testAverageScore() {
        gradeBook.grades.add(new DisciplineData("Math",
                1, TypeOfControl.EXAM, Grade.EXCELLENT));
        gradeBook.grades.add(new DisciplineData("OOP",
                1, TypeOfControl.DIFF_CREDIT, Grade.GOOD));
        gradeBook.grades.add(new DisciplineData("History",
                1, TypeOfControl.EXAM, Grade.SATISFACTORY));

        gradeBook.grades.add(new DisciplineData("PE",
                1, TypeOfControl.CREDIT, Grade.PASS));

        assertEquals(4.0, gradeBook.getAverageScore(), 0.001);
    }

    @Test
    void testRedDiplomaSuccess() {
        gradeBook.grades.add(new DisciplineData("S1",
                1, TypeOfControl.EXAM, Grade.EXCELLENT));
        gradeBook.grades.add(new DisciplineData("S2",
                1, TypeOfControl.EXAM, Grade.EXCELLENT));
        gradeBook.grades.add(new DisciplineData("S3",
                1, TypeOfControl.EXAM, Grade.EXCELLENT));
        gradeBook.grades.add(new DisciplineData("S4",
                1, TypeOfControl.EXAM, Grade.GOOD));

        gradeBook.grades.add(new DisciplineData("Thesis",
                8, TypeOfControl.THESIS_DEFENSE, Grade.EXCELLENT));

        assertTrue(gradeBook.canGetRedDiploma());
    }

    @Test
    void testRedDiplomaFailDueToGrade3() {
        gradeBook.grades.add(new DisciplineData("Math",
                1, TypeOfControl.EXAM, Grade.SATISFACTORY));
        gradeBook.grades.add(new DisciplineData("OOP",
                1, TypeOfControl.EXAM, Grade.EXCELLENT));
        gradeBook.grades.add(new DisciplineData("Thesis",
                8, TypeOfControl.THESIS_DEFENSE, Grade.EXCELLENT));

        assertFalse(gradeBook.canGetRedDiploma());
    }

    @Test
    void testRedDiplomaFailDueToThesis() {
        gradeBook.grades.add(new DisciplineData("Math",
                1, TypeOfControl.EXAM, Grade.EXCELLENT));
        gradeBook.grades.add(new DisciplineData("Thesis",
                8, TypeOfControl.THESIS_DEFENSE, Grade.GOOD));

        assertFalse(gradeBook.canGetRedDiploma());
    }

    @Test
    void testIncreasedStipend() {
        gradeBook.grades.add(new DisciplineData("Math",
                1, TypeOfControl.EXAM, Grade.GOOD));

        gradeBook.grades.add(new DisciplineData("Java",
                2, TypeOfControl.EXAM, Grade.EXCELLENT));
        gradeBook.grades.add(new DisciplineData("C++",
                2, TypeOfControl.DIFF_CREDIT, Grade.EXCELLENT));

        assertTrue(gradeBook.canGetPGAS());
    }

    @Test
    void testNoStipendIfGoodGradeInLastSemester() {
        gradeBook.grades.add(new DisciplineData("Java",
                2, TypeOfControl.EXAM, Grade.GOOD));

        assertFalse(gradeBook.canGetPGAS());
    }

    @Test
    void testTransferToBudget() {
        gradeBook.grades.add(new DisciplineData("Math",
                1, TypeOfControl.EXAM, Grade.GOOD));
        gradeBook.grades.add(new DisciplineData("Physics",
                2, TypeOfControl.EXAM, Grade.EXCELLENT));
        gradeBook.grades.add(new DisciplineData("Project",
                2, TypeOfControl.DIFF_CREDIT, Grade.SATISFACTORY));

        assertTrue(gradeBook.TransferToBudget());
    }

    @Test
    void testTransferToBudgetFail() {
        gradeBook.grades.add(new DisciplineData("Math",
                1, TypeOfControl.EXAM, Grade.EXCELLENT));
        gradeBook.grades.add(new DisciplineData("Physics",
                2, TypeOfControl.EXAM, Grade.SATISFACTORY));

        assertFalse(gradeBook.TransferToBudget());
    }

    @Test
    void testMainRuns() {
        Main.main(new String[]{});
    }
}