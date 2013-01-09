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

            content = content.replaceAll("\\\"", "\\\\\"");

            String source = "" + //
                    "package " + packageName + ";\n\n" + //
                    "import java.io.*;\n" + //
                    "import java.util.*;\n" + //
                    "import li.template.*;\n\n" + //
                    "public class " + className + " extends Template {\n" + //
                    "    protected void doRender(Map params, Writer writer) throws Exception {\n" + //
                    "        writer.write(\"index_htm_utf8_writer \\n\");\n" + //
                    "        writer.write(\"params : \" + params + \"\\n\");\n" + //
                    "        writer.write(\"content : \" +\"" + content + "\");\n" + //
                    "        writer.flush();\n" + //
                    "    }\n" + //
                    "}";

            System.out.println(source);

            Class clazz = compiler.doCompile(packageName + "." + className, source);
            return (Template) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}