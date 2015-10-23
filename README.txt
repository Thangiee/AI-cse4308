Thang Le
1000787155

Language used: Scala

----- compile into a single jar and executing it ----

Note: to execute the jar on omega, jdk6 need to be set as default jdk when executing step 4.
      Pre-included compute_a_posteriori.jar is jdk 6 compatible

1. Download and install Scala 2.11.7 binaries: http://www.scala-lang.org/index.html

2. Download and install SBT (Simple Build tool): http://www.scala-sbt.org/download.html

3. Open a terminal/command line and navigate to the root of the homework folder.
   You know you're in the correct directory if you also see a build.sbt and this README file.

4. and run "sbt assembly" (it many take a while on the first run).

5. After finishing, it should create a new compute_a_posteriori.jar in the same directory.

6. To execute:
    java -jar compute_a_posteriori.jar [observations]