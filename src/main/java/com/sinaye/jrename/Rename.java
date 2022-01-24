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
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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
    private ObservableList<File> arrBackUp;
    private ObservableList<String> arrFileTypes;
    private boolean undoSelected;
    private boolean fromRename2;
    
    @FXML
    private TextField txfDefaultFileName;
    
    @FXML
    private TextField txfFileNumber;
    
    @FXML
    private TextField txfFolderAddress;
    
    @FXML
    private TextArea txaFilenames;

    @FXML
    private TextField txfSourceText;

    @FXML
    private TextField txfSample;
    
    @FXML
    private Button btnRename1;
    
    @FXML
    private Button btnRename2;
    
    @FXML
    private Button btnFirstUndo;

    @FXML
    private Button btnSecUndo;

    @FXML
    private Button btnThirdUndo;

    @FXML
    private Button btnRemoveText;
    
    @FXML
    private RadioButton rbtPrefix;
    
    @FXML
    private RadioButton rbtSuffix;
   
    @FXML
    private ComboBox<String> cmbBrackets;
    
    @FXML
    private ComboBox<String> cmbSpaceReplace;
    
    @FXML
    private ComboBox<String> cmbFileTypes;
    private ChangeListener<? super java.lang.String> changeListener;
    @FXML
    private void initialize(){
         changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                fillSourceText(newValue.toString());
            }
        };
        rbtPrefix.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
            rbtSuffix.setSelected(false);
        });
        
        rbtSuffix.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
            rbtPrefix.setSelected(false);
        });
        
        txfFolderAddress.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
            cmbFileTypes.getSelectionModel().selectedItemProperty().removeListener(changeListener);
            cmbFileTypes.getItems().clear();
            cmbFileTypes.setPromptText("File Types");

            DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            dirChooser.setTitle("Choose Folder");
            selectedDir = dirChooser.showDialog(null);
            if(selectedDir != null){
                txfFolderAddress.setText(selectedDir.getAbsolutePath());
            }
            getFileTypes();
        });
        
        btnRename1.addEventHandler(ActionEvent.ACTION, e->{
            rename1();
        });
        
        btnRename2.addEventHandler(ActionEvent.ACTION, e->{
            rename2();
        });

        btnRemoveText.addEventHandler(ActionEvent.ACTION, e->{
            removeText();
        });

        btnFirstUndo.addEventHandler(ActionEvent.ACTION, e->{
            Undo();
        });

        btnSecUndo.addEventHandler(ActionEvent.ACTION, e->{
            Undo();
        });

        btnThirdUndo.addEventHandler(ActionEvent.ACTION, e->{

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
        arrFileTypes = FXCollections.observableArrayList();
    }

    public Rename(){
        newFileName = "";
        BRACKET_END = "";
        BRACKET_START = "";
        undoSelected = false;
        fromRename2 = false;
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
                               if(file.getAbsolutePath().endsWith(getSelectedFileType())){
                                   if(rbtPrefix.isSelected()){
                                       int index = file.getName().lastIndexOf(".");
                                       String fileNameEnd = file.getName().substring(index);
                                       newFileName = BRACKET_START + fileNo + BRACKET_END + " " + 
                                               (txfDefaultFileName.getText().isEmpty() ? file.getName() : 
                                               txfDefaultFileName.getText() + fileNameEnd);
                                   }else{
                                       int index = file.getName().lastIndexOf(".");
                                       //Extract the filename
                                       String fileNameStart = file.getName().substring(0, index);
                                       String fileNameEnd = file.getName().substring(index);

                                       newFileName = (txfDefaultFileName.getText().isEmpty() ? fileNameStart : 
                                               txfDefaultFileName.getText()) 
                                               + BRACKET_START + fileNo + BRACKET_END + fileNameEnd;
                                   }

                                   fileNo++;
                                   arrFileNames.add(j, new File(selectedDir.getAbsolutePath() + "\\" + 
                                           (repl.isEmpty() ? newFileName : newFileName.replaceAll(" ", repl))));
                                   j++;
                               }
                           }
                       }
                       finalStep(arrFileNames);
                    }catch(NumberFormatException nfe){}
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
                arrNewFNames.add(sanitise(line, ""));
            }
            
            /**
             * First checks that the total lines or filenames in the text area
             * is equal to the total files in the selected folder. 
             * If true, it begins renaming each file using the new file names
             * read from the text area
             */
//            if(arrNewFNames.size() == selectedDir.listFiles().length){
                //Store files in the current folder in an array
                File arrFiles[] = selectedDir.listFiles();
                int fIndex = 0;
                int fileNo = 1;
                String fileType = "";
                
                for(File currFile : arrFiles){
                    //without this if statement, the app crashes for some reason
                    if(currFile.getAbsolutePath().endsWith(getSelectedFileType())){
                        if(currFile.isFile()){
                            //Gets the file type of the current file in arrFile[i]
                           fileType = currFile.getName().substring(currFile.getName().lastIndexOf("."));
                           arrFileNames.add(fIndex, new File(selectedDir.getAbsolutePath() 
                                   + "\\" + fileNo + ". " + arrNewFNames.remove(0) + fileType));
                           fileNo++;
                           fIndex++;
                        }
                    }
                }
                fromRename2 = true;
                finalStep(arrFileNames);
