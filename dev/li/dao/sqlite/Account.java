package li.dao.sqlite;

import li.annotation.Table;
import li.annotation.Trans;
import li.dao.Record;

@Table("t_account")
public class Account extends Record<Account, Integer> {
    private static final long serialVersionUID = 5372906515532858259L;

    @Trans
    public Boolean save(Account t) {
        return super.save(t);
    }
}