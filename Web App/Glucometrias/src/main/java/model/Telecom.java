package model;

/**
 * This class is used to build Telecom field according to HL7-Fhir standard
 * Called in RegisterPacient.java and RegisterPersonal.java
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class Telecom {
    DataTelecom[] telecom;     
    /**
     * @param telecom Array according to FHIR
     * @see https://www.hl7.org/fhir/patient-definitions.html#Patient.telecom
     * @see https://www.hl7.org/fhir/practitioner-definitions.html#Practitioner.telecom
     */
    public void setDataTelecom(DataTelecom[] telecom) {this.telecom = telecom;} 
}
class DataTelecom {
    String system; 
    String value;    
    String use;
    public void setSystem(String system) { this.system = system; }
    public void setValue(String value) { this.value = value; }
    public void setUse(String use) { this.use = use; }
    public void setSystemW(String system) { this.system = system; }
    public void setValueW(String value) { this.value = value; }
    public void setUseW(String use) { this.use = use; }
    public void setSystemE(String system) { this.system = system; }
    public void setValueE(String value) { this.value = value; }
    public void setUseE(String use) { this.use = use; }
    public void setSystemH(String system) { this.system = system; }
    public void setValueH(String value) { this.value = value; }
    public void setUseH(String use) { this.use = use; }
}

