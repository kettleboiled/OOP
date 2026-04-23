task("Task_2_3_1") {
    name = "Змейка"
    maxPoints = 1
    softDeadline = "2026-04-04"
    hardDeadline = "2026-04-13"
}

group("24216") {
    student("kettleboiled", "Ryzhneva Anastasia Victorovna", "https://github.com/kettleboiled/OOP")
}

check {
    tasks("Task_2_3_1")
    groups("24216")
}