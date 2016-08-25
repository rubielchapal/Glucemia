/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Controler.ListGluc;
import Controler.ListPacient;
import com.google.gson.JsonElement;
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

/**
 *
 * @author Dell
 */
public class DAO {
    public Connection conexion;
    public final static String userDb = "root";
    public final static String passDb = "pepo1606";            
    PreparedStatement ps = null;         
    String sql = null;
    ResultSet rs = null;
    
    
    //Conectar a la Base de datos
    public void conectar() throws SQLException,ClassNotFoundException{
         Class.forName("com.mysql.jdbc.Driver");
         conexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/fhir",userDb, passDb);
    }
    //Desconectar a la Base de datos
    public void desconectar() throws SQLException, ClassNotFoundException{
        conexion.close();
    }
    
  
    public boolean isPatient(String user) throws SQLException{
        sql = "SELECT identifier FROM patient WHERE identifier->'$.identifier[0].value'='"+user+"'";
        //String sql = "SELECT * FROM usuarios WHERE usuario='"+user+"' OR documento='"+user+"' AND password='"+password+"'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();            
        return rs.next();
    } 
    
    public boolean isPersonal(String user) throws SQLException{
        sql = "SELECT identifier FROM personal WHERE identifier->'$.identifier[0].value'='"+user+"'";
        //String sql = "SELECT * FROM usuarios WHERE usuario='"+user+"' OR documento='"+user+"' AND password='"+password+"'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();            
        return rs.next();
    }
    
    public boolean findUser(String ndivalue, String password, String tab) throws SQLException{
        String table = "personal";
        if(tab.equalsIgnoreCase("patient")){
            table = tab;
        }
        sql = "SELECT identifier FROM "+table+" WHERE identifier->'$.identifier[0].value'='"+ndivalue+"' AND password='"+password+"'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        return  rs.next();
    }
    
    public String getName(String ndivalue, String tab) throws SQLException{
        String table = "personal";
        if(tab.equalsIgnoreCase("patient")){
            table = tab;
        }
        sql = "SELECT CONVERT(CONCAT(JSON_UNQUOTE(name->'$.name[0].given[0]'),' ',JSON_UNQUOTE(name->'$.name[0].family[0]')) USING utf8)"
                + " AS subject FROM "+table+" WHERE identifier->'$.identifier[0].value'='"+ndivalue+"'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        String subject = null;
        while(rs.next()){
            subject = rs.getString("subject").trim();
        }
        return  subject;
    }
    
    public String getToken(String ndivalue, String tab) throws SQLException{
        String table = "personal";
        if(tab.equalsIgnoreCase("patient")){
            table = tab;
        }
        sql = "SELECT token_id FROM "+table+" WHERE identifier->'$.identifier[0].value'='"+ndivalue+"'";
        //SELECT token_id FROM patient WHERE identifier->'$.identifier[0].value'='1075257898'
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        String token = null;
        while(rs.next()){
            token = rs.getString("token_id").trim();
        }
        return token;
    }
    
    public void updateToken(String mToken, String mTo, String tab) throws SQLException {
        String table = "personal";
        if(tab.equalsIgnoreCase("patient")){
            table = tab;
        }
        sql = "UPDATE "+table+" SET token_id = '"+mToken+"' WHERE identifier->'$.identifier[0].value'='"+mTo+"'";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }
    
