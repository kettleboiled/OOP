package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.gradebook.GradeRecord;
import java.time.LocalDate;
import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;

/**
 * Тесты для GradeRecord.
 */
class GradeRecordTest {

    @Test
    void testGradeRecordStoresData() {
        LocalDate date = LocalDate.of(2025, 5, 20);
        GradeRecord record = new GradeRecord(Grade.EXCELLENT, date);
        assertEquals(Grade.EXCELLENT, record.getGrade());
        assertEquals(date, record.getDate());
    }
}