/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.usuarios;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modulo.usuarios.FUsuario;
import modulo.usuarios.dto.RolDto;
import modulo.usuarios.dto.UsuarioDto;
import org.apache.commons.codec.EncoderException;
import utilidades.Correo;
import utilidades.Encriptar;

/**
 *
 * @author Akashyro
 */
public class ControladorUsuarios extends HttpServlet {

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
        request.setCharacterEncoding("UTF-8");
        FUsuario faUsu = new FUsuario();
        UsuarioDto usuario;
        Encriptar encript;
        String salida;
        if (request.getParameter("botonRegistro") != null) {
            RolDto suRol = new RolDto();
            suRol.setIdRol(Integer.parseInt(request.getParameter("ruRol")));

            UsuarioDto nuevoUsuario = new UsuarioDto();
            nuevoUsuario.setIdUsuario(Long.parseLong(request.getParameter("ruDocumento")));
            nuevoUsuario.setNombres(request.getParameter("ruNombres"));
            nuevoUsuario.setApellidos(request.getParameter("ruApellidos"));
            nuevoUsuario.setClave(request.getParameter("ruClave"));
            nuevoUsuario.setCorreo(request.getParameter("ruCorreo"));
            nuevoUsuario.setFechaNacimiento(request.getParameter("ruFechaNacimiento"));
            nuevoUsuario.setDireccion(request.getParameter("ruDireccion"));
            nuevoUsuario.setIdCiudad(Integer.parseInt(request.getParameter("ruCiudad")));
            nuevoUsuario.setImagen(null);
            nuevoUsuario.setEstado(1);
            if (request.getParameter("notificaciones") == null) {

            } else if (request.getParameter("notificaciones") != null) {
                nuevoUsuario.setNotifaciones(1);
            }
            salida = faUsu.registrarUsuario(nuevoUsuario, suRol);

            if (salida.equals("ok")) {
                response.sendRedirect("index.jsp?msg=<strong><i class='glyphicon glyphicon-ok'></i> ¡Registro Éxitoso!</strong> puede iniciar sesión.&tipoAlert=success");
            } else if (salida.equals("okno")) {
                response.sendRedirect("index.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Algo salió mal!</strong> Por favor intentelo de nuevo.&tipoAlert=warning");
            } else {
                response.sendRedirect("index.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Ocurrió un error!</strong> Detalle: " + salida + "&tipoAlert=danger");
            }
        } else if (request.getParameter("recuperar") != null) {
            encript = new Encriptar();

            usuario = faUsu.obtenerUsuarioPorCorreo(request.getParameter("rcCorreo"));
            String correo = usuario.getCorreo();

            if (!correo.equals("")) {
                try {

                    String con = Long.toString(usuario.getIdUsuario());
                    String ced = encript.encode(con);

                    String url = "http://localhost:8088/FarmersMarket/index.jsp?id=" + (ced);
                    String mensaje = "<!DOCTYPE html>";
                    mensaje += "<body>";
                    mensaje += "<p>Que tal usuario por favor ingresa a la siguiente enlace para poder recuperar tu contraseña</p>";
                    mensaje += "<a href=" + url + ">Recuperar contraseña</a>";
                    if (Correo.sendMail("Recuperar Contraseña", mensaje, correo)) {
                        response.sendRedirect("index.jsp?msg=<strong><i class='glyphicon glyphicon-ok'></i> ¡Solicitud enviada!</strong> Por favor revise su correo.&tipoAlert=success");
                    } else {
                        response.sendRedirect("index.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Ha ocurrido algo!</strong> Vuelva a intentarlo.&tipoAlert=warning");
                    }

                } catch (EncoderException ex) {
                    response.sendRedirect("index.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Ocurrió un error!</strong> Detalle: " + ex.getMessage() + "&tipoAlert=danger");
                }
            } else if (correo.equals("")) {
                response.sendRedirect("index.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Lo sentimos!</strong> El correo no se encuentra registrado." + correo + "&tipoAlert=warning");
            }
        } else if (request.getParameter("recuperarCodigo") != null) {
            encript = new Encriptar();
            if (request.getParameter("rcCodigo").trim().equals(request.getParameter("codigo"))) {

                try {
                    String cedula = encript.Decode(request.getParameter("encriptacion"));
                    usuario = faUsu.obtenerUsuarioPorDocumento(Long.parseLong(cedula));
                    if (!usuario.getClave().equals("")) {
                        response.sendRedirect("index.jsp?msg=<strong><i class='glyphicon glyphicon-ok'></i> ¡Hemos recuperado tu contraseña :D es la siguiente: <i>" + usuario.getClave() + "</i>!</strong> te recomendamos  cambiarla lo antes posible.&tipoAlert=success");
                    } else {
                        response.sendRedirect("index.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Ha ocurrido algo!</strong> Vuelva a intentarlo.&tipoAlert=warning");
                    }
                } catch (Exception ex) {
                    response.sendRedirect("index.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Ocurrió un error!</strong> Detalle: " + ex.getMessage() + "&tipoAlert=danger");
                }
            } else {
                response.sendRedirect("index.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡El codigo que ingreso no es correcto!</strong> Intentelo de nuevo.&tipoAlert=warning");
            }
        } else if (request.getParameter("cambiarPass") != null) {

            usuario = faUsu.obtenerUsuarioPorDocumento(Long.parseLong(request.getParameter("ccDocumento")));
            boolean admin = false;
            HttpSession miSesionRoles = request.getSession(false);
            ArrayList<RolDto> rolesActuales;
            rolesActuales = (ArrayList<RolDto>) miSesionRoles.getAttribute("roles");
            for (RolDto rol : rolesActuales) {
                if (rol.getIdRol() == 3) {
                    admin = true;
                }
            }
            if (usuario.getClave().equals(request.getParameter("ccClaveAntigua"))) {
                salida = faUsu.cambiarContrasena(request.getParameter("ccClaveNueva"), Long.parseLong(request.getParameter("ccDocumento")));
                if (salida.equals("ok")) {
                    if (admin) {
                        response.sendRedirect("pages/indexadmin.jsp?msg=<strong><i class='glyphicon glyphicon-ok'></i> ¡Su contraseña a sido modificada!</strong>&tipoAlert=success");
                    } else {
                        response.sendRedirect("pages/indexp.jsp?msg=<strong><i class='glyphicon glyphicon-ok'></i> ¡Su contraseña a sido modificada!</strong>&tipoAlert=success");
                    }
                } else if (salida.equals("okno")) {
                    response.sendRedirect("pages/indexp.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Ha ocurrido algo!</strong> Vuelva a intentarlo.&tipoAlert=warning");
                }

            } else {
                if (admin) {
                    response.sendRedirect("pages/indexadmin.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡La contraseña no es correcta!</strong> Vuelva a intentarlo.&tipoAlert=warning");
                } else {
                    response.sendRedirect("pages/indexp.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡La contraseña no es correcta!</strong> Vuelva a intentarlo.&tipoAlert=warning");
                }
            }
        } else if (request.getParameter("idUsuarioConsulta") != null) {

            UsuarioDto informacion = faUsu.obtenerUsuarioPorDocumento(Long.parseLong(request.getParameter("idUsuarioConsulta")));
            HttpSession miSesion = request.getSession(true);
            miSesion.setAttribute("infUsuario", informacion);
            response.sendRedirect("pages/informacionusuario.jsp");
        } else if (request.getParameter("actualizarDatos") != null) {
            String correo = faUsu.correoRegistrado(request.getParameter("pffCorreo"), Long.parseLong(request.getParameter("auDocumento")));
            if (correo.equals("")) {
                String mensaje = faUsu.actualizarDatos(request.getParameter("pffDireccion"), request.getParameter("pffCorreo"), Long.parseLong(request.getParameter("auDocumento")));
                if (mensaje.equals("ok")) {
                    response.sendRedirect("pages/perfil.jsp?msg=<strong><i class='glyphicon glyphicon-ok'></i> ¡Sus datos han sido modificados!</strong>&tipoAlert=success");
                    HttpSession miSesion = request.getSession(false);
                    UsuarioDto datosPersona = faUsu.obtenerUsuarioPorDocumento(Long.parseLong(request.getParameter("auDocumento")));
                    miSesion.setAttribute("usuarioEntro", datosPersona);
                } else {
                    response.sendRedirect("pages/perfil.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡No se logro actualizar sus datos!</strong> Vuelva a intentarlo.&tipoAlert=warning");
                }
            } else {
                response.sendRedirect("pages/perfil.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡El correo ya se encuentra registrado!</strong> Vuelva a intentarlo.&tipoAlert=warning");
            }
        } else {
            response.sendRedirect("pages/indexp.jsp");
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
