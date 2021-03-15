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
package jr;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author Sinaye Nhlanzeko
 */
public class Rename {
    private File selectedDir;
    private String newFileName;
    private String BRACKET_START;
    private String BRACKET_END;
    private ObservableList<File> arrFileNames;
    private ObservableList<String> arrNewFNames;
    private ObservableList<String> arrBackUp;
    
    @FXML
    private TextField txfDefaultFileName;
    
    @FXML
    private TextField txfFileNumber;
    
    @FXML
    private TextField txfFolderAddress;
    
    @FXML
    private TextArea txaFilenames;
    
    @FXML
    private Button btnRename1;
    
    @FXML
    private Button btnRename2;
    
    @FXML
    private Button btnUndo;
    
    @FXML
    private RadioButton rbtPrefix;
    
    @FXML
    private RadioButton rbtSuffix;
   
    @FXML
    private ComboBox cmbBrackets;
    
    @FXML
    private ComboBox cmbSpaceReplace;
    
    @FXML
    private void initialize(){
        rbtPrefix.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
            rbtSuffix.setSelected(false);
        });
        
        rbtSuffix.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
            rbtPrefix.setSelected(false);
        });
        
        txfFolderAddress.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
            DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            dirChooser.setTitle("Choose Folder");
            selectedDir = dirChooser.showDialog(null);
            if(selectedDir != null){
                txfFolderAddress.setText(selectedDir.getAbsolutePath());
            }
        });
        
        btnRename1.addEventHandler(ActionEvent.ACTION, e->{
            rename1();
        });
        
        btnRename2.addEventHandler(ActionEvent.ACTION, e->{
            rename2();
        });
        
        btnUndo.addEventHandler(ActionEvent.ACTION, e->{
            Undo();
        });
        
        cmbBrackets.getItems().addAll("Ignore", "Square Brackets - []", "Parentheses - ()", "Curly Brackets - {}");
        cmbSpaceReplace.getItems().addAll("Ignore", "Fullstop - .", "Dash - ", "Underscore - _");
        
        cmbBrackets.setTooltip(new Tooltip("Choose brackets to enclose file numbers with"));
        cmbSpaceReplace.setTooltip(new Tooltip("Choose a character to replace all spaces with"));
        txfDefaultFileName.setTooltip(new Tooltip("Set the default name for all files"));
        txfFileNumber.setTooltip(new Tooltip("Set the starting file number"));
        txfFolderAddress.setTooltip(new Tooltip("Click to choose a folder"));
        arrFileNames = FXCollections.observableArrayList();
        arrNewFNames = FXCollections.observableArrayList();
        arrBackUp = FXCollections.observableArrayList();
    }
    
    public Rename(){
        newFileName = "";
        BRACKET_END = "";
        BRACKET_START = "";
    }
    
    private void rename1(){
        newFileName = "";
        arrFileNames.clear();
        if(!txfFileNumber.getText().isEmpty()){
            if(!(rbtPrefix.isSelected()) || !(rbtSuffix.isSelected())){
                if(selectedDir != null){
                    try{
                       int fileNo = Integer.parseInt(txfFileNumber.getText());
                       
                       int bracketIndex = cmbBrackets.getSelectionModel().getSelectedIndex();
                       int spaceIndex = cmbSpaceReplace.getSelectionModel().getSelectedIndex();
                       
                       String repl = "";
                       
                       switch(spaceIndex){
                           case 0 -> {
                            }
                           case 1 -> repl = ".";
                           case 2 -> repl = "-";
                           case 3 -> repl = "_";
                       }
                       
                       switch(bracketIndex){
                           case 0 -> {
                               BRACKET_START = "";
                               BRACKET_END = "";
                            }
                           
                           case 1 -> {
                               BRACKET_START = "[";
                               BRACKET_END = "]";
                            }
                           case 2 -> {
                               BRACKET_START = "(";
                               BRACKET_END = ")";
                            }
                           case 3 -> {
                               BRACKET_START = "{";
                               BRACKET_END = "}";
                            }
                       }
                       int j = 0;
                       for(File file : selectedDir.listFiles()){
                           if(file.isFile()){
                               if(rbtPrefix.isSelected()){
                                   int index = file.getName().lastIndexOf(".");
                                   String fileNameEnd = file.getName().substring(index);
                                   newFileName = BRACKET_START + fileNo + BRACKET_END + " " + 
                                           (txfDefaultFileName.getText().isEmpty() ? file.getName() : txfDefaultFileName.getText() + fileNameEnd);
                               }else{
                                   int index = file.getName().lastIndexOf(".");
                                   String fileNameStart = file.getName().substring(0, index);
                                   String fileNameEnd = file.getName().substring(index);
                                   
                                   newFileName = (txfDefaultFileName.getText().isEmpty() ? fileNameStart : txfDefaultFileName.getText()) 
                                           + " " + BRACKET_START + fileNo + BRACKET_END + fileNameEnd;
                               }
                              
                               fileNo++;
                               arrFileNames.add(j, new File(selectedDir.getAbsolutePath() + "\\" + 
                                       (repl.isEmpty() ? newFileName : newFileName.replaceAll(" ", repl))));
                               j++;
                           }
                       }
                       finalStep(arrFileNames);
                    }catch(NumberFormatException nfe){
                        
                    }
                }else{
                    
                }
            }else{
                
            }
        }else{
            
        }
    }
    
    private void rename2(){
        arrFileNames.clear();
        if(!txaFilenames.getText().isEmpty()){
            //Reads text from the text area line by line
            for(String line : txaFilenames.getText().split("\n")){
                arrNewFNames.add(line);
            }
            
            /**
             * First checks that the total lines or filenames in the text area
             * is equal to the total files in the selected folder. 
             * If true, it begins renaming each file using the new file names
             * read from the text area
             */
            if(arrNewFNames.size() == selectedDir.listFiles().length){
                //Store files in the current folder in an array
                File arrFiles[] = selectedDir.listFiles();
                int i = 0;
                String fileType = "";
                for(String fName : arrNewFNames){
                    if(arrFiles[i].isFile()){
                       //Gets the file type of the current file in arrFile[i]
                       fileType = arrFiles[i].getName().substring(arrFiles[i].getName().lastIndexOf("."));
                       arrFileNames.add(i, new File(selectedDir.getAbsolutePath() + "\\" + fName + fileType));
                       i++;
                    }
                }
                finalStep(arrFileNames);
            }
        }
    }
    
    private void finalStep(ObservableList<File> arr){
        Start.getFileList().setMsg(arrFileNames);
        boolean apply = Start.getFileList().isApply();
        if(apply){
           System.out.println(apply);
           int i = 0;
           for(File f : selectedDir.listFiles()){
               arrBackUp.add(i, f.getName());
               f.renameTo(arrFileNames.get(i));
               i++;
           }
        }
    }
    
    private void Undo(){
        if(!arrBackUp.isEmpty()){
            arrFileNames.clear();
            int i = 0;
            for(File f : selectedDir.listFiles()){
                arrFileNames.add(i, new File(selectedDir.getAbsolutePath() + "\\" + arrBackUp.get(i)));
                i++;
            }
            finalStep(arrFileNames);
        }
    }
}
