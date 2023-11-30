package com.example.acessodatostrimestre1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDatos {

    //URL de la base de datos
    public static String url = "jdbc:mysql://localhost:3306/northwind";

    //Atributo estatico para la conexion
    public static Connection con = null;

    //Abre la conexion a la base de datos
    public static Connection abrirConexion(String url, String usuario, String contraseña) throws SQLException {
        try {
                con = DriverManager.getConnection(url, usuario, contraseña);
                return con;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Cierra la conexion a la base de datos
    public static void CerrarConexion() throws SQLException {
        try {
                con.close();
            System.out.println("Conexion cerrada");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
