package li.edm.sender;

import java.util.List;

import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.edm.collector.record.Email;

@Bean
public class EmailSender {
    @Inject
    Email emailDao;

    public void run() {
        List<Email> emails = emailDao.list(new Page());
        for (Email email : emails) {

        }
    }
}