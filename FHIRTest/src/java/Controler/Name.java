package Controler;

import java.util.List;

/**
 *
 * @author Alejo
 */
public class Name {
    DataName[] name;    
    public void setDataName(DataName[] name) {this.name = name;}     
}
class DataName {
    String use;
    List<String> family; 
    List<String> given; 
    public void setUse(String use) { this.use = use; }
    public void setFamily(List<String> family) {this.family = family;}
    public void setGiven(List<String> given) {this.given = given;} 
}

