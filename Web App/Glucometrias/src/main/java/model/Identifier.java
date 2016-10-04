package model;

/**
 * This class is used to build Identifier field according to HL7-Fhir standard
 * Called in RegisterPacient.java, RegisterPacient.java and RegisterGlucose.java
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class Identifier {
    DataIdentifier[] identifier;  
    /**
     * @param identifier Array acording to FHIR
     * @see https://www.hl7.org/fhir/patient-definitions.html#Patient.identifier
     * @see https://www.hl7.org/fhir/practitioner-definitions.html#Practitioner.identifier
     * @see https://www.hl7.org/fhir/observation-definitions.html#Observation.identifier
    */
    public void setDataIdentifier(DataIdentifier[] identifier) {this.identifier = identifier;}    
}
class DataIdentifier {
    String use;
    String system;
    String value;
    String display;
    String password;
    public void setUse(String use) { this.use = use; }
    public void setSystem(String system) { this.system = system; }
    public void setValue(String value) { this.value = value; }
    public void setDisplay(String display) { this.display = display; }    
    public void setPassword(String password) { this.password = password; } 
}
