
package model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import entities.Patient;
import java.util.ArrayList;
import java.util.List;

/**
 *  create json data with Patient object 
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class JsonDataPatient {

    private JsonElement identifier;
    private String password;
    private JsonElement name;
    private JsonElement telecom;
    private String gender;
    private String birthDate;
    private String bloodType;
    private JsonElement address;
    private JsonElement maritalStatus;
    private JsonElement contact;
    private JsonElement communication;
    private String managingOrganization;
    private String token;
    private int id;

    private JsonElement subject;

    /**
     * generate json data from  Patient object
     * @param patient
     * @return
     */
    public JsonDataPatient generateJsonDataPatient(Patient patient) {

        JsonDataPatient jsonDataPatient = new JsonDataPatient();

        jsonDataPatient.setPassword(patient.getPassword());
        jsonDataPatient.setGender(patient.getGender());
        jsonDataPatient.setBirthDate(patient.getBirthDate());
        jsonDataPatient.setBloodType(patient.getBlood());
        jsonDataPatient.setManagingOrganization(patient.getManagingOrganization());
        jsonDataPatient.setToken("");
        jsonDataPatient.setId(patient.getId());

        Gson gson = new Gson();

        Identifier ID = new Identifier();
        DataIdentifier dataIdentifier = new DataIdentifier();
        dataIdentifier.setUse("official");
        dataIdentifier.setSystem("urn:oid:2.16.840.1.113883.2.17.6.3");
        dataIdentifier.setValue(patient.getNdivalue());
        dataIdentifier.setDisplay(patient.getNdi());
        ID.setDataIdentifier(new DataIdentifier[]{dataIdentifier});

        jsonDataPatient.setIdentifier(gson.toJsonTree(ID));

        Name NAME = new Name();
        DataName dataName = new DataName();
        dataName.setUse("official");
        List<String> lastname = new ArrayList<>();
        lastname.add(patient.getFamily());
        List<String> dataname = new ArrayList<>();
        dataname.add(patient.getGiven());
        dataName.setFamily(lastname);
        dataName.setGiven(dataname);
        NAME.setDataName(new DataName[]{dataName});

        jsonDataPatient.setName(gson.toJsonTree(NAME));

        Telecom TELECOM = new Telecom();
        DataTelecom dataMobile = new DataTelecom();
        dataMobile.setSystem("phone");
        dataMobile.setValue(patient.getTelmobile());
        dataMobile.setUse("mobile");
        DataTelecom dataHome = new DataTelecom();
        dataHome.setSystemH("phone");
        dataHome.setValueH(patient.getTelhome());
        dataHome.setUseH("home");
        DataTelecom dataWork = new DataTelecom();
        dataWork.setSystemW("phone");
        dataWork.setValueW(patient.getTelwork());
        dataWork.setUseW("work");
        DataTelecom dataEmail = new DataTelecom();
        dataEmail.setSystemE("email");
        dataEmail.setValueE(patient.getEmail());
        dataEmail.setUseE("home");
        TELECOM.setDataTelecom(new DataTelecom[]{dataMobile, dataHome, dataWork, dataEmail});

        jsonDataPatient.setTelecom(gson.toJsonTree(TELECOM));

        Address ADDRESS = new Address();
        DataAddress dataAddress = new DataAddress();
        dataAddress.setUse("home");
        List<String> lines = new ArrayList<>();
        lines.add(patient.getLine());
        dataAddress.setLine(lines);
        dataAddress.setCity(patient.getCity());
        dataAddress.setCountry("COL");
        ADDRESS.setDataAddress(new DataAddress[]{dataAddress});

        jsonDataPatient.setAddress(gson.toJsonTree(ADDRESS));

        Marital MARITAL = new Marital();
        DataMarital dataMarital = new DataMarital();
        DataCoding dataCoding = new DataCoding();
        dataCoding.setCode(patient.getMaritalStatus());
        dataCoding.setSystem("http://hl7.org/fhir/v3/MaritalStatus");
        switch (patient.getMaritalStatus()) {
            case "M":
                dataCoding.setDisplay("Married");
                break;
            case "U":
                dataCoding.setDisplay("Unmarried");
                break;
            case "T":
                dataCoding.setDisplay("Domestic partner");
                break;
            case "W":
                dataCoding.setDisplay("Widowed");
                break;
            case "L":
                dataCoding.setDisplay("Legally Separated");
                break;
        }
        dataMarital.setDataAddress(new DataCoding[]{dataCoding});
        MARITAL.setMarital(dataMarital);

        jsonDataPatient.setMaritalStatus(gson.toJsonTree(MARITAL));

        Contact CONTACT = new Contact();
        DataContact dataContact = new DataContact();
        DataRelationship dataRelationship = new DataRelationship();
        DataCodingContact dataCodingContact = new DataCodingContact();
        dataCodingContact.setCode(patient.getRelationship());
        dataCodingContact.setSystem("http://hl7.org/fhir/patient-contact-relationship");
        NameContact nameContact = new NameContact();
        nameContact.setUse("official");
        List<String> lastnamec = new ArrayList<>();
        lastnamec.add(patient.getFamilyc());
        List<String> datanamec = new ArrayList<>();
        datanamec.add(patient.getGivenc());
        nameContact.setFamily(lastnamec);
        nameContact.setGiven(datanamec);
        DataTelecomContact dataTelecomContact = new DataTelecomContact();
        dataTelecomContact.setSystem("phone");
        dataTelecomContact.setValue(patient.getTelc());
        dataTelecomContact.setUse("mobile");
        dataRelationship.setDataContact(new DataCodingContact[]{dataCodingContact});
        dataContact.setRelationship(new DataRelationship[]{dataRelationship});
        dataContact.setNameContact(nameContact);
        dataContact.setTelecomContact(new DataTelecomContact[]{dataTelecomContact});
        CONTACT.setDataContact(new DataContact[]{dataContact});

        jsonDataPatient.setContact(gson.toJsonTree(CONTACT));

        Communication COM = new Communication();
        DataCommunication dataCommunication = new DataCommunication();
        dataCommunication.setPreferred(Boolean.TRUE);
        Language language = new Language();
        language.setText("Español Latino");
        LanguageCoding languageCoding = new LanguageCoding();
        languageCoding.setSystem("urn:ietf:bcp:47");
        languageCoding.setCode("es-419");
        languageCoding.setDisplay("Español");
        language.setLanguageCoding(new LanguageCoding[]{languageCoding});
        dataCommunication.setLanguage(language);
        COM.setDataCommunication(new DataCommunication[]{dataCommunication});

        jsonDataPatient.setCommunication(gson.toJsonTree(COM));

        //Subject
        Subject SUBJECT = new Subject();
        DataSubject dataSub = new DataSubject();
        dataSub.setReference(patient.getNdivalue());
        dataSub.setDisplay(patient.getGiven() + " " + patient.getFamily());
        SUBJECT.setDataSubject(dataSub);
        jsonDataPatient.setSubject(gson.toJsonTree(SUBJECT));
        
        
        jsonDataPatient.setManagingOrganization(patient.getManagingOrganization());

        return jsonDataPatient;
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
     * @return the bloodType
     */
    public String getBloodType() {
        return bloodType;
    }

    /**
     * @param bloodType the bloodType to set
     */
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
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
     * @return the maritalStatus
     */
    public JsonElement getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * @param maritalStatus the maritalStatus to set
     */
    public void setMaritalStatus(JsonElement maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * @return the contact
     */
    public JsonElement getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(JsonElement contact) {
        this.contact = contact;
    }

    /**
     * @return the communication
     */
    public JsonElement getCommunication() {
        return communication;
    }

    /**
     * @param communication the communication to set
     */
    public void setCommunication(JsonElement communication) {
        this.communication = communication;
    }

    /**
     * @return the managingOrganization
     */
    public String getManagingOrganization() {
        return managingOrganization;
    }

    /**
     * @param managingOrganization the managingOrganization to set
     */
    public void setManagingOrganization(String managingOrganization) {
        this.managingOrganization = managingOrganization;
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
     * @return the subject
     */
    public JsonElement getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(JsonElement subject) {
        this.subject = subject;
    }

}
