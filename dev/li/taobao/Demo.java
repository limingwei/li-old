package li.taobao;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.ProductsSearchRequest;
import com.taobao.api.response.ProductsSearchResponse;

public class Demo {
    private static final String url = "http://gw.api.tbsandbox.com/router/rest";

    private static final String appkey = "1021398772";
    private static final String secret = "sandbox86ff220eeb75676f9a2a7289a";

    private static final String sessionKey = null;

    public static void main(String[] args) throws Exception {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        ProductsSearchRequest req = new ProductsSearchRequest();
        req.setFields("product_id,name,pic_url,cid,props,price,tsc");
        req.setQ("女鞋");
        ProductsSearchResponse response = client.execute(req);
        System.out.println(response.getBody());
    }
}