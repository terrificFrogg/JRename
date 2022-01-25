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

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Sinaye Nhlanzeko
 */
public class Start extends Application{
    private Parent homeFXML;
    private Parent fileListFXML;
    private static Stage fileList;
    private static FileList flObj;

    @Override
    public void start(Stage primaryStage){
        try {
            homeFXML = FXMLLoader.load(getClass().getResource("home.fxml"));
            primaryStage.setScene(new Scene(homeFXML));
            primaryStage.show();
            primaryStage.setResizable(false);
            primaryStage.setTitle("JRename");
            
            fileListFXML = FXMLLoader.load(getClass().getResource("filelist.fxml"));
            fileList = new Stage();
            fileList.initModality(Modality.APPLICATION_MODAL);
            fileList.setScene(new Scene(fileListFXML));
            fileList.initStyle(StageStyle.UTILITY);
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void setFileListCtrl(FileList fl){
        Start.flObj = fl;
    }
    
    public static FileList getFileList(){
        return flObj;
    }
    
    public static Stage getMsgWindow(){
        return Start.fileList;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
