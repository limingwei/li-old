package li.weibo;

public class Demo {
    static SinaWeibo weibo = new SinaWeibo();

    public static void main(String[] args) throws Exception {
        String code = weibo.authorize("limingwei@mail.com", "buxiaode");

        // String token = weibo.access_token(code);
        String token = "2.003rRhwB0_5y5Hae3969dd4b4sy55D";

        // System.out.println(weibo.update(token, "恭喜 @成大职前实践协会 荣获四川省大学生百佳魅力社团 - by robot"));

        System.out.println(weibo.repost(token, "3527881974550169", "恭喜 @成大职前实践协会 荣获四川省大学生百佳魅力社团, - robot 转发 评论给原微博 评论给当前微博", 3));
    }
}