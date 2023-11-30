package com.example.acessodatostrimestre1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BuscarTablas implements Initializable{

    //Referencia al ComboBox del fxml
    @FXML
    private ComboBox<String> comboBoxTablas;

    //Array con los nombres de las tablas disponibles
    //Se usa observable porque es una interfaz de javaFX que extiende de List
    //Esta permite que si se hacen cambios en ella se actualizara automaticamente
    private ObservableList<String> nombresTablas = FXCollections.observableArrayList();

    //Referencia al Button del fxml
    @FXML
    private Button siguiente;

    //Nombre de la tabla seleccionada
    public String nombreTablaSeleccionada = null;

    //Referencia al Label del fxml
    @FXML
    private Label error;

    //Devuelve el nombre de la tabla seleccionada
    public String getNombreTablaSeleccionada() {
        return nombreTablaSeleccionada;
    }

    //Modifica el nombre de la tabla seleccionada
    public void setNombreTablaSeleccionada(String nombreTablaSeleccionada) {
        this.nombreTablaSeleccionada = nombreTablaSeleccionada;
    }

    //Inicializa la pantalla cargando el ComboBox con los nombres de las tablas
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarNombresTablas();
        comboBoxTablas.setItems(nombresTablas);
    }

    //Rellena el array de las tablas con los nombres de las tablas disponibles
    private void cargarNombresTablas() {
        try {

            //Coge los metadatos de la base de datos
            DatabaseMetaData metaData = BaseDatos.con.getMetaData();

            //Guarda en un ResulSet los nombres de las tablas
            ResultSet resultSet = metaData.getTables(null, "northwind", null, new String[]{"TABLE"});
            while (resultSet.next()) {

                //Guarda el nombre de la tabla en una variable
                String tableName = resultSet.getString(3);

                //Lo añade a la lista de tablas
                nombresTablas.add(tableName);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Pasa a la siguiente pantalla guardando el nnombre de la tabla seleccionada
    @FXML
    protected void siguiente(ActionEvent event) {

        //Guarda en una variable el nombre de la tabla seleccionada
        nombreTablaSeleccionada = comboBoxTablas.getValue();

        if (nombreTablaSeleccionada != null && !nombreTablaSeleccionada.isEmpty()){

            //Llama al metodo abrirNuevaVentana para ejecutar la tercera pantalla
            abrirNuevaVentana(event);

        } else {

            //Rellena el Label error si no se ha seleccionado ninguna tabla
            error.setText("Seleccione una tabla");
        }
    }

    //Crea la tercera pantalla
    private void abrirNuevaVentana(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MostrarTablas.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            MostrarTablas mostrarTablasController = fxmlLoader.getController();

            //Modifica la variable tabla seleccionada de la clase MostrarTabla para que sea igual
            //que la variable tabla seleccionada de la clase BuscarTabla
            mostrarTablasController.setNombreTablaSeleccionada(this.nombreTablaSeleccionada);

            Stage stage = new Stage();
            stage.setTitle("Mostrar Tabla");
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
