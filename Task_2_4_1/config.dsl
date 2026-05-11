task("Task_1_1_1") {
    name = "Пирамидальная сортировка"
    maxPoints = 1
    softDeadline = "2025-09-13"
    hardDeadline = "2025-09-13"
}

task("Task_1_1_2") {
    name = "Консольный блэкджек"
    maxPoints = 2
    softDeadline = "2025-09-20"
    hardDeadline = "2025-09-27"
}

task("Task_1_1_3") {
    name = "Операции с уравнениями"
    maxPoints = 2
    softDeadline = "2025-10-04"
    hardDeadline = "2025-10-11"
}

task("Task_1_2_1") {
    name = "Граф"
    maxPoints = 2
    softDeadline = "2025-10-18"
    hardDeadline = "2025-11-01"
}

task("Task_1_2_2") {
    name = "Хеш-таблица"
    maxPoints = 2
    softDeadline = "2025-11-08"
    hardDeadline = "2025-11-15"
}

task("Task_1_3_1") {
    name = "Поиск подстроки"
    maxPoints = 2
    softDeadline = "2025-11-22"
    hardDeadline = "2025-11-22"
}

task("Task_1_4_1") {
    name = "Зачетная книжка"
    maxPoints = 1
    hardDeadline = "2025-11-29"
}

task("Task_1_5_1") {
    name = "Markdown generator"
    maxPoints = 4
    softDeadline = "2025-12-13"
    hardDeadline = "2025-12-27"
}

activityWindow("2025-09-01", "2025-12-31")

group("24216") {
    student("24216-Nerlich-Anna", "Nerlich Anna", "https://github.com/ashemchuk/OOP")
    student("24216-Maslova-Alina", "Maslova Alina", "https://github.com/24216-Maslova-Alina/OOP")
}

check {
    tasks(
            "Task_1_1_1",
            "Task_1_1_2",
            "Task_1_1_3",
            "Task_1_2_1",
            "Task_1_2_2",
            "Task_1_3_1",
            "Task_1_4_1",
            "Task_1_5_1"
    )
    groups("24216")
}
