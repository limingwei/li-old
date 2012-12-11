package li.mvc;

/**
 * HTTP请求类型常量集合 GET POST ..
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2012-12-11)
 */
public interface RequestMethod {
    public static final String GET = "GET";

    public static final String POST = "POST";

    public static final String PUT = "PUT";

    public static final String DELETE = "DELETE";

    public static final String HEAD = "HEAD";

    public static final String OPTIONS = "OPTIONS";

    public static final String TRACE = "TRACE";

    public static final String ALL = ".*";// 任何类型
}