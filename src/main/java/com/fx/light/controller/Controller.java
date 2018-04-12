package com.fx.light.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;


public class Controller {

    @FXML
    private Button addButton;

    @FXML
    private Button convertButton;


    /** добовляем файл для конвертации
    **/
    @FXML
    private void addOriginalFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        fileChooser.setTitle("Open Document");//Заголовок диалога
       //Расширение
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*")
                );
        List<File> files = fileChooser.showOpenMultipleDialog(addButton.getScene().getWindow());//Указываем текущую сцену CodeNote.mainStage
        //File file = chooser.showOpenDialog(new Stage());
        if (files != null) {
            //Open
            for (File file : files) {
                System.out.println("Процесс открытия файла:  " + file.getName());
            }

        }
    }

}
