package model;

import java.util.List;

/**
 * This class is used to build Name field according to HL7-Fhir standard
 * Called in RegisterPacient.java and RegisterPersonal.java
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class Name {
    DataName[] name; 
    /**
     * @param name Array acording to FHIR
     * @see https://www.hl7.org/fhir/patient-definitions.html#Patient.name
     * @see https://www.hl7.org/fhir/practitioner-definitions.html#Practitioner.name
     */   
    public void setDataName(DataName[] name) {this.name = name;}     
}
class DataName {
    String use;
    List<String> family; 
    List<String> given; 
    public void setUse(String use) { this.use = use; }
    public void setFamily(List<String> family) {this.family = family;}
    public void setGiven(List<String> given) {this.given = given;} 
}

