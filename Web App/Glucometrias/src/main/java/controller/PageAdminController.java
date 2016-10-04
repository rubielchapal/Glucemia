package controller;

import Dao.DAO;
import entities.Admin;
import entities.Patient;
import entities.Personal;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import model.JsonDataPatient;
import model.JsonDataPersonal;
import org.primefaces.context.RequestContext;

/**
 * class to control actions user root
 *
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
@ManagedBean
@ViewScoped
public class PageAdminController implements Serializable {

    private Admin admin = new Admin();
    private Personal personal = new Personal();
    private Patient patient = new Patient();
    private String deleteUserPersonal;
    private String deleteUserPatient;
    private String confirPassword;

    /**
     *
     */
    public PageAdminController() {

    }

    /**
     * @return the admin
     */
    public Admin getAdmin() {
        return admin;
    }

    /**
     * @param admin the admin to set
     */
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    /**
     * get data user root
     */
    public void datosAdmin() {
        DAO d = new DAO();

        try {
            d.conectar();
            setAdmin(d.getRoot());
            d.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }
    }

    /**
     * redirect to page insert patient
     *
     * @throws IOException
     */
    public void insertPatientFromAdmin() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pageAdminInsertPatient.xhtml");
    }

    /**
     * redirect to page insert personal
     *
     * @throws IOException
     */
    public void insertPersonalFromAdmin() throws IOException {

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pageAdminInsertPersonal.xhtml");
    }

    /**
     * search user Personal from database
     * @param ndipersonal document of user Personal 
     * @throws IOException
     */
    public void searchPersonal(String ndipersonal) throws IOException {
        DAO d = new DAO();
        try {
            d.conectar();
            if (d.isPersonal(ndipersonal)) {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/pageAdminUpdatePersonal.xhtml");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta:", "No existe el usuario de personal"));
            }
            d.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }
    }

    /**
     * search user patient from database
     * @param ndipaciente document of user patient 
     * @throws IOException
     */
    public void searchPaciente(String ndipaciente) throws IOException {
        DAO d = new DAO();
        try {
            d.conectar();
            if (d.isPatient(ndipaciente)) {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/pageAdminUpdatePatient.xhtml");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta:", "No existe el paciente"));
            }
            d.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }
    }

    /**
     * redirect to page update admin
     * @throws IOException
     */
    public void redirectpageAdminUpdateRoot() throws IOException {

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pageAdminUpdateRoot.xhtml");
    }

    /**
     *
     * @throws IOException
     */
    public void redirectpageAdmin() throws IOException {

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pageAdmin.xhtml");
    }

    /**
     * redirect to page admin
     *
     * @throws IOException
     */
    public void pageAdmin() throws IOException {

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pageAdmin.xhtml");
    }

    /**
     * set data user Personal from database
     * @param personal
     */
    public void datosPersonal(String personal) {
        DAO d = new DAO();

        try {
            d.conectar();
            setPersonal(d.getDataPersonal(personal));
            d.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }
    }

    /**
     * set data user patient from database
     * @param patien
     */
    public void datosPatient(String patien) {
        DAO d = new DAO();
        try {
            d.conectar();
            setPatient(d.getPatient(patien));
            d.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }
    }

    /**
     * update data user Admin on database
     */
    public void updateAdmin() {
        DAO d = new DAO();
        RequestContext context = RequestContext.getCurrentInstance();

        try {
            d.conectar();
            d.updateRoot(getAdmin().getName(), getAdmin().getNdi(), getAdmin().getNdivalue(), getAdmin().getPhone(), getAdmin().getAddress(), getAdmin().getEmail(), getAdmin().getPassword());
            d.desconectar();
            context.execute("PF('dialog').show();");

        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }
    }

    /**
     * update data user personal on database
     * @param ndiValueOld
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void updatePersonal(String ndiValueOld) throws SQLException, ClassNotFoundException {
        DAO d = new DAO();
        RequestContext context = RequestContext.getCurrentInstance();
        JsonDataPersonal jsonDataPersonal = new JsonDataPersonal();
        jsonDataPersonal = jsonDataPersonal.generateJsonDataPersonal(getPersonal());

        try {
            d.conectar();
            d.updatePersonal(jsonDataPersonal, ndiValueOld);
            d.updatePersonalOnGlucose(jsonDataPersonal.getPerformer(), ndiValueOld);
            d.desconectar();
            context.execute("PF('dialog').show();");
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }
    }

    /**
     * update data user patient on database
     * @param ndiValueOld
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void updatePatient(String ndiValueOld) throws SQLException, ClassNotFoundException {
        DAO d = new DAO();
        RequestContext context = RequestContext.getCurrentInstance();
        JsonDataPatient jsonDataPatient = new JsonDataPatient();
        jsonDataPatient = jsonDataPatient.generateJsonDataPatient(getPatient());

        try {
            d.conectar();
            d.updatePatient(jsonDataPatient, ndiValueOld);
            d.updatePatientOnGlucose(jsonDataPatient.getSubject(), ndiValueOld);
            d.desconectar();
            context.execute("PF('dialog').show();");
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }
    }

    /**
     * confirm dialog if admin want to delete user patient
     */
    public void confirmDeletePatient() {
        DAO d = new DAO();
        try {
            d.conectar();
            if (d.isPatient(getDeleteUserPatient())) {
                RequestContext context = RequestContext.getCurrentInstance();
                              
                context.execute("PF('dialogPatient').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El paciente: " + getDeleteUserPatient() + ", no existe"));               
            }
            d.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }

    }

    /**
     * confirm dialog if admin want to delete user Personal
     */
    public void confirmDeletePersonal() {
        DAO d = new DAO();
        try {
            d.conectar();
            if (d.isPersonal(getDeleteUserPersonal())) {
                RequestContext context = RequestContext.getCurrentInstance();
               
                context.execute("PF('dialogPersonal').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El usuario personal " + getDeleteUserPersonal() + ", no existe"));
             
            }
            d.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }

    }

    /**
     * verify password Admin and delete user Patient
     * @param passwordSession
     */
    public void deletePatient(String passwordSession) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dialogPatient').hide();");
        if (getConfirPassword().equals(passwordSession)) {
            DAO d = new DAO();
            try {
                d.conectar();
                d.deletePatient(getDeleteUserPatient());
                d.deleteGlucose(getDeleteUserPatient());
                d.desconectar();

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "El paciente: " + getDeleteUserPatient() + ", Fué eliminado"));
            } catch (SQLException | ClassNotFoundException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Credenciales inválidas"));
        }
        
        setConfirPassword("");
    }

    /**
     * verify password Admin and delete user Personal
     * @param passwordSession
     */
    public void deletePersonal(String passwordSession) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dialogPersonal').hide();");
        if (getConfirPassword().equals(passwordSession)) {
            DAO d = new DAO();
            try {
                d.conectar();
                d.deletePersonal(getDeleteUserPersonal());
                d.desconectar();

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "El usuario: " + getDeleteUserPersonal() + ", Fué eliminado"));
            } catch (SQLException | ClassNotFoundException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Credenciales inválidas"));
        }
     
        setConfirPassword("");

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

    /**
     * @return the confirPassword
     */
    public String getConfirPassword() {
        return confirPassword;
    }

    /**
     * @param confirPassword the confirPassword to set
     */
    public void setConfirPassword(String confirPassword) {
        this.confirPassword = confirPassword;
    }

    /**
     * @return the deleteUserPersonal
     */
    public String getDeleteUserPersonal() {
        return deleteUserPersonal;
    }

    /**
     * @param deleteUserPersonal the deleteUserPersonal to set
     */
    public void setDeleteUserPersonal(String deleteUserPersonal) {
        this.deleteUserPersonal = deleteUserPersonal;
    }

    /**
     * @return the deleteUserPatient
     */
    public String getDeleteUserPatient() {
        return deleteUserPatient;
    }

    /**
     * @param deleteUserPatient the deleteUserPatient to set
     */
    public void setDeleteUserPatient(String deleteUserPatient) {
        this.deleteUserPatient = deleteUserPatient;
    }
}
