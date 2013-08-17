package li.test;

import java.io.PrintWriter;

/**
 * Action测试类的基类,会将response输出到console
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2012-07-21)
 */
public class ConsoleWritterActionTest extends ActionTest {
    public ConsoleWritterActionTest() {
        this.setWritter(new PrintWriter(System.out));
    }
}