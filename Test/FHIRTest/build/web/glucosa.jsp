<%-- 
    Document   : glucosa
    Created on : 5/06/2016, 01:42:02 PM
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
                <!--Paciente-->
                <div>
                    Paciente<input type="text" name="pacient" id="pacient" placeholder="Documento" pattern="[0-9]+" title="Sólo Números" required=""/>
                </div>
                <br>
                <!--Personal-->
                <div>
                    Tomada por<input type="text" name="personal" id="personal" placeholder="Documento" pattern="[0-9]+" title="Sólo Números" />
                </div>
                <br>
                <!--Valor muestra-->
                <div>
                    Valor:<input type="text" name="data" id="data" placeholder="Valor" required=""/><br>
                </div> 
                <br>                
                <!--Fecha de Ingreso-->               
                <div >
                    Fecha<input type="datetime-local" id="date" name="date" style="color:#000000;background:rgba(144, 144, 144, 0.25)" value="YYYY-MM-DDThh:mm">
                </div>
                <br>                
                <!--Estado-->               
                <div >
                    Fecha
                    <select name="state" id="state">
                        <option value="Antes de Desayunar">Antes de Desayunar</option>
                        <option value="Despues de Desayunar">Despues de Desayunar</option>
                        <option value="Antes de Almorzar">Antes de Almorzar</option>
                        <option value="Despues de Almorzar">Despues de Almorzar</option>
                        <option value="Antes de Cenar">Antes de Cenar</option>
                        <option value="Despues de Cenar">Después de Cenar</option>
                        <option value="Antes de Dormir">Antes de Dormir</option>
                        <option value="Despues de Dormir">Después de Dormir</option>
                        <option value="Ayunas">Ayunas</option>                        
                    </select>
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