    public String[] getDetail(String ndivalue) throws SQLException{
        String[] table = new String[3];
        sql = "SELECT CONVERT(CONCAT(JSON_UNQUOTE(name->'$.name[0].given[0]'),' ',JSON_UNQUOTE(name->'$.name[0].family[0]')) USING utf8) AS subject,"
                +" birthDate, (2016 - CONVERT(SUBSTRING_INDEX(birthDate, '-', 1), UNSIGNED INTEGER)) AS year"
                +" FROM patient WHERE identifier->'$.identifier[0].value'='"+ndivalue+"'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        while(rs.next()){
            table[0] = rs.getString("subject").trim();
            table[1] = rs.getString("birthDate").trim();
            table[2] = String.valueOf(rs.getInt("year"));
        }
        return  table;
    }
    
    public List<ListPacient> listPacient(String user) throws SQLException {
        List<ListPacient> lista = new ArrayList<>();
        sql = "SELECT DISTINCT CONVERT(JSON_UNQUOTE(subject->'$.subject.reference') USING utf8) AS id,"
                + "CONVERT(JSON_UNQUOTE(subject->'$.subject.display') USING utf8) AS name "
                + "FROM glucose WHERE performer->'$.performer[0].reference'='"+user+"'";
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
    
    public List<ListGluc> listGlucose(String user) throws SQLException, ParseException {
        List<ListGluc> lista = new ArrayList<>();
        sql = "SELECT issued, comments, CONVERT(JSON_UNQUOTE(performer->'$.performer[0].display') USING utf8) AS performer,"
                + " CONVERT(JSON_UNQUOTE(valueQuantity->'$.valueQuantity.value') USING utf8) AS value,"
                + " CONVERT(JSON_UNQUOTE(interpretation->'$.interpretation.coding[0].code') USING utf8) AS code "
                + "FROM glucose WHERE subject->'$.subject.reference'='"+user+"'";
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
            //String issued = DDate.toString();
            //list.setIssued(rs.getString("issued"));
            list.setIssued(formattedTime);
            list.setPerformer(rs.getString("performer"));
            list.setCode(rs.getString("code"));
            list.setState(rs.getString("comments"));
            lista.add(list);              
        }
        return lista;
    }
    
    public List<ListGluc> DateFilterGlucose(String user, String fecha) throws SQLException, ParseException {
        List<ListGluc> lista = new ArrayList<>();        
        sql = "SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(issued, 'T', 2), 'T', -1) AS time, comments, "
                + "CONVERT(JSON_UNQUOTE(performer->'$.performer[0].display') USING utf8) AS performer,"
                + " CONVERT(JSON_UNQUOTE(valueQuantity->'$.valueQuantity.value') USING utf8) AS value,"
                + " CONVERT(JSON_UNQUOTE(interpretation->'$.interpretation.coding[0].code') USING utf8) AS code "
                + "FROM glucose WHERE subject->'$.subject.reference'='"+user+"' AND SUBSTRING_INDEX(issued, 'T', 1) = '"+fecha+"'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            ListGluc list = new ListGluc();
            list.setValueQuantity(rs.getString("value"));
            String date = rs.getString("time");
            //String issued = DDate.toString();
            //list.setIssued(rs.getString("issued"));
            list.setIssued(date);
            list.setPerformer(rs.getString("performer"));
            list.setCode(rs.getString("code"));
            list.setState(rs.getString("comments"));
            lista.add(list);              
        }
        return lista;
    }
    
    public List<ListGluc> ValueFilterGlucose(String user, String[] params) throws SQLException, ParseException {
        List<ListGluc> lista = new ArrayList<>();
        if(!(params[0].equalsIgnoreCase("nn"))){
            sql = "SELECT issued, comments, CONVERT(JSON_UNQUOTE(performer->'$.performer[0].display') USING utf8) AS performer,"
                + " CONVERT(JSON_UNQUOTE(valueQuantity->'$.valueQuantity.value') USING utf8) AS value,"
                + " CONVERT(JSON_UNQUOTE(interpretation->'$.interpretation.coding[0].code') USING utf8) AS code "
                + "FROM glucose WHERE subject->'$.subject.reference'='"+user+"' AND valueQuantity->'$.valueQuantity.value'='"+params[0]+"'";                    
        }else if(!(params[1].equalsIgnoreCase("nn")) && params[0].equalsIgnoreCase("nn") && params[2].equalsIgnoreCase("nn")){
            sql = "SELECT issued, comments, CONVERT(JSON_UNQUOTE(performer->'$.performer[0].display') USING utf8) AS performer,"
                + " CONVERT(JSON_UNQUOTE(valueQuantity->'$.valueQuantity.value') USING utf8) AS value,"
                + " CONVERT(JSON_UNQUOTE(interpretation->'$.interpretation.coding[0].code') USING utf8) AS code "
                + "FROM glucose WHERE subject->'$.subject.reference'='"+user+"' AND valueQuantity->'$.valueQuantity.value'>='"+params[1]+"'";                    
        }else{           
            sql = "SELECT issued, comments, CONVERT(JSON_UNQUOTE(performer->'$.performer[0].display') USING utf8) AS performer,"
                + " CONVERT(JSON_UNQUOTE(valueQuantity->'$.valueQuantity.value') USING utf8) AS value,"
                + " CONVERT(JSON_UNQUOTE(interpretation->'$.interpretation.coding[0].code') USING utf8) AS code "
                + "FROM glucose WHERE subject->'$.subject.reference'='"+user+"'"
                + "AND valueQuantity->'$.valueQuantity.value'>='"+params[1]+"' AND valueQuantity->'$.valueQuantity.value'<='"+params[2]+"'";                                
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
            //String issued = DDate.toString();
            //list.setIssued(rs.getString("issued"));
            list.setIssued(formattedTime);
            list.setPerformer(rs.getString("performer"));
            list.setCode(rs.getString("code"));
            list.setState(rs.getString("comments"));
            lista.add(list);            
        }
        return lista;
    }
    
    
     
    public void registerPacient(JsonElement identifier, String password, JsonElement name, JsonElement telecom, String gender, String birthDate, JsonElement address, JsonElement maritalStatus, JsonElement contact, JsonElement communication, String managingOrganization) throws SQLException {        
        sql = "INSERT INTO patient (identifier,password,name,telecom,gender,birthDate,address,maritalStatus,contact,communication,managingOrganization)"+
        "VALUES ('"+identifier+"','"+password+"','"+name+"','"+telecom+"','"+gender+"','"+birthDate+"','"+address+"','"+maritalStatus+"','"+contact+"','"+communication+"','"+managingOrganization+"')";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }
    
    public void registerPersonal(JsonElement identifier, String password, JsonElement name, JsonElement telecom, String gender, String birthDate, JsonElement address, JsonElement practitionerRole) throws SQLException {        
        sql = "INSERT INTO personal (identifier,password,name,gender,birthDate,practitionerRole,telecom,address)"+
        "VALUES ('"+identifier+"','"+password+"','"+name+"','"+gender+"','"+birthDate+"','"+practitionerRole+"','"+telecom+"','"+address+"')";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }
    
    public void registerGlucose(JsonElement identifier, JsonElement code, JsonElement subject, String issued, JsonElement performer, JsonElement valueQuantity, JsonElement interpretation, String comments, JsonElement referenceRange) throws SQLException{
        sql = "INSERT INTO glucose (identifier,code,subject,issued,performer,valueQuantity,interpretation,comments,referenceRange)"+
        "VALUES ('"+identifier+"','"+code+"','"+subject+"','"+issued+"','"+performer+"','"+valueQuantity+"','"+interpretation+"','"+comments+"','"+referenceRange+"')";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }   

    
}
