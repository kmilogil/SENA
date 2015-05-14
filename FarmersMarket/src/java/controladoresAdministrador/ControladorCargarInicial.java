/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladoresAdministrador;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utilidades.CargaInicial;

/**
 *
 * @author User
 */
public class ControladorCargarInicial extends HttpServlet {

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
            throws ServletException, IOException, FileUploadException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        DiskFileItemFactory ff = new DiskFileItemFactory();

        ServletFileUpload sfu = new ServletFileUpload(ff);

        List items = null;
        File archivo = null;
        try {
            items = sfu.parseRequest(request);
        } catch (FileUploadException ex) {
            out.print(ex.getMessage());
        }
        String nombre = "";
        FileItem item = null;
        for (int i = 0; i < items.size(); i++) {

            item = (FileItem) items.get(i);

            if (!item.isFormField()) {
                nombre = item.getName();
                archivo = new File(this.getServletContext().getRealPath("/archivos/") + "/" + item.getName());
                try {
                    item.write(archivo);
                } catch (Exception ex) {
                    out.print(ex.getMessage());
                }
            }
        }
        CargaInicial carga = new CargaInicial();
        String ubicacion = archivo.toString();
        int cargado = carga.cargar(ubicacion);
        if (cargado == 1) {
            response.sendRedirect("pages/indexadmin.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡No se encontro el archivo!</strong> Por favor intentelo de nuevo.&tipoAlert=warning");
        } else {
            response.sendRedirect("pages/indexadmin.jsp?msg=<strong>¡Carga inicial exitosa! <i class='glyphicon glyphicon-ok'></i></strong>&tipoAlert=success");
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
        try {
            processRequest(request, response);
        } catch (FileUploadException ex) {
            Logger.getLogger(ControladorCargarInicial.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (FileUploadException ex) {
            Logger.getLogger(ControladorCargarInicial.class.getName()).log(Level.SEVERE, null, ex);
        }
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
