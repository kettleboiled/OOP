package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.gradebook.Discipline;
import ru.nsu.ryzhneva.gradebook.Semester;
import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;
import ru.nsu.ryzhneva.gradebook.typesandgrades.TypeOfControl;

/**
 * Тесты для класса Semester.
 */
class SemesterTest {

    @Test
    void testIsExcellentReturnsTrueForPerfectGrades() {
        Semester semester = new Semester(1);
        semester.addDiscipline(new Discipline(
                "Math", TypeOfControl.EXAM, Grade.EXCELLENT));
        semester.addDiscipline(new Discipline(
                "PE", TypeOfControl.CREDIT, Grade.PASS));
        assertTrue(semester.isExcellent(),
                "Семестр должен быть отличным, если нет оценок ниже 5");
    }

    @Test
    void testIsExcellentReturnsFalseForGoodGrade() {
        Semester semester = new Semester(1);
        semester.addDiscipline(new Discipline(
                "Math", TypeOfControl.EXAM, Grade.GOOD));

        assertFalse(semester.isExcellent(),
                "Семестр не отличный, если есть четверка");
    }

    @Test
    void testIsExcellentReturnsFalseForEmptySemester() {
        Semester semester = new Semester(1);
        assertFalse(semester.isExcellent(),
                "Пустой семестр не может считаться отличным (стипендия не положена)");
    }

    @Test
    void testHasBadGradesInExamsOnly() {
        Semester semester = new Semester(1);
        semester.addDiscipline(new Discipline("Project",
                TypeOfControl.DIFF_CREDIT, Grade.SATISFACTORY));
        assertFalse(semester.hasBadGrades(true),
                "Тройка за диф.зачет не учитываться для плохих экзаменов");
        assertTrue(semester.hasBadGrades(false),
                "Тройка должна учитываться при полной проверке");
    }

    @Test
    void testHasBadGradesDetectsBadExam() {
        Semester semester = new Semester(1);
        semester.addDiscipline(new Discipline(
                "Math", TypeOfControl.EXAM, Grade.UNSATISFACTORY));
        assertTrue(semester.hasBadGrades(true),
                "Двойка за экзамен должна детектироваться");
    }

    @Test
    void testHasExams() {
        Semester semester = new Semester(1);
        semester.addDiscipline(new Discipline(
                "History", TypeOfControl.CREDIT, Grade.PASS));
        assertFalse(semester.hasExams());
        semester.addDiscipline(new Discipline(
                "Math", TypeOfControl.EXAM, Grade.EXCELLENT));
        assertTrue(semester.hasExams());
    }

    @Test
    void testRetakeFixesSemesterStatus() {
        Semester semester = new Semester(1);
        Discipline math = new Discipline(
                "Math", TypeOfControl.EXAM, Grade.SATISFACTORY);
        semester.addDiscipline(math);
        assertFalse(semester.isExcellent());
        math.retake(Grade.EXCELLENT, java.time.LocalDate.now());
        assertTrue(semester.isExcellent());
    }
}