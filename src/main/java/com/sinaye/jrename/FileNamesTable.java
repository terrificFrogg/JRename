package com.sinaye.jrename;

import javafx.beans.property.SimpleStringProperty;

public class FileNamesTable {
    private SimpleStringProperty oldName;
    private SimpleStringProperty newName;

    public FileNamesTable(String oldName, String newName){
        this.oldName = new SimpleStringProperty(oldName);
        this.newName = new SimpleStringProperty(newName);
    }

    public String getOldName(){
        return oldName.get();
    }

    public String getNewName(){
        return newName.get();
    }
}
