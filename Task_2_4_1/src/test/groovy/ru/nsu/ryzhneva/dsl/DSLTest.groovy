package ru.nsu.ryzhneva.dsl

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertTrue

import org.codehaus.groovy.control.CompilerConfiguration
import org.junit.jupiter.api.Test
import ru.nsu.ryzhneva.domain.CheckAssignment
import ru.nsu.ryzhneva.domain.CourseConfig
import ru.nsu.ryzhneva.domain.Task
import ru.nsu.ryzhneva.domain.Group

/**
 * Тест для проверки корректности парсинга DSL-скрипта
 * и создания конфигурации курса.
 */
class DSLTest {

    @Test
    void testBasicDSLScriptParsing() {
        String scriptText = """
        task("Task_2_3_1", {
            setName("Змейка")
            setMaxPoints(1)
            setSoftDeadline("2026-04-04")
            setHardDeadline("2026-04-13")
        })

        group("24216", {
            student("kettleboiled", "Ryzhneva Anastasia Victorovna", "https://github.com/kettleboiled/OOP")
        })

        check({
            tasks("Task_2_3_1")
            groups("24216")
        })
        
        gradeCriteria(80, 60, 40)
        """

        CompilerConfiguration compilerConfig = new CompilerConfiguration()
        compilerConfig.setScriptBaseClass(CourseDSLScript.class.getName())
        GroovyShell shell = new GroovyShell(compilerConfig)

        CourseDSLScript script = (CourseDSLScript) shell.parse(scriptText)
        script.run()
        
        CourseConfig config = script.getConfig()

        assertNotNull(config)
        assertEquals(80, config.getScoreExcellent())
        
        List<Task> tasks = config.getTasks()
        assertEquals(1, tasks.size())
        assertEquals("Task_2_3_1", tasks[0].getId())
        assertEquals(1, tasks[0].getMaxPoints())

        List<Group> groups = config.getGroups()
        assertEquals(1, groups.size())
        assertEquals("24216", groups[0].getName())
        assertEquals("Ryzhneva Anastasia Victorovna", groups[0].getStudents()[0].getFullName())

        CheckAssignment ca = config.getCheckAssignment()
        assertNotNull(ca)
        assertTrue(ca.getTaskIds().contains("Task_2_3_1"))
        assertTrue(ca.getTargetIdentifiers().contains("24216"))
    }
}
