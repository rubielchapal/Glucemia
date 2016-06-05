/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

import com.google.gson.JsonElement;

/**
 *
 * @author Dell
 */
public class ListGluc {
    private String subject;
    private String performer;
    private String valueQuantity;
    private String interpretation;
    private String issued;

    
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getValueQuantity() {
        return valueQuantity;
    }

    public void setValueQuantity(String valueQuantity) {
        this.valueQuantity = valueQuantity;
    }

    public String getInterpretation() {
        return interpretation;
    }

    public void setInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    
    
    
    
}
