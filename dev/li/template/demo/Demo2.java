package li.template.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class Demo2 {
    public static void main(String[] args) throws Exception {
        String source = "public class Test { public static void sayHi() { System.err.println(\"Test is saying hi\"); }}";
        Class<?> type = new DefaultCompiler().doCompile("Test", source);
        Method method = type.getMethod("sayHi");
        method.invoke(null);
    }
}

class DefaultCompiler {
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private final DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
    private final ClassLoaderImpl classLoader;
    private final JavaFileManagerImpl javaFileManager;
    private final List<String> options = new ArrayList<String>();
    private final List<String> lintOptions = new ArrayList<String>();

    public DefaultCompiler() {
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

class ClassLoaderImpl extends ClassLoader {
    private final Map<String, JavaFileObject> classes = new HashMap<String, JavaFileObject>();

    ClassLoaderImpl(final ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    Collection<JavaFileObject> files() {
        return Collections.unmodifiableCollection(classes.values());
    }

    protected Class<?> findClass(final String qualifiedClassName) throws ClassNotFoundException {
        JavaFileObject file = classes.get(qualifiedClassName);
        if (file != null) {
            byte[] bytes = ((JavaFileObjectImpl) file).getByteCode();
            return defineClass(qualifiedClassName, bytes, 0, bytes.length);
        }
        try {
            return Class.forName(qualifiedClassName);
        } catch (ClassNotFoundException nf) {
            return super.findClass(qualifiedClassName);
        }
    }

    void add(final String qualifiedClassName, final JavaFileObject javaFile) {
        classes.put(qualifiedClassName, javaFile);
    }
}

class JavaFileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {
    private final ClassLoaderImpl classLoader;
    private final Map<URI, JavaFileObject> fileObjects = new HashMap<URI, JavaFileObject>();

    public JavaFileManagerImpl(JavaFileManager fileManager, ClassLoaderImpl classLoader) {
        super(fileManager);
        this.classLoader = classLoader;
    }

    public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
        FileObject o = fileObjects.get(uri(location, packageName, relativeName));
        if (o != null) {
            return o;
        }
        return super.getFileForInput(location, packageName, relativeName);
    }

    public void putFileForInput(StandardLocation location, String packageName, String relativeName, JavaFileObject file) {
        fileObjects.put(uri(location, packageName, relativeName), file);
    }

    private URI uri(Location location, String packageName, String relativeName) {
        try {
            return new URI(location.getName() + '/' + packageName + '/' + relativeName);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public JavaFileObject getJavaFileForOutput(Location location, String qualifiedName, Kind kind, FileObject outputFile) throws IOException {
        JavaFileObject file = new JavaFileObjectImpl(qualifiedName, kind);
        classLoader.add(qualifiedName, file);
        return file;
    }

    public ClassLoader getClassLoader(JavaFileManager.Location location) {
        return classLoader;
    }

    public String inferBinaryName(Location loc, JavaFileObject file) {
        if (file instanceof JavaFileObjectImpl)
            return file.getName();
        return super.inferBinaryName(loc, file);
    }

    public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse) throws IOException {
        Iterable<JavaFileObject> result = super.list(location, packageName, kinds, recurse);
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        List<URL> urlList = new ArrayList<URL>();
        Enumeration<URL> e = contextClassLoader.getResources("httl");
        while (e.hasMoreElements()) {
            urlList.add(e.nextElement());
        }
        ArrayList<JavaFileObject> files = new ArrayList<JavaFileObject>();
        if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
            for (JavaFileObject file : fileObjects.values()) {
                if (file.getKind() == Kind.CLASS && file.getName().startsWith(packageName)) {
                    files.add(file);
                }
            }
            files.addAll(classLoader.files());
        } else if (location == StandardLocation.SOURCE_PATH && kinds.contains(JavaFileObject.Kind.SOURCE)) {
            for (JavaFileObject file : fileObjects.values()) {
                if (file.getKind() == Kind.SOURCE && file.getName().startsWith(packageName)) {
                    files.add(file);
                }
            }
        }
        for (JavaFileObject file : result) {
            files.add(file);
        }
        return files;
    }
}

class JavaFileObjectImpl extends SimpleJavaFileObject {
    private ByteArrayOutputStream bytecode;
    private final CharSequence source;

    public JavaFileObjectImpl(final String baseName, final CharSequence source) {
        super(toURI(baseName + ".java"), Kind.SOURCE);
        this.source = source;
    }

    public JavaFileObjectImpl(final String name, final Kind kind) {
        super(toURI(name), kind);
        source = null;
    }

    public JavaFileObjectImpl(URI uri, Kind kind) {
        super(uri, kind);
        source = null;
    }

    public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws UnsupportedOperationException {
        if (source == null) {
            throw new UnsupportedOperationException("source == null");
        }
        return source;
    }

    public OutputStream openOutputStream() {
        return bytecode = new ByteArrayOutputStream();
    }

    public byte[] getByteCode() {
        return bytecode.toByteArray();
    }

    public static URI toURI(String name) {
        try {
            return new URI(name);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}