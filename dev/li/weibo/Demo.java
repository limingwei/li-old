package li.weibo;

import weibo4j.Timeline;
import weibo4j.model.Status;

public class Demo {
    public static void main(String[] args) throws Exception {
        Timeline timeline = new Timeline();
        timeline.setToken("2.00V8KUuBsed84D548067dcdezPAJlB");
        Status status = timeline.UpdateStatus("一条新的微博");
        System.out.println(status);
    }
}