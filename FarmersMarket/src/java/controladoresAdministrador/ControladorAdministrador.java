/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladoresAdministrador;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modulo.usuarios.FUsuario;

/**
 *
 * @author User
 */
public class ControladorAdministrador extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        FUsuario faUsu = new FUsuario();
        String salida = "";
        if (request.getParameter("cambiarEstado") != null) {

            int resultado = faUsu.cambiarRol(Long.parseLong(request.getParameter("idUsuarioCambio")), Integer.parseInt(request.getParameter("idRolCambio")));
            if (resultado == 0) {
                response.sendRedirect("pages/permisos.jsp?msg=<strong>¡Cambio de rol exitoso! <i class='glyphicon glyphicon-ok'></i></strong>&tipoAlert=success");
            } else {
                response.sendRedirect("pages/permisos.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Algo salió mal!</strong> Por favor intentelo de nuevo.&tipoAlert=warning");
            }

        } else if (request.getParameter("suspender") != null) {

            if (Integer.parseInt(request.getParameter("estado")) == 3) {
                salida = faUsu.activarUsuario(Long.parseLong(request.getParameter("idUsuario")));
                if (salida.equals("ok")) {
                    response.sendRedirect("pages/permisos.jsp?msg=<strong>¡Usuario Activado! <i class='glyphicon glyphicon-ok'></i></strong>&tipoAlert=success");
                } else if (salida.equals("no")) {
                    response.sendRedirect("pages/permisos.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Algo salió mal!</strong> Por favor intentelo de nuevo.&tipoAlert=warning");
                } else {
                    response.sendRedirect("pages/permisos.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Ocurrió un error!</strong> Detalle: " + salida + "&tipoAlert=danger");
                }
            } else {

                salida = faUsu.suspenderUsuario(Long.parseLong(request.getParameter("idUsuario")));
                if (salida.equals("ok")) {
                    response.sendRedirect("pages/permisos.jsp?msg=<strong>¡Usuario suspendido! <i class='glyphicon glyphicon-ok'></i></strong>&tipoAlert=success");
                } else if (salida.equals("no")) {
                    response.sendRedirect("pages/permisos.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Algo salió mal!</strong> Por favor intentelo de nuevo.&tipoAlert=warning");
                } else {
                    response.sendRedirect("pages/permisos.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Ocurrió un error!</strong> Detalle: " + salida + "&tipoAlert=danger");
                }
            }
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
