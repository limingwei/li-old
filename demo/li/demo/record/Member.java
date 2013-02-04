package li.demo.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_member")
public class Member extends Record<Member> {
    private static final long serialVersionUID = -1448317348765665003L;

    public Member find(Integer id) {
        String sql = "SELECT t_member.#,t_account.# AS account_# FROM t_member,t_account WHERE t_account.id=t_member.account_id AND t_member.id=?";
        return find(sql, id);
    }
}