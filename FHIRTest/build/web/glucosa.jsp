<%-- 
    Document   : glucosa
    Created on : 5/06/2016, 01:42:02 PM
    Author     : Dell
--%>

<%-- 
    Document   : paciente
    Created on : 8/05/2016, 06:20:34 PM
    Author     : Alejo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>    
    <head>
        <meta charset="UTF-8">
        <title>Registro</title>
    </head>
    <body>
        <header id="header"> 
            <h1>Datos :D</h1>
        </header>
        <form action="RegisterGlucose" method="post">
            <fieldset>
                <legend>Datos Personales</legend>
                <!--Documento-->
                <div>
                    Paciente<input type="text" name="pacient" id="pacient" placeholder="Documento" pattern="[0-9]+" title="Sólo Números" required=""/>
                </div>
                <br>
                <div>
                    Tomada por<input type="text" name="personal" id="personal" placeholder="Documento" pattern="[0-9]+" title="Sólo Números" />
                </div>
                <br>
                <!--Genero-->
                <div>
                    Valor:<input type="text" name="data" id="data" placeholder="Valor" required=""/><br>
                </div> 
                <br>                
                <!--Fecha de Nacimiento-->               
                <div >
                    Fecha<input type="date" id="date" name="date" style="color:#000000;background:rgba(144, 144, 144, 0.25)">
                </div>
                <br>                
                <div>                    
                    <ul class="actions">
                        <li><input type="submit" value="Enviar"/></li>
                        <li><input type="reset" value="Limpiar" /></li>
                    </ul>
                </div>
        </form>
    </body>
</html>
