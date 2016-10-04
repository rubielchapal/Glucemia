package model;

import Dao.DAO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet hows take the http request from Mobile Application and send the response
 * Used to register new Glucose, called by pacient or personal logged in AndroidApp
 * @author GTST : Grupo de Tratamiento de Señales  y telecomunicaciones
 */
public class RegisterGlucose extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private static final String GOOGLE_SERVER_KEY = "key=AIzaSyCqGX1bv6mbFlv3ZhlJlRHAazOw6sfSb4M"; //Server key got from Google developer console
                                                       //AIzaSyDLYEuF7LsSj_g-nrwytWqlwNYzvPK7STE 
    static final String MESSAGE_KEY = "message";
    private static final String URL_FIREBASE = "https://fcm.googleapis.com/fcm/send"; //URL to use FCM   
    
    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
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
        String state = request.getParameter("state");
        double vValue = Double.parseDouble(data);
        DAO d = new DAO();
        Gson gson = new Gson();
        JsonObject jsonObject =  new JsonObject();
        try {
            d.conectar();
            String Pacient;
            String Performer = null;
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
                Performer = d.getName(personal, "personal");
                //Performer
                JsonElement performer;
                if(!personal.isEmpty()){
                    Performer PERFORMER = new Performer();
                    DataPerformer dataPerf = new DataPerformer();
                    dataPerf.setReference(personal);
                    dataPerf.setDisplay(Performer);
                    PERFORMER.setDataPerformer(new DataPerformer[]{dataPerf});
                    performer = gson.toJsonTree(PERFORMER);
                }else{
                    performer = gson.toJsonTree("null");
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
                    List<ListPersonal> items = d.getPersonal(pacient);
                    String[] w = new String[items.size()];
                    for(int i = 0; i < items.size(); i++){                        
                        w[i] = d.getToken(items.get(i).getId(), "personal");
                        Send(w[i], dDisplay, vValue, Performer);
                    } 
                }else if(c2 > 0){
                    cCode = "H";
                    dDisplay = "High"; 
                    List<ListPersonal> items = d.getPersonal(pacient);
                    String[] w = new String[items.size()];
                    for(int i = 0; i < items.size(); i++){                        
                        w[i] = d.getToken(items.get(i).getId(), "personal");
                        Send(w[i], dDisplay, vValue, Performer);
                    }                    
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
                
                d.registerGlucose(identifier, code, subject, date, performer, value, interpretation, state, referenceRange);
                jsonObject.addProperty("glucose", true);   
            }else{
                jsonObject.addProperty("glucose", false);
            }                                        
            response.getWriter().write(jsonObject.toString());
            d.desconectar();
        }  catch (SQLException  | IOException ex) {
            jsonObject.addProperty("error", ex.toString());            
            response.getWriter().write(jsonObject.toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RegisterGlucose.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This class get params and build the notification to send to FireBase
     * @param string
     * @param dDisplay
     * @param vValue
     * @param Performer
     * @return Response from FCM Notification 
     */
    private String Send(String string, String dDisplay, double vValue, String Performer) {
        Gson gson = new Gson();
        String value = String.valueOf(vValue*18);
        String response = null;
        try {
            Field FIELD = new Field();
            FIELD.setTo(string);
            FIELD.setPriority("high"); 
            Notification NOTIFICATION = new Notification();
            NOTIFICATION.setBody("El paciente "+Performer+" ha ingresado un dato de: "+value+", revisalo !");
            NOTIFICATION.setTitle("Alerta de Glucosa "+dDisplay);
            NOTIFICATION.setSound("mysound");
            FIELD.setNotification(NOTIFICATION);
            DataField DATAFIELD = new DataField();
            DATAFIELD.setMessage("hola");
            FIELD.setData(DATAFIELD);
            JsonElement name = gson.toJsonTree(FIELD);
            HttpURLConnection httpcon = (HttpURLConnection) ((new URL(URL_FIREBASE).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Authorization", GOOGLE_SERVER_KEY);
            httpcon.setRequestMethod("POST");
            httpcon.connect();                
            System.out.println("Connected!");
            byte[] outputBytes = name.toString().getBytes("UTF-8");
            OutputStream os = httpcon.getOutputStream();
            os.write(outputBytes);
            os.close();
            // Reading response
            InputStream input = httpcon.getInputStream();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                for (String line; (line = reader.readLine()) != null;) {
                    System.out.println(line);
                    response = line;
                }
            }                  
        } catch (MalformedURLException ex) { 
            Logger.getLogger(FCM_Server.class.getName()).log(Level.SEVERE, null, ex);
            response = ex.toString();
        } catch (IOException ex) {
            Logger.getLogger(FCM_Server.class.getName()).log(Level.SEVERE, null, ex);
            response = ex.toString();
        }
        return response;
    }

}
