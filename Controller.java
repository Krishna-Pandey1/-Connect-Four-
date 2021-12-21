package com.internshala.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.*;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable{

    private  static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private static  final int CIRCLE_DIAMETER = 80;
    private static final String discColor1 = "#00FF00";
    private static final String discColor2 = "#0247fe";

    private static String PLAYER_ONE = "Player One";
    private static String PLAYER_TWO = "Player Two";
    Scanner scanner = new Scanner(System.in);


    private boolean isPlayerOneTurn = true;           //if turn of player one then it will show true and after turn it turn to false //when game will be start then it will be true means playerOne turn

    private Disc[][]  insertedDiscsArray = new Disc[ROWS][COLUMNS];    //for structural changes for the developer


    @FXML
    public GridPane rootGridPane;

    @FXML
    public Pane insertedDiscPane;

    @FXML
    public Label playerNameLabel;

    @FXML
    public TextField playerOneTextField , playerTwoTextField;

    @FXML
    public Button setNamesButton;



    private boolean isAllowedToInsert = true;               //Flag to avoid same color disc being added

    public void createPlayground(){                                   //is function ko Main.java se call kia jaega        //CREATING PLAY GROUND WHICH IS RECTANGULAR
Shape rectangleWithHOles = createStructuralGrid();            //yha pr createStructuralGrid method ko call kia gya hai
        rootGridPane.add(rectangleWithHOles,0,1);               //add rectangleWithHoles in rootGridPane at the position of 0,1

    List<Rectangle> rectangleList = createClikableColumns();                         //calling createClikableColumns();
    for(Rectangle rectangle: rectangleList ) {                                          //since rectangleList contain all the rectangle then use for loop for adding all the rectangel
        rootGridPane.add(rectangle,0,1);          //we are adding hovering rectangle at the 0,1 in gridpane
    }

    }
    private Shape createStructuralGrid(){
        Shape rectangleWithHOles =  new Rectangle((COLUMNS +1) * CIRCLE_DIAMETER , (ROWS +1)* CIRCLE_DIAMETER);           //creating rectangle
        for(int row =0 ; row < ROWS ; row++){
            for (int col = 0 ; col<COLUMNS;col++){
                Circle circle = new Circle();                     //creating circle
                circle.setRadius(CIRCLE_DIAMETER/2);              //setting radius
                circle.setCenterX(CIRCLE_DIAMETER/2);          //circle ke center ko x axis me aage bdahya gya
                circle.setCenterY(CIRCLE_DIAMETER/2);          //circle ke center ko y axis me aage bdahya  gya
                circle.setSmooth(true);                           //circle ko smooth kia ja rha hai

                circle.setTranslateX(col*(CIRCLE_DIAMETER+5) +CIRCLE_DIAMETER/4);                          //IT WILL TRANSLATE THE ANIMATION IN X AXIS                   //here we are setting margin also
                circle.setTranslateY(row*(CIRCLE_DIAMETER+5) +CIRCLE_DIAMETER/4);                          //IT WILL TRANSLATE THE ANIMATION IN Y AXIS               //IF THESE TWO STATEMENT WILL NOT BE WRITTEN THEN CIRCLES WILL BE REMOVED FROM ONLY ONE PLACE
                rectangleWithHOles = Shape.subtract(rectangleWithHOles, circle);                //it will substract the circle for once  hence for doing many time we will use for loop
            }
        }
        rectangleWithHOles.setFill(Color.BLACK);             //adding color in rectangle
        return rectangleWithHOles;
    }

    private List<Rectangle>  createClikableColumns(){
        List<Rectangle> rectangleList = new ArrayList<>();                 //list which contain Rectangle object
        for(int col=0;col<COLUMNS ; col++){                    //this loop will create 7 hovering rectangle
            //ye wo rectangle hai jo hover krne pr show hoga  //hovering effect color change ki wajah se nhi hota hai. hover effect ke liye hme rectangle use krne honge
            Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER,(ROWS +1) * CIRCLE_DIAMETER);                              //width of these rectangle will be same as diameter of circle and height will be same as height of rectangle on which circles are substracted
            rectangle.setFill(Color.TRANSPARENT);                                                                                       //is rectangle me Blue color fill kia ja rha hai
            rectangle.setTranslateX(col*(CIRCLE_DIAMETER+5) +CIRCLE_DIAMETER/4);

            rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee40")));                //jb mouse le jaenge to ek bhut light color aaega
            rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));                         //jb mouse htaenge to transparent ho jaega

            final  int column = col;              //jo bhi variable lambda expression me use hota hai wo final hona chahiye
            rectangle.setOnMouseClicked(event -> {
                if (isAllowedToInsert) {

                    isAllowedToInsert = false;                 //When disc is being dropped then no more disc will be inserted //it will stop the user to  enter a disc multiple time
//                insertDisc(new Disc(isPlayerOneTurn),column);                      //here new Disc will trigger the parametrial constructor of Disc class
                    insertDisc(new Disc(isPlayerOneTurn), column);
                }
            });                                                                                               //click event on click on every hovering rectangle
            rectangleList.add(rectangle);             //jitni bar rectangle bnega use rectangle list me store kr lia jaega
        }
        return  rectangleList;                //jis rectangle ko create kia hai use return krenge
    }
