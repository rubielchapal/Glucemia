package model;

import Dao.DAO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
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
 * Servlet hows take the http request from Mobile Application and send the response
 * Used to list all records from the pacient store in the DB
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class ListGlucose extends HttpServlet {
    
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
        //Data from Android App        
        String ndivalue = request.getParameter("ndivalue");
        DAO dao = new DAO();
        Gson gson = new Gson();
        JsonObject jsonObject =  new JsonObject();
        try {
            dao.conectar(); //Start connection with DB
            JsonArray arrayObj = new JsonArray();
            if(dao.isPatient(ndivalue)){                         
                /* List to search all records
                *Lambda expression: List records -> Json*/
                List<ListGluc> items = dao.listGlucose(ndivalue);
                items.stream().map((mensajes) -> gson.toJsonTree(mensajes)).forEach((mensajesObj) -> {
                arrayObj.add(mensajesObj);
                });
                int status = 0;
                if(items.isEmpty()){
                    status = 1;                
                }         
                jsonObject.addProperty("status", status);                
                jsonObject.add("obs_glucose", arrayObj);
            }else{            
                jsonObject.addProperty("status", false);               
            }
            response.getWriter().write(jsonObject.toString());
            dao.desconectar(); //End connection with DB
        } catch (SQLException | ClassNotFoundException | IOException ex){
            jsonObject.addProperty("error", ex.toString());            
            response.getWriter().write(jsonObject.toString());
        } catch (ParseException ex) {
            Logger.getLogger(ListGlucose.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

}
