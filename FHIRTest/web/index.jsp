<%-- 
    Document   : index
    Created on : 8/05/2016, 06:05:15 PM
    Author     : Alejo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>  
<%@taglib prefix="t" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">        
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <script>
            function showForm(){
                var perfil = document.getElementById("demo-category");
                var valor = perfil.options[perfil.selectedIndex].value;
                var div1 = document.getElementById("n1");
                var div2 = document.getElementById("n2");
                if (valor == 1){				
                    div1.style.display = 'block'; 
                }
                else if(valor == 2){
                    div2.style.display = 'block'; 
                    div1.style.display = 'none';
                }
                else {
                    div1.style.display = 'none';
                    div2.style.display = 'none';
                }
            }
            function showReg(){
                var registro = document.getElementById("reg");
                registro.style.display = 'block'; 
            }
        </script>
        <title>INICIO :3</title>
    </head>
    <body>
        <header id="header"> 
            <h1>Hola, por favor Ingrese o Registrese</h1>
        </header>
        <article id="main">
            <section>                
            <p><input type="submit" value="Iniciar Sesión" onclick="showLog()" /></p>
            <p><input type="submit" value="Registrarse" onclick="showReg()" /></p> 
            </section>
            <section id="reg" style="display:none">
                    <div>
                        <section>
                            <h4>Selecciona tu perfil</h4>
                            <div>											
                                <div>
                                    <div>
                                        <select name="profile" id="demo-category">
                                            <option value="0">- Perfil -</option>
                                            <option value="1">Paciente</option>
                                            <option value="2">Personal</option>
                                        </select>
                                    </div>
                                </div>
                                <div >
                                    <ul>
                                        <li><input type="submit" value="Cargar Formulario" onclick="showForm()" /></li>
                                    </ul>
                                </div>
                            </div>
                        </section>
                        <hr />
                        <section id="n1" style="display:none">
                            <%@ include file="paciente.jsp" %>
                        </section>
                        <section id="n2" style="display:none">
                            <%@ include file="personal.jsp" %>                        
                        </section>
                    </div>
                </section>
            </article>       
    </body>
</html>
