package com.fx.light.model;

import java.io.File;

public class OriginalFile {

    private String path;
    private String name;
    private File file;

    public OriginalFile(File file) {
        this.path = file.getPath();
        this.name = file.getName();
        this.file = file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
