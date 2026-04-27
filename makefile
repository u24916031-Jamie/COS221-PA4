all:
	del *.class
	javac -cp ".;lib/*" *.java
	java -cp ".;lib/*" Main
	del *.class



build:
	del *.class
	javac -cp ".;lib/*" *.java

run:
	java -cp ".;lib/*" Main


clean:
	del *.class