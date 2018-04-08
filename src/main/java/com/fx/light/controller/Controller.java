package com.fx.light.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;


public class Controller {

    @FXML
    private Button addButton;

    @FXML
    private Button convertButton;


    /** добовляем фойл для конвертации
    **/
    @FXML
    private void addOriginalFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        fileChooser.setTitle("Open Document");//Заголовок диалога
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");//Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(addButton.getScene().getWindow());//Указываем текущую сцену CodeNote.mainStage
        //File file = chooser.showOpenDialog(new Stage());
        if (file != null) {
            //Open
            System.out.println("Процесс открытия файла");
        }
    }

}