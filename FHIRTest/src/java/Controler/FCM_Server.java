/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

import Models.DAO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
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
public class FCM_Server extends HttpServlet {

    private static final long serialVersionUID = 1L;
    // Put your Google API Server Key here
    private static final String GOOGLE_SERVER_KEY = "key=AIzaSyDLYEuF7LsSj_g-nrwytWqlwNYzvPK7STE"; //Server key got from Google developer console
    static final String MESSAGE_KEY = "message";
    private static final String URL_FIREBASE = "https://fcm.googleapis.com/fcm/send";
    private String mTitle, mBody, mTo, mToken, mFrom;
    Gson gson = new Gson();
    DAO dao = new DAO();  
    
    public FCM_Server() {super();}

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {        
        
        int mFlag = Integer.parseInt(request.getParameter("flag"));
        if(mFlag == 0){
            mFrom = request.getParameter("from");  
            mToken = request.getParameter("token");        
            response.getWriter().write(Update(mToken, mFrom));
        }else if(mFlag == 1){
            mTitle = request.getParameter("title");        
            mBody = request.getParameter("body");
            mTo = request.getParameter("to");  
            response.getWriter().write(Send(mTitle, mBody, mTo));
        } 
    }

    private String Send(String mTitle, String mBody, String mTo) {
        String response = null;
        try {
            dao.conectar();
            String string = null;
            if(mTitle.equalsIgnoreCase("") || mBody.equalsIgnoreCase("") || mTo.equalsIgnoreCase("")){                
                System.out.println("Vacio!");
                response = "Nada";
            }else{
                if(dao.isPatient(mTo)){
                    string = dao.getToken(mTo, "patient");
                }
                else if (dao.isPersonal(mTo)){
                    string = dao.getToken(mTo, "personal");
                }
                Field FIELD = new Field();
                FIELD.setTo(string);
                //FIELD.setTo("doiOzhpA-eQ:APA91bEN4yHZ5d1RnTpu6jD_yIhjzo2hBzdt_F7KbCwjiMTY5KqXy7TpXuzgJ9aAhe5Lk_h9kgeM1dW62ynYWtaHB6mStGzpJJC-i-nQ7kc8endB_px4s9XG5YhB5FE5Yw1WdDvaYdkj");
                //FIELD.setTo("eaZUTgfQng8:APA91bGnlOEhg0RdDOAt6TNPyxArfxDhCcTVTNszYAMF8Ir7tFppoSz8yPLxa2Czffb2nIiL4MmwK0afLAbKAuWxCHvyIVDjaQm_AD2C6_-FrfHV1Xhu8uPWwxg6jBl1lYCbtcvIvSdP");
                FIELD.setPriority("high");        
                Notification NOTIFICATION = new Notification();
                NOTIFICATION.setBody(mBody);
                NOTIFICATION.setTitle(mTitle);
                NOTIFICATION.setSound("mysound");
                FIELD.setNotification(NOTIFICATION);
                DataField DATAFIELD = new DataField();
                DATAFIELD.setMessage("hola");
                FIELD.setData(DATAFIELD);
                JsonElement name = gson.toJsonTree(FIELD);
                //response.getWriter().write(name.toString());
                HttpURLConnection httpcon = (HttpURLConnection) ((new URL(URL_FIREBASE).openConnection()));
                httpcon.setDoOutput(true);
                httpcon.setRequestProperty("Content-Type", "application/json");
                //httpcon.setRequestProperty("Authorization", "key=AIzaSyDLYEuF7LsSj_g-nrwytWqlwNYzvPK7STE");
                httpcon.setRequestProperty("Authorization", GOOGLE_SERVER_KEY);
                httpcon.setRequestMethod("POST");
                httpcon.connect();
                System.out.println("Connected!");
                //byte[] outputBytes = "{\"notification\":{\"title\": \"My title\", \"text\": \"My text\", \"sound\": \"default\"}, \"to\": \"cAhmJfN...bNau9z\"}".getBytes("UTF-8");
                byte[] outputBytes = name.toString().getBytes("UTF-8");
                OutputStream os = httpcon.getOutputStream();
                os.write(outputBytes);
                os.close();
                // Reading response
                InputStream input = httpcon.getInputStream();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                for (String line; (line = reader.readLine()) != null;) {
                    System.out.println(line);
                    response = line;
                    //response.getWriter().write(line);
                    }
                }
            }            
        } catch (SQLException ex) {
            Logger.getLogger(FCM_Server.class.getName()).log(Level.SEVERE, null, ex);
            response = ex.toString();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FCM_Server.class.getName()).log(Level.SEVERE, null, ex);
            response = ex.toString();
        } catch (MalformedURLException ex) { 
            Logger.getLogger(FCM_Server.class.getName()).log(Level.SEVERE, null, ex);
            response = ex.toString();
        } catch (IOException ex) {
            Logger.getLogger(FCM_Server.class.getName()).log(Level.SEVERE, null, ex);
            response = ex.toString();
        }
        return response;
    }

    private String Update(String mToken, String mTo) {
        String actToken;
        String response = "Same Token";
        try {
            dao.conectar();
            if(dao.isPatient(mTo)){
                actToken = dao.getToken(mTo, "patient");                 
                if(!mToken.equalsIgnoreCase(actToken)){
                    dao.updateToken(mToken, mTo, "patient");
                    response = "Update !";
                }
            }
            else if (dao.isPersonal(mTo)){
                actToken = dao.getToken(mTo, "personal");                  
                if(!mToken.equalsIgnoreCase(actToken)){
                    dao.updateToken(mToken, mTo, "personal");
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
