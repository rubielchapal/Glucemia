/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Dao.DAO;
import entities.Patient;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.JsonDataPatient;
import org.primefaces.context.RequestContext;

/**
 * class to control actions  to register patient on database
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
@ManagedBean
@ViewScoped
public class RegisterPatientController implements Serializable{
    
    private Patient patient = new Patient();
    
    /**
     * register patient on database
     */
    public void createPatient(){

        DAO d = new DAO();


         try {
             d.conectar();
             if (!d.isPatient(getPatient().getNdivalue())) {
                 
                 JsonDataPatient jsonDataPatient  = new JsonDataPatient();
            jsonDataPatient = jsonDataPatient.generateJsonDataPatient(getPatient());
             
             d.registerPacient(
                     jsonDataPatient.getIdentifier(),
             jsonDataPatient.getPassword(),
             jsonDataPatient.getName(),
             jsonDataPatient.getTelecom(),
             jsonDataPatient.getGender(),
             jsonDataPatient.getBirthDate(),
             jsonDataPatient.getBloodType(),
             jsonDataPatient.getAddress(),
             jsonDataPatient.getMaritalStatus(),
             jsonDataPatient.getContact(),
             jsonDataPatient.getCommunication(),
             jsonDataPatient.getManagingOrganization(),
             jsonDataPatient.getToken()
             );
           RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('dialog').show();");
            } else {

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: ", "El paciente ya existe"));
            }
               
         }catch (SQLException ex) {
                       
           
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RegisterPatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    /**
     * @return the patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * @param patient the patient to set
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    
    
}
