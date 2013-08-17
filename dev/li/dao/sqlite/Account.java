package li.dao.sqlite;

import li.annotation.Table;
import li.annotation.Trans;
import li.dao.Record;

@Table("t_account")
public class Account extends Record<Account> {
    @Trans
    public Boolean save(Account t) {
        return super.save(t);
    }
}