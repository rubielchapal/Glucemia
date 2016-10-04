package model;

/**
 * This class is used to build Value field according to HL7-Fhir standard
 * Called in and RegisterGlucose.java
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class Value {
    DataValue valueQuantity;
    /**
     * @param valueQuantity Array according to FHIR
     * @see https://www.hl7.org/fhir/observation-definitions.html#Observation.value_x_
     */
    public void setDataValue(DataValue valueQuantity){ this.valueQuantity = valueQuantity;}       
}

class DataValue{
    String value;
    String unit;
    String system;
    String code;
    public void setValue(String value) { this.value = value; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setSystem(String system) { this.system = system; }
    public void setCode(String code) { this.code = code; }     
}


