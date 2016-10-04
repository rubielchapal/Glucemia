
package model;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import entities.Personal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * create json data with Personal object
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class JsonDataPersonal {

    private JsonElement identifier;
    private String password;
    private JsonElement name;
    private JsonElement telecom;
    private String gender;
    private String birthDate;
    private JsonElement address;
    private JsonElement practitionerRole;
    private String token;
    private int id;
    
    private JsonElement performer;

    /**
     * generate json data with Personal object
     * @param personal
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public JsonDataPersonal generateJsonDataPersonal(Personal personal) throws SQLException, ClassNotFoundException {

        JsonDataPersonal jsonDataPersonal = new JsonDataPersonal();
        jsonDataPersonal.setBirthDate(personal.getBirthDate());
        jsonDataPersonal.setPassword(personal.getPassword());
        jsonDataPersonal.setGender(personal.getGender());
        jsonDataPersonal.setToken("");
        jsonDataPersonal.setId(personal.getId());
        

        Gson gson = new Gson();

        Identifier ID = new Identifier();
        DataIdentifier dataIdentifier = new DataIdentifier();
        dataIdentifier.setUse("official");
        dataIdentifier.setSystem("urn:oid:2.16.840.1.113883.2.17.6.3");
        dataIdentifier.setValue(personal.getNdivalue());
        dataIdentifier.setDisplay(personal.getNdi());
        ID.setDataIdentifier(new DataIdentifier[]{dataIdentifier});

        jsonDataPersonal.setIdentifier(gson.toJsonTree(ID));

        Name NAME = new Name();
        DataName dataName = new DataName();
        dataName.setUse("official");
        List<String> lastname = new ArrayList<>();
        lastname.add(personal.getFamily());
        List<String> dataname = new ArrayList<>();
        dataname.add(personal.getGiven());
        dataName.setFamily(lastname);
        dataName.setGiven(dataname);
        NAME.setDataName(new DataName[]{dataName});

        jsonDataPersonal.setName(gson.toJsonTree(NAME));

        Telecom TELECOM = new Telecom();
        DataTelecom dataMobile = new DataTelecom();
        dataMobile.setSystem("phone");
        dataMobile.setValue(personal.getTelmobile());
        dataMobile.setUse("mobile");

        DataTelecom dataWork = new DataTelecom();
        dataWork.setSystemW("phone");
        dataWork.setValueW(personal.getTelwork());
        dataWork.setUseW("work");

        DataTelecom dataEmail = new DataTelecom();
        dataEmail.setSystemE("email");
        dataEmail.setValueE(personal.getEmail());
        dataEmail.setUseE("home");
        TELECOM.setDataTelecom(new DataTelecom[]{dataMobile, dataWork, dataEmail});

        jsonDataPersonal.setTelecom(gson.toJsonTree(TELECOM));

        Address ADDRESS = new Address();
        DataAddress dataAddress = new DataAddress();
        dataAddress.setUse("home");
        List<String> lines = new ArrayList<>();
        lines.add(personal.getLine());
        dataAddress.setLine(lines);
        dataAddress.setCity(personal.getCity());
        dataAddress.setCountry("COL");
        ADDRESS.setDataAddress(new DataAddress[]{dataAddress});

        jsonDataPersonal.setAddress(gson.toJsonTree(ADDRESS));

        Rol ROL = new Rol();
        DataRol dataRol = new DataRol();
        DataCoding dataCoding = new DataCoding();
        dataCoding.setCode(personal.getRole());
        dataCoding.setSystem("http://snomed.info/sct");

        switch (personal.getRole()) {
            case "61894003":
                dataCoding.setDisplay("Endocrinologist");
                break;
            case "59058001":
                dataCoding.setDisplay("General Doctor");
                break;
            case "398130009":
                dataCoding.setDisplay("Medical Student");
                break;
            case "309446002":
                dataCoding.setDisplay("Nurse Manager");
                break;
            case "310182000":
                dataCoding.setDisplay("General Nurse");
                break;
        }
        
        DataRole dataRole = new DataRole();
        dataRole.setDataCod(new DataCoding[]{dataCoding});
        dataRol.setMo(personal.getManagingOrganization());
        dataRol.setRole(dataRole);
        ROL.setDataRol(new DataRol[]{dataRol});
        
        jsonDataPersonal.setPractitionerRole(gson.toJsonTree(ROL));
        
        Performer PERFORMER = new Performer();
        DataPerformer dataPerf = new DataPerformer();
        dataPerf.setReference(personal.getNdivalue());
        dataPerf.setDisplay(personal.getGiven()+" "+personal.getFamily());
        PERFORMER.setDataPerformer(new DataPerformer[]{dataPerf});
        jsonDataPersonal.setPerformer(gson.toJsonTree(PERFORMER));

        return jsonDataPersonal;

    }
    
   

    /**
     * @return the identifier
     */
    public JsonElement getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(JsonElement identifier) {
        this.identifier = identifier;
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

    /**
     * @return the name
     */
    public JsonElement getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(JsonElement name) {
        this.name = name;
    }

    /**
     * @return the telecom
     */
    public JsonElement getTelecom() {
        return telecom;
    }

    /**
     * @param telecom the telecom to set
     */
    public void setTelecom(JsonElement telecom) {
        this.telecom = telecom;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the birthDate
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate the birthDate to set
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * @return the address
     */
    public JsonElement getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(JsonElement address) {
        this.address = address;
    }

    /**
     * @return the practitionerRole
     */
    public JsonElement getPractitionerRole() {
        return practitionerRole;
    }

    /**
     * @param practitionerRole the practitionerRole to set
     */
    public void setPractitionerRole(JsonElement practitionerRole) {
        this.practitionerRole = practitionerRole;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the performer
     */
    public JsonElement getPerformer() {
        return performer;
    }

    /**
     * @param performer the performer to set
     */
    public void setPerformer(JsonElement performer) {
        this.performer = performer;
    }


}
