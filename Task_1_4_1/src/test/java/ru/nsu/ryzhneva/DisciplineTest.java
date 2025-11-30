package ru.nsu.ryzhneva;

import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.gradebook.Discipline;
import ru.nsu.ryzhneva.gradebook.GradeRecord;
import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;
import ru.nsu.ryzhneva.gradebook.typesandgrades.TypeOfControl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DisciplineTest {
    @Test
    void testDisciplineHistoryTracking() {
        LocalDate date1 = LocalDate.now().minusDays(10);
        LocalDate date2 = LocalDate.now();

        Discipline discipline = new Discipline(
                "Java", TypeOfControl.EXAM, Grade.UNSATISFACTORY, date1);
        discipline.retake(Grade.EXCELLENT, date2);

        List<GradeRecord> history = discipline.getHistory();

        assertEquals(2, history.size(),
                "В истории должно быть 2 записи");
        assertEquals(Grade.UNSATISFACTORY, history.get(0).getGrade());
        assertEquals(date1, history.get(0).getDate());
        assertEquals(Grade.EXCELLENT, history.get(1).getGrade());
        assertEquals(date2, history.get(1).getDate());
    }

    @Test
    void testHistoryEncapsulation() {
        Discipline discipline = new Discipline("C++", TypeOfControl.DIFF_CREDIT, Grade.GOOD);
        List<GradeRecord> history = discipline.getHistory();
        history.clear();
        assertEquals(1, discipline.getHistory().size());
    }
}
