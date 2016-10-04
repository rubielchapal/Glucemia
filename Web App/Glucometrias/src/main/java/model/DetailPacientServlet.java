package model;

import Dao.DAO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet hows take the http request from Mobile Application and send the response
 * Used when a Personal select a Pacient to know detailed information
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class DetailPacientServlet extends HttpServlet {    
    
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        //Data from Android App     
        String ndivalue = request.getParameter("user");
        String date = request.getParameter("date");
        DAO dao = new DAO();
        Gson gson = new Gson();
        JsonObject jsonObject =  new JsonObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //Declare params utilities
        Date mDate;
        Calendar cal;
        int year, month, day, dd;
        String mDay, mMonth;
        try {
            dao.conectar(); //Start communication with BD
            JsonArray arrayObj = new JsonArray(); 
            JsonArray aob = new JsonArray();
            if(dao.isPatient(ndivalue)){
                String data[] = dao.getDetail(ndivalue);
                JsonObject job = new JsonObject();              
                /* List to search all records
                *Lambda expression: List records -> Json*
                List<ListGluc> items = dao.listGlucose(ndivalue);
                items.stream().map((mensajes) -> gson.toJsonTree(mensajes)).forEach((mensajesObj) -> {
                arrayObj.add(mensajesObj);
                });*/             
                //Create a List to store all records
                List<ListGluc> items = dao.DateFilterGlucose(ndivalue, date);
                int i = 0;
                mDate = sdf.parse(date);
                cal = Calendar.getInstance();
                cal.setTime(mDate);
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);                     
                dd = day;
                //Search the records from the actual day or the nearest
                while(items.isEmpty()){
                    if(dd == 0){
                        i = 0;
                        dd = 31;
                        day = 31;
                    }else{
                        dd = day - i;
                    }
                    if(dd == 31){
                        mMonth = converted(month);
                        mDay = converted(dd);
                        date = year+"-"+mMonth+"-"+mDay;  
                        month = month - 1;
                    }else{
                        mMonth = converted(month+1); 
                        mDay = converted(dd);
                        date = year+"-"+mMonth+"-"+mDay;                        
                    }                 
                    items = dao.DateFilterGlucose(ndivalue, date);                    
                    i++;
                }
                //Lambda expression: List records -> Json               
                /*items.stream().map((mensajes) -> gson.toJsonTree(mensajes)).forEach((mensajesObj) -> {
                arrayObj.add(mensajesObj);
                });*/                
                job.addProperty("nombre", data[0]);
                job.addProperty("email", data[1]);
                job.addProperty("birthdate", data[2]);
                job.addProperty("age", data[3]);
                job.addProperty("blood", data[4]);                
                job.addProperty("last_record", date);
                aob.add(job);
                jsonObject.addProperty("status", 0);  
                jsonObject.add("detail",aob);  
            }else{            
                jsonObject.addProperty("status", 1);               
            }
            response.getWriter().write(jsonObject.toString());
            dao.desconectar();
        } catch (SQLException | ClassNotFoundException | IOException ex){
            jsonObject.addProperty("status", 2);     
            response.getWriter().write(jsonObject.toString());
        } catch (ParseException ex) {
            Logger.getLogger(DetailPacientServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * @param date
     * @return Add 0 to one digit month or day
     */    
    private String converted(int date){
        String s = ""+date;
        if(date < 10){
               s = "0"+s;
        }
        return s;
    }
    
}
