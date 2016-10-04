package Controler;
/**
 *
 * @author Alejo
 */
public class Identifier {
    DataIdentifier[] identifier;    
    public void setDataIdentifier(DataIdentifier[] identifier) {this.identifier = identifier;}    
}
class DataIdentifier {
    String use;
    String system;
    String value;
    String display;
    String password;
    public void setUse(String use) { this.use = use; }
    public void setSystem(String system) { this.system = system; }
    public void setValue(String value) { this.value = value; }
    public void setDisplay(String display) { this.display = display; }    
    public void setPassword(String password) { this.password = password; } 
}
