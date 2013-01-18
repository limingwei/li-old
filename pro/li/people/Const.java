package li.people;

import li.dao.Page;

public interface Const {
    public static final String LIST = "list";

    public static final String PAGE = "page";

    public static final Page MAX_PAGE = new Page(1, Integer.MAX_VALUE);
}