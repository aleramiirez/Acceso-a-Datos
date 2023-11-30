package com.example.acessodatostrimestre1;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

@XmlRootElement
public class Customers {

    //Coleccion de los clientes
    Collection<Customer> customers = new ArrayList<>();

    //Devuelve la coleccion de los clientes y establece el nombre en singular para que
    //en el XML salga cada cliente con la etiqueta <customer>
    @XmlElement(name = "customer")
    public Collection<Customer> getCustomers() {
        return customers;
    }

    //Modifica la coleccion de clientes
    public void setCustomers(Collection<Customer> customers) {
        this.customers = customers;
    }
}
