<%-- 
    Document   : ReporteHistorico
    Created on : 14/05/2015, 11:10:22 PM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" href="../img/favicon.ico">
        <link rel="stylesheet" type="text/css" href="../css/bootstrap.css">
        <title>Historico ventas</title>
    </head>
    <body class="container text-center">
        
        <%
            HttpSession sesion = request.getSession(false);
            if (sesion.getAttribute("historico") != null) {
        %>
        <object type="application/pdf" data="http://localhost:8080/FarmersMarket/ControladorReportes" width="1000" height="650"></object>
        <%}%>
    </body>
</html>
