package model;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class FormatDate {

    /**
     *
     * @return actual date format yyyy-MM-dd
     */
    public static String getFechaActual() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     *
     * @return actual hour format hh:mm
     */
    public static String getHoraActual() {
        Date hour = new Date();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        return format.format(hour);
    }
    
    /**
     * 
     * @return format date
     */
    public static String getFormatDate(){
        
        return getFechaActual()+"T"+getHoraActual();
    }
    
    /**
     * 
     * @return date and hour
     */
    public static String getFechayHoraView(){
        
        return getFechaActual()+" a las "+getHoraActual();
    }
    
 
    

}