private  void insertDisc(Disc disc, int column){

        int row = ROWS -1; //value of index is always less than total no of the value because the index will be started from 0.
        while(row >=0){
            if(getDiscIfPresent(row,column)==null)     //checking emptyness of array for the given row and column
                break;
            row--;
        }
        if(row < 0)          //if row is not empty //means column is completly filled
            return;                //then do nothing
        insertedDiscsArray[row][column]= disc;           //For structural changes : for developers     //ye changes wo hai jo developer krega
        insertedDiscPane.getChildren().add(disc);       //ye wo change hai jo visually show hoga     //this will occur in second pane in which rectangles are present  //means second pane me disc insert kr rhe hai
        disc.setTranslateX(column*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
        int currentRow = row;               //lambda take only defined value
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.25),disc);
        translateTransition.setToY(row*(CIRCLE_DIAMETER+5) +CIRCLE_DIAMETER/4);     //it will show the animationn in y direction   //it will put the disc at the last position of a column
        translateTransition.setOnFinished(event ->{                                                //after fininshing the  animation once then this code will be run

        isAllowedToInsert = true;                       //finally when disc is dropped allow next player to insert disc
        if(gameEnded(currentRow,column)){
            gameOver();
            return ;
        }
        isPlayerOneTurn = !isPlayerOneTurn;
   playerNameLabel.setText (isPlayerOneTurn?PLAYER_ONE:PLAYER_TWO);
   } );

    translateTransition.play();                       //by this code the animation will start
    }

    private boolean gameEnded(int row,int column){

        //vertical points.
       List<javafx.geometry.Point2D> verticalPoints = IntStream.rangeClosed(row-3,row+3)
//                                                             .  .mapToObj(r->new javafx.geometry.Point2D(r,column))        //renge of row values = 0,1,2,3,4,5
                                                              .mapToObj(r->new javafx.geometry.Point2D(r,column))
                                                                .collect(Collectors.toList());                                 //this method will return list of 2D array element     //the value of column is constant

        //horizontal points
        List<javafx.geometry.Point2D> horizontalPoints = IntStream.rangeClosed(column-3,column+3)
                                                                  .mapToObj(col->new javafx.geometry.Point2D(row,col))
                                                                  .collect(Collectors.toList());                                 //this method will return list of 2D array element     //the value of row is constant


        javafx.geometry.Point2D startPoint1  = new javafx.geometry.Point2D(row-3,column+3);                  //checking all the possible combinanation of diagonal 1
        List<javafx.geometry.Point2D> diagonal1Points = IntStream.rangeClosed(0,6)
                                                                 .mapToObj(i -> startPoint1.add(i,-i))
                                                                  .collect(Collectors.toList());

        javafx.geometry.Point2D startPoint2  = new javafx.geometry.Point2D(row-3,column-3);                  //checking all the possible combinanation of diagonal 2
        List<javafx.geometry.Point2D> diagonal2Points = IntStream.rangeClosed(0,6)
                .mapToObj(i -> startPoint2.add(i,i))
                .collect(Collectors.toList());

        boolean isEnded = checkCombinatins(verticalPoints) ||checkCombinatins(horizontalPoints) || checkCombinatins(diagonal1Points)||checkCombinatins(diagonal2Points);                    //THIS LINE WILL CHECK THE HORIZONTAL COMBINATION OR VERTICAL COMBINATION ,diagonal 1 , diagonal 2
             return isEnded;
    }
    private boolean checkCombinatins(List<javafx.geometry.Point2D> points) {
        int chain =0;
        for (javafx.geometry.Point2D point:points) {
            int rowIndexForArray = (int) point.getX();
            int columnIndexForArray = (int) point.getY();
            Disc disc = getDiscIfPresent(rowIndexForArray,columnIndexForArray);
            if (disc != null && disc.isPlayerOneMOve == isPlayerOneTurn) {
                chain++;
                if (chain == 4) {
                    return true;
                }  }
                else{
                    chain = 0;
                }
               }
return false;
        }
        private Disc getDiscIfPresent(int row , int column){                   //to prevent ArrayIndexOfBoundException
        if(row>=ROWS || row <0 || column>=COLUMNS || column <0)                      //if row or column index is invalid
            return null;
        return insertedDiscsArray[row][column];
    }
    private void gameOver(){
    String winner = isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO;
        System.out.println("Winner is : " + winner);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect Four");
        alert.setHeaderText("The Winner is " + winner);
        alert.setContentText("Want to play again? ");
        ButtonType yesBtn = new ButtonType("Yes");
        ButtonType noBtn = new ButtonType("No,Exit");
        alert.getButtonTypes().setAll(yesBtn,noBtn);
        Platform.runLater(()-> {                        //it will ensure these all the code will execute after the animation
            Optional<ButtonType> btnClicked = alert.showAndWait();
            if (btnClicked.isPresent() &&btnClicked.get()==yesBtn){
                //user chose YES so RESET the game
                resetGame();
            }else{
                Platform.exit();
                System.exit(0);
                }
            });
    }
    public void resetGame() {
        insertedDiscPane.getChildren().clear();                //remove all the disc from the pane means clear the pane //means structuraly all the block store null

    for(int row =0;row<insertedDiscsArray.length;row++){
        for(int col = 0; col < insertedDiscsArray[row].length;col++){
            insertedDiscsArray[row][col]= null;
        }
    }
    isPlayerOneTurn = true;                     //let player start the game
        playerNameLabel.setText(PLAYER_ONE);
        createPlayground();               //it will create playground again
    }

    private static class Disc extends Circle{                 //it is Disc class
        private final boolean isPlayerOneMOve;                 //instance variable

        public Disc (boolean isPlayerOneMOve){                  //parametrial constructor
            this.isPlayerOneMOve = isPlayerOneMOve;
            setRadius(CIRCLE_DIAMETER/2);
       setFill(isPlayerOneMOve?Color.valueOf(discColor1): Color.valueOf(discColor2));               //jab player one ki turn hogi to disc color ki value disColor1 hogi and if player two ki move hogi to disc color ki value discColor2 hogi
//      setFill(isPlayerOneMOve?Color.valueOf(discColor1):Color.valueOf(discColor2));
       setCenterX(CIRCLE_DIAMETER/2);
       setCenterY(CIRCLE_DIAMETER/2);
        }
        }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
