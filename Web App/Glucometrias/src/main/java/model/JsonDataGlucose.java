
package model;

import Dao.DAO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entities.Glucose;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * create json data with glucose object 
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class JsonDataGlucose {
    
    private JsonElement identifier;
    private JsonElement code;
    private JsonElement subject;
    private String issued;
    private JsonElement performer;
    private JsonElement valueQuantity;
    private JsonElement interpretation;
    private String comments;
    private JsonElement referenceRange;

    /**
     * generate json data of the glucose
     * @param glucose
     * @return
     */
    public JsonDataGlucose generateJsonDataGlucose(Glucose glucose){      
        
         JsonDataGlucose  jsonDataGlucose = new JsonDataGlucose();
        DAO d = new DAO();
        Gson gson = new Gson();
        JsonObject jsonObject =  new JsonObject();
        double vValue = Double.parseDouble(glucose.getData());
         try {
            d.conectar();
            String Pacient;
            String Performer;
            
            Identifier ID = new Identifier();
                DataIdentifier dataIdentifier = new DataIdentifier();
                dataIdentifier.setUse("official");
                dataIdentifier.setSystem("http://www.bmc.nl/zorgportal/identifiers/observations");
                dataIdentifier.setValue("6323");
                ID.setDataIdentifier(new DataIdentifier[] { dataIdentifier });
                
                jsonDataGlucose.setIdentifier(gson.toJsonTree(ID));
                
                //Code
                Code CODE = new Code();
                DataCode dataCode = new DataCode();
                DataCoding dataCoding = new DataCoding();
                dataCoding.setCode("15074-8");
                dataCoding.setSystem("http://loinc.org");
                dataCoding.setDisplay("Glucose [Moles/volume] in Blood");
                dataCode.setDataCod(new DataCoding[]{dataCoding});
                CODE.setCode(dataCode);
                
                jsonDataGlucose.setCode(gson.toJsonTree(CODE));
                //Subject
                Pacient = d.getName(glucose.getPacient(), "patient");
                Subject SUBJECT = new Subject();
                DataSubject dataSub = new DataSubject();
                dataSub.setReference(glucose.getPacient());
                dataSub.setDisplay(Pacient);
                SUBJECT.setDataSubject(dataSub);
                jsonDataGlucose.setSubject(gson.toJsonTree(SUBJECT));
               
                //Performer
                
                if(!glucose.getPersonal().isEmpty()){
                    Performer = d.getName(glucose.getPersonal(), "personal");
                    Performer PERFORMER = new Performer();
                    DataPerformer dataPerf = new DataPerformer();
                    dataPerf.setReference(glucose.getPersonal());
                    dataPerf.setDisplay(Performer);
                    PERFORMER.setDataPerformer(new DataPerformer[]{dataPerf});
                    jsonDataGlucose.setPerformer(gson.toJsonTree(PERFORMER));
                   
                    
                }else{
                    jsonDataGlucose.setPerformer(gson.toJsonTree("null"));
                   
                         
                }              
                //Value
                Value VALUE = new Value();
                DataValue dataValue = new DataValue();
                dataValue.setValue(glucose.getData());
                dataValue.setUnit("mmol/l");
                dataValue.setSystem("http://unitsofmeasure.org");
                dataValue.setCode("mmol/L");
                VALUE.setDataValue(dataValue);
                jsonDataGlucose.setValueQuantity(gson.toJsonTree(VALUE));
              
                //Reference Range
                double low = 3.1;
                double high = 6.2;
                Range RANGE = new Range();
                DataRange dataRange = new DataRange();
                Data lData = new Data();
                lData.setValue(Double.toString(low));
                lData.setUnit("mmol/l");
                lData.setSystem("http://unitsofmeasure.org");
                lData.setCode("mmol/l");
                Data hData = new Data();
                hData.setValue(Double.toString(high));
                hData.setUnit("mmol/l");
                hData.setSystem("http://unitsofmeasure.org");
                hData.setCode("mmol/l");
                dataRange.setDataLow(new Data[]{lData});
                dataRange.setDataHigh(new Data[]{hData});               
                RANGE.setDataRange(new DataRange[]{dataRange});
                jsonDataGlucose.setReferenceRange(gson.toJsonTree(RANGE));

                

                //Interpretation
                Interpretation INTER = new Interpretation();
                DataInter dataInter = new DataInter();
                DataCoding dCoding = new DataCoding();
                String cCode;
                String dDisplay;
                int c1 = Double.compare(vValue, low);
                int c2 = Double.compare(vValue, high);
                if(c1 > 0 && c2 < 0){
                    cCode = "N";
                    dDisplay = "Normal";
                }else if(c1 < 0){
                    cCode = "L";
                    dDisplay = "Low";                    
                }else if(c2 > 0){
                    cCode = "H";
                    dDisplay = "High";                    
                }
                else{
                    cCode = "ND";
                    dDisplay = "Not Detected";                    
                }
                dCoding.setCode(cCode);
                dCoding.setSystem("http://hl7.org/fhir/v2/0078");
                dCoding.setDisplay(dDisplay); 
                dataInter.setInterpretation(new DataCoding[]{dCoding});
                INTER.setDataInter(dataInter);  
                
                jsonDataGlucose.setInterpretation(gson.toJsonTree(INTER));
                jsonDataGlucose.setIssued(FormatDate.getFormatDate()); 
                jsonDataGlucose.setComments(glucose.getState());
        
                
         }catch (SQLException ex) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ",ex.toString()));
           
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JsonDataGlucose.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         return jsonDataGlucose;
    }
     
    /**
     * @return the identifier
     */
    public JsonElement getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(JsonElement identifier) {
        this.identifier = identifier;
    }

    /**
     * @return the code
     */
    public JsonElement getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(JsonElement code) {
        this.code = code;
    }

    /**
     * @return the subject
     */
    public JsonElement getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(JsonElement subject) {
        this.subject = subject;
    }

    /**
     * @return the issued
     */
    public String getIssued() {
        return issued;
    }

    /**
     * @param issued the issued to set
     */
    public void setIssued(String issued) {
        this.issued = issued;
    }

    /**
     * @return the performer
     */
    public JsonElement getPerformer() {
        return performer;
    }

    /**
     * @param performer the performer to set
     */
    public void setPerformer(JsonElement performer) {
        this.performer = performer;
    }

    /**
     * @return the valueQuantity
     */
    public JsonElement getValueQuantity() {
        return valueQuantity;
    }

    /**
     * @param valueQuantity the valueQuantity to set
     */
    public void setValueQuantity(JsonElement valueQuantity) {
        this.valueQuantity = valueQuantity;
    }

    /**
     * @return the interpretation
     */
    public JsonElement getInterpretation() {
        return interpretation;
    }

    /**
     * @param interpretation the interpretation to set
     */
    public void setInterpretation(JsonElement interpretation) {
        this.interpretation = interpretation;
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return the referenceRange
     */
    public JsonElement getReferenceRange() {
        return referenceRange;
    }

    /**
     * @param referenceRange the referenceRange to set
     */
    public void setReferenceRange(JsonElement referenceRange) {
        this.referenceRange = referenceRange;
    }


     
     

    
}
