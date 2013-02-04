package li.demo.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_account")
public class Account extends Record<Account> {
    private static final long serialVersionUID = 5520627406091675937L;

    public Account login(Account account) {
        // String sql = "SELECT t_account.#,t_member.# AS member_# FROM t_account,t_member WHERE t_member.account_id=t_account.id AND username=?";
        // Account ac = find(sql, account.get("username"));
        // return null != ac && ac.get("password").equals(Convert.toMD5(account.get("password"))) ? ac : null;
        return null;
    }
}