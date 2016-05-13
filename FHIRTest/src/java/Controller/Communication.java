package Controller;

/**
 *
 * @author Alejo
 */
public class Communication {
    DataCommunication[] communication;    
    public void setDataCommunication(DataCommunication[] communication) {this.communication = communication;}   
}
class DataCommunication{
    Boolean preferred;
    Language language;
    public void setLanguage(Language language) {this.language = language;} 
    public void setPreferred(Boolean preferred) {this.preferred = preferred;}
}
class Language{
    String text;
    LanguageCoding[] coding;
    public void setText(String text) {this.text = text;} 
    public void setLanguageCoding(LanguageCoding[] coding) {this.coding = coding;}   
}
class LanguageCoding{
    String system; 
    String code;    
    String display;
    public void setSystem(String system) { this.system = system; }
    public void setCode(String code) { this.code = code; }
    public void setDisplay(String display) { this.display = display; }
}