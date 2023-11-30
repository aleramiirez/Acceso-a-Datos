package com.example.acessodatostrimestre1;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class MostrarTablas {

    //Nombre de la tabla seleccionada en la pantalla anterior
    private String nombreTablaSeleccionada;

    @FXML
    private TableView<ObservableList<String>> tableView;

    //Modifica el nombre de la tabla seleccionada
    public void setNombreTablaSeleccionada(String nombreTablaSeleccionada) {
        this.nombreTablaSeleccionada = nombreTablaSeleccionada;
        cargarContenidoTabla();
    }

    //Vuelve a la pantalla anterior
    @FXML
    private void volverAtras(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("BuscarTablas.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 320, 350);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    }

    //Exporta el la tabla en un archivo CSV
    @FXML
    private void exportarCSV() {

        //Crea un objeto FileChooser
        //Clase en JavaFX que proporciona una interfaz gráfica para que el usuario elija o guarde
        //archivos en el sistema de archivos
        FileChooser fileChooser = new FileChooser();

        //Establece el nombre predeterminado como nombreTablaSeleccionada.csv
        fileChooser.setInitialFileName(nombreTablaSeleccionada + ".csv");

        //Permite al usuario seleccionar o ingresar el nombre y la ubicación
        //de un archivo para guardar
        File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());

        if (file != null) {

            //Mete los objetos en el try para no tener que cerrarlos
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

                //Recorre todas las columnas de la tabla
                for (int i = 0; i < tableView.getColumns().size(); i++) {

                    //Obtiene una columna de la tabla
                    //Trabaja con tipos de datos Observale<String> y el "?" significa
                    //que no esta especificado el tipo de dato exacto
                    TableColumn<ObservableList<String>, ?> column = tableView.getColumns().get(i);

                    //Escribe los datos en el writer
                    writer.write(column.getText());

                    //Itera hasta la penultima columna
                    if (i < tableView.getColumns().size() - 1) {

                        //Separa las columnas con un "|"
                        writer.write(" | ");
                    }
                }

                //Escribe otra linea
                writer.newLine();

                //Obtiene las filas de la tabla
                for (ObservableList<String> row : tableView.getItems()) {

                    //Guarda en una variable el tamaño de la fila
                    int size = row.size();

                    //Itera atraves de las celdas de la fila
                    for (int i = 0; i < size; i++) {

                        //Obtiene el valor de la celda en la posicion i
                        String value = row.get(i);

                        //Verifica que el valor no es nulo
                        if (value != null) {

                            //Escribe los datos en el writer
                            writer.write(value);

                            //Itera hasta la penultima columna
                            if (i != size - 1) {

                                //Separa las columnas con un "|"
                                writer.write(" | ");
                            }
                        } else {

                            //Si no i no esta en la ultima columna
                            if (i != size - 1) {

                                //Escribe que es nulo y pone el separador de columnas
                                writer.write("null | ");
                            } else {

                                //Escribe nulo y no pone separador ya que es la ultima columna
                                writer.write("null");
                            }
                        }
                    }

                    //Escribe una nueva linea
                    writer.newLine();
                }

                //Imprime un mensaje de que ha funcionado la exportacion
                System.out.println("Exportación a CSV completada.");
            } catch (IOException e) {
                //Imprime un mensaje de error si no se ha podido exportar correctamente
                System.out.println("Error al exportar a CSV.");
            }
        }
    }

    //Exporta la tabla en un archivo XML
    @FXML
    private void exportarXML() {
        try {

            //Verifica si el nombre de la tabla seleccionada es "categories"
            if (nombreTablaSeleccionada.equals("categories")) {

                //Crea una coleccion de categorias llamada registros y llama al metodo
                // obtenerRegistrosDeCategorias para rellenarla
                Collection<Categorie> registros = obtenerRegistrosDeCategories();

                //Crea un objeto de categorias
                Categories categories = new Categories();

                //Modifica la coleccion de categorias de la clase Categories con los valores de
                //la colecion de registros
                categories.setCategories(registros);

                //Crea un objeto FileChooser
                FileChooser fileChooser = new FileChooser();

                //Establece el nombre predeterminado como "categories.xml"
                fileChooser.setInitialFileName("categories.xml");

                //Permite al usuario seleccionar o ingresar el nombre y la ubicación
                //de un archivo para guardar
                File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());

                if (file != null) {

                    //Crea un contexto JAXB
                    JAXBContext context = JAXBContext.newInstance(Categories.class);

                    //Convierte el objeto java en su representacion XML
                    Marshaller marshaller = context.createMarshaller();

                    //Formatea la salida XML de manera legible
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                    //Serializa categories
                    marshaller.marshal(categories, file);

                    //Imprime un mensaje de que ha funcionado la exportacion
                    System.out.println("Exportación a XML completada.");
                }

            }

            //Verifica si el nombre de la tabla seleccionada es "region"
            if (nombreTablaSeleccionada.equals("region")){

                //Crea una coleccion de regiones llamada registros y llama al metodo
                // obtenerRegistroDeRegiones para rellenarla
                Collection<Region> registros = obtenerRegistrosDeRegions();

                //Crea un objeto de regiones
                Regions regions = new Regions();

                //Modifica la coleccion de regiones de la clase Regions con los valores de
                // la colecion de registros
                regions.setRegions(registros);

                //Crea un objeto FileChooser
                FileChooser fileChooser = new FileChooser();

                //Establece el nombre predeterminado como "region.xml"
                fileChooser.setInitialFileName("region.xml");

                //Permite al usuario seleccionar o ingresar el nombre y la ubicación
                //de un archivo para guardar
                File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());

                if (file != null) {

                    //Crea un contexto JAXB
                    JAXBContext context = JAXBContext.newInstance(Regions.class);

                    //Convierte el objeto java en su representacion XML
                    Marshaller marshaller = context.createMarshaller();

                    //Formatea la salida XML de manera legible
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                    //Serializa regions
                    marshaller.marshal(regions, file);

                    //Imprime un mensaje de que ha funcionado la exportacion
                    System.out.println("Exportación a XML completada.");
                }

            }

            //Verifica si el nombre de la tabla seleccionada es "customers"
            if (nombreTablaSeleccionada.equals("customers")) {

                //Crea una coleccion de clientes llamada registros y llama al metodo
                // obtenerRegistroDeClientes para rellenarla
                Collection<Customer> registros = obtenerRegistrosDeCustomers();

                //Crea un objeto de clientes
                Customers customers = new Customers();

                //Modifica la coleccion de clientes de la clase Customers con los valores de
                // la colecion de registros
                customers.setCustomers(registros);

                //Crea un objeto FileChooser
                FileChooser fileChooser = new FileChooser();

                //Establece el nombre predeterminado como "custormes.xml"
                fileChooser.setInitialFileName("custormes.xml");

                //Permite al usuario seleccionar o ingresar el nombre y la ubicación
                //de un archivo para guardar
                File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());

                if (file != null) {

                    //Crea un contexto JAXB
                    JAXBContext context = JAXBContext.newInstance(Customers.class);

                    //Convierte el objeto java en su representacion XML
                    Marshaller marshaller = context.createMarshaller();

                    //Formatea la salida XML de manera legible
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                    //Serializa customers
                    marshaller.marshal(customers, file);

                    //Imprime un mensaje de que ha funcionado la exportacion
                    System.out.println("Exportación a XML completada.");
                }

            }
        } catch (JAXBException | SQLException e) {
            System.out.println("Error al exportar a XML.");
        }
    }

    //Obtiene los registros de la tabla de clientes
    private Collection<Customer> obtenerRegistrosDeCustomers() throws SQLException {

        //Crea una coleccion de clientes
        Collection<Customer> registros = new ArrayList<>();

        //Crea la QUERY que te seleccione todo de la tabla indicada
        String query = "SELECT * FROM " + nombreTablaSeleccionada + ";";

        //Mete los objetos en el try para no tener que cerrarlos
        try (PreparedStatement statement = BaseDatos.con.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            //Itera mientras haya un siguiente resultado en los valores obtenidos por la QUERY
            while (resultSet.next()) {

                //Guarda los valores en una variable
                String customerID = resultSet.getString("CustomerID");
                String companyName = resultSet.getString("CompanyName");
                String contactName = resultSet.getString("ContactName");
                String contactTitle = resultSet.getString("ContactTitle");
                String address = resultSet.getString("Address");
                String city = resultSet.getString("City");
                String region = resultSet.getString("Region");
                String postalCode = resultSet.getString("PostalCode");
                String country = resultSet.getString("Country");
                String phone = resultSet.getString("Phone");
                String fax = resultSet.getString("Fax");

                //Crea un objeto cliente con los valores obtenidos en dicha iteracion
                Customer customer = new Customer(customerID, companyName, contactName, contactTitle, address, city, region, postalCode, country, phone, fax);

                //Añade el objeto cliente a la coleccion llamada registros
                registros.add(customer);
            }
        }

        //Retorna la coleccion registros
        return registros;
    }

    //Obtiene los registros de la tabla de regiones
    private Collection<Region> obtenerRegistrosDeRegions() throws SQLException {

        //Crea una coleccion de regiones
        Collection<Region> registros = new ArrayList<>();

        //Crea la QUERY que te seleccione todo de la tabla indicada
        String query = "SELECT * FROM " + nombreTablaSeleccionada + ";";

        //Mete los objetos en el try para no tener que cerrarlos
        try (PreparedStatement statement = BaseDatos.con.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            //Itera mientras haya un siguiente resultado en los valores obtenidos por la QUERY
            while (resultSet.next()) {

                //Guarda los valores en una variable
                String regionID = resultSet.getString("RegionID");
                String regionDescription = resultSet.getString("RegionDescription");

                //Crea un objeto region con los valores obtenidos en dicha iteracion
                Region region = new Region(regionID, regionDescription);

                //Añade el objeto region a la coleccion llamada registros
                registros.add(region);
            }
        }

        //Retorna la coleccion registros
        return registros;
    }

    //Obtiene los registros de la tabla de categorias
    private Collection<Categorie> obtenerRegistrosDeCategories() throws SQLException {

        //Crea una coleccion de categorias
        Collection<Categorie> registros = new ArrayList<>();

        //Crea la QUERY que te seleccione todo de la tabla indicada
        String query = "SELECT * FROM " + nombreTablaSeleccionada + ";";

        //Mete los objetos en el try para no tener que cerrarlos
        try (PreparedStatement statement = BaseDatos.con.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            //Itera mientras haya un siguiente resultado en los valores obtenidos por la QUERY
            while (resultSet.next()) {

                //Guarda los valores en una variable
                long categoryID = resultSet.getLong("CategoryID");
                String categoryName = resultSet.getString("CategoryName");
                String description = resultSet.getString("Description");
                String picture = resultSet.getString("Picture");

                //Crea un objeto categoria con los valores obtenidos en dicha iteracion
                Categorie categorie = new Categorie(categoryID, categoryName, description, picture);

                //Añade el objeto categoria a la coleccion llamada registros
                registros.add(categorie);
            }
        }

        //Retorna la coleccion registros
        return registros;
    }

    //Carga el contenido de la tabla seleccionada en la pantalla anterior
    public void cargarContenidoTabla() {
        try {

            //Coge la variable con que guarda la conexion que se abrio en el login
            Connection con = BaseDatos.con;

            //Construye la QUERY
            String sql = "SELECT * FROM " + nombreTablaSeleccionada + ";";

            //Preparar una sentencia SQL parametrizada usando PreparedStatement
            PreparedStatement statement = con.prepareStatement(sql);

            //Guada los resulatados de la consulta
            ResultSet resultSet = statement.executeQuery();

            //Obtiene los meta datos
            ResultSetMetaData metaData = resultSet.getMetaData();

            //Itera sobre todas las columnas de la QUERY
            for (int i = 1; i <= metaData.getColumnCount(); i++) {

                //Obtiene el titulo de la columna
                String titulo = metaData.getColumnLabel(i);

                //Crea una nueva columna para la tabla con el titulo obtenido
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(titulo);

                //Crea una variable final para utilizarla dentro de expresiones lambda
                final int iFinal = i;

                //Establece la factoria de celdas para la columna
                //Esto define como obtener los valores que se mostraran en las celdas
                column.setCellValueFactory(cellData -> {

                    //Devuelve la fila a la que pertenece la celda
                    ObservableList<String> row = cellData.getValue();

                    //Resta 1 al indice
                    int adjustedi = iFinal - 1;

                    //Verifica si el indice ajustado esta dentro del rango valido de la fila
                    if (adjustedi >= 0 && adjustedi < row.size()) {

                        //Se devuelve un ReadOnlyObjectWrapper con el valor de la celda
                        //ReadOnlyObjectWrapper es un contenedor proporcionado por JavaFX que envuelve
                        // un objeto y proporciona propiedades de solo lectura para él.
                        return new ReadOnlyObjectWrapper<>(row.get(adjustedi));
                    } else {

                        //Se devuelve un ReadOnlyObjectWrapper con una cadena vacia
                        return new ReadOnlyObjectWrapper<>("");
                    }
                });

                //Añade una columna a la tabla
                tableView.getColumns().add(column);
            }

            //Se crea una lista que representa una fila en la tabla
            ObservableList<ObservableList<String>> datosTabla = FXCollections.observableArrayList();

            //Itera mientras haya un resultado siguiente
            while (resultSet.next()) {

                //Crea una lista para almacenar los valores de una fila
                ObservableList<String> fila = FXCollections.observableArrayList();

                //Itera sobre las columnas del ResulSet
                for (int i = 1; i <= metaData.getColumnCount(); i++) {

                    //Obtiene el valor de la columna actual
                    String valor = resultSet.getString(i);

                    // Verificar si el valor no es nulo ni un formato que se suele utilizar
                    //para las fechas vacias en SQL
                    if (valor != null && !valor.equals("0000-00-00 00:00:00")) {

                        //Agrega el valor a la lista fila
                        fila.add(valor);

                    } else {

                        //Agrega el valor nulo a la lista fila
                        fila.add(null);
                    }
                }

                //Agrega la lista de la fila a la lista principal, representando una fila completa
                datosTabla.add(fila);
            }

            //Establece los datos de la tabla en tableView
            tableView.setItems(datosTabla);

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("No se ha podido cargar el contenido de la tabla");
        }
    }
}
