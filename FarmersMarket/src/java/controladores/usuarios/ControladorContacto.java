/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.usuarios;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modulo.usuarios.dto.RolDto;
import utilidades.CorreoContactenos;

/**
 *
 * @author User
 */
public class ControladorContacto extends HttpServlet {

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

        if (request.getParameter("contactar") != null) {
            String persona = request.getParameter("mcNombre");
            String correoElectronico = request.getParameter("mcCorreo");
            String texto = request.getParameter("mcMensaje");

            String mensaje = "<!DOCTYPE html>";
            mensaje += "<body>";
            mensaje +="<p>En el siguiente correo podra encontrar la persona que ha enviado su inquitud, queja o reclamo, su correspondiente correo"
                    + " y la inquitud, queja o reclamo que nos quiere dar a conocer</p>";
            mensaje += "<p class=" + "pull-right" + ">Nombre de la persona:<br><Strong>" + persona + "</Strong></p>";
            mensaje += "<p>Correo de la persona:<br><Strong>" + correoElectronico + "</Strong></p>";
            mensaje += "<p>" + texto + "</p>";
            mensaje += "<p>Podra dar respuesta al usuario a traves del correo que el proporciona, asegurece de por aclarar todas las inquietudes del usuario"
                    + "</p>";
            mensaje += "-----------------------------------------------------------------------------------------------------------------";
            mensaje += "<p>Por favor no responder a este correo, es de uso exclusivo para notificaciones y solicitudes."
                    + "<br>No se le dara una respuesta por motivos de administración y seguridad</p>";
            mensaje += "<a href='http://localhost:8080/FarmersMarket/index.jsp'>Farmers Market</a>";
            mensaje += "<br>-----------------------------------------------------------------------------------------------------------------";

            if (CorreoContactenos.sendMail("Inquietud cliente", mensaje, "mercadosagricultor@gmail.com")) {
                response.sendRedirect("index.jsp?msg=<strong><i class='glyphicon glyphicon-ok'></i> ¡Mensaje enviado!</strong> Por favor este atento a nuestro respuesta.&tipoAlert=success");

            } else {

                response.sendRedirect("index.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Ha ocurrido algo!</strong> Por favor intentelo de nuevo.&tipoAlert=warning");
            }

        } else if (request.getParameter("contactarAdmin") != null) {

            String persona = request.getParameter("usuario");
            String correoElectronico = request.getParameter("correo");
            String texto = request.getParameter("mcMensaje");
            String asunto = request.getParameter("mcAsunto");

            String mensaje = "<!DOCTYPE html>";
            mensaje += "<body>";
            mensaje += "<p class=" + "pull-right" + ">Nombre del usuario:<br><Strong>" + persona + "</Strong></p>";
            mensaje += "<p>Correo de la persona:<br><Strong>" + correoElectronico + "</Strong></p>";
            mensaje += "<p>" + texto + "</p>";
            mensaje += "<p>Podra dar respueta al usuario a traves del correo que el proporciona, asegurece de por aclarar todas las inquietudes del usuario"
                    + "</p>";

            if (CorreoContactenos.sendMail(asunto, mensaje, "mercadosagricultor@gmail.com")) {
                response.sendRedirect("pages/indexp.jsp?msg=<strong><i class='glyphicon glyphicon-ok'></i> ¡Mensaje enviado!</strong> Por favor este atento a nuestra respuesta.&tipoAlert=success");
            } else {
                response.sendRedirect("pages/indexp.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Ha ocurrido algo!</strong> Por favor intentelo de nuevo.&tipoAlert=warning");
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
