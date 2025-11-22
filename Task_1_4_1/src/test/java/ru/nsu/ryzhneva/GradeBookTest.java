package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ru.nsu.ryzhneva.gradebook.Discipline;
import ru.nsu.ryzhneva.gradebook.Semester;
import ru.nsu.ryzhneva.gradebook.Student;
import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;
import ru.nsu.ryzhneva.gradebook.typesandgrades.TypeOfControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты для зачетной книжки.
 */
class GradeBookTest {

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("Test Student", true, 101);
    }

    @Test
    void testAverageScore() {
        Semester s1 = new Semester(1);
        s1.addDiscipline(new Discipline("Math",
                TypeOfControl.EXAM, Grade.EXCELLENT));
        s1.addDiscipline(new Discipline("OOP",
                TypeOfControl.DIFF_CREDIT, Grade.GOOD));
        s1.addDiscipline(new Discipline("History",
                TypeOfControl.EXAM, Grade.SATISFACTORY));
        s1.addDiscipline(new Discipline("PE",
                TypeOfControl.CREDIT, Grade.PASS));

        student.getGradeBook().addSemester(s1);
        assertEquals(4.0, student.getGradeBook().getAverageScore(), 0.001);
    }

    @Test
    void testIncreasedStipendSuccess() {
        Semester s1 = new Semester(1);
        s1.addDiscipline(new Discipline("Math",
                TypeOfControl.EXAM, Grade.EXCELLENT));
        s1.addDiscipline(new Discipline("History",
                TypeOfControl.DIFF_CREDIT, Grade.EXCELLENT));

        student.getGradeBook().addSemester(s1);

        assertTrue(student.hasIncreasedScholarship());
    }

    @Test
    void testIncreasedStipendFailDueToGoodGrade() {
        Semester s1 = new Semester(1);
        s1.addDiscipline(new Discipline("Math",
                TypeOfControl.EXAM, Grade.GOOD));

        student.getGradeBook().addSemester(s1);

        assertFalse(student.hasIncreasedScholarship());
    }

    @Test
    void testIncreasedStipendFailForPaidStudent() {
        student.setIsBudget(false);
        Semester s1 = new Semester(1);
        s1.addDiscipline(new Discipline("Math", TypeOfControl.EXAM, Grade.EXCELLENT));

        student.getGradeBook().addSemester(s1);

        assertFalse(student.hasIncreasedScholarship());
    }

    @Test
    void testTransferToBudgetSuccess() {
        student.setIsBudget(false);

        Semester s1 = new Semester(1);
        s1.addDiscipline(new Discipline("Math", TypeOfControl.EXAM, Grade.GOOD));
        student.getGradeBook().addSemester(s1);
        student.moveToNextSemester();

        Semester s2 = new Semester(2);
        s2.addDiscipline(new Discipline("Physics",
                TypeOfControl.EXAM, Grade.EXCELLENT));
        s2.addDiscipline(new Discipline("Project",
                TypeOfControl.DIFF_CREDIT, Grade.SATISFACTORY));

        student.getGradeBook().addSemester(s2);

        assertTrue(student.transferToBudget());
    }

    @Test
    void testTransferToBudgetFail() {
        student.setIsBudget(false);

        Semester s1 = new Semester(1);
        s1.addDiscipline(new Discipline("Math",
                TypeOfControl.EXAM, Grade.EXCELLENT));
        student.getGradeBook().addSemester(s1);
        student.moveToNextSemester();

        Semester s2 = new Semester(2);
        s2.addDiscipline(new Discipline("Physics",
                TypeOfControl.EXAM, Grade.SATISFACTORY));
        student.getGradeBook().addSemester(s2);

        assertFalse(student.transferToBudget());
    }

    @Test
    void testRedDiplomaSuccess() {
        Semester s1 = new Semester(1);
        s1.addDiscipline(new Discipline("S1",
                TypeOfControl.EXAM, Grade.EXCELLENT));
        student.getGradeBook().addSemester(s1);

        Semester s2 = new Semester(2);
        s2.addDiscipline(new Discipline("S2",
                TypeOfControl.EXAM, Grade.EXCELLENT));
        student.getGradeBook().addSemester(s2);

        Semester s3 = new Semester(3);
        s3.addDiscipline(new Discipline("S3",
                TypeOfControl.EXAM, Grade.EXCELLENT));
        student.getGradeBook().addSemester(s3);

        Semester s4 = new Semester(4);
        s4.addDiscipline(new Discipline("S4",
                TypeOfControl.EXAM, Grade.GOOD));
        student.getGradeBook().addSemester(s4);

        Semester s8 = new Semester(8);
        s8.addDiscipline(new Discipline("Thesis",
                TypeOfControl.THESIS_DEFENSE, Grade.EXCELLENT));
        student.getGradeBook().addSemester(s8);

        assertTrue(student.getGradeBook().canGetRedDiploma());
    }

    @Test
    void testRedDiplomaFailDueToThesis() {
        Semester s1 = new Semester(1);
        s1.addDiscipline(new Discipline("Math",
                TypeOfControl.EXAM, Grade.EXCELLENT));
        student.getGradeBook().addSemester(s1);

        Semester s8 = new Semester(8);
        s8.addDiscipline(new Discipline("Thesis",
                TypeOfControl.THESIS_DEFENSE, Grade.GOOD));
        student.getGradeBook().addSemester(s8);

        assertFalse(student.getGradeBook().canGetRedDiploma());
    }

    @Test
    void testMainRuns() {
        Main.main(new String[]{});
    }
}