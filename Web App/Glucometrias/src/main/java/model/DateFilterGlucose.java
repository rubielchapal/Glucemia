package model;

import Dao.DAO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * Used when a Pacient or Personal want to know records from a certain day
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class DateFilterGlucose extends HttpServlet {

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
        String flag = request.getParameter("flag");
        DAO dao = new DAO();
        Gson gson = new Gson();
        JsonObject jsonObject =  new JsonObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //Declare params utilities
        Date mDate;
        Calendar cal;
        int year, month, day, dd;
        String mMonth, mDay;
        try {
            dao.conectar(); //Start communication with BD
            JsonArray arrayObj = new JsonArray();
            if(dao.isPatient(ndivalue)){
                //Create a List to store all records
                List<ListGluc> items = dao.DateFilterGlucose(ndivalue, date);
                int status = 1;
                int i = 0;
                mDate = sdf.parse(date);
                cal = Calendar.getInstance();
                cal.setTime(mDate);
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);                     
                dd = day;
                if(flag.contentEquals("0")){
                    if(!date.contentEquals("2016-01-01")){
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
                            if(date.contentEquals("2016-01-01")){
                                items = emptygluc();
                            }else{
                                items = dao.DateFilterGlucose(ndivalue, date);                    
                            }
                            i++;
                        }
                    }else{
                        status = 0;
                    }
                }else if(flag.contentEquals("1")){
                    if(items.isEmpty()){
                        status = 0;
                    }
                }                
                //Lambda expression: List records -> Json               
                items.stream().map((mensajes) -> gson.toJsonTree(mensajes)).forEach((mensajesObj) -> {
                arrayObj.add(mensajesObj);
                });
                //Construct the params for the JSON response
                jsonObject.addProperty("status", status);                
                jsonObject.addProperty("date", date); 
                jsonObject.add("obs_glucose", arrayObj);
            }else{            
                jsonObject.addProperty("status", 2);               
            }            
            response.getWriter().println(jsonObject); 
            dao.desconectar(); //End communication with BD
        } catch (SQLException | ClassNotFoundException | IOException ex){
            jsonObject.addProperty("error", ex.toString());            
            response.getWriter().write(jsonObject.toString());
        } catch (ParseException ex) {
            Logger.getLogger(ListGlucose.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private List<ListGluc> emptygluc(){  
        List<ListGluc> lista = new ArrayList<>();
        ListGluc list = new ListGluc();
            list.setValueQuantity("value");
            list.setIssued("time");
            list.setPerformer("performer");
            list.setCode("code");
            list.setState("comments");              
            lista.add(list);
            return lista;
    }
    
}