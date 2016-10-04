package model;

/**
 * This class is used to build Marital field according to HL7-Fhir standard
 * Called in RegisterPacient.java
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class Marital{
    DataMarital maritalStatus;
    /**
     * @param maritalStatus Array acording to FHIR
     * @see https://www.hl7.org/fhir/patient-definitions.html#Patient.maritalStatus
     */
    public void setMarital(DataMarital maritalStatus) {this.maritalStatus = maritalStatus;}
}
class DataMarital{    
    DataCoding[] coding;    
    public void setDataAddress(DataCoding[] coding) {this.coding = coding;}    
}
class DataCoding{
    String system;
    String code;
    String display;
    public void setSystem(String system) { this.system = system; }
    public void setCode(String code) { this.code = code; }
    public void setDisplay(String display) { this.display = display; } 
}