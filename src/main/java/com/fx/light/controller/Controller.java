package com.fx.light.controller;


import com.fx.light.model.OriginalFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Controller {

    @FXML
    public ListView<OriginalFile> listFiles;
    @FXML
    private Button buttonConvert;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonDelit;
    @FXML
    private Button buttonUp;
    @FXML
    private Button buttonDown;


    private ObservableList<OriginalFile> originalFileList = FXCollections.observableArrayList();


    /** добовляем файл для конвертации
    **/
    @FXML
    private void addOriginalFiles(ActionEvent event){
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        fileChooser.setTitle("Open Document");//Заголовок диалога
       //Расширение
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG","*.png"),
                new FileChooser.ExtensionFilter("TXT","*.txt")
                );
        List<File> files = fileChooser.showOpenMultipleDialog(buttonAdd.getScene().getWindow());//Указываем текущую сцену CodeNote.mainStage
        //File file = chooser.showOpenDialog(new Stage());
        if (files != null) {
            for (File file : files) {
                originalFileList.add(new OriginalFile(file));
            }

        }
        listFiles.setItems(originalFileList);
    }

    @FXML
    private void initialize() {
        listFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listFiles.setCellFactory(param -> new ListCell<OriginalFile>(){
            @Override
            protected void updateItem(OriginalFile item, boolean empty){
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }
}
