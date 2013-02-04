package li.demo.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.demo.record.Account;
import li.demo.record.Member;
import li.mvc.AbstractAction;

@Bean
public class MemberAction extends AbstractAction {
    @Inject
    Member memberDao;

    @At("member")
    public void find(Integer id) {
        Member member = memberDao.find(id);
        Account account = (Account) getSession("account");
        if (null != member && null != account && member.get("account_id").equals(account.get("id"))) {
            setRequest("member", member);
            view("WEB-INF/view/member_show.jsp");
        } else {
            write("你没登陆");
        }
    }
}