package model;

/**
 * This class is used to build List to store the personal,
 * used when a pacient add new record of glucose in AndroidApp and notify all the
 * personal interested
 * Used in DAO.java and RegisterGlucose.java
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class ListPersonal {
    private String id;

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
