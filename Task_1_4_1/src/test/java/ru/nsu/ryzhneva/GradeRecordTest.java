package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.nsu.ryzhneva.gradebook.GradeRecord;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;

class GradeRecordTest {

    @Test
    void testGradeRecordStoresData() {
        LocalDate date = LocalDate.of(2025, 5, 20);
        GradeRecord record = new GradeRecord(Grade.EXCELLENT, date);
        assertEquals(Grade.EXCELLENT, record.getGrade());
        assertEquals(date, record.getDate());
    }
}