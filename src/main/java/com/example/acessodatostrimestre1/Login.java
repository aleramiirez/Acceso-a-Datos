package com.example.acessodatostrimestre1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Login {

    //Referencia al TextField del fxml
    @FXML
    private TextField login;

    //Referencia al PasswordField del fxml
    @FXML
    private PasswordField password;

    //Referencia al Label del fxml
    @FXML
    private Label error;


    //Inicia sesion al pulsar enter si el usuario y la contraseña son correctos
    @FXML
    protected void enter(ActionEvent actionEvent) {
        String usuario = login.getText();
        String contraseña = password.getText();

        try {
            //Abre la conexion a la base de datos
            BaseDatos.con = BaseDatos.abrirConexion(BaseDatos.url, usuario, contraseña);

            //Llama al metodo abrirNuevaVentana para ejecutar la segunda pantalla
            abrirNuevaVentana(actionEvent);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {

            //Rellena el Label error si el usuario o la contraseña no son correctos
            error.setText("\"Usuario o contraseña incorrectos\"");

        }
    }

    //Crea la segunda pantalla
    private void abrirNuevaVentana(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("BuscarTablas.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 350);
            Stage stage = new Stage();

            //No permite cambiar el tamaño de la pantalla
            stage.setResizable(false);

            stage.setTitle("Elegir Tabla");
            stage.setScene(scene);
            stage.show();

            //Cuando cierras la ventana se cierra la conexion a la base de datos
            stage.setOnCloseRequest(event1 -> {
                try {
                    BaseDatos.CerrarConexion();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            Node source = (Node) event.getSource();
            stage = (Stage) source.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}