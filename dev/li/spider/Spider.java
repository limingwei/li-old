package li.spider;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public abstract class Spider {
    /**
     * 开始页地址
     */
    public abstract String startUrl();

    /**
     * 抓取主题
     */
    public abstract List<Thread> getThreads(Element content);

    /**
     * 抓取帖子
     */
    public abstract List<Post> getPosts(Element content);

    /**
     * 没有下一页返回null
     */
    public abstract String nextThreadsPageUrl(Element content);

    /**
     * 没有下一页返回null
     */
    public abstract String nextPostsPageUrl(Element content);

    /**
     * down一个页面
     */
    public static Element getContent(String url) {
        System.err.println(url);
        try {
            return Jsoup.connect(url).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理主题
     */
    public void _threads(String url) {
        Element content = getContent(url);
        for (Thread thread : getThreads(content)) {
            System.err.println(thread.getSubject());
            _posts(thread.getUrl());
        }
        String nextThreadsPageUrl = nextThreadsPageUrl(content);
        if (null != nextThreadsPageUrl && !nextThreadsPageUrl.trim().isEmpty()) {
            _threads(nextThreadsPageUrl);
        }
    }

    /**
     * 处理回复
     */
    public void _posts(String url) {
        Element content = getContent(url);
        for (Post post : getPosts(content)) {
            System.out.println(post.getContent());
        }
        String nextPostsPageUrl = nextPostsPageUrl(content);
        if (null != nextPostsPageUrl && !nextPostsPageUrl.trim().isEmpty()) {
            _posts(nextPostsPageUrl);
        }
    }

    /**
     * 开始
     */
    public void start() {
        System.out.println("start");
        _threads(startUrl());
    }
}