package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.gradebook.Discipline;
import ru.nsu.ryzhneva.gradebook.Semester;
import ru.nsu.ryzhneva.gradebook.Student;
import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;
import ru.nsu.ryzhneva.gradebook.typesandgrades.TypeOfControl;

/**
 * Тесты для класса Student.
 */
class StudentTest {

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("Test Student", true, 101);
    }

    @Test
    void testIncreasedStipendSuccess() {
        Semester s1 = new Semester(1);
        s1.addDiscipline(new Discipline(
                "Math", TypeOfControl.EXAM, Grade.EXCELLENT));
        s1.addDiscipline(new Discipline(
                "History", TypeOfControl.DIFF_CREDIT, Grade.EXCELLENT));
        student.getGradeBook().addSemester(s1);
        assertTrue(student.hasIncreasedScholarship());
    }

    @Test
    void testIncreasedStipendFailDueToGoodGrade() {
        Semester s1 = new Semester(1);
        s1.addDiscipline(new Discipline(
                "Math", TypeOfControl.EXAM, Grade.GOOD));
        student.getGradeBook().addSemester(s1);
        assertFalse(student.hasIncreasedScholarship());
    }

    @Test
    void testIncreasedStipendFailForPaidStudent() {
        student.setIsBudget(false);
        Semester s1 = new Semester(1);
        s1.addDiscipline(new Discipline(
                "Math", TypeOfControl.EXAM, Grade.EXCELLENT));
        student.getGradeBook().addSemester(s1);
        assertFalse(student.hasIncreasedScholarship());
    }

    @Test
    void testTransferToBudgetSuccess() {
        student.setIsBudget(false);
        Semester s1 = new Semester(1);
        s1.addDiscipline(new Discipline(
                "Math", TypeOfControl.EXAM, Grade.GOOD));
        student.getGradeBook().addSemester(s1);
        student.moveToNextSemester();
        Semester s2 = new Semester(2);
        s2.addDiscipline(new Discipline(
                "Physics", TypeOfControl.EXAM, Grade.EXCELLENT));
        s2.addDiscipline(new Discipline(
                "Project", TypeOfControl.DIFF_CREDIT, Grade.SATISFACTORY));
        student.getGradeBook().addSemester(s2);
        assertTrue(student.transferToBudget());
    }

    @Test
    void testTransferToBudgetFail() {
        student.setIsBudget(false);
        Semester s1 = new Semester(1);
        s1.addDiscipline(new Discipline(
                "Math", TypeOfControl.EXAM, Grade.EXCELLENT));
        student.getGradeBook().addSemester(s1);
        student.moveToNextSemester();
        Semester s2 = new Semester(2);
        s2.addDiscipline(new Discipline(
                "Physics", TypeOfControl.EXAM, Grade.SATISFACTORY));
        student.getGradeBook().addSemester(s2);
        assertFalse(student.transferToBudget());
    }

    @Test
    void testTransferToBudgetAfterRetake() {
        student.setIsBudget(false);
        Semester s1 = new Semester(1);
        s1.addDiscipline(new Discipline(
                "History", TypeOfControl.EXAM, Grade.GOOD));
        student.getGradeBook().addSemester(s1);
        student.moveToNextSemester();
        Semester s2 = new Semester(2);
        Discipline physics = new Discipline(
                "Physics", TypeOfControl.EXAM, Grade.UNSATISFACTORY);
        s2.addDiscipline(physics);
        student.getGradeBook().addSemester(s2);
        assertFalse(student.transferToBudget());
        physics.retake(Grade.GOOD, java.time.LocalDate.now());
        assertTrue(student.transferToBudget());
    }
}
