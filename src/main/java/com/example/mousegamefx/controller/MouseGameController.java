package com.example.mousegamefx.controller;

import com.example.mousegamefx.Main;
import com.example.mousegamefx.endGameException;
import com.example.mousegamefx.model.MouseCheeseGame;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

public class MouseGameController {

    @FXML
    Pane pane;

    @FXML
    private Label points;

    @FXML
    ImageView img00;
    @FXML
    ImageView img01;
    @FXML
    ImageView img02;
    @FXML
    ImageView img03;
    @FXML
    ImageView img10;
    @FXML
    ImageView img11;
    @FXML
    ImageView img12;
    @FXML
    ImageView img13;
    @FXML
    ImageView img20;
    @FXML
    ImageView img21;
    @FXML
    ImageView img22;
    @FXML
    ImageView img23;
    @FXML
    ImageView img30;
    @FXML
    ImageView img31;
    @FXML
    ImageView img32;
    @FXML
    ImageView img33;
    @FXML
    ImageView btn_right;
    @FXML
    ImageView btn_left;
    @FXML
    ImageView btn_up;
    @FXML
    ImageView btn_down;

    Image imageMouse = new Image(new File("src/main/resources/images/mouse.png").toURI().toString());
    Image imageCheese = new Image(new File("src/main/resources/images/cheese.png").toURI().toString());
    Image imageFoot = new Image(new File("src/main/resources/images/footPrints.png").toURI().toString());
    Image imageLucky = new Image(new File("src/main/resources/images/luckyMouse.png").toURI().toString());
    Image imageThief = new Image(new File("src/main/resources/images/thief.png").toURI().toString());
    Image imageCat = new Image(new File("src/main/resources/images/cat.png").toURI().toString());
    Image imageNoWay = new Image(new File("src/main/resources/images/noWay.png").toURI().toString());
    Image imageCheeseMouse = new Image(new File("src/main/resources/images/cheeseMouse.png").toURI().toString());




    int currentRowMousePosition = 0;
    int currentColMousePosition = 0;
    String currentCellFigure ="";
    int newRowMousePosition = 0;
    int newColMousePosition = 0;
    MouseCheeseGame game;
    ImageView[][] arrImg;

    Stack<String> log = new Stack();
    int pointsLog = 0;
    int currentPoints = 0;



    @FXML
    public void initialize () {
        game = new MouseCheeseGame(4,4,true);
        game.start();

        arrImg = new ImageView[4][4];
        arrImg[0][0]=img00;
        arrImg[0][1]=img01;
        arrImg[0][2]=img02;
        arrImg[0][3]=img03;
        arrImg[1][0]=img10;
        arrImg[1][1]=img11;
        arrImg[1][2]=img12;
        arrImg[1][3]=img13;
        arrImg[2][0]=img20;
        arrImg[2][1]=img21;
        arrImg[2][2]=img22;
        arrImg[2][3]=img23;
        arrImg[3][0]=img30;
        arrImg[3][1]=img31;
        arrImg[3][2]=img32;
        arrImg[3][3]=img33;
        arrImg[0][0].setImage(imageMouse);
        arrImg[3][3].setImage(imageCheese);

        log.push("00 start "+game.getTotalEarnedPoints());
        pointsLog = game.getTotalEarnedPoints();
    }

    @FXML
    protected void moveRight() {
        this.move("r");
    }

    @FXML
    protected void moveLeft() {
        this.move("l");
    }

    @FXML
    protected void moveUp() {
        this.move("u");
    }

    @FXML
    protected void moveDown() {
        this.move("d");
    }

    public String move(String movement) {
        return performMovement(game.startMouseMovement(movement));
    }

    private String performMovement(String question) {

        if(question == null) {
            newRowMousePosition = game.getRowMousePosition();
            newColMousePosition = game.getColMousePosition();
            if(newRowMousePosition != currentRowMousePosition || newColMousePosition != currentColMousePosition) {
                moveMouseToNewCell();
                logRegister();
                try {
                    winCondition();
                }catch (endGameException e){
                    System.out.println(e.getMessage());
                }
                points.setText(String.format("Puntos acumulados: %3d\n",game.getTotalEarnedPoints()));
            }
            return null;
        }

        TextInputDialog textInput = new TextInputDialog();
        textInput.setTitle("Answer the question correctly!");
        textInput.getDialogPane().setContentText(question);
        Optional<String> result = textInput.showAndWait();
        TextField input = textInput.getEditor();
        game.completeMouseMovement(input.getText());

        return performMovement(null);

    }

    private void logRegister(){
        currentPoints = game.getTotalEarnedPoints()-pointsLog;
        log.push(currentColMousePosition+""+currentRowMousePosition+" "+cellTraslator());
        pointsLog = game.getTotalEarnedPoints();
    }

    private String cellTraslator(){
        String cellTraslated = "";
        switch (currentCellFigure){
            case "++":
                cellTraslated = "Lucky " + currentPoints;
                break;
            case "--":
                cellTraslated = "Thief " + currentPoints;
                break;
            case "CC":
                cellTraslated = "Cat " + game.getTotalEarnedPoints();
                break;
            case "CH":
                cellTraslated = "Cheese " + game.getTotalEarnedPoints();
                break;
            default:
                cellTraslated = "empty " + currentPoints;
                break;

        }
        return cellTraslated;
    }

    private void winCondition() throws endGameException {
        if (game.hasLost()){
            changeScene("You loose!", imageCat);
            throw new endGameException("You loose");
        }
        if (game.hasWon()){
            String winingText = "You win \nTotal points = "+game.getTotalEarnedPoints();
            changeScene(winingText, imageCheeseMouse);
            throw new endGameException("You Win!");

        }
    }

    @FXML
    private void changeScene(String text, Image image) {
        writeLog();

        Stage stage = (Stage) points.getScene().getWindow();
        VBox panel = new VBox(20);
        Label endText = new Label(text);
        ImageView endImage = new ImageView(image);
        panel.getChildren().addAll(endText, endImage);
        Scene scene = new Scene(panel, 400, 450);

        endText.setStyle("-fx-font-size: 15pt; -fx-font-weight: bold");
        panel.setAlignment(Pos.CENTER);

        stage.setScene(scene);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                },
                2500
        );
    }


    private void moveMouseToNewCell() {
        arrImg[newColMousePosition][newRowMousePosition].setImage(imageMouse);
        switch (currentCellFigure){
            case "++":
                arrImg[currentColMousePosition][currentRowMousePosition].setImage(imageLucky);
                break;
            case "--":
                arrImg[currentColMousePosition][currentRowMousePosition].setImage(imageThief);
                break;
            default:
                arrImg[currentColMousePosition][currentRowMousePosition].setImage(imageFoot);
                break;

        }
        currentCellFigure = game.getCurrentFigure();
        currentRowMousePosition = newRowMousePosition;
        currentColMousePosition = newColMousePosition;
    }

    private void writeLog() {
        Path path = Paths.get("src/main/resources/files/backtrack_marioZappulla.txt");
        BufferedWriter bufferWriter = null;
        String line;
        try {
            if (new File(String.valueOf(path)).exists())
                bufferWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
            else
                bufferWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);

            for (int i=log.size()-1; i >= 0;i--){
                line = log.get(i);
                bufferWriter.write(i+" "+line);
                bufferWriter.newLine();
            }
            bufferWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}