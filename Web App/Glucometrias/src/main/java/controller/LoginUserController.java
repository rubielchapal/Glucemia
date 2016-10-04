package controller;

import Dao.DAO;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * class to control actions to login and session
 *
 *@author GTST : Grupo de Tratamiento de Señales  y telecomunicaciones
 */
@ManagedBean
@SessionScoped
public class LoginUserController implements Serializable {

    private String ndivalue;
    private String password;
    private String userType = "";
    private String ndipaciente;

    /**
     *
     */
    public LoginUserController() {

    }

    /**
     * Login user and select page using credentials
     *
     * @throws IOException
     */
    public void loginUser() throws IOException {
        DAO d = new DAO();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

        try {
            d.conectar();
            if (getNdivalue().equalsIgnoreCase("root") && !d.existRoot()) {
                ec.redirect(ec.getRequestContextPath() + "/insertRoot.xhtml");
                setUserType("root");
            } else if (d.findUser(this.ndivalue, this.password, "patient")) {
                ec.redirect(ec.getRequestContextPath() + "/pagePatient.xhtml");
                setUserType("patient");
            } else if (d.findUser(this.ndivalue, this.password, "personal")) {
                ec.redirect(ec.getRequestContextPath() + "/pagePersonal.xhtml");
                setUserType("personal");
            } else if (d.isRoot(getNdivalue(), getPassword())) {
                ec.redirect(ec.getRequestContextPath() + "/pageAdmin.xhtml");
                setUserType("admin");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales inválidas", null));
            }
            d.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conexión fallida a la base de datos",""));
        }
    }
    
    /**
     * clean ndi patient from view
     */
    public void clean(){
        setNdipaciente(new String());
    }

    /**
     * if the user does not belong to the domain of the page, the session is
     * invalidated
     *
     * @param page current page
     * @throws IOException
     */
    public void validateSession(String page) throws IOException {
        if (!(getUserType().equals(page)) || getUserType() == null) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.invalidateSession();
            ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
        }
    }

    /**
     * Logout user and invalidate session
     *
     * @throws IOException
     */
    public void logoutUser() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
    }

    /**
     * redirect to recoveryPassword page
     *
     * @throws IOException
     */
    public void redirectRecoveryPassword() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        ec.redirect(ec.getRequestContextPath() + "/recoveryPassword.xhtml");
    } 
    

   
    /**
     * @return the userType
     */
    public String getUserType() {
        return userType;
    }

    /**
     * @param userType the userType to set
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * @return the ndipaciente
     */
    public String getNdipaciente() {
        return ndipaciente;
    }

    /**
     * @param ndipaciente the ndipaciente to set
     */
    public void setNdipaciente(String ndipaciente) {
        this.ndipaciente = ndipaciente;
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

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
