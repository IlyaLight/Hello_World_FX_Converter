package com.fx.light.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class OriginalFile {

    private SimpleStringProperty path;
    private SimpleStringProperty name;
    private File file;

    public OriginalFile(File file) {
        this.path = new SimpleStringProperty(file.getPath());
        this.name = new SimpleStringProperty(file.getName());
        this.file = file;
    }

    public String getPath() {
        return path.get();
    }

    public SimpleStringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
