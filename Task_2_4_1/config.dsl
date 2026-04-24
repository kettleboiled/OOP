task("Task_2_3_1") {
    name = "Змейка"
    maxPoints = 1
    softDeadline = "2026-04-04"
    hardDeadline = "2026-04-13"
}

task("Task_2_2_1") {
    name = "Пиццерия"
    maxPoints = 1
    softDeadline = "2026-03-07"
    hardDeadline = "2026-03-23"
}

group("24216") {
    student("kettleboiled", "Ryzhneva Anastasia Victorovna", "https://github.com/kettleboiled/OOP")
    student("WorstNormal", "Gaev Vladislav Denisovich", "https://github.com/WorstNormal/OOP")
    student("24216-Maslova-Alina", "Maslova Alina", "https://github.com/24216-Maslova-Alina/OOP")
}



check {
    tasks("Task_2_3_1", "Task_2_2_1")
    groups("24216")
}