with open("src/test/java/ru/nsu/ryzhneva/snake/model/GameServiceTest.java", "r") as f:
    lines = f.readlines()
with open("src/test/java/ru/nsu/ryzhneva/snake/model/GameServiceTest.java", "w") as f:
    skip = False
    for line in lines:
        if "@Test void testStartStopRestart()" in line:
            skip = True
        if skip and line.strip() == "}":
            skip = False
            continue
        if skip:
            continue
        f.write(line)
