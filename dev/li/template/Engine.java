package li.template;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import li.template.compiler.Compiler;
import li.util.FileUtil;
import li.util.Log;

/**
 * @author li
 */
public class Engine {
    private static final Log log = Log.init();

    private Map<String, Template> templates = new ConcurrentHashMap<String, Template>();

    private Map<String, String> config = new ConcurrentHashMap<>();

    private Compiler compiler = new Compiler();

    public static synchronized Engine getIntense(Map config) {
        Engine engine = new Engine();
        engine.config.putAll(config);
        return engine;
    }

    public Template getTemplate(String name) {
        Template template = templates.get(name);
        if (null == template) {
            template = parse(name, FileUtil.read(new File(config.get("TEMPLATE_DIRECTORY") + name)));
            templates.put(name, template);
        }
        return template;
    }

    public Template parse(String name, String content) {
        log.info("parsing template " + name);
        try {
            String packageName = "li.template.impl";
            String className = name.replace(".", "_") + "_utf8_writer";

            String source = head(packageName, className);

            String local_variables = "";
            String page_code = "";
            String temp1 = "";
            for (int i = 0; i < content.length(); i++) {
                char each = content.charAt(i);

                if ('<' == each) {
                    if ('!' == content.charAt(++i) && '-' == content.charAt(++i) && '-' == content.charAt(++i)) {// 语句开始
                        if (!temp1.isEmpty()) {
                            page_code += write("\"" + temp1 + "\"");// 输出缓冲的静态文本
                            temp1 = "";// 清空静态文本缓冲区
                        }

                        String value = "";// 静态文本
                        char[] temp = new char[3];
                        while (true) {
                            temp[0] = content.charAt(++i);
                            if ('-' == temp[0]) {
                                temp[1] = content.charAt(++i);
                                temp[2] = content.charAt(++i);
                                if ('-' == temp[1] && '>' == temp[2]) {// 语句结束
                                    page_code += value;// 语句输出
                                    break;
                                } else {
                                    value += new String(temp);
                                }
                            } else {
                                value += temp[0];
                            }
                        }
                    }
                } else if ('$' == each) {
                    if ('{' == content.charAt(++i)) {// 表达式
                        if (!temp1.isEmpty()) {
                            page_code += write("\"" + temp1 + "\"");// 输出缓冲的静态文本
                            temp1 = "";// 清空静态文本缓冲区
                        }

                        String value = "";
                        while (true) {
                            each = content.charAt(++i);
                            if ('}' == each) {
                                page_code += write(value);// 取值输出
                                break;
                            } else {
                                value += each;
                            }
                        }
                    }
                } else {// 文本
                    temp1 += each;
                }
            }

            source += local_variables;
            source += page_code;
            source += foot();

            System.out.println(source);

            Class clazz = compiler.doCompile(packageName + "." + className, source);
            return (Template) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String head(String packageName, String className) {
        return "package " + packageName + ";" + //
                "import java.io.*;" + //
                "import java.util.*;" + //
                "import li.template.*;" + //
                "public class " + className + " extends Template {" + //
                "protected void doRender(Map map, Writer out) throws Exception {";
    }

    private String foot() {
        return "out.flush();" + //
                "out.close();" + //
                "}" + //
                "}";
    }

    private String write(String value) {
        return "out.write(" + value + "+\"\");";
    }
}