package li.template.compiler;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

import li.template.utils.ClassUtils;

/**
 * @author li
 */
public final class JavaFileObjectImpl extends SimpleJavaFileObject {

    private ByteArrayOutputStream bytecode;

    private final CharSequence source;

    public JavaFileObjectImpl(final String baseName, final CharSequence source) {
        super(ClassUtils.toURI(baseName + ".java"), Kind.SOURCE);
        this.source = source;
    }

    public JavaFileObjectImpl(final String name, final Kind kind) {
        super(ClassUtils.toURI(name), kind);
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
}