/*
 * Copyright (C) 2021 Sinaye Nhlanzeko
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.sinaye.jrename;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Sinaye Nhlanzeko
 */
public class FileList {
    private boolean apply;
    private ObservableList<FileNamesTable> tableItems;

    @FXML
    private Button btnApply;
    
    @FXML
    private Button btnCancel;

    @FXML
    private TableView <FileNamesTable> tblFileNames;

    @FXML
    private void initialize(){
        btnApply.addEventHandler(ActionEvent.ACTION, e->{
            btnApplyClicked();
        });
        
        btnCancel.addEventHandler(ActionEvent.ACTION, e->{
            apply = false;
            Start.getMsgWindow().close();
        });
        tableItems = FXCollections.observableArrayList();

        Start.setFileListCtrl(this);
    }
    
    public FileList(){
        this.apply = false;
    }
    
    public boolean isApply(){
        return apply;
    }

    private void btnApplyClicked(){
        apply = true;
        Start.getMsgWindow().close();
    }

    /**
    * It displays old and new file names side by side for easy
    * comparison using a <code>TableView</code>. It requires 3 arguments: A file array of files from the selected folder,
    * an <code>ObseverableList</code> of the new file names and a String that has the file type. The file type is
    * used to filter the files in the File array.
    */
    public void buildCompareTable(File[] ogFiles, ObservableList<File> newFileNames, String fileType){
        tblFileNames.getColumns().clear();
        tableItems.clear();

        TableColumn<FileNamesTable, String> oldName = new TableColumn<>("Old FileName");
        TableColumn<FileNamesTable, String> newName = new TableColumn<>("New FileName");

        oldName.setCellValueFactory(new PropertyValueFactory<>("oldName"));
        newName.setCellValueFactory(new PropertyValueFactory<>("newName"));

        for(int i = 0; i <= newFileNames.size() - 1; i++){
            if(ogFiles[i].isFile()){
                if(ogFiles[i].getName().endsWith(fileType)){
                    tableItems.add(i, new FileNamesTable(ogFiles[i].getName(), newFileNames.get(i).getName()));
                }
            }
        }

        tblFileNames.getColumns().addAll(oldName, newName);
        tblFileNames.setItems(tableItems);
        Start.getMsgWindow().showAndWait();
    }
}