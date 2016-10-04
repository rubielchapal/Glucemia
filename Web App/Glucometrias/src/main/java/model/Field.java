package model;

/**
 * This class is used to build the notification's body according to FireBase
 * @see https://firebase.google.com/docs/cloud-messaging/android/client
 * Called in RegisterGlucose.java
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class Field {
    
    String to;
    String priority;   
    Notification notification; 
    DataField data;
    
    /**
     *
     * @return
     */
    public String getPriority() {return priority;}

    /**
     *
     * @param priority
     */
    public void setPriority(String priority) {this.priority = priority;}

    /**
     *
     * @return
     */
    public Notification getNotification() {return notification;}

    /**
     *
     * @param notification
     */
    public void setNotification(Notification notification) {this.notification = notification;}
    
    /**
     *
     * @return
     */
    public DataField getData() {return data;}

    /**
     *
     * @param data
     */
    public void setData(DataField data) {this.data = data;}
    
    /**
     *
     * @return
     */
    public String getTo() {return to;}

    /**
     *
     * @param to
     */
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
