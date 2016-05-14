/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Models.DAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author Alejo
 */
public class Register extends HttpServlet {
    
    private String displaym;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request,response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        // Set response content type        
        //HttpSession respuesta = request.getSession(true);
        String given = request.getParameter("given");        
        String family = request.getParameter("family");
        String password = request.getParameter("password");
        String ndi = request.getParameter("ndi");
        String ndivalue = request.getParameter("ndivalue");
        String gender = request.getParameter("gender");
        String birthDate = request.getParameter("birthDate");
        String marital = request.getParameter("maritalStatus");        
        String telhome = request.getParameter("telhome");
        String telmobile = request.getParameter("telmobile");
        String telwork = request.getParameter("telwork");
        String correo = request.getParameter("email");
        String line = request.getParameter("line");
        String city = request.getParameter("city");
        String givenc = request.getParameter("givenc");
        String familyc = request.getParameter("familyc");
        String telc = request.getParameter("telc");        
        String relationship = request.getParameter("relationship");
        String managingOrganization = request.getParameter("managingOrganization");
        DAO d = new DAO();
        Gson gson = new Gson();
        JsonObject jsonObject =  new JsonObject();
        try {
            d.conectar();
            if(!d.isIdentifierExist(ndivalue)){
                
            //Creamos los JSON para insertar en la BD
            /*/Campo identifier
            String identifier = "'identifier':["
                    + " {'use' : 'official',"
                    + " 'system': 'urn:oid:2.16.840.1.113883.2.17.6.3',"
                    + " 'value': '" +ndivalue+ "',"
                    + " 'display': '" +ndi+ "'}]";*/
            Identifier ID = new Identifier();
            DataIdentifier dataIdentifier = new DataIdentifier();
            dataIdentifier.setUse("official");
            dataIdentifier.setSystem("urn:oid:2.16.840.1.113883.2.17.6.3");
            dataIdentifier.setValue(ndivalue);
            dataIdentifier.setDisplay(ndi);
            dataIdentifier.setPassword(password);
            ID.setDataIdentifier(new DataIdentifier[] { dataIdentifier });            
            JsonElement identifier = gson.toJsonTree(ID);
            /*/Campo name
            String name = "["
                    + " {'use' : 'official',"
                    + " 'family': ['"+family+"'],"
                    + " 'given': ['"+given+"']"
                    + "}]";*/
            Name NAME = new Name();
            DataName dataName = new DataName();
            dataName.setUse("official");
            List<String> lastname = new ArrayList<>();
            lastname.add(family);
            List<String> dataname = new ArrayList<>();
            dataname.add(given);
            dataName.setFamily(lastname);
            dataName.setGiven(dataname);
            NAME.setDataName(new DataName[] { dataName });
            JsonElement name = gson.toJsonTree(NAME);
            /*/Campo telecom, se valida si están todos o por lo menos el mobile
            mobile = " {'system': 'phone',"
                    + " 'value': '"+telmobile+"',"
                    + " 'use': 'mobile'}";     
            home = " {'system': 'phone',"
                    + " 'value': '"+telhome+"',"
                    + " 'use': 'home'}";
            work = " {'system': 'phone',"
                    + " 'value': '"+telwork+"',"
                    + " 'use': 'work'}";     
            email = " {'system': 'email',"
                    + " 'value': '"+correo+"',"
                    + " 'use': 'home'}";*/
            Telecom TELECOM = new Telecom();  
            DataTelecom dataMobile = new DataTelecom();
            dataMobile.setSystem("phone");
            dataMobile.setValue(telmobile);
            dataMobile.setUse("mobile");
            DataTelecom dataHome = new DataTelecom();           
            dataHome.setSystemH("phone");
            dataHome.setValueH(telhome);
            dataHome.setUseH("home"); 
            DataTelecom dataWork = new DataTelecom();
            dataWork.setSystemW("phone");
            dataWork.setValueW(telwork);
            dataWork.setUseW("work");
            DataTelecom dataEmail = new DataTelecom();
            dataEmail.setSystemE("email");
            dataEmail.setValueE(correo);
            dataEmail.setUseE("home");
            if(telhome.isEmpty() && telwork.isEmpty() && correo.isEmpty()){                
                //telecom = "["+mobile+"]";           
                TELECOM.setDataTelecom(new DataTelecom[] { dataMobile });
            }else if(!telhome.isEmpty() && telwork.isEmpty() && correo.isEmpty()){
                //telecom = "["+mobile+","+home+"]";             
                TELECOM.setDataTelecom(new DataTelecom[] {dataMobile, dataHome});
            }else if(telhome.isEmpty() && !telwork.isEmpty() && correo.isEmpty()){
                //telecom = "["+mobile+","+work+"]";
                TELECOM.setDataTelecom(new DataTelecom[] {dataMobile, dataWork});
            }else if(telhome.isEmpty() && telwork.isEmpty() && !correo.isEmpty()){
                //telecom = "["+mobile+","+email+"]";
                TELECOM.setDataTelecom(new DataTelecom[] {dataMobile, dataEmail});
            }else if(!telhome.isEmpty() && !telwork.isEmpty() && correo.isEmpty()){
                //telecom = "["+mobile+","+home+","+work+"]";
                TELECOM.setDataTelecom(new DataTelecom[] {dataMobile, dataHome, dataWork});
            }else if(telhome.isEmpty() && !telwork.isEmpty() && !correo.isEmpty()){
                //telecom = "["+mobile+","+work+","+email+"]";
                TELECOM.setDataTelecom(new DataTelecom[] {dataMobile, dataWork, dataEmail});
            }else if(!telhome.isEmpty() && telwork.isEmpty() && !correo.isEmpty()){
                //telecom = "["+mobile+","+home+","+email+"]";
                TELECOM.setDataTelecom(new DataTelecom[] {dataMobile, dataHome, dataEmail});
            }
            else{
                //telecom = "["+mobile+","+home+","+work+","+email+"]";
                TELECOM.setDataTelecom(new DataTelecom[] {dataMobile, dataHome, dataWork, dataEmail});
            }
            JsonElement telecom = gson.toJsonTree(TELECOM);
            /*/Campo address
            String address = "["
                    + " {'use' : 'home',"
                    + " 'line': ['"+line+"'],"
                    + " 'city': '"+city+"',"
                    + " 'country': 'COL'"
                    + "}]";*/
            Address ADDRESS = new Address();
            DataAddress dataAddress = new DataAddress();
            dataAddress.setUse("home");
            List<String> lines = new ArrayList<>();
            lines.add(line);
            dataAddress.setLine(lines);
            dataAddress.setCity(city);
            dataAddress.setCountry("COL");
            ADDRESS.setDataAddress(new DataAddress[] {dataAddress});
            JsonElement address = gson.toJsonTree(ADDRESS);
            //Campo maritalStatus
            if(marital.equalsIgnoreCase("M")){
                displaym = "Married";                        
            }else if(marital.equalsIgnoreCase("U")){
                displaym = "Unmarried";                        
            }else if(marital.equalsIgnoreCase("T")){
                displaym = "Domestic partner";                        
            }else if(marital.equalsIgnoreCase("W")){
                displaym = "Widowed";                        
            }else{
                displaym = "Legally Separated";                        
            }
            Marital MARITAL = new Marital();
            DataMarital dataMarital = new DataMarital();
            DataCoding dataCoding = new DataCoding();
            dataCoding.setCode(marital);
            dataCoding.setSystem("http://hl7.org/fhir/v3/MaritalStatus");
            dataCoding.setDisplay(displaym);
            dataMarital.setDataAddress(new DataCoding[]{ dataCoding });            
            MARITAL.setMarital(dataMarital);
            JsonElement maritalStatus = gson.toJsonTree(MARITAL);
            /*String maritalStatus = "{"
                    + " 'coding': ["
                    + "{'system': 'http://hl7.org/fhir/v3/MaritalStatus',"
                    + " 'code': '"+marital+"',"
                    + " 'display': '"+displaym+"'}]"
                    + "}";*/
            
            /*/Campo Contact
            String contact = "["
                    + "{ 'relationship': [{"
                    + " 'coding': [{"
                    + " 'system': 'http://hl7.org/fhir/patient-contact-relationship',"
                    + " 'code': '"+relationship+"'}]}],"
                    + " 'name': {"
                    + " 'use' : 'official',"
                    + " 'family': ['"+familyc+"'],"
                    + " 'given': ['"+givenc+"']},"
                    + " 'telecom': [{"
                    + " 'system': 'phone',"
                    + " 'value': '"+telc+"',"
                    + " 'use': 'mobile'}]"
                    + "}]";*/
            Contact CONTACT = new Contact();
            DataContact dataContact = new DataContact();
            DataRelationship dataRelationship = new DataRelationship();
            DataCodingContact dataCodingContact = new DataCodingContact();
            dataCodingContact.setCode(relationship);
            dataCodingContact.setSystem("http://hl7.org/fhir/patient-contact-relationship");
            NameContact nameContact = new NameContact();
            nameContact.setUse("official");
            List<String> lastnamec = new ArrayList<>();
            lastnamec.add(familyc);
            List<String> datanamec = new ArrayList<>();
            datanamec.add(givenc);
            nameContact.setFamily(lastnamec);
            nameContact.setGiven(datanamec);
            DataTelecomContact dataTelecomContact = new DataTelecomContact();
            dataTelecomContact.setSystem("phone");
            dataTelecomContact.setValue(telc);
            dataTelecomContact.setUse("mobile");
            dataRelationship.setDataContact(new DataCodingContact[] {dataCodingContact});
            dataContact.setRelationship(new DataRelationship[] {dataRelationship});
            dataContact.setNameContact(nameContact);
            dataContact.setTelecomContact(new DataTelecomContact[] {dataTelecomContact});
            CONTACT.setDataContact(new DataContact[] {dataContact});
            JsonElement contact = gson.toJsonTree(CONTACT);
            /*/Campo communication
            String communication = "[{"
                    + " 'language': {"
                    + " 'coding': [{"
                    + " 'system': 'urn:ietf:bcp:47',"
                    + " 'code': 'es-419',"
                    + " 'display': 'Espanish'}],"
                    + " 'text':'Espa&ntildeol'},"
                    + " 'preferred': true}]";*/
            Communication COM = new Communication();
            DataCommunication dataCommunication = new DataCommunication();
            dataCommunication.setPreferred(Boolean.TRUE);
            Language language = new Language();
            language.setText("Español Latino");
            LanguageCoding languageCoding = new LanguageCoding();
            languageCoding.setSystem("urn:ietf:bcp:47");
            languageCoding.setCode("es-419");
            languageCoding.setDisplay("Español");
            language.setLanguageCoding(new LanguageCoding[] {languageCoding});
            dataCommunication.setLanguage(language);
            COM.setDataCommunication(new DataCommunication[] {dataCommunication});
            JsonElement communication = gson.toJsonTree(COM);
            // Show it.           
            //out.println(gson.toJsonTree(ID));
            /*response.getWriter().write(identifier.toString()+"\n \n"+name.toString()+"\n \n"+telecom.toString()+
                    "\n \n"+address.toString()+"\n \n"+maritalStatus.toString()+"\n \n"+contact.toString()+
                    "\n \n"+communication.toString());*/
            d.registerPacient(identifier, name, telecom, gender, birthDate, address, maritalStatus, contact, communication, managingOrganization);
            jsonObject.addProperty("register", true);
            /*respuesta.setAttribute("session", contact);
            respuesta.setAttribute("sessionName", name);
            respuesta.setAttribute("sessionIdentifier", "lol");
            request.getRequestDispatcher("page.jsp").forward(request, response);*/
            //d.registerUser(ndi, password, line);            
            }
            else{
                jsonObject.addProperty("register", false);
            }
            
            response.getWriter().write(jsonObject.toString());
        } catch (Exception ex){
            jsonObject.addProperty("error", ex.toString());            
            response.getWriter().write(jsonObject.toString());
        }
        //response.sendRedirect("index.jsp");
        //out.close();
    }


}
