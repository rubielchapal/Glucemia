<%-- 
    Document   : personal
    Created on : 9/05/2016, 10:48:51 PM
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
        <form action="Register" method="post">
            <fieldset>
                <legend>Datos Personales</legend>
                <!--Nombres-->
                <div>
                    Nombre(s) <input type="text" name="given" id="given" placeholder="Alejandro" pattern="[A-Za-z-\s]+" title="Sólo Letras" required=""/>
                </div>
                <br>
                <div>
                    Apellido(s) <input type="text" name="family" id="family" placeholder="Mora Gómez" pattern="[A-Za-z-\s]+" title="Sólo Letras" required=""/>
                </div>
                <br>
                <!--Documento-->
                <div>                    
                    <select name="ndi" id="ndi">
                        <option value="Cédula de Ciudadanía">C.C.</option>
                        <option value="Tarjeta de Identidad">T.I.</option>
                        <option value="Registro Civil">R.C.</option>
                    </select>
                    <input type="text" name="ndivalue" id="ndivalue" placeholder="Documento" pattern="[0-9]+" title="Sólo Números" required=""/>
                </div>
                <br>
                <!--Genero-->
                <div>
                    Género: 
                    <label for="male">Hombre</label>
                    <input type="radio" name="gender" id="male" value="male"><br>
                    <label for="female">Mujer</label>
                    <input type="radio" name="gender" id="female" value="female"><br>
                    <label for="other">Otro</label>
                    <input type="radio" name="gender" id="other" value="other"><br><br>
                </div> 
                <br>                
                <!--Fecha de Nacimiento-->               
                <div >
                    Fecha de Nacimiento <input type="date" id="birthDate" name="birthDate" style="color:#000000;background:rgba(144, 144, 144, 0.25)">
                </div>
                <br>
                <!--Estado Civil-->
                <div>
                    Estado Civil: <select name="maritalStatus" id="maritalStatus">
                        <option value="M">Casado(a)</option>
                        <option value="U">Soltero(a)</option>
                        <option value="T">Unión Libre</option>
                        <option value="W">Viudo(a)</option>
                        <option value="L">Separado(a)</option>
                    </select> 
                </div>
                <br>
                <div>
                    Contraseña: <input type="password" name="password" id="password" placeholder="Contraseña" title="Min. 3" required=""/>
                </div>
            </fieldset>
            <fieldset>
                <legend>Datos de Contacto</legend>
                <br>
                <!--Teléfonos-->
                <div >
                    Tel.Casa <input type="text" id="telhome"  placeholder="Ej. 8700000" name="telhome" pattern="[0-9]+" title="Sólo números">                                        
                </div>
                <br>
                <div>
                    Tel.Móvil <input type="text" id="telmobile" placeholder="Ej. 3201234567" name="telmobile" pattern="[0-9]+" title="Sólo números" required="">                                        
                </div>
                <br>
                <div>
                    Tel.Oficina <input type="text" id="telwork" placeholder="Ej. 8700000" name="telwork" pattern="[0-9]+" title="Sólo números">
                </div>
                <br>
                <div >
                    Correo Electrónico <input type="email" id="email" placeholder="tucorreo@corre.com" name="email">
                </div>
                <br>
                <!--Dirección-->
                <div>
                    Dirección <input type="text" id="line" placeholder="calle 1 # 2-3" name="line">
                    <select name="city" id="city">
                            <option value="Neiva">Neiva</option>
                            <option value="Aipe">Aipe</option>
                            <option value="Tello">Tello</option>
                    </select>
                </div>
                <br>
                <!--Contacto-->
                <div>
                    <h3>Contactar a:</h3>                    
                    <div>
                    Nombre(s) <input type="text" name="givenc" id="givenc" placeholder="Alejandro" pattern="[A-Za-z]+" title="Sólo Letras" required=""/>
                    </div>
                    <br>
                    <div>
                        Apellido(s) <input type="text" name="familyc" id="familyc" placeholder="Mora Gómez" pattern="[A-Za-z]+" title="Sólo Letras" required=""/>
                    </div> 
                    <br> 
                    <div>
                        Teléfono:<input type="text" id="telc" placeholder="Ej. 8700000" name="telc" pattern="[0-9]+" title="Sólo números">
                    </div>
                    <br>
                    <select name="relationship" id="relationship">
                            <option value="family">Familiar</option>
                            <option value="partner">Pareja</option>
                            <option value="friend">Amigo</option>
                    </select>
                </div>
                <br>               
            </fieldset>
            <fieldset>
                <legend>EPS/IPS/Centro de Salud</legend>                
                <br>
                <div>
                    <input type="text" name="managingOrganization" id="managingOrganization" placeholder="CafeSalud" pattern="[A-Za-z]+" title="Sólo Letras" required=""/>
                </div>
            </fieldset>
                <div>                    
                    <ul class="actions">
                        <li><input type="submit" value="Enviar"/></li>
                        <li><input type="reset" value="Limpiar" /></li>
                    </ul>
                </div>
        </form>
    </body>
</html>
