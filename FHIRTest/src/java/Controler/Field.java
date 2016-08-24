/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

/**
 *
 * @author Alejandro Mora
 */
public class Field {
    
    String to;
    String priority;   
    Notification notification; 
    DataField data;
    
    public String getPriority() {return priority;}

    public void setPriority(String priority) {this.priority = priority;}

    public Notification getNotification() {return notification;}

    public void setNotification(Notification notification) {this.notification = notification;}
    
    public DataField getData() {return data;}

    public void setData(DataField data) {this.data = data;}
    
    public String getTo() {return to;}

    public void setTo(String to) {this.to = to;} 
    
}
class Notification {
    String title;
    String body;
    String sound;

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getBody() {return body;}

    public void setBody(String body) {this.body = body;}    

    public String getSound() {return sound;}

    public void setSound(String sound) {this.sound = sound;}
    
    
}

class DataField{
    String message;
    
    public void setMessage(String message){this.message = message;}
    
}
