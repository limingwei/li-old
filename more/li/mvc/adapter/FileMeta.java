package li.mvc.adapter;

import java.io.File;

public class FileMeta {
    private String fieldName;
    private String localName;
    private File file;

    public FileMeta(String fieldName, String localName, File file) {
        this.fieldName = fieldName;
        this.localName = localName;
        this.file = file;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getLocalName() {
        return this.localName;
    }

    public File getFile() {
        return this.file;
    }

    public String getExtension() {
        String name = this.getLocalName();
        int pos = name.lastIndexOf('.');
        if (pos >= 0)
            return name.substring(pos);
        return "";
    }

    public String toString() {
        return super.toString() + " fieldName:" + fieldName + " localName:" + localName + " file:" + file.getAbsolutePath();
    }
}