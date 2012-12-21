package li.people.record;

import java.util.List;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Page;
import li.dao.Record;
import li.util.Convert;

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

    public List<Account> list(Page page) {
        String sql = "SELECT a.*,r.name role_name FROM t_account a,t_role r WHERE r.id=a.role_id";
        return list(page, sql);
    }
}