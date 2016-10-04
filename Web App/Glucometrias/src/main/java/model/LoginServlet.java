package model;

import Dao.DAO;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet hows take the http request from Mobile Application and send the response
 * Used to handled login from AndroidApp
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class LoginServlet extends HttpServlet {

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
         //Data from Android
        String ndivalue = request.getParameter("user");
        String password = request.getParameter("password");
        DAO d = new DAO();
        JsonObject jsonObject =  new JsonObject();
        //Check patient table and personal table to compare with data 
            try {     
            d.conectar(); //Start connection with DB            
            if(d.findUser(ndivalue, password, "patient")){
                String name = d.getName(ndivalue, "patient");
                jsonObject.addProperty("status", 0);
                jsonObject.addProperty("name", name);
            }
            else if (d.findUser(ndivalue, password, "personal")){
                String name = d.getName(ndivalue, "personal");
                jsonObject.addProperty("status", 1);
                jsonObject.addProperty("name", name);
            }
            else{
                jsonObject.addProperty("status", 2);                
            }
            response.getWriter().write(jsonObject.toString());
            d.desconectar(); //End connection with DB
        } catch (SQLException | ClassNotFoundException | IOException ex){
            jsonObject.addProperty("error", ex.toString());            
            response.getWriter().write(jsonObject.toString());
        }
    }


}
