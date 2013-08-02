package li.mvc.adapter;

import javax.servlet.ServletRequest;

public class JsonAdapter extends AbstractAdapter {
    public Boolean match(Class<?> type) {
        return true;
    }

    public Object adapt(ServletRequest request, Integer argIndex) throws Exception {
        return null;
    }
}