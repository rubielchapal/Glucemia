/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

import Models.DAO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Dell
 */
public class RegisterGlucose extends HttpServlet {

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        //Data
        String pacient = request.getParameter("pacient");
        String date = request.getParameter("date"); 
        String personal =  request.getParameter("personal");
        String data =  request.getParameter("data");
        double vValue = Double.parseDouble(data);
        DAO d = new DAO();
        Gson gson = new Gson();
        JsonObject jsonObject =  new JsonObject();
        try {
            d.conectar();
            String Pacient;
            String Performer;
            if(d.isPatient(pacient)){
                //Identifier
                Identifier ID = new Identifier();
                DataIdentifier dataIdentifier = new DataIdentifier();
                dataIdentifier.setUse("official");
                dataIdentifier.setSystem("http://www.bmc.nl/zorgportal/identifiers/observations");
                dataIdentifier.setValue("6323");
                ID.setDataIdentifier(new DataIdentifier[] { dataIdentifier });            
                JsonElement identifier = gson.toJsonTree(ID);
                //Code
                Code CODE = new Code();
                DataCode dataCode = new DataCode();
                DataCoding dataCoding = new DataCoding();
                dataCoding.setCode("15074-8");
                dataCoding.setSystem("http://loinc.org");
                dataCoding.setDisplay("Glucose [Moles/volume] in Blood");
                dataCode.setDataCod(new DataCoding[]{dataCoding});
                CODE.setCode(dataCode);
                JsonElement code = gson.toJsonTree(CODE);
                //Subject
                Pacient = d.getName(pacient, "patient");
                Subject SUBJECT = new Subject();
                DataSubject dataSub = new DataSubject();
                dataSub.setReference(pacient);
                dataSub.setDisplay(Pacient);
                SUBJECT.setDataSubject(dataSub);
                JsonElement subject = gson.toJsonTree(SUBJECT);
                //Performer
                JsonElement performer;
                String asd = "na";
                if(!personal.isEmpty()){
                    Performer = d.getName(personal, "personal");
                    Performer PERFORMER = new Performer();
                    DataPerformer dataPerf = new DataPerformer();
                    dataPerf.setReference(personal);
                    dataPerf.setDisplay(Performer);
                    PERFORMER.setDataPerformer(new DataPerformer[]{dataPerf});
                    performer = gson.toJsonTree(PERFORMER);
                }else{
                    performer = gson.toJsonTree("null");
                    asd = performer.getAsString();
                }              
                //Value
                Value VALUE = new Value();
                DataValue dataValue = new DataValue();
                dataValue.setValue(data);
                dataValue.setUnit("mmol/l");
                dataValue.setSystem("http://unitsofmeasure.org");
                dataValue.setCode("mmol/L");
                VALUE.setDataValue(dataValue);
                JsonElement value = gson.toJsonTree(VALUE);
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
                JsonElement referenceRange = gson.toJsonTree(RANGE);
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
                JsonElement interpretation = gson.toJsonTree(INTER);                                
                
                d.registerGlucose(identifier, code, subject, date, performer, value, interpretation, referenceRange);
                jsonObject.addProperty("glucose", true);
                
                /*if(asd.equalsIgnoreCase("hola")){                
                jsonObject.addProperty("pacient", true);
                response.getWriter().write(identifier.toString()+"\n \n"+code.toString()+"\n \n"+subject.toString()+
                        "\n \n"+value.toString()+"\n \n"+referenceRange.toString()+
                    "\n \n"+interpretation.toString());/
                }
                else{    
                response.getWriter().write(identifier.toString()+"\n \n"+code.toString()+"\n \n"+subject.toString()+
                        "\n \n"+performer.toString()+"\n \n"+value.toString()+"\n \n"+referenceRange.toString()+
                    "\n \n"+interpretation.toString()+"\n \n");*
                }*/
            }else{
                jsonObject.addProperty("glucose", false);
            }                                        
            response.getWriter().write(jsonObject.toString());
        }  catch (SQLException  | IOException ex) {
            jsonObject.addProperty("error", ex.toString());            
            response.getWriter().write(jsonObject.toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RegisterGlucose.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
