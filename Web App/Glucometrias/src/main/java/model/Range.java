package model;

/**
 * This class is used to build Address field according to HL7-Fhir standard
 * Called in RegisterGlucose.java
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class Range {
    DataRange[] referenceRange;
    /**
     * @param referenceRange Array according to FHIR
     * @see https://www.hl7.org/fhir/observation-definitions.html#Observation.referenceRange
     */    
    public void setDataRange(DataRange[] referenceRange) {this.referenceRange = referenceRange;}    
}

class DataRange{
    Data[] low, high;
    public void setDataLow(Data[] low) {this.low = low;}
    public void setDataHigh(Data[] high) {this.high = high;}   
}

class Data{
    String value;
    String unit;
    String system;
    String code;
    public void setValue(String value) { this.value = value; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setSystem(String system) { this.system = system; } 
    public void setCode(String code) { this.code = code; }   
}
