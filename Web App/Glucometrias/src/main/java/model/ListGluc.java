package model;

/**
 * This class is used to build List to store the records from Bloodglucose
 * Used in DAO.java, DateFilterGlucose.java, DetailPacientServlet.java, ListGluc.java
 *         ListGlucose.java and ValueFilterGlucose.java
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class ListGluc {
    private String subject;
    private String performer;
    private String value;
    private String code;
    private String issued;
    private String state;

    /**
     *
     * @return
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     * @return
     */
    public String getSubject() {
        return subject;
    }

    /**
     *
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     *
     * @return
     */
    public String getPerformer() {
        return performer;
    }

    /**
     *
     * @param performer
     */
    public void setPerformer(String performer) {
        this.performer = performer;
    }

    /**
     *
     * @return
     */
    public String getValueQuantity() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValueQuantity(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return
     */
    public String getIssued() {
        return issued;
    }

    /**
     *
     * @param issued
     */
    public void setIssued(String issued) {
        this.issued = issued;
    }

    
    
    
    
}