//            }
        }
    }

    private void fillSourceText(String fileType){
        File[] fList = selectedDir.listFiles();
        if(fList != null){
            for(File f : fList){
                if(f.isFile()){
                    if(f.getName().endsWith(fileType)){
                        txfSourceText.setText(f.getName().substring(0, f.getName().lastIndexOf(".")));
                        break;  //Break out of loop after the first file is name found
                    }
                }
            }
        }
    }

    /**
     * TODO
     * Add options to remove text at various position like beginning, middle and end of text.
     * Add other replace options like replace with another text or replace all or just one
     */
    private void removeText(){
        String sampleText = txfSample.getText();
        if(!sampleText.isEmpty()){
            arrFileNames.clear();
            String fileExtension = "";
            String fileName = "";
            for(File currFile : Objects.requireNonNull(selectedDir.listFiles())){
                if(currFile.isFile()){
                    fileName = currFile.getName().substring(0, currFile.getName().lastIndexOf(".")); //get the file name without the file extension
                    fileExtension = currFile.getName().substring(currFile.getName().lastIndexOf(".")); //get the file extension
                    arrFileNames.add(new File(selectedDir.getAbsolutePath() + "\\" + fileName.replaceAll("[" + sampleText + "]", "") + fileExtension));
                }
            }
            finalStep(arrFileNames);
        }
    }

    /**
     * Replaces all characters that are not digits, white space or letter.
     * Returns a string without the characters that matched the regex [^\\d\\sa-zA-Z]
     */
    private String sanitise(String str, String repl){
        Pattern pat = Pattern.compile("[^\\d\\sa-zA-Z]");
        Matcher mat = pat.matcher(str);
        return mat.replaceAll(repl);
    }
    
    /**
     * Looks into an ObservableList and replaces all characters that are not digits, white space or letter.
     * Returns an ObservableList without the characters that matched the regex [^\\d\\sa-zA-Z]
     */
    private ObservableList<String> sanitise(ObservableList<String> arr, String repl){
        Pattern pat = Pattern.compile("[^\\d\\sa-zA-Z]");
        Matcher mat = null;
        for(int i = 0; i <= arr.size() - 1; i++){
            mat = pat.matcher(arr.get(i));
            arr.set(i, mat.replaceAll(repl));
        }
        return arr;
    }
    
    private void finalStep(ObservableList<File> arr){
        File[] currentFilesInFolder = (undoSelected ? sortArrBackUp(selectedDir.listFiles()) : selectedDir.listFiles());

        //Calls a method to build a table that will display old and new file names side by side for easy comparison.
        Start.getFileList().buildCompareTable(currentFilesInFolder, arr, getSelectedFileType());
        boolean apply = Start.getFileList().isApply();

        if(apply){
           undoSelected = false;
           int i = 0;
           for(File currentFile : Objects.requireNonNull(currentFilesInFolder)){
               if(currentFile.getAbsolutePath().endsWith(getSelectedFileType())){
                   arrBackUp.add(i, new File(currentFile.getAbsolutePath()));

                   try {
                       Files.move(currentFile.toPath(), currentFile.toPath().resolveSibling(arr.get(i).getName()), REPLACE_EXISTING);
                       i++;
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }
        }
    }

    /**
     * This method exists to sort the files that have been already renamed. It should be used if the file numbers were
     * added as a prefix.
     * */
    private File[] sortArrBackUp(File[] unsortedFileList){
        if(rbtPrefix.isSelected() || fromRename2) {
            Arrays.sort(unsortedFileList, new Comparator<File>() {

                @Override
                public int compare(File file1, File file2) {
                    int n1 = extractNumbers(file1.getName());
                    int n2 = extractNumbers(file2.getName());
                    return n1 - n2;
                }

                private int extractNumbers(String name) {
                    String numberFound = "";
                    Pattern pattern = Pattern.compile("^[^\\d]*(\\d+)");
                    Matcher matcher = pattern.matcher(name);
                    boolean found = false;
                    while (matcher.find()) {
                        numberFound += matcher.group();
                        found = true;
                    }

                    if (found) {
                        /*
                         * The first regex finds the first occurrence of numbers, regardless of their length. These numbers
                         * maybe within brackets such as (), {} or [] or none. If they're within brackets, the regex is able
                         * to find the numbers however it also includes the first opening bracket. I'm too lazy to fix. So
                         * the regex below then removes that opening bracket.
                         */
                        pattern = Pattern.compile("\\d");
                        matcher = pattern.matcher(numberFound);
                        String num = "";
                        while (matcher.find()) {
                            num += matcher.group();
                        }
                        return Integer.parseInt(num);
                    }
                    return 0;
                }
            });
        }
        fromRename2 = false;
        return unsortedFileList;
    }

    private void Undo(){
        if(!arrBackUp.isEmpty()){
            undoSelected = true;
            arrFileNames.clear();
            for(File currentFile : arrBackUp){
                arrFileNames.add(currentFile);
            }
            arrBackUp.clear();
            finalStep(arrFileNames);
        }
    }
    
    private void getFileTypes(){
        if(selectedDir != null){
            arrFileTypes.clear();
            String fType = "";
            for(File f : selectedDir.listFiles()){
                if(f.isFile()){
                    fType = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("."));
                    if(!arrFileTypes.contains(fType)){
                        arrFileTypes.add(fType);
                    }
                }
            }
            cmbFileTypes.getItems().addAll(arrFileTypes);
            cmbFileTypes.getSelectionModel().selectedItemProperty().addListener(changeListener);
        }
    }
    
    private String getSelectedFileType(){
        return cmbFileTypes.getSelectionModel().getSelectedItem() + "";
    }
}
