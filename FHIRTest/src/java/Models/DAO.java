/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Controler.ListGluc;
import com.google.gson.JsonElement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    
    public List<ListGluc> listGlucose(String user) throws SQLException {
        List<ListGluc> lista = new ArrayList<>();
        sql = "SELECT issued, CONVERT(JSON_UNQUOTE(performer->'$.performer[0].display') USING utf8) AS performer,"
                + " CONVERT(JSON_UNQUOTE(valueQuantity->'$.valueQuantity.value') USING utf8) AS value "
                + "FROM glucose WHERE subject->'$.subject.reference'='"+user+"'";
        ps = conexion.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            ListGluc list = new ListGluc();
            list.setValueQuantity(rs.getString("value"));           
            list.setIssued(rs.getString("issued"));
            list.setPerformer(rs.getString("performer"));
            lista.add(list);              
        }
        return lista;
    }
     
    public void registerPacient(JsonElement identifier, JsonElement name, JsonElement telecom, String gender, String birthDate, JsonElement address, JsonElement maritalStatus, JsonElement contact, JsonElement communication, String managingOrganization) throws SQLException {        
        sql = "INSERT INTO patient (identifier,name,telecom,gender,birthDate,address,maritalStatus,contact,communication,managingOrganization)"+
        "VALUES ('"+identifier+"','"+name+"','"+telecom+"','"+gender+"','"+birthDate+"','"+address+"','"+maritalStatus+"','"+contact+"','"+communication+"','"+managingOrganization+"')";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }
    
    public void registerPersonal(JsonElement identifier, JsonElement name, JsonElement telecom, String gender, String birthDate, JsonElement address, JsonElement practitionerRole) throws SQLException {        
        sql = "INSERT INTO personal (identifier,name,gender,birthDate,practitionerRole,telecom,address)"+
        "VALUES ('"+identifier+"','"+name+"','"+gender+"','"+birthDate+"','"+practitionerRole+"','"+telecom+"','"+address+"')";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }
    
    public void registerGlucose(JsonElement identifier, JsonElement code, JsonElement subject, String issued, JsonElement performer, JsonElement valueQuantity, JsonElement interpretation, JsonElement referenceRange) throws SQLException{
        sql = "INSERT INTO glucose (identifier,code,subject,issued,performer,valueQuantity,interpretation,referenceRange)"+
        "VALUES ('"+identifier+"','"+code+"','"+subject+"','"+issued+"','"+performer+"','"+valueQuantity+"','"+interpretation+"','"+referenceRange+"')";
        ps = conexion.prepareStatement(sql);
        ps.executeUpdate();
    }

    
}
