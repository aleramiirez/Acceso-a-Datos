package com.example.acessodatostrimestre1;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

@XmlRootElement
public class Categories {

    //Coleccion de las categorias
    Collection<Categorie> categories = new ArrayList<>();

    //Devuelve la coleccion de las categorias y establece el nombre en singular para que
    //en el XML salga cada categoria con la etiqueta <categorie>
    @XmlElement(name = "categorie")
    public Collection<Categorie> getCategories() {
        return categories;
    }

    //Modifica la coleccion de categorias
    public void setCategories(Collection<Categorie> categories) {
        this.categories = categories;
    }
}
