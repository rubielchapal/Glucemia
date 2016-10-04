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
 * @author Alejandro Mora
 */
public class PacientListServlet extends HttpServlet {

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
        DAO dao = new DAO();
        Gson gson = new Gson();
        JsonObject jsonObject =  new JsonObject();
        try {
            dao.conectar();
            //List<Device> products = dao.findDevice(params);
            JsonArray arrayObj = new JsonArray();
            if(dao.isPersonal(ndivalue)){
                List<ListPacient> items = dao.listPacient(ndivalue);
                items.stream().map((mensajes) -> gson.toJsonTree(mensajes)).forEach((mensajesObj) -> {
                arrayObj.add(mensajesObj);
                });
                int status = 0;
                if(items.isEmpty()){
                    status = 1;                
                }         
                jsonObject.addProperty("status", status);                
                jsonObject.add("pacient", arrayObj);
            }else{            
                jsonObject.addProperty("status", 1);               
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
        }
    }

}
