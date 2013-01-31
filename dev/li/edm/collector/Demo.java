package li.edm.collector;

import li.edm.collector.record.File;
import li.ioc.Ioc;

public class Demo {
    static File fileDao = Ioc.get(File.class);

    public static void main(String[] args) {
        // File file = fileDao.find("WHERE suffix=? AND status=?", ".txt", 1);
        File file = fileDao.find("WHERE id=1");

        EmailCollector emailCollector = Ioc.get(EmailCollector.class);
        emailCollector.setFile(file);
        emailCollector.run();
    }
}