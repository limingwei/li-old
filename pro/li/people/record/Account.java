package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_account")
public class Account extends Record<Account> {
    private static final long serialVersionUID = 3084398087892682872L;

    public Boolean login(Account account) {
        String sql = "WHERE username=#username AND password=#password";
        return null != find(sql, account);
    }
}