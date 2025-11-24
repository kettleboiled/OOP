
javac -d build src/main/java/ru/nsu/ryzhneva/Main.java src/main/java/ru/nsu/ryzhneva/HeapSort.java src/main/java/ru/nsu/ryzhneva/Benchmark.java

javadoc -d build/docs/javadoc -sourcepath src/main/java -subpackages ru.nsu.ryzhneva

java -cp build ru.nsu.ryzhneva.Main
