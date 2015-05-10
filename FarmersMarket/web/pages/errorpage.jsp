<%-- 
    Document   : errorpage
    Created on : 29-abr-2015, 23:21:16
    Author     : LENOVO
--%>




<%@page import="modulo.usuarios.dto.UsuarioDto"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileNotFoundException"%>
<%@page contentType="text/html" isErrorPage="true" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>   
    <%
        UsuarioDto actualUsuario;
        HttpSession miSesion = request.getSession(false);
        actualUsuario = (UsuarioDto) miSesion.getAttribute("usuarioEntro");
    %>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            if (actualUsuario != null) {

        %>
        <link rel="shortcut icon" href="../img/favicon.ico">
        <link rel="stylesheet" type="text/css" href="../css/bootstrap.css">
        
        <%        } else if (actualUsuario == null) {

        %>
        <link rel="shortcut icon" href="img/favicon.ico">
        <link rel="stylesheet" type="text/css" href="css/bootstrap.css">    
        <%                }

        %>
        <title>Ha ocurrido algo!</title>
        
    </head>
    <body><%        if (actualUsuario != null) {


        %>
    <center>
        <%            if (request.getParameter("no") != null) {
        %>
        <h1 style="color:#2b542c">Pagina no encontrada!</h1>    
        <%
        } else {

        %>
        <h1 style="color:#2b542c">Error Interno!</h1>
        <%            }
        %>

        <h2>Contacte con el administrador para más información</h2>
        <h2><a href="../pages/indexp.jsp" class="alert-link">Regresar</a></h2>
        <img src="../img/errorpage.jpg" width="800" height="700">
    </center>

    <%            } else if (actualUsuario == null) {

    %>
    <center>
        <%            if (request.getParameter("no") != null) {
        %>
        <h1 style="color:#2b542c">Pagina no encontrada!</h1>    
        <%
        } else {

        %>
        <h1 style="color:#2b542c">Error Interno!</h1>
        <%            }
        %>

        <h2>Contacte con el administrador para más información</h2>
        <h2><a href="/FarmersMarket/index.jsp" class="alert-link">Regresar</a></h2>
        <img src="img/errorpage.jpg" width="800" height="700">
    </center>
    <%                }

    %>



</body>
</html>
