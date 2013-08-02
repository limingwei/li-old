package li.mvc.view;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import li.json.Json;

public class JsonView extends AbstractView {
    public void render(String tempPath, HttpServletResponse response, Map<String, Object> map) throws Exception {
        response.getWriter().write(Json.toJson(map));
    }
}