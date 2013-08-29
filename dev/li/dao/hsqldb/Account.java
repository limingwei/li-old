package li.dao.hsqldb;

import li.annotation.Table;
import li.dao.Record;

@Table("t_account")
public class Account extends Record<Account, Integer> {
    private static final long serialVersionUID = -2233481174367637186L;
}