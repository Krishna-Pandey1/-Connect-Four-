package com.internshala.connectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class Main extends Application {

    private Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootGridPane = loader.load();

        controller = loader.getController();                   //loader se controller ko extract krke controller me store kia ja rha hai
        controller.createPlayground();                           //controller.java file me se createPlayground() method ko call kia ja rha hai

        MenuBar menuBar = createMenu();                     //yha createMenu ko call kia gya hai and jo bhi MenuBar me aaega use menuBar me store kia gya hai
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());                   //now width of menuBar will be same as primary stage
        Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
        menuPane.getChildren().add(menuBar);               //yha pr menuPane me menuBar ko add kia gya hai

        Scene scene = new Scene(rootGridPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect Four");
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    private MenuBar createMenu(){

        //File Menu
        Menu fileMenu = new Menu("File");
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(event -> controller.resetGame());                 //event handler on newGame menuItem using lambda

        MenuItem resetGame = new MenuItem("Resert Game");
        resetGame.setOnAction(event -> controller.resetGame());                      //event handler on resetGame menuitem using lambda

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

        MenuItem exitGame = new MenuItem("Exit Game");
        exitGame.setOnAction(event -> exitGame() );                         //event handler on exitGame

        fileMenu.getItems().addAll(newGame,resetGame,separatorMenuItem,exitGame);         //adding menuItems in File menu

        //Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutGame = new MenuItem("About Game");
        aboutGame.setOnAction(event -> aboutConnect4());                    //event handler of aboutGame

        SeparatorMenuItem separatorMenuItem1 = new SeparatorMenuItem();

        MenuItem aboutCreator = new MenuItem("About Creator");
        aboutCreator.setOnAction(event -> aboutMe());

        helpMenu.getItems().addAll(aboutGame,separatorMenuItem1,aboutCreator);        //adding menuItems in Help menu

        //Menu Bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,helpMenu);

        return menuBar;
    }

    private void aboutMe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("About Creator");
    alert.setHeaderText("KRISHNA MOHAN PANDEY");
    alert.setContentText("I Love to play and create games like this . Ofcourse I am beginner now but soon I will get good grab on this topic");
    alert.show();
    }

    private void aboutConnect4() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect Four");
        alert.setHeaderText("How to Play?");
        alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping " +
                "colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the" +
                "next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line"
                + "of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");

    alert.show();
    }

    private void exitGame() {
        Platform.exit();
        System.exit(0);
        }

    private void resetGame() {
        //TODO
    }


    public static void main(String[] args) {
        launch(args);
    }
}
