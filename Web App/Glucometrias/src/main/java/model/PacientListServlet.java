package model;

 
import Dao.DAO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet hows take the http request from Mobile Application and send the response
 * Used to list the pacients stored in the DB
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class PacientListServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
    
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
        String ndivalue = request.getParameter("user");
        DAO dao = new DAO();
        Gson gson = new Gson();
        JsonObject jsonObject =  new JsonObject();
        try {
            dao.conectar(); //Start connection with DB
            JsonArray arrayObj = new JsonArray();
            if(dao.isPersonal(ndivalue)){              
                /* List to search all pacient
                *Lambda expression: List records -> Json*/
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
            response.getWriter().write(jsonObject.toString());
            dao.desconectar();
        } catch (SQLException | ClassNotFoundException | IOException ex){
            jsonObject.addProperty("error", ex.toString());            
            response.getWriter().write(jsonObject.toString());
        }
    }

}
