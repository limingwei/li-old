package li.template;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import li.template.compiler.Compiler;
import li.util.Files;
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
            template = parse(name, Files.read(new File(config.get("TEMPLATE_DIRECTORY") + name)));
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

            String text = "";
            for (int i = 0; i < content.length(); i++) {
                char each = content.charAt(i);

                if ('<' == each) {
                    if ('!' == content.charAt(++i) && '-' == content.charAt(++i) && '-' == content.charAt(++i)) {// 语句开始
                        source += write("\"" + text + "\"");// 输出缓冲的静态文本
                        text = "";// 清空静态文本缓冲区

                        String value = "";
                        char[] temp = new char[3];
                        while (true) {
                            temp[0] = content.charAt(++i);
                            if ('-' == temp[0]) {
                                temp[1] = content.charAt(++i);
                                temp[2] = content.charAt(++i);
                                if ('-' == temp[1] && '>' == temp[2]) {// 语句结束
                                    source += value;
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
                        source += write("\"" + text + "\"");// 输出缓冲的静态文本
                        text = "";// 清空静态文本缓冲区

                        String value = "";
                        while (true) {
                            each = content.charAt(++i);
                            if ('}' == each) {
                                source += write(value(value));
                                break;
                            } else {
                                value += each;
                            }
                        }
                    }
                } else {// 文本
                    text += each;
                }
            }

            source += foot();

            System.out.println(source);

            Class clazz = compiler.doCompile(packageName + "." + className, source);
            return (Template) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String head(String packageName, String className) {
        return "package " + packageName + ";\n\n" + //
                "import java.io.*;\n" + //
                "import java.util.*;\n" + //
                "import li.template.*;\n\n" + //
                "public class " + className + " extends Template {\n" + //
                "    protected void doRender(Map map, Writer writer) throws Exception {\n";
    }

    private String foot() {
        return "        writer.flush();\n" + //
                "        writer.close();\n" + //
                "    }\n" + //
                "}";
    }

    private String write(String value) {
        return "        writer.write(" + value + ".toString());\n";
    }

    private String value(String value) {
        return "map.get(\"" + value + "\")";
    }
}