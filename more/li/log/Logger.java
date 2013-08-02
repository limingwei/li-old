package li.log;

import org.apache.log4j.Priority;

public class Logger {
    public static void main(String[] args) {
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("");

        Priority[] priorities = Priority.getAllPossiblePriorities();
        for (Priority priority : priorities) {
            System.out.println(priority);
        }
    }
}