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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author Alejo
 */
public class RegisterPersonal extends HttpServlet {
    
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
        String role = request.getParameter("role");
        String telmobile = request.getParameter("telmobile");
        String telwork = request.getParameter("telwork");
        String correo = request.getParameter("email");
        String line = request.getParameter("line");
        String city = request.getParameter("city");
        String managingOrganization = request.getParameter("managingOrganization");
        DAO d = new DAO();
        Gson gson = new Gson();
        JsonObject jsonObject =  new JsonObject();
        try {
            d.conectar();
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
            //Campo Rol
            if(role.equalsIgnoreCase("61894003")){
                displaym = "Endocrinologist";                        
            }else if(role.equalsIgnoreCase("59058001")){
                displaym = "General Doctor";                        
            }else if(role.equalsIgnoreCase("398130009")){
                displaym = "Medical Student";
            }else if(role.equalsIgnoreCase("309446002")){
                displaym = "Nurse Manager";                        
            }else{
                displaym = "General Nurse";                        
            }
            Rol ROL = new Rol();
            DataRol dataRol = new DataRol();
            DataCoding dataCoding = new DataCoding();
            dataCoding.setCode(role);
            dataCoding.setSystem("http://snomed.info/sct");
            dataCoding.setDisplay(displaym);
            DataRole dataRole = new DataRole();
            dataRole.setDataCod(new DataCoding[]{ dataCoding });
            dataRol.setMo(managingOrganization);
            dataRol.setRole(dataRole);            
            ROL.setDataRol(new DataRol[]{ dataRol });
            JsonElement practitionerRole = gson.toJsonTree(ROL);                         
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
            DataTelecom dataWork = new DataTelecom();
            dataWork.setSystemW("phone");
            dataWork.setValueW(telwork);
            dataWork.setUseW("work");
            DataTelecom dataEmail = new DataTelecom();
            dataEmail.setSystemE("email");
            dataEmail.setValueE(correo);
            dataEmail.setUseE("home");
            if(telwork.isEmpty() && correo.isEmpty()){                
                //telecom = "["+mobile+"]";           
                TELECOM.setDataTelecom(new DataTelecom[] { dataMobile });            
            }else if(!telwork.isEmpty() && correo.isEmpty()){
                //telecom = "["+mobile+","+work+"]";
                TELECOM.setDataTelecom(new DataTelecom[] {dataMobile, dataWork});
            }else if(telwork.isEmpty() && !correo.isEmpty()){
                //telecom = "["+mobile+","+email+"]";
                TELECOM.setDataTelecom(new DataTelecom[] {dataMobile, dataEmail});
            }
            else{
                //telecom = "["+mobile+","+work+","+email+"]";
                TELECOM.setDataTelecom(new DataTelecom[] {dataMobile, dataWork, dataEmail});
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
            // Show it.           
            //out.println(gson.toJsonTree(ID));
            //response.getWriter().write(identifier.toString()+"\n \n"+name.toString()+"\n \n"+telecom.toString()+
            //        "\n \n"+address.toString()+"\n \n"+practitionerRole.toString()+"\n");
            if(!d.isPersonal(ndivalue)){
                d.registerPersonal(identifier, password, name, telecom, gender, birthDate, address, practitionerRole);
                jsonObject.addProperty("register", true);
            /*respuesta.setAttribute("session", contact);
            respuesta.setAttribute("sessionName", name);
            respuesta.setAttribute("sessionIdentifier", "lol");
            request.getRequestDispatcher("page.jsp").forward(request, response);*
            //d.registerUser(ndi, password, line);            */
            }
            else{
                jsonObject.addProperty("regiser", false);
            }            
            response.getWriter().write(jsonObject.toString());
            d.desconectar();
        } catch (SQLException | IOException ex){
            jsonObject.addProperty("error", ex.toString());            
            response.getWriter().write(jsonObject.toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RegisterPacient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //response.sendRedirect("index.jsp");
        //out.close();
        //response.sendRedirect("index.jsp");
        //out.close();
    }   


}
