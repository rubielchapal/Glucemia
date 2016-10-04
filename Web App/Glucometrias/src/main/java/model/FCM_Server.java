package model;

import Dao.DAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to update Token. Called always when AndroidApp is open.
 * Compare the Token send with the Token store, if are different update the new value
 * Token = Unique ID to handle push notification using FireBase
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class FCM_Server extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private String mToken, mUser;
    Gson gson = new Gson();
    DAO dao = new DAO();  
    
    /**
     *
     */
    public FCM_Server() {super();}
    
    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
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
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {       
        mUser = request.getParameter("document");  
        mToken = request.getParameter("token");       
        JsonObject jsonObject =  new JsonObject();
        jsonObject.addProperty("status", Update(mToken, mUser));
        response.getWriter().write(jsonObject.toString());
    }
    
    /**
     * @param mToken String Token send by App
     * @param mUser String the document from the pacient or personal who open the App
     * @return the comparation from the Token in DB and the Tokend send 
     */
    private String Update(String mToken, String mUser) {
        String actToken;
        String response = "Same Token";
        try {
            dao.conectar();
            if(dao.isPatient(mUser)){
                actToken = dao.getToken(mUser, "patient");                 
                if(!mToken.equalsIgnoreCase(actToken)){
                    dao.updateToken(mToken, mUser, "patient");
                    response = "Update !";
                }
            }
            else if (dao.isPersonal(mUser)){
                actToken = dao.getToken(mUser, "personal");                  
                if(!mToken.equalsIgnoreCase(actToken)){
                    dao.updateToken(mToken, mUser, "personal");
                    response = "Update !";
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            response = ex.toString();
            Logger.getLogger(FCM_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

}
