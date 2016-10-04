/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Dao.DAO;
import entities.Personal;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.JsonDataPersonal;
import org.primefaces.context.RequestContext;

/**
 * class to register user Personal on database
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
@ManagedBean
@ViewScoped
public class RegisterPersonalController implements Serializable {

    private Personal personal = new Personal();

    /**
     * register user Personal on database
     */
    public void createPersonal() {

        DAO d = new DAO();

        try {
            d.conectar();
            if (!d.isPersonal(getPersonal().getNdivalue())) {

                JsonDataPersonal jsonDataPersonal = new JsonDataPersonal();
                jsonDataPersonal = jsonDataPersonal.generateJsonDataPersonal(getPersonal());

                d.registerPersonal(
                        jsonDataPersonal.getIdentifier(),
                        jsonDataPersonal.getPassword(),
                        jsonDataPersonal.getName(),
                        jsonDataPersonal.getTelecom(),
                        jsonDataPersonal.getGender(),
                        jsonDataPersonal.getBirthDate(),
                        jsonDataPersonal.getAddress(),
                        jsonDataPersonal.getPractitionerRole(),
                        jsonDataPersonal.getToken()
                );

                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('dialog').show();");
            } else {

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: ", "El usuario ya existe"));
            }

        } catch (SQLException ex) {
 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: ", ex.toString()));
           
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RegisterPatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the personal
     */
    public Personal getPersonal() {
        return personal;
    }

    /**
     * @param personal the personal to set
     */
    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

}
