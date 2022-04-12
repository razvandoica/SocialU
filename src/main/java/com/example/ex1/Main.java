package com.example.ex1;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import socialnetwork.repository.database.*;
import socialnetwork.service.Service;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 *  1. Parole (functia HashCode, salvam hashcode in bd) ✔
 *  2. Service-uri mai multe ✔✔✔✔✔✔✔✔✔
 *  3. Main window design
 *  4. DATA la cereri prietenie ✔
 *  5. search bar-uri pt prieteni/cereri/evenimente (substring matching)
 */


public class Main extends Application {
    CereriPrietenieDataBase crdb = new CereriPrietenieDataBase("jdbc:postgresql://localhost:5432/retea","postgres", "postgres");
    UtilizatorDataBase userdb = new UtilizatorDataBase("jdbc:postgresql://localhost:5432/retea","postgres", "postgres");
    PrietenieDataBase prdb = new PrietenieDataBase("jdbc:postgresql://localhost:5432/retea","postgres", "postgres");
    MessageDataBase msgdb = new MessageDataBase("jdbc:postgresql://localhost:5432/retea","postgres", "postgres");
    GroupDataBase grdb = new GroupDataBase("jdbc:postgresql://localhost:5432/retea","postgres", "postgres");
    EvenimenteDataBase edb = new EvenimenteDataBase("jdbc:postgresql://localhost:5432/retea","postgres", "postgres");

    Service service = new Service(userdb, prdb, crdb, msgdb, grdb, edb);

    private static Stage currentStage;

    double mousePressedX;

    double mousePressedY;

    @Override
    public void start(Stage stage) throws IOException {

        currentStage = stage;

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("login.fxml"));
        AnchorPane root=loader.load();
        LogInController controller=loader.getController();
        controller.setLogInService(service);

        controller.setLogInService(service);
        Pane header = controller.headerPane;

        header.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mousePressedX = mouseEvent.getX();
                mousePressedY = mouseEvent.getY();
            }
        });
        header.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double crrX = mouseEvent.getScreenX();
                double crrY = mouseEvent.getScreenY();
                Stage stage = (Stage) currentStage.getScene().getWindow();
                stage.setX(crrX - mousePressedX);
                stage.setY(crrY - mousePressedY);
            }
        });

        Button minLog = controller.minimize;
        Button exitLog = controller.exit;

        exitLog.setOnMouseClicked((ActionEvent) -> {
            Stage stage1 = (Stage) currentStage.getScene().getWindow();
            stage1.close();
        });

        minLog.setOnMouseClicked((ActionEvent) -> {
            Stage stage2 = (Stage) currentStage.getScene().getWindow();
            stage2.setIconified(true);
        });

        currentStage.setScene(new Scene(root, 559, 360));
        currentStage.setTitle("Log in");
        currentStage.initStyle(StageStyle.UNDECORATED);

        currentStage.show();
    }

    public void switchToMain(String fxml,String email) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
        AnchorPane root=loader.load();
        MainController controller=loader.getController();
        controller.setUserService(service,email);

        Pane header = controller.headerPane;

        header.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mousePressedX = mouseEvent.getX();
                mousePressedY = mouseEvent.getY();
            }
        });
        header.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double crrX = mouseEvent.getScreenX();
                double crrY = mouseEvent.getScreenY();
                Stage stage = (Stage) currentStage.getScene().getWindow();
                stage.setX(crrX - mousePressedX);
                stage.setY(crrY - mousePressedY);
            }
        });

        Button minLog = controller.minimize;
        Button exitLog = controller.exit;

        exitLog.setOnMouseClicked((ActionEvent) -> {
            Stage stage = (Stage) currentStage.getScene().getWindow();
            stage.close();
        });

        minLog.setOnMouseClicked((ActionEvent) -> {
            Stage stage = (Stage) currentStage.getScene().getWindow();
            stage.setIconified(true);
        });

        currentStage.setScene(new Scene(root, 905, 633));
        currentStage.setTitle("Main page");
        currentStage.show();
    }

    public void switchToLogIn(String fxml) throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
        AnchorPane root=loader.load();
        LogInController controller=loader.getController();
        controller.setLogInService(service);
        Pane header = controller.headerPane;

        header.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mousePressedX = mouseEvent.getX();
                mousePressedY = mouseEvent.getY();
            }
        });
        header.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double crrX = mouseEvent.getScreenX();
                double crrY = mouseEvent.getScreenY();
                Stage stage = (Stage) currentStage.getScene().getWindow();
                stage.setX(crrX - mousePressedX);
                stage.setY(crrY - mousePressedY);
            }
        });

        Button minLog = controller.minimize;
        Button exitLog = controller.exit;

        exitLog.setOnMouseClicked((ActionEvent) -> {
            Stage stage = (Stage) currentStage.getScene().getWindow();
            stage.close();
        });

        minLog.setOnMouseClicked((ActionEvent) -> {
            Stage stage = (Stage) currentStage.getScene().getWindow();
            stage.setIconified(true);
        });

        currentStage.setScene(new Scene(root, 559, 360));
        currentStage.setTitle("Log in");
        currentStage.show();
    }

    public void switchToSignUp(String fxml) throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
        AnchorPane root=loader.load();
        SignUpController controller=loader.getController();
        controller.setSignUpService(service);

        Pane header = controller.headerPane;

        header.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mousePressedX = mouseEvent.getX();
                mousePressedY = mouseEvent.getY();
            }
        });
        header.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double crrX = mouseEvent.getScreenX();
                double crrY = mouseEvent.getScreenY();
                Stage stage = (Stage) currentStage.getScene().getWindow();
                stage.setX(crrX - mousePressedX);
                stage.setY(crrY - mousePressedY);
            }
        });

        Button minLog = controller.minimize;
        Button exitLog = controller.exit;

        exitLog.setOnMouseClicked((ActionEvent) -> {
            Stage stage = (Stage) currentStage.getScene().getWindow();
            stage.close();
        });

        minLog.setOnMouseClicked((ActionEvent) -> {
            Stage stage = (Stage) currentStage.getScene().getWindow();
            stage.setIconified(true);
        });

        currentStage.setScene(new Scene(root, 474, 544));
        currentStage.setTitle("Sign up");
        currentStage.show();
    }

    public static void main(String[] args) {
        launch();

    }

}