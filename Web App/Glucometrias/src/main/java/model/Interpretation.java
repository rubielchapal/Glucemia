package model;

/**
 * This class is used to build Interpretation field acording to HL7-Fhir standard
 * Called in RegisterGlucose.java
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class Interpretation {
    DataInter interpretation;
    /**
     * @param interpretation 
     * @see https://www.hl7.org/fhir/observation-definitions.html#Observation.interpretation
     */
    public void setDataInter(DataInter interpretation) {this.interpretation = interpretation;}
}
class DataInter{    
    DataCoding[] coding;    
    public void setInterpretation(DataCoding[] coding) {this.coding = coding;}    
}
