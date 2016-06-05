package Controler;

import java.util.List;

/**
 *
 * @author Alejo
 */
public class Address {
    DataAddress[] address;    
    public void setDataAddress(DataAddress[] address) {this.address = address;}    
}

class DataAddress{
    String use;
    String city;
    String country;
    List<String> line;
    public void setUse(String use) { this.use = use; }
    public void setLine(List<String> line) {this.line = line;}
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }    
}

