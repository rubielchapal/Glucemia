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
public class Performer {
    DataPerformer[] performer;    
    public void setDataPerformer (DataPerformer[] performer) {this.performer = performer;}    
}

class DataPerformer{
    String reference;
    String display;
    public void setReference(String reference) { this.reference = reference; }
    public void setDisplay(String display) { this.display = display; }     
}

