<%-- 
    Document   : cargaReportes
    Created on : 14/05/2015, 10:42:10 PM
    Author     : User
--%>

<%@page import="modulo.reportes.VentaCiudad"%>
<%@page import="modulo.reportes.HistoricoVentas"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modulo.reportes.Reportes"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">        
    </head>
    <body>
        <%

            if (request.getParameter("tblHistorial") != null) {
                HttpSession sesion = request.getSession(true);
                Reportes reporte = new Reportes();
                ArrayList<HistoricoVentas> historico = (ArrayList<HistoricoVentas>) reporte.historicoVentas();
                sesion.removeAttribute("ventas");
                sesion.setAttribute("historico", historico);
                if (historico.isEmpty()) {
        %>
        <div class="col-md-12">
            <div class="alert alert-info text-center">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <p>
                    <strong>No se encuentran ningun venta por el momento</strong>

                </p>
            </div>
        </div>
        <%
        } else {
        %>
        <h2 class="text-center">Historico de ventas</h2> <a href="../ControladorReportes" class="pull-right">Exportar a pdf</a>
        <table class="table table-hover" id="tblReport">
            <thead>
                <tr>
                    <th>Productor</th>
                    <th>Producto</th>
                    <th>Primer Mes</th>
                    <th>Segundo Mes</th>
                </tr>
            </thead>
            <tbody>
                <%                    for (HistoricoVentas historial : historico) {

                %>
                <tr>
                    <td><%=historial.getProductor()%></td>
                    <td><%=historial.getProducto()%></td>
                    <td><%=historial.getPrimerMes()%></td>
                    <td><%=historial.getSegundoMes()%></td>
                </tr>
                <%
                    }
                %>

            </tbody>
        </table> 

        <%
            }
        } else if (request.getParameter("tblVentas") != null) {
            HttpSession sesion = request.getSession(true);
            Reportes reporte = new Reportes();
            ArrayList<VentaCiudad> ventas = (ArrayList<VentaCiudad>) reporte.ventaCiudad();
            sesion.removeAttribute("historico");
            sesion.setAttribute("ventas", ventas);
            if (ventas.isEmpty()) {
        %>
        <div class="col-md-12">
            <div class="alert alert-info text-center">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <p>
                    <strong>No se encuentran ningun venta por el momento</strong>
                </p>
            </div>
        </div>
        <%
        } else {
        %>
        <h2 class="text-center">Ventas por ciudad</h2> <a href="../ControladorReportes" class="pull-right">Exportar a pdf</a>
        <table class="table table-hover" id="tblReport">
            <thead>
                <tr>
                    <th>Productor</th>
                    <th>Ciudad</th>
                    <th>Mes</th>
                    <th>Total Venta</th>
                </tr>
            </thead>
            <tbody>
                <%                    for (VentaCiudad venta : ventas) {

                %>
                <tr>
                    <td><%=venta.getProductor()%></td>
                    <td><%=venta.getCiudad()%></td>
                    <td><%=venta.getMes()%></td>
                    <td><%=venta.getTotal()%></td>
                </tr>
                <%
                    }
                %>

            </tbody>
        </table>
        <%                }
            }

        %>
    </body>
</html>
