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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 *
 * @author Sinaye Nhlanzeko
 */
public class FileList {
    private boolean apply;
    
    @FXML
    private TextArea txaNewFileNames;
    
    @FXML
    private Button btnApply;
    
    @FXML
    private Button btnCancel;
    
    @FXML
    private void initialize(){
        btnApply.addEventHandler(ActionEvent.ACTION, e->{
            btnApplyClicked();
        });
        
        btnCancel.addEventHandler(ActionEvent.ACTION, e->{
            apply = false;
            Start.getMsgWindow().close();
        });
        
        Start.setFileListCtrl(this);
    }
    
    public FileList(){
        apply = false;
    }
    
    public boolean isApply(){
        return apply;
    }
    
    public void setMsg(ObservableList<File> arr){
        txaNewFileNames.setText("");
        arr.forEach(fileName -> {
            txaNewFileNames.appendText(fileName.getName() + "\n");
        });
        Start.getMsgWindow().showAndWait();
    }
    
    private void btnApplyClicked(){
        apply = true;
        Start.getMsgWindow().close();
    }
}