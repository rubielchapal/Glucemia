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
        <form action="RegisterPersonal" method="post">
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
                    Cédula de Ciudadanía:<input type="text" name="ndivalue" id="ndivalue" placeholder="Documento" pattern="[0-9]+" title="Sólo Números" required=""/>
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
                <div>
                    Contraseña: <input type="password" name="password" id="password" placeholder="Contraseña" title="Min. 3" required=""/>
                </div>
            </fieldset>
            <fieldset>
                <legend>Datos de Rol</legend>
                <br>
                <!--Role-->
                <div>
                    Profesión: <select name="role" id="role">
                        <option value="61894003">Médico(a) Endocrinólogo(a)</option>
                        <option value="59058001">Médico(a) General</option>
                        <option value="398130009">Estudiante de Medicina</option>
                        <option value="309446002">Enfermero(a) Jefe</option>
                        <option value="310182000">Enfermero(a)</option>
                    </select> 
                </div>
                <br>
                <!--Teléfonos-->
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
                    Dirección Oficina <input type="text" id="line" placeholder="calle 1 # 2-3" name="line">
                    <select name="city" id="city">
                            <option value="Neiva">Neiva</option>
                            <option value="Aipe">Aipe</option>
                            <option value="Tello">Tello</option>
                    </select>
                </div>
                <br>
                <div>
                    EPS/IPS/Centro de Salud<input type="text" name="managingOrganization" id="managingOrganization" placeholder="CafeSalud" pattern="[A-Za-z]+" title="Sólo Letras" required=""/>
                </div>
                <br>               
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
