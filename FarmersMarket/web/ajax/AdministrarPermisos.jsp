<%-- 
    Document   : AdministrarPermisos
    Created on : 13/05/2015, 08:02:48 PM
    Author     : User
--%>

<%@page import="modulo.usuarios.dto.PermisoDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modulo.usuarios.dto.RolDto"%>
<%@page import="modulo.usuarios.dto.UsuarioDto"%>
<%@page import="modulo.usuarios.FUsuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/Validacion.js"></script>
        <title>JSP Page</title>
    </head>
    <body>
        <%

            if (request.getParameter("idUsuario") != null) {
                FUsuario faUsu = new FUsuario();
                long idUsuario = Long.parseLong(request.getParameter("idUsuario"));
                UsuarioDto usuario = faUsu.obtenerUsuarioPorDocumento(idUsuario);
        %>

        <input type="hidden" name="idUsuarioCambio" id="idUsuarioCambio" value="<%=request.getParameter("idUsuario")%>">
        <input type="hidden" name="idRolCambio" id="idRolCambio" value="<%=usuario.getRol().getIdRol()%>">

        <%
        } else if (request.getParameter("idUsuarioSus") != null && request.getParameter("estado") != null) {

        %>
        <input type="hidden" name="idUsuario" id="idUsuario" value="<%=request.getParameter("idUsuarioSus")%>">
        <input type="hidden" name="estado" id="estado" value="<%=request.getParameter("estado")%>">
        <%
            }
        %>

    </body>
</html>
