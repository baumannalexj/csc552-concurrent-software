In src/alexbaumann/hammingnumbers/HammingGeneratorMain.java,

set your targetNumber == the number you'd like to generate Hamming numbers up to
(or run the program with argument of the targetnumber you'd like)

Then run (using java 8):

    ant compile
    java -classpath ./build/classes/ alexbaumann.hammingnumbers.HammingGeneratorMain <optional argument for targetNumber>