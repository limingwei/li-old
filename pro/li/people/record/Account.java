package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;
import li.more.Convert;

@Bean
@Table("t_account")
public class Account extends Record<Account> {
    private static final long serialVersionUID = 3084398087892682872L;

    public Account login(Account account) {
        account.set("password", Convert.toMD5(account.get("password")));
        String sql = "WHERE (username=#username OR email=#username) AND password=#password";
        return find(sql, account);
    }

    public Account findByUsername(String username) {
        String sql = "WHERE username=?";
        return find(sql, username);
    }

    public Object findByEmail(String email) {
        String sql = "WHERE email=?";
        return find(sql, email);
    }
}