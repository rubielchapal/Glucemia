package entities;
import java.io.Serializable;

/**
 * class data glucose
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class Glucose  implements Serializable{
                
    private String pacient;
    private String date;
    private String personal;
    private String data;
    private String state;    

    /**
     * @return the pacient
     */
    public String getPacient() {
        return pacient;
    }

    /**
     * @param pacient the pacient to set
     */
    public void setPacient(String pacient) {
        this.pacient = pacient;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the personal
     */
    public String getPersonal() {
        return personal;
    }

    /**
     * @param personal the personal to set
     */
    public void setPersonal(String personal) {
        this.personal = personal;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }
                
}
