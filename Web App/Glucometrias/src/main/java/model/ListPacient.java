package model;

/**
 * This class is used to build List to store the pacients,
 * used when a personal logged in AndroidApp to show the pacient whos care
 * Call in DAO.java and PacientListServlet.java
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class ListPacient {
    private String subject;
    private String id;

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

   
    
}
