module com.example.acessodatostrimestre1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.xml.bind;


    opens com.example.acessodatostrimestre1 to javafx.fxml, java.xml.bind;
    exports com.example.acessodatostrimestre1;
}