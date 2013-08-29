package li.dao.h2;

import li.annotation.Table;
import li.dao.Record;

@Table(value = "t_account")
public class Account extends Record<Account, Integer> {
    private static final long serialVersionUID = 7845844563447928367L;
}