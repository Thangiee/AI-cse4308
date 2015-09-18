Thang Le
1000787155

Language used: Scala

----- compile into a single jar and executing it ----

1. Download and install Scala 2.11.7 binaries: http://www.scala-lang.org/index.html

2. Download and install SBT (Simple Build tool): http://www.scala-sbt.org/download.html

3. Open a terminal/command line and navigate to the root of the homework folder.
   You know you're in the correct directory if you also see a build.sbt and this README file.

4. and run "sbt assembly" (it many take a while on the first run).

5. After finishing, it should create a new maxconnect4.jar in the same directory.

6. To execute:
    interactive mode:
        java -jar maxconnect4.jar interactive [input_file] [computer-next/human-next] [depth]

    One-move mode:
        java -jar maxconnect4.jar one-move [input_file] [output_file] [depth]

    Note: to execute the jar on omega, jdk6 need to be set as default jdk when executing step 4.
          Pre-included maxconnect4.jar is jdk 6 compatible

Execution Time (Omega, empty board, avg of 5 runs)
depth   time
1       .311 sec
2       .337 sec
3       .591 sec
4       .876 sec
5       .955 sec
6      1.801 sec
7      1.611 sec
8      3.013 sec
9      3.920 sec
10     30+   sec