package controller;

import Dao.DAO;
import entities.Patient;
import entities.Personal;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import model.ListGluc;
import model.FormatDate;

/**
 * class to control actions user Personal
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
@ManagedBean
@ViewScoped
public class PagePersonalController implements Serializable{

    private Personal personal = new Personal();
    private Patient patient = new Patient();
    private String fecha;
    private List<ListGluc> listaGlucose = new ArrayList<>();
    
    /**
     * set date
     * @throws ParseException
     */
    public PagePersonalController() throws ParseException {
        setFecha(FormatDate.getFechayHoraView());       
    }     

  
    /**
     * get data personal
     * @param ndivalue
     */
    public void datosPersonal( String ndivalue){
         DAO d = new DAO();

        try {          
            d.conectar();           
            setPersonal(d.getDataPersonal(ndivalue));        
            d.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }
    }    
  
    /**
     * get data patient from database
     * @param ndivalue
     */
    public void datosPaciente(String ndivalue){
         DAO d = new DAO();
        try {          
            d.conectar();
            setPatient(d.getPatient(ndivalue));                    
            d.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }
    }
    
    /**
     * search patient on database
     * @param ndipaciente
     * @throws IOException
     */
    public void searchPaciente(String ndipaciente) throws IOException{
         DAO d = new DAO();
        try {          
            d.conectar();           
            if(d.isPatient(ndipaciente)){
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pagePersonalViewPatient.xhtml");          
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta:" , "No existe el paciente"));
            }
            d.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }
    }
    
    /**
     * redirect to page patient table
     * @throws IOException
     */
    public void redirectPagePersonalViewPatientTable() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pagePersonalViewPatientTable.xhtml");          
    }
    
    /**
     * redirect to page personal
     * @throws IOException
     */
    public void redirectPagePersonal() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pagePersonal.xhtml");          
    }
    
    /**
     * redirect to page patient view
     * @throws IOException
     */
    public void redirectPagePersonalViewPatient() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pagePersonalViewPatient.xhtml");          
    }

    /**
     * redirect to page patient graphic
     * @throws IOException
     */
    public void redirectPagePersonalViewPatientGraphic() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pagePersonalViewPatientGraphic.xhtml");          
    }
    
    /**
     * redirect to page patient graphic time
     * @throws IOException
     */
    public void redirectPagePersonalViewPatientGraphicTime() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pagePersonalViewPatientGraphicTime.xhtml");          
    }
    
    /**
     * set data table glucose
     * @param ndivalue
     * @throws ParseException
     */
    public void showTable(String ndivalue) throws ParseException{
         DAO d = new DAO();
        try {          
            d.conectar();
            setListaGlucose(d.listGlucose(ndivalue));
            d.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
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

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the listaGlucose
     */
    public List<ListGluc> getListaGlucose() {
        return listaGlucose;
    }

    /**
     * @param listaGlucose the listaGlucose to set
     */
    public void setListaGlucose(List<ListGluc> listaGlucose) {
        this.listaGlucose = listaGlucose;
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
