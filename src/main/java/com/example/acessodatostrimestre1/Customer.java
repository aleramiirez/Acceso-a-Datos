package com.example.acessodatostrimestre1;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Customer {

    private String customerID;
    private String companyName;
    private String contactName;
    private String contactTitle;
    private String address;
    private String city;
    private String region;
    private String postalCode;
    private String country;
    private String phone;
    private String fax;

    //Constructor vacio
    public Customer() {
    }

    //Constructor que recibe como argumento todos los atributos
    public Customer(String customerID, String companyName, String contactName, String contactTitle, String address, String city, String region, String postalCode, String country, String phone, String fax) {
        this.customerID = customerID;
        this.companyName = companyName;
        this.contactName = contactName;
        this.contactTitle = contactTitle;
        this.address = address;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
        this.fax = fax;
    }

    //Devuelve el ID del cliente y lo marca como elemento XML
    @XmlElement
    public String getCustomerID() {
        return customerID;
    }

    //Devuelve el nombre de la compañia y lo marca como elemento XML
    @XmlElement
    public String getCompanyName() {
        return companyName;
    }

    //Devuelve el nombre de contacto y lo marca como elemento XML
    @XmlElement
    public String getContactName() {
        return contactName;
    }

    //Devuelve el titulo de contacto y lo marca como elemento XML
    @XmlElement
    public String getContactTitle() {
        return contactTitle;
    }

    //Devuelve la direccion y lo marca como elemento XML
    @XmlElement
    public String getAddress() {
        return address;
    }

    //Devuelve la ciudad y lo marca como elemento XML
    @XmlElement
    public String getCity() {
        return city;
    }

    //Devuelve la region y lo marca como elemento XML
    @XmlElement
    public String getRegion() {
        return region;
    }

    //Devuelve el codigo postal y lo marca como elemento XML
    @XmlElement
    public String getPostalCode() {
        return postalCode;
    }

    //Devuelve el pais y lo marca como elemento XML
    @XmlElement
    public String getCountry() {
        return country;
    }

    //Devuelve el numero de telefono y lo marca como elemento XML
    @XmlElement
    public String getPhone() {
        return phone;
    }

    //Devuelve el fax y lo marca como elemento XML
    @XmlElement
    public String getFax() {
        return fax;
    }
}
