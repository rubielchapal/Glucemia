package model;

import java.util.List;

/**
 * This class is used to build Address field according to HL7-Fhir standard
 * Called in RegisterPacient.java and RegisterPersonal.java
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class Address {
    DataAddress[] address;
    /**
     * @param address Array according to FHIR
     * @see https://www.hl7.org/fhir/patient-definitions.html#Patient.address
     * @see https://www.hl7.org/fhir/practitioner-definitions.html#Practitioner.address
     */
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

