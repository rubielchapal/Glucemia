package controller;

import Dao.DAO;
import entities.Admin;
import entities.Patient;
import entities.Personal;
import java.io.Serializable;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.SendMail;
import org.primefaces.context.RequestContext;

/**
 * class to control actions to recover password
 *
 *@author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
@ManagedBean
@ViewScoped
public class RecoverPasswordController implements Serializable {

    private String ndivalue;

    /**
     *
     */
    public RecoverPasswordController() {

    }      

    /**
     * send email to recover password
     */
    public void recoveryPassword() {
        DAO d = new DAO();
        SendMail sendmail = new SendMail();
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            d.conectar();
            if (d.isPatient(getNdivalue())) {
                Patient patient = d.getPatient(getNdivalue());
                sendmail.sendMail(patient.getEmail(), patient.getPassword(), patient.getGiven());               
                context.execute("PF('dialog').show();");
            } else if (d.isPersonal(getNdivalue())) {
                Personal personal = d.getDataPersonal(getNdivalue());
                sendmail.sendMail(personal.getEmail(), personal.getPassword(), personal.getGiven());
                context.execute("PF('dialog').show();");
                
            } else if (d.istRoot(getNdivalue())) {
                Admin admin = d.getRoot();
                sendmail.sendMail(admin.getEmail(), admin.getPassword(), admin.getName());
                context.execute("PF('dialog').show();");
               
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario no existe", ""));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }
    }   

    /**
     * @return the ndivalue
     */
    public String getNdivalue() {
        return ndivalue;
    }

    /**
     * @param ndivalue the ndivalue to set
     */
    public void setNdivalue(String ndivalue) {
        this.ndivalue = ndivalue;
    }    

}
