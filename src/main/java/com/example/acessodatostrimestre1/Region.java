package com.example.acessodatostrimestre1;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Region {

    private String regionID;

    private String regionDescription;

    //Constructor vacio
    public Region() {
    }

    //Constructor que recibe como argumentos el id de la region y su descripcion
    public Region(String regionID, String regionDescription) {
        this.regionID = regionID;
        this.regionDescription = regionDescription;
    }

    //Devuelve el id de la region y lo marca como elemento XML
    @XmlElement
    public String getRegionID() {
        return regionID;
    }

    //Devuelve la descripcion de la region y lo marca como elemento XML
    @XmlElement
    public String getRegionDescription() {
        return regionDescription;
    }
}
