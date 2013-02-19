package li.webqq;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class Demo {
    public static void main(String[] args) throws Exception {
        String url = "https://ssl.ptlogin2.qq.com/login" + //
                "?u=1055515958" + //
                "&p=8BCFAFA136059B769C2CFF89F5F857DB" + //
                "&verifycode=!HGZ" + //
                "&webqq_type=10" + //
                "&remember_uin=1" + //
                "&login2qq=1" + //
                "&aid=1003903" + //
                "&u1=http%3A%2F%2Fweb.qq.com%2Floginproxy.html%3Flogin2qq%3D1%26webqq_type%3D10" + //
                "&h=1" + //
                "&ptredirect=0" + //
                "&ptlang=2052&from_ui=1" + //
                "&pttype=1" + //
                "&dumy=" + //
                "&fp=loginerroralert" + //
                "&action=6-31-26079" + //
                "&mibao_css=m_webqq" + //
                "&t=1" + //
                "&g=1" + //
                "&js_type=0" + //
                "&js_ver=10021" + //
                "&login_sig=OXajLWsfZFsBAxtAlyt87orHUwHgKM1IkrzDM4KO73wxl-85Mkef3juapzhD3Qji";

        DefaultHttpClient httpClient = new DefaultHttpClient();

        CookieSpecFactory cookieSpecFactory = new CookieSpecFactory() {
            public CookieSpec newInstance(HttpParams httpParams) {
                return new BrowserCompatSpec() {
                    public void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException {
                        // Oh, I am easy
                    }
                };
            }
        };

        httpClient.getCookieSpecs().register("easy", cookieSpecFactory);

        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");

        CookieStore cookieStore = new BasicCookieStore();

        HttpContext httpContext = new BasicHttpContext();

        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        // 设置Cookies
        BasicClientCookie ac = new BasicClientCookie("ac", "1,030,006");
        ac.setDomain("qq.com");
        ac.setPath("/");

        BasicClientCookie chkuin = new BasicClientCookie("chkuin", "1055515958");
        chkuin.setDomain("ptlogin2.qq.com");
        chkuin.setPath("/");

        BasicClientCookie euin_cookie = new BasicClientCookie("euin_cookie", "F478D91917BBC0C3809C4343774AC3CCC8E933484D137642");
        euin_cookie.setDomain("qq.com");
        euin_cookie.setPath("/");
        BasicClientCookie uikey = new BasicClientCookie("uikey", "dd9bc40e9112a395c90d22def696946b12d6961d4e8906e2ba9d5ca39d900ace");
        uikey.setDomain("ptlogin2.qq.com");
        uikey.setPath("/");

        ((AbstractHttpClient) httpClient).getCookieStore().addCookie(ac);
        ((AbstractHttpClient) httpClient).getCookieStore().addCookie(chkuin);
        ((AbstractHttpClient) httpClient).getCookieStore().addCookie(euin_cookie);
        ((AbstractHttpClient) httpClient).getCookieStore().addCookie(uikey);

        String referer = "https://ui.ptlogin2.qq.com/cgi-bin/login?target=self&style=5&mibao_css=m_webqq&appid=1003903&enable_qlogin=0&no_verifyimg=1&s_url=http%3A%2F%2Fweb.qq.com%2Floginproxy.html&f_url=loginerroralert&strong_login=1&login_state=10&t=20121029001";

        HttpGet httpGet1 = new HttpGet(referer);
        // 设置http头信息
        httpGet1.setHeader("Accept", "application/javascript, */*;q=0.8");
        httpGet1.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet1.setHeader("Accept-Language", "zh-cn");
        httpGet1.setHeader("Connection", "Keep-Alive");
        httpGet1.setHeader("Host", "check.ptlogin2.qq.com");
        httpGet1.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");

        HttpResponse httpResponse = httpClient.execute(httpGet1, httpContext);

        String response = EntityUtils.toString(httpResponse.getEntity());

        Header[] headers = httpResponse.getAllHeaders();

        System.out.println("--- response ---");
        System.out.println(response);

        System.out.println("--- headers ---");
        for (Header header : headers) {
            System.out.println(header);
        }

        System.out.println("--- cookies ---");
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println(cookie);
        }

        HttpGet httpGet2 = new HttpGet(url);

        httpGet2.setHeader("Referer", referer);

        httpGet2.setHeader("Accept", "application/javascript, */*;q=0.8");
        httpGet2.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet2.setHeader("Accept-Language", "zh-cn");
        httpGet2.setHeader("Connection", "Keep-Alive");
        httpGet2.setHeader("Host", "check.ptlogin2.qq.com");
        httpGet2.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");

        HttpResponse httpResponse2 = httpClient.execute(httpGet2, httpContext);

        String response2 = EntityUtils.toString(httpResponse2.getEntity());

        Header[] headers2 = httpResponse2.getAllHeaders();

        System.out.println("--- response2 ---");
        System.out.println(response2);

        System.out.println("--- headers2 ---");
        for (Header header : headers2) {
            System.out.println(header);
        }

        System.out.println("--- cookies2 ---");
        List<Cookie> cookies2 = cookieStore.getCookies();
        for (Cookie cookie : cookies2) {
            System.out.println(cookie);
        }
    }
}