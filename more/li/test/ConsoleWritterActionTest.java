package li.test;

import java.io.PrintWriter;

public class ConsoleWritterActionTest extends ActionTest {
    public ConsoleWritterActionTest() {
        this.setWritter(new PrintWriter(System.out));
    }
}