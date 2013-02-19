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
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
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

        String referer = "https://ui.ptlogin2.qq.com/cgi-bin/login?target=self&style=5&mibao_css=m_webqq&appid=1003903&enable_qlogin=0&no_verifyimg=1&s_url=http%3A%2F%2Fweb.qq.com%2Floginproxy.html&f_url=loginerroralert&strong_login=1&login_state=10&t=20121029001";

        HttpResponse httpResponse = httpClient.execute(new HttpGet(referer), httpContext);

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

        DefaultHttpClient httpClient2 = new DefaultHttpClient();

        HttpResponse httpResponse2 = httpClient2.execute(new HttpGet(url), httpContext);

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