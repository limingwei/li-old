package li.edm.collector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import li.annotation.Bean;
import li.annotation.Inject;
import li.edm.collector.record.Email;
import li.edm.collector.record.File;
import li.util.Log;

@Bean
public class EmailCollector implements Const {
    private static final Log log = Log.init();

    Executor executor = Executors.newFixedThreadPool(50);

    @Inject
    File fileDao;

    @Inject
    Email emailDao;

    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    public void run() {
        log.info("starting EmailCollector for file " + file.get(String.class, "path"));
        BufferedReader bufferedReader = null;
        String line = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file.get(String.class, "path")));
            int index = 1;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("@")) {
                    Email email = new Email();
                    email.set("address", line).set("file_id", file.get("id")).set("line", index++);
                    save(email);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        done();
    }

    private void save(final Email email) {
        executor.execute(new Runnable() {
            public void run() {
                emailDao.saveIgnoreNull(email);
            }
        });
    }

    public void done() {
        String sql = "SET status=? WHERE id=?";
        fileDao.update(sql, STATUS_READED, file.get("id"));
    }
}