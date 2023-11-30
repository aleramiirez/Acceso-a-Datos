package com.example.acessodatostrimestre1;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

@XmlRootElement
public class Regions {

    //Coleccion de las regiones
    Collection<Region> regions = new ArrayList<>();

    //Devuelve la coleccion de la regiones y establece el nombre en singular para que
    //en el XML salga cada region con la etiqueta <region>
    @XmlElement(name = "region")
    public Collection<Region> getRegions() {
        return regions;
    }

    //Modifica la coleccion de regiones
    public void setRegions(Collection<Region> regions) {
        this.regions = regions;
    }
}
