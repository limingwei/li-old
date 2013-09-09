package li.compiler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class Demo {
    public static void main(String[] args) throws Exception {
        String source = "public class Test{public void sayHi(){System.err.println(\"你好\");}}";
        String type = "Test";
        String func = "sayHi";
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();

        StandardJavaFileManager javaFileManager = javaCompiler.getStandardFileManager(null, null, null);

        JavaFileObject javaFileObject = new StringJavaObject(type, source);

        List<String> options = new ArrayList<String>();
        // options.addAll(Arrays.asList("-Xlint:unchecked"));

        List<JavaFileObject> javaFileObjects = Arrays.asList(javaFileObject);

        CompilationTask compilationTask = javaCompiler.getTask(null, javaFileManager, null, options, null, javaFileObjects);

        if (compilationTask.call()) {
            Class<?> clazz = Class.forName(type);
            Object target = clazz.newInstance();
            Method method = clazz.getMethod(func);
            method.invoke(target);
        }
    }
}

class StringJavaObject extends SimpleJavaFileObject {
    private String source;

    protected StringJavaObject(String type, String source) {
        super(URI.create("String:///" + type + Kind.SOURCE.extension), Kind.SOURCE);
        this.source = source;
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return this.source;
    }
}