/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

import Models.DAO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alejo
 */
public class ValueFilterGlucose extends HttpServlet {

    
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
        //Datos Android        
        String ndivalue = request.getParameter("user");
        String fixvalue = request.getParameter("fixvalue");
        String minvalue = request.getParameter("minvalue");
        String maxvalue = request.getParameter("maxvalue");
        String params[] = new String[3];
        params[0] = fixvalue;
        params[1] = minvalue;
        params[2] = maxvalue;
        DAO dao = new DAO();
        Gson gson = new Gson();
        JsonObject jsonObject =  new JsonObject();
        try {
            dao.conectar();
            //List<Device> products = dao.findDevice(params);
            JsonArray arrayObj = new JsonArray();
            if(dao.isPatient(ndivalue)){
                List<ListGluc> items = dao.ValueFilterGlucose(ndivalue, params);
                items.stream().map((mensajes) -> gson.toJsonTree(mensajes)).forEach((mensajesObj) -> {
                arrayObj.add(mensajesObj);
                });
                boolean status = true;
                if(items.isEmpty()){
                    status = false;                
                }         
                jsonObject.addProperty("status", status);                
                jsonObject.add("obs_glucose", arrayObj);
            }else{            
                jsonObject.addProperty("status", false);               
            }
            /*List<Device> items = dao.allDevice();
            items.stream().map((mensajes) -> gson.toJsonTree(mensajes)).forEach((mensajesObj) -> {
            arrayObj.add(mensajesObj);
            });
            boolean status = false;
            if(!items.isEmpty()){
                status = true;                
            }         
            jsonObject.addProperty("status", status);                
            jsonObject.add("device", arrayObj);*/
            response.getWriter().write(jsonObject.toString());
            dao.desconectar();
        } catch (SQLException | ClassNotFoundException | IOException ex){
            jsonObject.addProperty("error", ex.toString());            
            response.getWriter().write(jsonObject.toString());
        } catch (ParseException ex) {
            Logger.getLogger(ListGlucose.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
