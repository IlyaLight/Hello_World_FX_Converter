package com.fx.light.controller;


import com.fx.light.model.OriginalFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Controller {

    @FXML
    public TableView<OriginalFile> fileTable;
    @FXML
    public TableColumn<OriginalFile, String> nameColumn;
    @FXML
    public TableColumn<OriginalFile, String> pathColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button convertButton;

    private ObservableList<OriginalFile> originalFileList = FXCollections.observableArrayList();


    /** добовляем файл для конвертации
    **/
    @FXML
    private void addOriginalFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        fileChooser.setTitle("Open Document");//Заголовок диалога
       //Расширение
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG","*.png"),
                new FileChooser.ExtensionFilter("TXT","*.txt")
                );
        List<File> files = fileChooser.showOpenMultipleDialog(addButton.getScene().getWindow());//Указываем текущую сцену CodeNote.mainStage
        //File file = chooser.showOpenDialog(new Stage());
        if (files != null) {
            for (File file : files) {
                originalFileList.add(new OriginalFile(file));
            }

        }
        fileTable.setItems(originalFileList);
    }

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<OriginalFile, String>("name"));
        pathColumn.setCellValueFactory(new PropertyValueFactory<OriginalFile, String>("path"));

    }
}
