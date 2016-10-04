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
public class Rol {
    DataRol[] practitionerRole;    
    public void setDataRol(DataRol[] practitionerRole) {this.practitionerRole = practitionerRole;}    
}

class DataRol{
    String managingOrganization;
    DataRole role;
    public void setMo(String managingOrganization) { this.managingOrganization = managingOrganization; }      
    public void setRole(DataRole role) {this.role = role;}
}

class DataRole{    
    DataCoding[] coding;    
    public void setDataCod(DataCoding[] coding) {this.coding = coding;}    
}
