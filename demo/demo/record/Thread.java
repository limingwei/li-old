package demo.record;

import java.util.List;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Page;
import li.dao.Record;

@Bean
@Table("t_thread")
public class Thread extends Record<Thread> {
    private static final long serialVersionUID = 7575718307924687544L;

    public List<Thread> listByForumId(Integer forumId, Page page) {
        String sql = "SELECT t_thread.#,t_member.name AS member_name FROM t_thread,t_member WHERE t_thread.forum_id=? AND t_thread.member_id=t_member.id";
        return list(page, sql, forumId);
    }

    public Thread find(Integer id) {
        String sql = "SELECT t_thread.#,t_member.name AS member_name,t_forum.name AS forum_name FROM t_thread,t_member,t_forum WHERE t_thread.id=? AND t_thread.member_id=t_member.id AND t_thread.forum_id=t_forum.id";
        return find(sql, id);
    }
}