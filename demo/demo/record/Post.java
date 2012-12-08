package demo.record;

import java.util.List;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Page;
import li.dao.Record;

@Bean
@Table("t_post")
public class Post extends Record<Post> {
    private static final long serialVersionUID = -7438329457522666306L;

    public List<Post> listByThreadId(Integer threadId, Page page) {
        String sql = "SELECT t_post.#,t_member.name AS member_name FROM t_post,t_member WHERE t_post.thread_id=? AND t_member.id=t_post.member_id";
        return list(page, sql, threadId);
    }

    public Integer deleteByThreadId(Integer threadId) {
        return delete("WHERE thread_id=?", threadId);
    }
}