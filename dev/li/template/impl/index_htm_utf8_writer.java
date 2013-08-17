package li.template.impl;

import java.io.Writer;
import java.util.Map;

import li.template.Template;

public class index_htm_utf8_writer extends Template {
    protected void doRender(Map<?, ?> map, Writer out) throws Exception {
        String name = (String) map.get("name");
        Boolean flag = (Boolean) map.get("flag");

        out.write("你好 " + "");
        out.write(name + "");
        out.write(" " + "");
        out.write(name.isEmpty() + "");
        out.write(" " + "");
        out.write(name.length() + "");
        if (flag) {
            out.write("	真的" + "");
        } else {
            out.write("	假的" + "");
        }
        for (int i = 1; i <= 9; i++) {
            out.write("	第" + "");
            out.write(i + "");
            out.write("个" + "");
        }
        int a = 1;
        while (a <= 9) {
            out.write("	第" + "");
            out.write(a + "");
            out.write("个" + "");
            a++;
        }
        out.flush();
        out.close();
    }
}
