all:
	del *.class
	javac -cp ".;lib/*" *.java
	java -cp ".;lib/*" Main