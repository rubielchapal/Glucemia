<%-- 
    Document   : page
    Created on : 8/05/2016, 10:46:14 PM
    Author     : Dell
--%>


<%@taglib prefix="t" uri="http://java.sun.com/jsp/jstl/core" %>
      
<%@page contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<%--<%@page contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%> --%>
<!DOCTYPE html>
<html>
    <head>     
        <title>Logueo Exitoso</title>
    </head>
    <body>        
        <h1>name <%= session.getAttribute("sessionName") %> </h1>
        <hr>
        <h2>identifier <%= session.getAttribute("sessionIdentifier") %></h2>
        <hr>
        <h2>info <%= session.getAttribute("session") %></h2>
        <form action="Logout" method="post">
            <p><input type="submit" value="Cerrar Sesión"></p>
        <form>
    </body>
</html>
