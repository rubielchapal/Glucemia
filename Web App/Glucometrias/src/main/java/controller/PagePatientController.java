package controller;
import Dao.DAO;
import entities.Patient;
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
import model.ListGraphic;

/**
 * class to control actions user Patient
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
@ManagedBean
@ViewScoped
public class PagePatientController implements Serializable {

    private Patient patient = new Patient();
    private List<ListGluc> listaGlucose = new ArrayList<>();
    private String fecha;
    private List<ListGraphic> grafico = new ArrayList<>();

    /**
     *
     */
    public PagePatientController() {
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
     * set data Patient from database
     * @param ndivalue
     */
    public void datosPaciente(String ndivalue) {
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
     * redirect to page patient
     * @throws IOException
     */
    public void redirectPagePatient() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pagePatient.xhtml");
    }
    
    /**
     * redirect to page graphic
     * @throws IOException
     */
    public void redirectPagePatientViewGraphicTime() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pagePatientViewGraphicTime.xhtml");
    }

    /**
     * redirect to page graphic
     * @throws IOException
     */
    public void redirectPagePatientViewGraphic() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pagePatientViewGraphic.xhtml");
    }

    /**
     * redirect to page with datatable
     * @throws IOException
     */
    public void redirectPagePatientViewTabla() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pagePatientViewTable.xhtml");
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
     * get glucose list
     * @param ndivalue
     * @throws ParseException
     */
    public void mostrarTabla(String ndivalue) throws ParseException {
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
     * @return the grafico
     */
    public List<ListGraphic> getGrafico() {
        return grafico;
    }

    /**
     * @param grafico the grafico to set
     */
    public void setGrafico(List<ListGraphic> grafico) {
        this.grafico = grafico;
    }

}
