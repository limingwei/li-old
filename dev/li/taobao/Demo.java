package li.taobao;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.ProductsSearchRequest;
import com.taobao.api.response.ProductsSearchResponse;

public class Demo {
    // protected static String URL = "http://gw.api.tbsandbox.com/router/rest";
    protected static String URL = "http://gw.api.taobao.com/router/rest";

    private static final String APP_KEY = "21398772";
    private static final String APP_SECRET = "7942c4886ff220eeb75676f9a2a7289a";

    private static final String SESSION = "6100e22526fa34d5bac96306c5b97a1cfba6952969c65a4700442350";
    private static final String REFRESH_TOKEN = "6100722d1cef430a5e4d48fbbf5c5c185acee16afa90fa2700442350";

    public static void main(String[] args) throws Exception {
        TaobaoClient client = new DefaultTaobaoClient(URL, APP_KEY, APP_SECRET);
        ProductsSearchRequest req = new ProductsSearchRequest();
        req.setFields("product_id,cid,props,name,pic_url");
        req.setQ("女鞋");
        ProductsSearchResponse response = client.execute(req);
        System.out.println(response.getBody());
    }
}