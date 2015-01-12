javasebasic Usage Instructions
===========

#Prerequisites

To build the project Java SE 8 JDK and Maven are required and their `bin` folders should be added to the system environment variable `PATH` to allow executing commands from the command prompt.

#Build

Project can be built by executing `mvn clean compile assembly:assembly` in the system command prompt from the project root directory. Executing this should compile sources and create the jar file `javasebasic-0.0.1-jar-with-dependencies.jar`.

#Input

The easiest way to locate `inputs.zip` is to copy it into folder with jar to be on one level with jar. If program does not find it next to the jar, it will ask for path to folder containing it. Alternatively path to the zip file can be supplied as the first program argument like so `java -jar javasebasic-0.0.1-jar-with-dependencies.jar "C:\"`.

#Run

To execute the jar from the command prompt navigate to folder containing the jar and run the following  `java -jar javaSEBasic-0.0.1.jar`. Another way to run the program is to use `run.bat` file located in the root of the project. Place it next to the jar and run .bat file by double clicking it in System Explorer.

#Result

Program will unzip all files into system temporary folder, parse all text files to collect phone numbers and emails, and create new zip file `inputsv2.zip` next to the jar. Resulting archive will be a copy of the original `inputs.zip` with 2 new text files added to the root -- `phones.txt` and `emails.txt`.
