

package controller;
import Dao.DAO;
import entities.Glucose;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.JsonDataGlucose;



/**
 * class to control actions  to register glucose on database
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
@ManagedBean
@ViewScoped
public class RegisterGlucoseController  implements Serializable{
 
     private String unit;
    
    private Glucose glucose = new Glucose();


    

    
    /**
     * register data glucose on database
     */
    public void crearGlucose(){    
        
        DAO d = new DAO();
        
        if(getUnit().equals("mg/dl")){
            float as = Float.parseFloat(getGlucose().getData())/18;
            String data = Float.toString(as);
            glucose.setData(data);
            
        }


         try {
             JsonDataGlucose jsonDataGlucose = new JsonDataGlucose();           
             jsonDataGlucose = jsonDataGlucose.generateJsonDataGlucose(getGlucose());
             d.conectar();
             d.registerGlucose(
                     jsonDataGlucose.getIdentifier(),
                     jsonDataGlucose.getCode(),
                     jsonDataGlucose.getSubject(),
                     jsonDataGlucose.getIssued(),
                     jsonDataGlucose.getPerformer(),
                     jsonDataGlucose.getValueQuantity(),
                     jsonDataGlucose.getInterpretation(),
                     jsonDataGlucose.getComments(),
                     jsonDataGlucose.getReferenceRange()
             );        
           
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Guardado ","exitosamente"));
                
         }catch (SQLException ex) {
                       
           
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RegisterGlucoseController.class.getName()).log(Level.SEVERE, null, ex);
        }         
       glucose.setData("");
    }

    /**
     *
     */
    public RegisterGlucoseController() {
    }
    


    /**
     * @return the glucose
     */
    public Glucose getGlucose() {
        return glucose;
    }

    /**
     * @param glucose the glucose to set
     */
    public void setGlucose(Glucose glucose) {
        this.glucose = glucose;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
}


