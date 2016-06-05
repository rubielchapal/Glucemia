/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

import Models.DAO;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Dell
 */
public class LoginServlet extends HttpServlet {

    

    
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
         //Usuario y contraseña desde Android
        String ndivalue = request.getParameter("ndivalue");
        DAO d = new DAO();
        JsonObject jsonObject =  new JsonObject();
        try { //Reviso quien requiere logueo            
            d.conectar();            
            if(d.isPatient(ndivalue)){
                jsonObject.addProperty("status", 0);
            }
            else if (d.isPersonal(ndivalue)){
                jsonObject.addProperty("status", 1);
            }
            else{
                jsonObject.addProperty("status", 2);                
            }
            response.getWriter().write(jsonObject.toString());
        } catch (SQLException | ClassNotFoundException | IOException ex){
            jsonObject.addProperty("error", ex.toString());            
            response.getWriter().write(jsonObject.toString());
        }
    }


}
