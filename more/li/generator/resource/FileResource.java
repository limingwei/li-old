package li.generator.resource;

import java.io.File;

import li.util.IOUtil;

public class FileResource extends Resource {
    private File file;

    public Resource setFile(File file) {
        this.file = file;
        return this;
    }

    public String getBody() {
        return IOUtil.read(this.file);
    }
}