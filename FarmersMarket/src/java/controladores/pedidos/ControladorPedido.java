/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.pedidos;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modulo.pedidos.FPedido;
import modulo.pedidos.dto.CarritoDto;
import modulo.pedidos.dto.ProductoCarritoDto;
import modulo.usuarios.FUsuario;
import modulo.usuarios.dto.UsuarioDto;
import utilidades.Correo;

/**
 *
 * @author User
 */
public class ControladorPedido extends HttpServlet {

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
        FPedido faPedi = null;
        int salida = 0;
        String mensaje = "";
        if (request.getParameter("pedirProducto") != null) {
            faPedi = new FPedido();
            salida = faPedi.crearPedido(Long.parseLong(request.getParameter("usuario")), Integer.parseInt(request.getParameter("idOfer")),
                    Integer.parseInt(request.getParameter("cantidadPedir")));

            if (salida == 1 || salida == 2) {
                response.sendRedirect("pages/ofertas.jsp?msg=<strong>¡Su pedido fue añadido a la cotización.! <i class='glyphicon glyphicon-ok'></i></strong>.&tipoAlert=success");

            } else if (salida == 3) {
                response.sendRedirect("pages/ofertas.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Usted ya tiene 5 pedidos en espera!</strong> &tipoAlert=warning");

            } else if (salida == 0) {
                response.sendRedirect("pages/ofertas.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡No se logro guardar su pedido!</strong> No hay suficiente cantidad de ese producto &tipoAlert=warning");
            }
        } else if (request.getParameter("idOferta") != null) {
            faPedi = new FPedido();
            mensaje = faPedi.eliminarDelCarrito(Integer.parseInt(request.getParameter("idOferta")));
            if (mensaje.equals("ok")) {
                response.sendRedirect("pages/carrito.jsp?msg=<strong>¡Se ha eliminado el producto! <i class='glyphicon glyphicon-ok'></i></strong> &tipoAlert=success");
            } else if (mensaje.equals("okno")) {
                response.sendRedirect("pages/carrito.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡No se logro eliminar su pedido!</strong> &tipoAlert=warning");

            } else {
                response.sendRedirect("pages/carrito.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Ocurrió un error!</strong> Detalle: " + mensaje + "&tipoAlert=danger");
            }

        } else if (request.getParameter("realizarPedidoFinal") != null) {
            faPedi = new FPedido();
            ArrayList<CarritoDto> carrito = (ArrayList<CarritoDto>) faPedi.obtenerCarrito(Long.parseLong(request.getParameter("idCliente")));
            String[] salidaCa = null;
            for (int i = 0; i < carrito.size(); i++) {
                if (carrito.get(i).getCantidad() > carrito.get(i).getOfDto().getInDto().getCantidad()) {
                    salidaCa = new String[carrito.size()];
                    salidaCa[0] = "no";
                    break;

                } else if (carrito.get(i).getCantidad() <= carrito.get(i).getOfDto().getInDto().getCantidad()) {
                    salidaCa = new String[carrito.size()];
                    salidaCa[0] = "si";
                }
            }
            int i = 0;
            int idPedido = carrito.get(i).getIdPedido();
            String v = "";
            for (String salida1 : salidaCa) {
                if (salida1 != null) {
                    v = salida1;
                }
            }
            if (v.equals("no")) {
                response.sendRedirect("pages/carrito.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Verifique la cantidad solicitada de uno de sus pedidos excede la disponible!</strong> Verifique su pedidos&tipoAlert=warning");
            } else if (v.equals("si")) {
                faPedi.descontarInventario(carrito);
                faPedi.activarPedido(idPedido);

                if (enviarCorreo(Long.parseLong(request.getParameter("idCliente")))) {
                    response.sendRedirect("pages/carrito.jsp?msg=<strong>¡Se realizado su pedido y se le envio un correo! <i class='glyphicon glyphicon-ok'></i></strong> Tiene 24 horas disponibles, si desea cancelarlo.&tipoAlert=success");
                } else {
                    response.sendRedirect("pages/carrito.jsp?msg=<strong>¡Se realizado su pedido! <i class='glyphicon glyphicon-ok'></i></strong> Tiene 24 horas disponibles, si desea cancelarlo.&tipoAlert=success");
                }
            }
        } else if (request.getParameter("idPedido") != null) {
            faPedi = new FPedido();
            int estado = faPedi.obtenerEstadoPedido(Integer.parseInt(request.getParameter("idPedido")));
            if (estado == 2) {
                ArrayList<String> correos = (ArrayList<String>) faPedi.buscarCorreos(Integer.parseInt(request.getParameter("idPedido")));
                StringBuilder correo = new StringBuilder("");
                for (int i = 0; i < correos.size(); i++) {
                    correo.append(correos.get(i));
                    if (correos.size() > i) {
                        correo.append(", ");
                    }
                }
                String cuerpo = "";
                HttpSession miSesion = request.getSession(false);
                UsuarioDto usuario = (UsuarioDto) miSesion.getAttribute("usuarioEntro");
                cuerpo += "<h3>Estimado Usuario:</h3>";
                cuerpo += "<p>Le notificamos que uno de sus pedidos a sido cancelado.</p>";
                cuerpo += "<p>Le proporcionamos la información del usuario en caso de que desee contactarse con el:</p>";
                cuerpo += "<p>Nombre del cliente: " + usuario.getNombres() + " " + usuario.getApellidos() + " </p>";
                cuerpo += "<p>Dirección de residencia: " + usuario.getDireccion() + "</p>";
                cuerpo += "<p>Dirección de correo eletronico: " + usuario.getCorreo() + "</p>";
                cuerpo += "<p>Ciudad: " + usuario.getCiDto().getNombre() + " en el departamento de " + usuario.getCiDto().getDepartamento().getDepartamento() + "</p>";
                cuerpo += "<p>Para saber mas sobre los detalles del pedido cancelado por favor ingrese en nuestro sistema y en la sección del historial encontra el pedido que le fue cancelado</p>";
                cuerpo += "<br>-----------------------------------------------------------------------------------------------------------------";
                cuerpo += "<p>Por favor no responder a este correo, es de uso exclusivo para notificaciones y solicitudes."
                        + "<br>No se le dara una respuesta por motivos de administración y seguridad, si tiene algun inconveniente por favor"
                        + " contacte a nuestros administradores desde nuestro sistema en el siguiente enlace</p>";
                cuerpo += "<a href='http://localhost:8080/FarmersMarket/index.jsp'>Farmers Market</a>";
                cuerpo += "<br>-----------------------------------------------------------------------------------------------------------------";

                String enviado = "";
                ArrayList<CarritoDto> ofertas = (ArrayList<CarritoDto>) faPedi.obtenerDetallePedidosCliente(Integer.parseInt(request.getParameter("idPedido")));
                mensaje = faPedi.cancelarPedido(Integer.parseInt(request.getParameter("idPedido")), ofertas);
                if (Correo.sendMail("Cancelacion del pedido", cuerpo, correo.toString())) {
                    enviado = " y notificado";
                }
                if (mensaje.equals("ok")) {
                    response.sendRedirect("pages/mispedidos.jsp?msg=<strong>¡El pedido ha sido cancelado" + enviado + " ! <i class='glyphicon glyphicon-ok'></i></strong> .&tipoAlert=success");
                } else if (estado == 3 || estado == 4) {
                    response.sendRedirect("pages/mispedidos.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡El pedido ya se encuentra cancelado!</strong>&tipoAlert=warning");
                } else if (mensaje.equals("no")) {
                    response.sendRedirect("pages/mispedidos.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡No se logro cancelar su pedido!</strong> Ya pasaron las 24 horas de diponibilidad para cancelarlo&tipoAlert=warning");
                }

            }
        }
    }

    private boolean enviarCorreo(long documento) {
        FUsuario usuariof = new FUsuario();
        String correo = usuariof.obtenerCorreoPorDocumento(documento);
        FPedido pedido = new FPedido();
        List<ProductoCarritoDto> productos;
        productos = pedido.obtenerUltimopedido(documento);
        String cuerpo = "<!DOCTYPE html>";
        cuerpo += "<br>";
        cuerpo += "<br>";
        cuerpo += "<br>";
        cuerpo += "<body>";
        cuerpo += "<center>";
        cuerpo += "<h2> <Strong>Su Pedido</Strong></h2>";
        cuerpo += "<table>";
        cuerpo += "<tr>";
        cuerpo += "<th>Nombre producto";
        cuerpo += "</th>";
        cuerpo += "<th>Presentacion";
        cuerpo += "</th>";
        cuerpo += "<th>Cantidad";
        cuerpo += "</th>";
        cuerpo += "<th>Fecha Entrega";
        cuerpo += "</th>";
        cuerpo += "<th>Precio venta";
        cuerpo += "</th>";
        cuerpo += "<th>Total Producto";
        cuerpo += "</th>";
        cuerpo += "</tr>";

        long totalp = 0;
        for (ProductoCarritoDto product : productos) {
            totalp += product.getTotal();
            cuerpo += "<tr>";
            cuerpo += "<td>" + product.getNombre() + "</td>";
            cuerpo += "<td>" + product.getPresentacion() + "</td>";
            cuerpo += "<td>" + product.getCantidad() + "</td>";
            cuerpo += "<td>" + product.getFechaEntrega() + "</td>";
            cuerpo += "<td>" + product.getPrecioVenta() + "</td>";
            cuerpo += "<td>" + product.getTotal() + "</td>";
            cuerpo += "</tr>";
        }

        cuerpo += "</table>";
        cuerpo += "<br>El total de su pedido es: " + totalp;

        cuerpo += "</center></body></html>";

        return Correo.sendMail("Pedido activo", cuerpo, correo);

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
