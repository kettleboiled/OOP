package ru.nsu.ryzhneva.dsl

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

import org.junit.jupiter.api.Test
import ru.nsu.ryzhneva.dsl.builders.CheckAssignmentBuilder

/**
 * Тесты для {@link CheckAssignmentBuilder}.
 */
class CheckAssignmentBuilderTest {

    @Test
    void buildCollectsTasksStudentsAndGroups() {
        def assignment = CheckAssignmentBuilder.build({
            tasks('Task_2_3_1', 'Task_2_2_1')
            students('kettleboiled', 'WorstNormal')
            groups('24216')
        })

        assertEquals(2, assignment.getTaskIds().size())
        assertTrue(assignment.getTaskIds().contains('Task_2_3_1'))
        assertTrue(assignment.getTaskIds().contains('Task_2_2_1'))
        assertEquals(3, assignment.getTargetIdentifiers().size())
        assertTrue(assignment.getTargetIdentifiers().contains('kettleboiled'))
        assertTrue(assignment.getTargetIdentifiers().contains('WorstNormal'))
        assertTrue(assignment.getTargetIdentifiers().contains('24216'))
    }

    @Test
    void buildWithEmptyClosureProducesEmptyAssignment() {
        def assignment = CheckAssignmentBuilder.build({ })
        assertTrue(assignment.getTaskIds().isEmpty())
        assertTrue(assignment.getTargetIdentifiers().isEmpty())
    }
}

