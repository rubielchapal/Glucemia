package Controller;

/**
 *
 * @author Alejo
 */
public class Marital{
    DataMarital maritalStatus;
    public void setMarital(DataMarital maritalStatus) {this.maritalStatus = maritalStatus;}
}
class DataMarital{    
    DataCoding[] coding;    
    public void setDataAddress(DataCoding[] coding) {this.coding = coding;}    
}
class DataCoding{
    String system;
    String code;
    String display;
    public void setSystem(String system) { this.system = system; }
    public void setCode(String code) { this.code = code; }
    public void setDisplay(String display) { this.display = display; } 
}