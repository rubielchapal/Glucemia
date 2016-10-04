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
 *
 * @author Dell
 */
public class DateFilterGlucose extends HttpServlet {

    
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
        String date = request.getParameter("date");
        //String flag = request.getParameter("flag");
        DAO dao = new DAO();
        Gson gson = new Gson();
        JsonObject jsonObject =  new JsonObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date mDate;
        Calendar cal;
        int year, month, day, dd;
        String mMonth = null, mDay = null;
        try {
            dao.conectar();
            //List<Device> products = dao.findDevice(params);
            JsonArray arrayObj = new JsonArray();
            if(dao.isPatient(ndivalue)){
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
                while(items.isEmpty()){
                    if(dd == 0){
                        i = 0;
                        dd = 31;
                        day = 31;
                    }else{
                        dd = day - i;
                    }
                    if(dd == 31){
                        //dd = 0;
                        //i = 0;
                        mMonth = converted(month);
                        mDay = converted(dd);
                        date = year+"-"+mMonth+"-"+mDay;  
                        month = month - 1;
                    }else{
                        mMonth = converted(month+1); 
                        mDay = converted(dd);
                        date = year+"-"+mMonth+"-"+mDay;                        
                    }
                    /*mDate = sdf.parse(date);
                    cal = Calendar.getInstance();
                    cal.setTime(mDate);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH); */                  
                    items = dao.DateFilterGlucose(ndivalue, date);                    
                    i++;
                }
                /*/if(status == 1){
                    mDate = sdf.parse(date);
                    cal = Calendar.getInstance();
                    cal.setTime(mDate);
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH)+1;
                    day = cal.get(Calendar.DAY_OF_MONTH); 
                    mMonth = converted(month+1);
                    mDay = converted(day);*/
                    
                    /*List<ListGluc> asd = dao.DateFilterGlucose(ndivalue, date);
                    asd.stream().map((mensajes) -> gson.toJsonTree(mensajes)).forEach((mensajesObj) -> {
                    arrayObj.add(mensajesObj);
                    });
                    if(items.isEmpty()){
                    status = 1;                
                    }else{
                        status = 0;
                    }
                }*/                    
                items.stream().map((mensajes) -> gson.toJsonTree(mensajes)).forEach((mensajesObj) -> {
                arrayObj.add(mensajesObj);
                });
                jsonObject.addProperty("status", status);                
                jsonObject.addProperty("date", year+"-"+mMonth+"-"+mDay); 
                jsonObject.add("obs_glucose", arrayObj);
            }else{            
                jsonObject.addProperty("status", 2);               
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
            //response.getWriter().write(jsonObject.toString());
            //int Flag = Integer.valueOf(flag);
            response.getWriter().println(jsonObject);
            /*if(Flag == 0){
                request.setAttribute("result", arrayObj);
                request.getRequestDispatcher("graph.jsp").forward(request, response);
            }*/
            dao.desconectar();
        } catch (SQLException | ClassNotFoundException | IOException ex){
            jsonObject.addProperty("error", ex.toString());            
            response.getWriter().write(jsonObject.toString());
        } catch (ParseException ex) {
            Logger.getLogger(ListGlucose.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String converted(int date){
        String s = ""+date;
        if(date < 10){
               s = "0"+s;
        }
        return s;
    }
    
}