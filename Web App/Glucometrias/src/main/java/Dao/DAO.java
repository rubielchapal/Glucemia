package Dao;

import com.google.gson.JsonElement;
import entities.Admin;
import entities.Patient;
import entities.Personal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.JsonDataPatient;
import model.JsonDataPersonal;
import model.ListGluc;
import model.ListPacient;
import model.ListPersonal;

/**
 * Data Access Object, class to handled the comunication between server and DB
 * Here we can found all SQL setences
 *
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class DAO {

    /**
     * Connection
     */
    public Connection conexion;

    /**
     * Database name
     */
    public final static String userDb = "root";

    /**
     * Database password
     */
    public final static String passDb = "albe1974";
    PreparedStatement ps = null;
    String sql = null;
    ResultSet rs = null;

    /**
     * Connect with DB
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void conectar() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/fhir", userDb, passDb);
    }

    /**
     * Disconnect with DB
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void desconectar() throws SQLException, ClassNotFoundException {
        conexion.close();
    }

    /**
     *
     * @param user String document value from patient
     * @return true if is a exist Pacient or false if not
     * @throws SQLException
     */
    public boolean isPatient(String user) throws SQLException {
        sql = "SELECT identifier FROM patient WHERE identifier->'$.identifier[0].value'='" + user + "'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        return rs.next();
    }

    /**
     *
     * @param user String document value from personal
     * @return true if is a exist Personal or false if not
     * @throws SQLException
     */
    public boolean isPersonal(String user) throws SQLException {
        sql = "SELECT identifier FROM personal WHERE identifier->'$.identifier[0].value'='" + user + "'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        return rs.next();
    }

    /**
     * Check data for the login
     *
     * @param ndivalue user document
     * @param password user password
     * @param tab domain user
     * @return true if is a exist user or false if not
     * @throws SQLException
     */
    public boolean findUser(String ndivalue, String password, String tab) throws SQLException {
        String table = "personal";
        if (tab.equalsIgnoreCase("patient")) {
            table = tab;
        }
        sql = "SELECT identifier FROM " + table + " WHERE identifier->'$.identifier[0].value'='" + ndivalue + "' AND password='" + password + "'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        return rs.next();
    }

    /**
     * Select name used to show in the HomeScreen
     *
     * @param ndivalue user document
     * @param tab domain user
     * @return user name
     * @throws SQLException
     */
    public String getName(String ndivalue, String tab) throws SQLException {
        String table = "personal";
        if (tab.equalsIgnoreCase("patient")) {
            table = tab;
        }
        sql = "SELECT CONVERT(CONCAT(JSON_UNQUOTE(name->'$.name[0].given[0]'),' ',JSON_UNQUOTE(name->'$.name[0].family[0]')) USING utf8)"
                + " AS subject FROM " + table + " WHERE identifier->'$.identifier[0].value'='" + ndivalue + "'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        String subject = null;
        while (rs.next()) {
            subject = rs.getString("subject").trim();
        }
        return subject;
    }

    /**
     *
     * @param ndivalue
     * @param tab
     * @return Token from patient or personal used to send push notification via
     * FCM
     * @throws SQLException
     */
    public String getToken(String ndivalue, String tab) throws SQLException {
        String table = "personal";
        if (tab.equalsIgnoreCase("patient")) {
            table = tab;
        }
        sql = "SELECT token_id FROM " + table + " WHERE identifier->'$.identifier[0].value'='" + ndivalue + "'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        String token = null;
        while (rs.next()) {
            token = rs.getString("token_id").trim();
        }
        return token;
    }

    /**
     * Class to update new Token FCM
     *
     * @param mToken
     * @param mTo
     * @param tab
     * @throws SQLException
     */
    public void updateToken(String mToken, String mTo, String tab) throws SQLException {
        String table = "personal";
        if (tab.equalsIgnoreCase("patient")) {
            table = tab;
        }
        sql = "UPDATE " + table + " SET token_id = '" + mToken + "' WHERE identifier->'$.identifier[0].value'='" + mTo + "'";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     *
     * @param document
     * @return
     * @throws SQLException
     */
    public List<ListPersonal> getPersonal(String document) throws SQLException {
        List<ListPersonal> lista = new ArrayList<>();
        sql = "SELECT DISTINCT JSON_UNQUOTE(performer->'$.performer[0].reference') AS performer"
                + " FROM glucose WHERE subject->'$.subject.reference'='" + document + "'"
                + " AND performer->'$.performer' IS NOT NULL";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            ListPersonal list = new ListPersonal();
            list.setId(rs.getString("performer"));
            lista.add(list);
        }
        return lista;
    }

    /**
     *
     * @param ndivalue
     * @return
     * @throws SQLException
     */
    public String[] getDetail(String ndivalue) throws SQLException {
        String[] table = new String[5];
        sql = "SELECT CONVERT(CONCAT(JSON_UNQUOTE(name->'$.name[0].given[0]'),' ',JSON_UNQUOTE(name->'$.name[0].family[0]')) USING utf8) AS subject,"
                + " CONVERT(JSON_UNQUOTE(telecom->'$.telecom[3].value') USING utf8) AS email,"
                + " birthDate, (2016 - CONVERT(SUBSTRING_INDEX(birthDate, '/', -1), UNSIGNED INTEGER)) AS age,"
                + " bloodType FROM patient WHERE identifier->'$.identifier[0].value'='" + ndivalue + "'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            table[0] = rs.getString("subject").trim();
            table[1] = rs.getString("email").trim();
            table[2] = rs.getString("birthDate").trim();
            table[3] = String.valueOf(rs.getInt("age"));
            table[4] = rs.getString("bloodType").trim();
        }
        return table;
    }

    /**
     *
     * @param user
     * @return
     * @throws SQLException
     */
    public List<ListPacient> listPacient(String user) throws SQLException {
        List<ListPacient> lista = new ArrayList<>();
        sql = "SELECT DISTINCT CONVERT(JSON_UNQUOTE(subject->'$.subject.reference') USING utf8) AS id,"
                + "CONVERT(JSON_UNQUOTE(subject->'$.subject.display') USING utf8) AS name "
                + "FROM glucose WHERE performer->'$.performer[0].reference'='" + user + "'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            ListPacient list = new ListPacient();
            list.setId(rs.getString("id"));
            list.setSubject(rs.getString("name"));
            lista.add(list);
        }

        return lista;
    }

    /**
     *
     * @param user
     * @return patient's glucose list
     * @throws SQLException
     * @throws ParseException
     */
    public List<ListGluc> listGlucose(String user) throws SQLException, ParseException {
        List<ListGluc> lista = new ArrayList<>();
        sql = "SELECT issued, comments, CONVERT(JSON_UNQUOTE(performer->'$.performer[0].display') USING utf8) AS performer,"
                + " CONVERT(JSON_UNQUOTE(valueQuantity->'$.valueQuantity.value') USING utf8) AS value,"
                + " CONVERT(JSON_UNQUOTE(interpretation->'$.interpretation.coding[0].code') USING utf8) AS code "
                + "FROM glucose WHERE subject->'$.subject.reference'='" + user + "'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            ListGluc list = new ListGluc();
            list.setValueQuantity(rs.getString("value"));
            String date = rs.getString("issued");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yy HH:mm");
            Date d = sdf.parse(date);
            String formattedTime = output.format(d);
            list.setIssued(formattedTime);
            list.setPerformer(rs.getString("performer"));
            list.setCode(rs.getString("code"));
            list.setState(rs.getString("comments"));
            lista.add(list);
        }
        return lista;
    }

    /**
     *
     * @param user
     * @param fecha
     * @return patient's glucose list by date
     * @throws SQLException
     * @throws ParseException
     */
    public List<ListGluc> DateFilterGlucose(String user, String fecha) throws SQLException, ParseException {
        List<ListGluc> lista = new ArrayList<>();
        sql = "SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(issued, 'T', 2), 'T', -1) AS time, comments, "
                + "CONVERT(JSON_UNQUOTE(performer->'$.performer[0].display') USING utf8) AS performer,"
                + " CONVERT(JSON_UNQUOTE(valueQuantity->'$.valueQuantity.value') USING utf8) AS value,"
                + " CONVERT(JSON_UNQUOTE(interpretation->'$.interpretation.coding[0].code') USING utf8) AS code "
                + "FROM glucose WHERE subject->'$.subject.reference'='" + user + "' AND SUBSTRING_INDEX(issued, 'T', 1) = '" + fecha + "' ORDER BY time";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            ListGluc list = new ListGluc();
            list.setValueQuantity(rs.getString("value"));
            String date = rs.getString("time");
            list.setIssued(date);
            list.setPerformer(rs.getString("performer"));
            list.setCode(rs.getString("code"));
            list.setState(rs.getString("comments"));
            lista.add(list);
        }
        return lista;
    }

    /**
     *
     * @param user
     * @param params
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public List<ListGluc> ValueFilterGlucose(String user, String[] params) throws SQLException, ParseException {
        List<ListGluc> lista = new ArrayList<>();
        if (!(params[0].equalsIgnoreCase("nn"))) {
            sql = "SELECT issued, comments, CONVERT(JSON_UNQUOTE(performer->'$.performer[0].display') USING utf8) AS performer,"
                    + " CONVERT(JSON_UNQUOTE(valueQuantity->'$.valueQuantity.value') USING utf8) AS value,"
                    + " CONVERT(JSON_UNQUOTE(interpretation->'$.interpretation.coding[0].code') USING utf8) AS code "
                    + "FROM glucose WHERE subject->'$.subject.reference'='" + user + "' AND valueQuantity->'$.valueQuantity.value'='" + params[0] + "'";
        } else if (!(params[1].equalsIgnoreCase("nn")) && params[0].equalsIgnoreCase("nn") && params[2].equalsIgnoreCase("nn")) {
            sql = "SELECT issued, comments, CONVERT(JSON_UNQUOTE(performer->'$.performer[0].display') USING utf8) AS performer,"
                    + " CONVERT(JSON_UNQUOTE(valueQuantity->'$.valueQuantity.value') USING utf8) AS value,"
                    + " CONVERT(JSON_UNQUOTE(interpretation->'$.interpretation.coding[0].code') USING utf8) AS code "
                    + "FROM glucose WHERE subject->'$.subject.reference'='" + user + "' AND valueQuantity->'$.valueQuantity.value'>='" + params[1] + "'";
        } else {
            sql = "SELECT issued, comments, CONVERT(JSON_UNQUOTE(performer->'$.performer[0].display') USING utf8) AS performer,"
                    + " CONVERT(JSON_UNQUOTE(valueQuantity->'$.valueQuantity.value') USING utf8) AS value,"
                    + " CONVERT(JSON_UNQUOTE(interpretation->'$.interpretation.coding[0].code') USING utf8) AS code "
                    + "FROM glucose WHERE subject->'$.subject.reference'='" + user + "'"
                    + "AND valueQuantity->'$.valueQuantity.value'>='" + params[1] + "' AND valueQuantity->'$.valueQuantity.value'<='" + params[2] + "'";
        }
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            ListGluc list = new ListGluc();
            list.setValueQuantity(rs.getString("value"));
            String date = rs.getString("issued");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yy HH:mm");
            Date d = sdf.parse(date);
            String formattedTime = output.format(d);
            list.setIssued(formattedTime);
            list.setPerformer(rs.getString("performer"));
            list.setCode(rs.getString("code"));
            list.setState(rs.getString("comments"));
            lista.add(list);
        }
        return lista;
    }

    /**
     * patient register
     *
     * @param identifier
     * @param password
     * @param name
     * @param telecom
     * @param gender
     * @param birthDate
     * @param bloodType
     * @param address
     * @param maritalStatus
     * @param contact
     * @param communication
     * @param managingOrganization
     * @throws SQLException
     */
    public void registerPacient(JsonElement identifier, String password, JsonElement name, JsonElement telecom, String gender, String birthDate, String bloodType, JsonElement address, JsonElement maritalStatus, JsonElement contact, JsonElement communication, String managingOrganization, String token) throws SQLException {
        sql = "INSERT INTO patient (identifier,password,name,telecom,gender,birthDate,bloodType,address,maritalStatus,contact,communication,managingOrganization,token_id)"
                + "VALUES ('" + identifier + "','" + password + "','" + name + "','" + telecom + "','" + gender + "','" + birthDate + "','" + bloodType + "','" + address + "','" + maritalStatus + "','" + contact + "','" + communication + "','" + managingOrganization + "','" + token + "')";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * personal register
     *
     * @param identifier
     * @param password
     * @param name
     * @param telecom
     * @param gender
     * @param birthDate
     * @param address
     * @param practitionerRole
     * @throws SQLException
     */
    public void registerPersonal(JsonElement identifier, String password, JsonElement name, JsonElement telecom, String gender, String birthDate, JsonElement address, JsonElement practitionerRole, String token) throws SQLException {
        sql = "INSERT INTO personal (identifier,password,name,gender,birthDate,practitionerRole,telecom,address,token_id)"
                + "VALUES ('" + identifier + "','" + password + "','" + name + "','" + gender + "','" + birthDate + "','" + practitionerRole + "','" + telecom + "','" + address + "','" + token + "')";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * glucose register
     *
     * @param identifier
     * @param code
     * @param subject
     * @param issued
     * @param performer
     * @param valueQuantity
     * @param interpretation
     * @param comments
     * @param referenceRange
     * @throws SQLException
     */
    public void registerGlucose(JsonElement identifier, JsonElement code, JsonElement subject, String issued, JsonElement performer, JsonElement valueQuantity, JsonElement interpretation, String comments, JsonElement referenceRange) throws SQLException {
        sql = "INSERT INTO glucose (identifier,code,subject,issued,performer,valueQuantity,interpretation,comments,referenceRange)"
                + "VALUES ('" + identifier + "','" + code + "','" + subject + "','" + issued + "','" + performer + "','" + valueQuantity + "','" + interpretation + "','" + comments + "','" + referenceRange + "')";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     *
     * @param ndivalue
     * @return if exist root
     * @throws SQLException
     */
    public boolean istRoot(String ndivalue) throws SQLException {
        sql = "SELECT id FROM root WHERE ndivalue= '" + ndivalue + "'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        return rs.next();
    }

    /**
     *
     * @return true if exist root
     * @throws SQLException
     */
    public boolean existRoot() throws SQLException {
        sql = "SELECT id FROM root WHERE id=1";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        return rs.next();
    }

    /**
     *
     * @param ndivalue
     * @param password
     * @return true if credentials are validate
     * @throws SQLException
     */
    public boolean isRoot(String ndivalue, String password) throws SQLException {
        sql = "SELECT id FROM root WHERE ndivalue= '" + ndivalue + "' AND password='" + password + "'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        return rs.next();
    }

    /**
     * data personal
     *
     * @param ndivalue
     * @return all data personal from database
     * @throws SQLException
     */
    public Personal getDataPersonal(String ndivalue) throws SQLException {

        Personal personal = new Personal();
        sql = "SELECT "
                + "CONVERT(JSON_UNQUOTE(name->'$.name[0].given[0]') USING utf8) AS given,"
                + "CONVERT(JSON_UNQUOTE(name->'$.name[0].family[0]') USING utf8) AS family,"
                + "password,"
                + "CONVERT(JSON_UNQUOTE(identifier->'$.identifier[0].display[0]') USING utf8) AS ndi,"
                + "gender,"
                + "birthDate,"
                + "CONVERT(JSON_UNQUOTE(telecom->'$.telecom[0].value[0]') USING utf8) AS telmobile,"
                + "CONVERT(JSON_UNQUOTE(telecom->'$.telecom[1].value[0]') USING utf8) AS telwork,"
                + "CONVERT(JSON_UNQUOTE(telecom->'$.telecom[2].value[0]') USING utf8) AS email,"
                + "CONVERT(JSON_UNQUOTE(address->'$.address[0].line[0]') USING utf8) AS line,"
                + "CONVERT(JSON_UNQUOTE(address->'$.address[0].city[0]') USING utf8) AS city,"
                + "CONVERT(JSON_UNQUOTE(practitionerRole->'$.practitionerRole[0].managingOrganization[0]') USING utf8) AS managingOrganization,"
                + "CONVERT(JSON_UNQUOTE(practitionerRole->'$.practitionerRole[0].role[0].coding[0].display[0]') USING utf8) AS role,"
                + "id "
                + " FROM personal WHERE identifier->'$.identifier[0].value'='" + ndivalue + "'";

        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            personal.setGiven(rs.getString("given"));
            personal.setFamily(rs.getString("family"));
            personal.setPassword(rs.getString("password"));
            personal.setNdi(rs.getString("ndi"));
            personal.setNdivalue(ndivalue);
            personal.setGender(rs.getString("gender"));
            personal.setBirthDate(rs.getString("birthDate"));
            personal.setTelmobile(rs.getString("telmobile"));
            personal.setTelwork(rs.getString("telwork"));
            personal.setEmail(rs.getString("email"));
            personal.setLine(rs.getString("line"));
            personal.setCity(rs.getString("city"));
            personal.setManagingOrganization(rs.getString("managingOrganization"));
            personal.setRole(rs.getString("role"));
            personal.setId(rs.getInt("id"));
        }
        return personal;
    }

    /**
     * data patient
     *
     * @param ndivalue
     * @return all data patient from database
     * @throws SQLException
     */
    public Patient getPatient(String ndivalue) throws SQLException {

        Patient patient = new Patient();
        sql = "SELECT "
                + "CONVERT(JSON_UNQUOTE(name->'$.name[0].given[0]') USING utf8) AS given,"
                + "CONVERT(JSON_UNQUOTE(name->'$.name[0].family[0]') USING utf8) AS family,"
                + "password,"
                + "CONVERT(JSON_UNQUOTE(identifier->'$.identifier[0].display[0]') USING utf8) AS ndi,"
                + "gender,"
                + "birthDate,"
                + "CONVERT(JSON_UNQUOTE(maritalStatus->'$.maritalStatus[0].coding[0].display[0]') USING utf8) AS maritalStatus,"
                + "CONVERT(JSON_UNQUOTE(telecom->'$.telecom[0].value[0]') USING utf8) AS telmobile,"
                + "CONVERT(JSON_UNQUOTE(telecom->'$.telecom[1].value[0]') USING utf8) AS telhome,"
                + "CONVERT(JSON_UNQUOTE(telecom->'$.telecom[2].value[0]') USING utf8) AS telwork,"
                + "CONVERT(JSON_UNQUOTE(telecom->'$.telecom[3].value[0]') USING utf8) AS email,"
                + "CONVERT(JSON_UNQUOTE(address->'$.address[0].line[0]') USING utf8) AS line,"
                + "CONVERT(JSON_UNQUOTE(address->'$.address[0].city[0]') USING utf8) AS city,"
                + "CONVERT(JSON_UNQUOTE(contact->'$.contact[0].name[0].given[0]') USING utf8) AS givenc,"
                + "CONVERT(JSON_UNQUOTE(contact->'$.contact[0].name[0].family[0]') USING utf8) AS familyc,"
                + "CONVERT(JSON_UNQUOTE(contact->'$.contact[0].telecom[0].value[0]') USING utf8) AS telc,"
                + "CONVERT(JSON_UNQUOTE(contact->'$.contact[0].relationship[0].coding[0].code[0]') USING utf8) AS relationship,"
                + "managingOrganization,"
                + "bloodType,"
                + "id "
                + "FROM patient WHERE identifier->'$.identifier[0].value'='" + ndivalue + "'";

        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            patient.setGiven(rs.getString("given"));
            patient.setFamily(rs.getString("family"));
            patient.setPassword(rs.getString("password"));
            patient.setNdi(rs.getString("ndi"));
            patient.setNdivalue(ndivalue);
            patient.setGender(rs.getString("gender"));
            patient.setBirthDate(rs.getString("birthDate"));
            patient.setMaritalStatus(rs.getString("maritalStatus"));
            patient.setTelmobile(rs.getString("telmobile"));
            patient.setTelhome(rs.getString("telhome"));
            patient.setTelwork(rs.getString("telwork"));
            patient.setEmail(rs.getString("email"));
            patient.setLine(rs.getString("line"));
            patient.setCity(rs.getString("city"));
            patient.setGivenc(rs.getString("givenc"));
            patient.setFamilyc(rs.getString("familyc"));
            patient.setTelc(rs.getString("telc"));
            patient.setRelationship(rs.getString("relationship"));
            patient.setManagingOrganization(rs.getString("managingOrganization"));
            patient.setBlood(rs.getString("bloodType"));
            patient.setId(rs.getInt("id"));
        }
        return patient;
    }

    /**
     *
     * @return root user
     * @throws SQLException
     */
    public Admin getRoot() throws SQLException {

        Admin admin = new Admin();
        sql = "SELECT * FROM root WHERE id=1";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            admin.setNdi(rs.getString("ndi"));
            admin.setName(rs.getString("name"));
            admin.setNdivalue(rs.getString("ndivalue"));
            admin.setPhone(rs.getString("phone"));
            admin.setAddress(rs.getString("address"));
            admin.setEmail(rs.getString("email"));
            admin.setPassword(rs.getString("password"));
        }
        return admin;
    }

    /**
     * root register
     *
     * @param name
     * @param ndi
     * @param ndivalue
     * @param phone
     * @param address
     * @param email
     * @param password
     * @throws SQLException
     */
    public void registrerRoot(String name, String ndi, String ndivalue, String phone, String address, String email, String password) throws SQLException {
        sql = "INSERT INTO root (id,ndi,ndivalue,name,phone,address,email,password)"
                + "VALUES ('1','" + ndi + "','" + ndivalue + "','" + name + "','" + phone + "','" + address + "','" + email + "','" + password + "')";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * update data user root
     *
     * @param name
     * @param ndi
     * @param ndivalue
     * @param phone
     * @param address
     * @param email
     * @param password
     * @throws SQLException
     */
    public void updateRoot(String name, String ndi, String ndivalue, String phone, String address, String email, String password) throws SQLException {

        sql = "UPDATE root SET ndi='" + ndi + "', "
                + "ndivalue='" + ndivalue + "', "
                + "name='" + name + "', "
                + "phone='" + phone + "', "
                + "address='" + address + "', "
                + "email='" + email + "', "
                + "password='" + password + "' "
                + "WHERE id=1";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * update data user personal
     *
     * @param jsonDataPersonal
     * @param ndiValueOld
     * @throws SQLException
     */
    public void updatePersonal(JsonDataPersonal jsonDataPersonal, String ndiValueOld) throws SQLException {
        sql = "UPDATE personal SET identifier='" + jsonDataPersonal.getIdentifier() + "', "
                + "password='" + jsonDataPersonal.getPassword() + "', "
                + "name='" + jsonDataPersonal.getName() + "', "
                + "gender='" + jsonDataPersonal.getGender() + "', "
                + "birthDate='" + jsonDataPersonal.getBirthDate() + "', "
                + "practitionerRole='" + jsonDataPersonal.getPractitionerRole() + "', "
                + "telecom='" + jsonDataPersonal.getTelecom() + "', "
                + "address='" + jsonDataPersonal.getAddress() + "' "
                + "WHERE id=" + jsonDataPersonal.getId() + "";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * update data user patient
     *
     * @param jsonDataPatient
     * @param ndiValueOld
     * @throws SQLException
     */
    public void updatePatient(JsonDataPatient jsonDataPatient, String ndiValueOld) throws SQLException {

        sql = "UPDATE patient SET identifier='" + jsonDataPatient.getIdentifier() + "', "
                + "password='" + jsonDataPatient.getPassword() + "', "
                + "name='" + jsonDataPatient.getName() + "', "
                + "managingOrganization='" + jsonDataPatient.getManagingOrganization() + "', "
                + "gender='" + jsonDataPatient.getGender() + "', "
                + "communication='" + jsonDataPatient.getCommunication() + "', "
                + "contact='" + jsonDataPatient.getContact() + "', "
                + "bloodType='" + jsonDataPatient.getBloodType() + "', "
                + "birthDate='" + jsonDataPatient.getBirthDate() + "', "
                + "maritalStatus='" + jsonDataPatient.getMaritalStatus() + "',  "
                + "telecom='" + jsonDataPatient.getTelecom() + "', "
                + "address='" + jsonDataPatient.getAddress() + "' "
                + "WHERE id=" + jsonDataPatient.getId() + "";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * update data user personal on glucose patient
     *
     * @param performer
     * @param ndiValueOld
     * @throws SQLException
     */
    public void updatePersonalOnGlucose(JsonElement performer, String ndiValueOld) throws SQLException {
        sql = "UPDATE glucose SET performer='" + performer + "' WHERE performer->'$.performer[0].reference'='" + ndiValueOld + "'";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * update data user patient on glucose patient
     *
     * @param subject
     * @param ndiValueOld
     * @throws SQLException
     */
    public void updatePatientOnGlucose(JsonElement subject, String ndiValueOld) throws SQLException {
        sql = "UPDATE glucose SET subject='" + subject + "' WHERE subject->'$.subject[0].reference'='" + ndiValueOld + "'";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * delete patient from database
     *
     * @param ndiValue
     * @throws SQLException
     */
    public void deletePatient(String ndiValue) throws SQLException {
        sql = "DELETE FROM patient WHERE identifier->'$.identifier[0].value'='" + ndiValue + "'";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * delete personal from database
     *
     * @param ndiValue
     * @throws SQLException
     */
    public void deletePersonal(String ndiValue) throws SQLException {
        sql = "DELETE FROM personal WHERE identifier->'$.identifier[0].value'='" + ndiValue + "'";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * delete data glucose from database
     *
     * @param ndiValue
     * @throws SQLException
     */
    public void deleteGlucose(String ndiValue) throws SQLException {
        sql = "DELETE FROM glucose WHERE subject->'$.subject[0].reference'='" + ndiValue + "'";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }
}


