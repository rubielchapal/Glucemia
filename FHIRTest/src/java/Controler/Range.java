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
public class Range {
    DataRange[] referenceRange;    
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
