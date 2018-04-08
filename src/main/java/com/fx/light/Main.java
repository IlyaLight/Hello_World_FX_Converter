package com.fx.light;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // чтобы создать JavaFX приложения, достаточно реализовать метод start(Stage)
    @Override
    public void start(Stage primaryStage) throws Exception{

        System.out.println(System.getProperty("user.dir"));
        Parent root = FXMLLoader.load(getClass().getResource("/view/Converter.fxml"));

        primaryStage.setTitle("Hello World");   // задаем заголовок окна
        // создаем сцену с заданными шириной и высотой и содержащую наш корневым контейнером, и связываем ее с окном
        primaryStage.setScene(new Scene(root));
        primaryStage.show();    // запускаем окно
    }

    // метод main в JavaFX приложениях не является обязательным
    public static void main(String[] args) {
        launch(args);
    }

}
