--------------------------------Project Setup--------------------------

----Requirements----

        Java 21 or newer
        Apache Maven
        Maven Installation

Download and install Maven from the official site:

    https://maven.apache.org

After installation, make sure Maven is added to your system PATH so the mvn command works in your terminal.
You can verify it with:

        mvn -v


-----------------------Running the Project------------------

Run the JavaFX application using the Maven goal:

    javafx:run

You can execute this:
    From your IDE (Maven Goals panel)
    Or via terminal using:
    
    mvn javafx:run

-------Notes------

-Make sure you're using Java 21+ before running the project

-If JavaFX dependencies are managed via Maven, no separate JavaFX SDK installation is needed
