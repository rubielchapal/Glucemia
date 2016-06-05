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
public class Subject {
    DataSubject subject;
    public void setDataSubject(DataSubject subject){ this.subject = subject;}       
}

class DataSubject{
    String reference;
    String display;
    public void setReference(String reference) { this.reference = reference; }
    public void setDisplay(String display) { this.display = display; }    
}
