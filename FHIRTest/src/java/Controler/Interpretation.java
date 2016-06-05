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
public class Interpretation {
    DataInter interpretation;
    public void setDataInter(DataInter interpretation) {this.interpretation = interpretation;}
}
class DataInter{    
    DataCoding[] coding;    
    public void setInterpretation(DataCoding[] coding) {this.coding = coding;}    
}
