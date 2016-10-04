package model;

import java.util.List;

/**
 * This class is used to build Contact field according to HL7-Fhir standard
 * Called in RegisterPacient.java
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class Contact {
    DataContact[] contact;    
    /**
     * @param contact Array acording to FHIR
     * @see https://www.hl7.org/fhir/patient-definitions.html#Patient.contact
     */    
    public void setDataContact (DataContact[] contact) {this.contact = contact;} 
}
class DataContact{
    DataRelationship[] relationship;
    DataTelecomContact[] telecom;
    NameContact name;
    public void setRelationship(DataRelationship[] relationship) {this.relationship = relationship;}
    public void setNameContact(NameContact name) {this.name = name;}    
    public void setTelecomContact(DataTelecomContact[] telecom) {this.telecom = telecom;}
}
class DataRelationship{
    DataCodingContact[] coding;
    public void setDataContact(DataCodingContact[] coding) {this.coding = coding;}
}
class DataCodingContact{    
    String system;
    String code;
    public void setSystem(String system) { this.system = system; }
    public void setCode(String code) { this.code = code; }
}
class NameContact{    
    String use;
    List<String> family; 
    List<String> given; 
    public void setUse(String use) { this.use = use; }
    public void setFamily(List<String> family) {this.family = family;}
    public void setGiven(List<String> given) {this.given = given;}
}
class DataTelecomContact{
    String system; 
    String value;    
    String use;
    public void setSystem(String system) { this.system = system; }
    public void setValue(String value) { this.value = value; }
    public void setUse(String use) { this.use = use; }    
}