package com.example.acessodatostrimestre1;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Categorie {

    private long categoryID;
    private String categoryName;
    private String description;
    private String picture;

    //Constructor vacio
    public Categorie() {
    }

    //Contructor que recibe como argumento todos los atributos
    public Categorie(long categoryID, String categoryName, String description, String picture) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.description = description;
        this.picture = picture;
    }

    //Devuelve el ID de la categoria y lo marca como elemento XML
    @XmlElement
    public long getCategoryID() {
        return categoryID;
    }


    //Devuelve el nombre de la categoria y lo marca como elemento XML
    @XmlElement
    public String getCategoryName() {
        return categoryName;
    }

    //Devuelve la descripcion y lo marca como elemento XML
    @XmlElement
    public String getDescription() {
        return description;
    }

    //Devuelve la imagen y lo marca como elemento XML
    @XmlElement
    public String getPicture() {
        return picture;
    }
}
