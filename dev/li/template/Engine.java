package li.template;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import li.template.compiler.Compiler;

public class Engine {
    private static final Map<String, Template> TEMPLATES = new ConcurrentHashMap<String, Template>();

    private static final Compiler compiler = new Compiler();

    public static Engine getIntense() {
        return new Engine();
    }

    public Template getTemplate(String name) {
        Template template = TEMPLATES.get(name);
        if (null == template) {
            template = parse(name);
            TEMPLATES.put(name, template);
        }
        return template;
    }

    private Template parse(String name) {
        try {
            String packageName = "li.template.impl";
            String className = name.replace(".", "_") + "_utf8_writer";

            String source = "" + //
                    "package " + packageName + ";" + //
                    "import java.io.*;" + //
                    "import java.util.*;" + //
                    "import li.template.*;" + //
                    "public class " + className + " extends Template {" + //
                    "    protected void doRender(Map params, Writer writer) throws Exception {" + //
                    "        writer.write(\"index_htm_utf8_writer\");" + //
                    "        writer.write(\"params : \" + params);" + //
                    "        writer.flush();" + //
                    "    }" + //
                    "}";
            Class clazz = compiler.doCompile(packageName + "." + className, source);
            return (Template) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}