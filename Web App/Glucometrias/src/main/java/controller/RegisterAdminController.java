package controller;

import Dao.DAO;
import entities.Admin;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 * class to register admin on database
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
@ManagedBean
@ViewScoped
public class RegisterAdminController implements Serializable {

    /**
     * class data user root
     */
    public Admin admin = new Admin();

    /**
     *
     */
    public RegisterAdminController() {
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
     * register user root on database
     */
    public void crearAdmin() {
        DAO dao = new DAO();
        try {
            dao.conectar();
            dao.registrerRoot(getAdmin().getName(), getAdmin().getNdi(), getAdmin().getNdivalue(), getAdmin().getPhone(), getAdmin().getAddress(), getAdmin().getEmail(), getAdmin().getPassword());
            dao.desconectar();
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dialog').show();");
        } catch (SQLException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.toString()));
        } catch (ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.toString()));
            Logger.getLogger(RegisterAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
