package li.spider;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TiebaSpider extends Spider {
    private static final String URL = "http://tieba.baidu.com";

    public String startUrl() {
        return URL + "/f?kw=%B3%C9%B6%BC%B4%F3%D1%A7";
    }

    public List<Thread> getThreads(Element content) {
        List<Thread> threads = new ArrayList<Thread>();
        Elements elements = content.select("a.j_th_tit");
        for (Element element : elements) {
            Thread thread = new Thread();
            thread.setUrl(URL + element.attr("href"));
            thread.setSubject(element.html());
            threads.add(thread);
        }
        return threads;
    }

    public String nextThreadsPageUrl(Element content) {
        Elements elements = content.select("a.next");
        if (null != elements) {
            return URL + elements.attr("href");
        }
        return null;
    }

    public List<Post> getPosts(Element content) {
        List<Post> posts = new ArrayList<Post>();
        Elements elements = content.select("div.p_content");
        for (Element element : elements) {
            Post post = new Post();
            post.setContent(element.select("div.d_post_content").html());
            posts.add(post);
        }
        return posts;
    }

    public String nextPostsPageUrl(Element content) {
        Elements elements = content.select("ul.l_posts_num a");
        if (null != elements) {
            for (Element element : elements) {
                if (element.html().contains("下一页")) {
                    return URL + element.attr("href");
                }
            }
        }
        return null;
    }
}