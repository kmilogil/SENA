<%-- 
    Document   : eliminarProducto
    Created on : 29/04/2015, 07:23:07 PM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            if (request.getParameter("idproducto") != null) {

        %>
        <div class="modal-header">
            <h4 class="modal-title text-center" id="myModalLabel">¿Está seguro que desea eliminar este producto?</h4>
        </div>
        <form action="../ControladorOferta" method="post">
            <input type="hidden" name="idProducto" id="idProducto" value="<%=request.getParameter("idproducto")%>">
            <div class="modal-footer">
                <center>
                    <button type="submit" class="btn btn-success" name="eliminarProducto" id="eliminarProducto" >Sí</button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">No</button>                                                
                </center>                                            
            </div>
        </form>
        <%                }

        %>
    </body>
</html>
