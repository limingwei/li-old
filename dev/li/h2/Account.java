package li.h2;

import li.annotation.Table;
import li.dao.Record;

@Table(value = "t_account", id = "ID")
public class Account extends Record<Account> {

}