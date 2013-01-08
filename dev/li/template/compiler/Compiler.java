package li.template.compiler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 * JdkCompiler. (SPI, Singleton, ThreadSafe)
 * 
 * @see httl.spi.parsers.AbstractParser#setCompiler(Compiler)
 * 
 * @author Liang Fei (liangfei0201 AT gmail DOT com)
 */
public class Compiler {

    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    private final DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();

    private final ClassLoaderImpl classLoader;

    private final JavaFileManagerImpl javaFileManager;

    private final List<String> options = new ArrayList<String>();

    private final List<String> lintOptions = new ArrayList<String>();

    public Compiler() {
        if (compiler == null) {
            throw new IllegalStateException("Can not get system java compiler. Please add jdk tools.jar to your classpath.");
        }
        StandardJavaFileManager manager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader instanceof URLClassLoader && (!loader.getClass().getName().equals("sun.misc.Launcher$AppClassLoader"))) {
            try {
                URLClassLoader urlClassLoader = (URLClassLoader) loader;
                List<File> files = new ArrayList<File>();
                for (URL url : urlClassLoader.getURLs()) {
                    files.add(new File(url.getFile()));
                }
                manager.setLocation(StandardLocation.CLASS_PATH, files);
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        classLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoaderImpl>() {
            public ClassLoaderImpl run() {
                return new ClassLoaderImpl(loader);
            }
        });
        javaFileManager = new JavaFileManagerImpl(manager, classLoader);
        lintOptions.add("-Xlint:unchecked");
    }

    public Class<?> doCompile(String name, String sourceCode) throws Exception {
        try {
            return doCompile(name, sourceCode, options);
        } catch (Exception e) {
            if (e.getMessage().contains("-Xlint:unchecked")) {
                try {
                    return doCompile(name, sourceCode, lintOptions);
                } catch (Exception e2) {
                    throw e2;
                }
            }
            throw e;
        }
    }

    private Class<?> doCompile(String name, String sourceCode, List<String> options) throws Exception {
        try {
            return classLoader.findClass(name);
        } catch (ClassNotFoundException e) {
            int i = name.lastIndexOf('.');
            String packageName = i < 0 ? "" : name.substring(0, i);
            String className = i < 0 ? name : name.substring(i + 1);
            JavaFileObjectImpl javaFileObject = new JavaFileObjectImpl(className, sourceCode);
            javaFileManager.putFileForInput(StandardLocation.SOURCE_PATH, packageName, className + ".java", javaFileObject);
            Boolean result = compiler.getTask(null, javaFileManager, diagnosticCollector, options, null, Arrays.asList(new JavaFileObject[] { javaFileObject })).call();
            if (result == null || !result.booleanValue()) {
                throw new IllegalStateException("Compilation failed. class: " + name + ", diagnostics: " + diagnosticCollector.getDiagnostics());
            }
            return classLoader.loadClass(name);
        }
    }
}