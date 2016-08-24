/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

/**
 *
 * @author Dell
 */
public class Value {
    DataValue valueQuantity;
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


