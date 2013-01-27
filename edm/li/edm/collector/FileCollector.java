package li.edm.collector;

import java.io.File;
import java.util.List;

import li.ioc.Ioc;
import li.util.Files;

public class FileCollector {
    static String ROOT_PATH = "E:/data/";
    static String REGEX = "^.*$";

    static li.edm.collector.record.File fileDao = Ioc.get(li.edm.collector.record.File.class);

    public void run() {
        List<String> files = Files.list(new File(ROOT_PATH), REGEX, true);
        for (String path : files) {
            li.edm.collector.record.File file = new li.edm.collector.record.File();
            file.set("path", path.replace("\\", "\\\\")).set("suffix", path.substring(path.lastIndexOf('.')));

            fileDao.saveIgnoreNull(file);
        }
    }
}