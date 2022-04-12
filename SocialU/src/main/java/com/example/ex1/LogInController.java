package com.example.ex1;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import socialnetwork.domain.Page;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.database.*;
import socialnetwork.service.Service;

import java.io.File;
import java.io.IOException;

public class LogInController {
    Service srv;

    @FXML
    private TextField logInEmail;

    @FXML
    private PasswordField logInPw;

    @FXML
    private Label eroare;

    @FXML
    private ImageView logImage;

    @FXML
    private ImageView signImage;

    @FXML
    public Pane headerPane;

    @FXML
    public Button minimize;

    @FXML
    public Button exit;

    @FXML
    private ImageView logo;

    @FXML
    public ImageView logoUp;


    public void setLogInService(Service service) {



        File filelogFile = new File("resurse/login4.png");
        Image imgLog = new Image(filelogFile.toURI().toString());
        logImage.setImage(imgLog);

        File filesignFile = new File("resurse/signup.png");
        Image imgSign = new Image(filesignFile.toURI().toString());
        signImage.setImage(imgSign);

        File fileLogoFile = new File("resurse/SocialU.png");
        Image imgLogo = new Image(fileLogoFile.toURI().toString());
        logo.setImage(imgLogo);


        File fileLogoUpFile = new File("resurse/SocialULogo.png");
        Image imgLogoUp = new Image(fileLogoUpFile.toURI().toString());
        logoUp.setImage(imgLogoUp);

        this.srv=service;
    }

    public void logIn() throws IOException{
        String email = logInEmail.getText();
        String pw = logInPw.getText();

        if (email.isEmpty() || pw.isEmpty()){
            eroare.setText("Date invalide");
            eroare.setVisible(true);
        }
        else if (srv.searchUtilizator(email) == null) {
            eroare.setText("Cont inexistent");
            eroare.setVisible(true);
        }
        else if (!srv.searchUtilizator(email).getPassword().equals(String.valueOf(logInPw.getText().hashCode()))){
            eroare.setText("Parola incorecta");
            eroare.setVisible(true);
        }
        else{
            Main main = new Main();
            main.switchToMain("main.fxml", email);
        }
    }

    public void signUp() throws IOException{
        Main main = new Main();
        main.switchToSignUp("sign_up.fxml");
    }
}