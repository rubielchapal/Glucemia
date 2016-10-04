package model;

/**
 * This class is used to build Code field according to HL7-Fhir standard
 * Called in RegisterGlucose.java
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class Code {
    DataCode code;   
    /**
     * @param code Array according to FHIR
     * @see https://www.hl7.org/fhir/observation-definitions.html#Observation.code
     */
    public void setCode(DataCode code) {this.code = code;}        
}

class DataCode{    
    DataCoding[] coding;    
    public void setDataCod(DataCoding[] coding) {this.coding = coding;}      
}