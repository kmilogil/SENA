<%-- 
    Document   : plantilla
    Created on : 27/02/2015, 12:22:03 PM
    Author     : kennross
--%>

<%@page import="modulo.usuarios.dto.DepartamentoDto"%>
<%@page import="modulo.ofertas.FOferta"%>
<%@page import="modulo.pedidos.dto.PedidoDto"%>
<%@page import="modulo.pedidos.FPedido"%>
<%@page import="modulo.usuarios.FUsuario"%>
<%@page import="modulo.usuarios.dto.PermisoDto"%>
<%@page import="modulo.usuarios.dao.PermisoDao"%>
<%@page import="modulo.usuarios.dto.RolDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modulo.usuarios.dto.UsuarioDto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    HttpSession miSesion = request.getSession(false);
    HttpSession miSesionRoles = request.getSession(false);

    UsuarioDto actualUsuario;
    ArrayList<RolDto> rolesActuales;

    actualUsuario = (UsuarioDto) miSesion.getAttribute("usuarioEntro");
    rolesActuales = (ArrayList<RolDto>) miSesionRoles.getAttribute("roles");

    if (actualUsuario == null) {
        response.sendRedirect("../index.jsp?msg=<strong><i class='glyphicon glyphicon-exclamation-sign'></i> ¡Ups!</strong> Inicie Sesión Primero.&tipoAlert=info");
    } else {
        RolDto primerRol = rolesActuales.get(0);

        FUsuario faUsu = new FUsuario();
        FPedido faPedi = new FPedido();
        FOferta faOfer = new FOferta();
        String pagActual = "indexp.jsp";

        // Validación para poder entrar
        boolean poderEntrar = false;

        for (RolDto rol : rolesActuales) {
            if (rol.getIdRol() == 1 || rol.getIdRol() == 2 || rol.getIdRol() == 3) {
                if (rol.getIdRol() == 3) {
                    pagActual = "indexadmin.jsp";
                }
                poderEntrar = true;
            }
        }

        if (poderEntrar) {

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" href="../img/favicon.ico">
        <link rel="stylesheet" type="text/css" href="../css/bootstrap.css">
        <link rel="stylesheet" type="text/css" href="../css/font-awesome.css">
        <script type="text/javascript" src="../js/jquery-1.11.2.js"></script>
        <script type="text/javascript" src="../js/bootstrap.js"></script>
        <script type="text/javascript" src="../js/ajax.js"></script>
        <script type="text/javascript" src="../js/Validacion.js"></script>
        <script type="text/javascript" src="../js/validacionesAjax.js"></script>
        <title>Mis Pedidos - Farmer's Market</title>
        <script type="text/javascript">
            $(document).ready(function() {
                // Initialize tooltip
                $('[data-toggle="tooltip"]').tooltip({
                    placement: 'top'
                });
            });
        </script>
    </head>
    <body>
        <div class="container">
            <!-- Banner Farmer's Market -->
            <div class="row">
                <div class="col-md-12">
                    <img src="../img/banner.jpg" alt="Banner de Farmer's Market" width="1000px">
                </div>
            </div>
            <!-- Fin del Banner  -->
            <br>
            <!-- Contenedor Principal de la Página -->
            <div class="row">
                <!-- Dashboard -->
                <div class="col-md-2" style="background: #FAFAFA; border-radius: 3px">
                    <!-- Información del Rol iniciado -->
                    <div class="media">
                        <div class="media-left">
                            <a href="#" data-toggle="modal" data-target="#modalSubirFoto">
                                <img class="media-object img-circle" width="50"                                    
                                     src="../img/avatars/user.png" alt="Mi foto de perfil">
                            </a>
                        </div>
                        <div class="media-body">
                            <p></p>
                            <h4 class="media-heading">
                                <%
                                    for (RolDto rol : rolesActuales) {
                                        if (rol.getIdRol() == 1) {


                                %>
                                Productor
                                <%      } else if (rol.getIdRol() == 2) {

                                %>
                                Cliente
                                <%                                                    }

                                    }
                                %>
                            </h4>
                            <%= actualUsuario.getNombres() + " " + actualUsuario.getApellidos()%>
                        </div>
                    </div>
                    <!-- Fin del rol iniciado -->
                    <hr>

                    <!-- Menú de navegación -->
                    <ul class="nav nav-pills nav-stacked" id="sesionPermisos">
                        <%
                            ArrayList<PermisoDto> listaPermisos;
                            listaPermisos = (ArrayList<PermisoDto>) faUsu.obtenerPermisosPorRol(primerRol.getIdRol());
                            for (PermisoDto temPermiso : listaPermisos) {
                        %>
                        <li role="presentation" class="
                            <% if (temPermiso.getUrl().equals(pagActual)) {
                                    out.print("active ");
                                }
                            %>
                            text-left">
                            <a href="<%= temPermiso.getUrl()%>"><%= temPermiso.getPermiso() + " " + temPermiso.getIcono()%></a>
                        </li>
                        <%
                            }
                        %>
                    </ul>
                    <!-- Fin del menú de navegación -->

                </div>
                <!-- Fin de la Dashboard -->


                <!-- Contenedor de Segundo-->
                <div class="col-md-10">
                    <!-- Menú de Sesion, buscar, idiomas y info -->
                    <nav class="navbar navbar-default">
                        <div class="container-fluid">

                            <div class="navbar-header">
                                <%
                                    for (RolDto rol : rolesActuales) {
                                        if (rol.getIdRol() == 1) {
                                            int ofertadas = faOfer.obtenerOfertasOfertadas(actualUsuario.getIdUsuario());
                                            int pedidos = faPedi.obtenerPedidosPendientesProductor(actualUsuario.getIdUsuario());
                                %>
                                <a href="mispedidos.jsp" class="navbar-brand text-success">
                                    Pedidos <span class="badge info"><%=pedidos%></span> 
                                </a>
                                <a href="misofertas.jsp" class="navbar-brand text-success">
                                    Ofertas <span class="badge"><%=ofertadas%></span>
                                </a>
                                <%
                                } else if (rol.getIdRol() == 2) {
                                    int pedidos = faPedi.obtenerPedidosPendientesCliente(actualUsuario.getIdUsuario());
                                %>
                                <a href="mispedidos.jsp" class="navbar-brand text-success">
                                    Pedidos en espera <span class="badge info"><%=pedidos%></span> 
                                </a>
                                <%
                                        }
                                    }
                                %>
                            </div>
                            <!-- Collect the nav links, forms, and other content for toggling -->
                            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                                <ul class="nav navbar-nav navbar-right">                                    
                                    <li><a href="#"><img src="../img/flag/ing/flag-ingles-16.png" alt="Cambiar idioma a Inglés" title="Cambiar idioma a Inglés"></a></li>
                                    <li><a href="#"><img src="../img/flag/spa/flag-spanis16.png" alt="Cambiar idioma a Español" title="Cambiar idioma a Español"></a></li>
                                    <li class="dropdown">
                                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"> <%= actualUsuario.getNombres() + " " + actualUsuario.getApellidos()%> <span class="fa fa-chevron-down"></span></a>
                                        <ul class="dropdown-menu" role="menu">
                                            <li class="text-center"><a href="../ControladorSesiones?op=salir">Cerrar Sesión</a></li>
                                            <li class="divider"></li>
                                            <li class="text-center"><a href="perfil.jsp">Mi Perfil</a></li>
                                            <li class="divider"></li>
                                            <li class="text-center"><a href="#" data-toggle="modal" data-target="#modalCambiarClave">Cambiar Contraseña</a></li>
                                            <li class="divider"></li>
                                            <li class="text-center"><a href="#">Ayuda <i class="fa fa-exclamation-circle"></i></a></li>
                                        </ul>
                                    </li>
                                </ul>                              

                            </div>
                        </div>
                    </nav>
                    <!-- Fin de menú de sesión, buscar, idiomas y info -->

                    <!-- Miga de pan -->
                    <ol class="breadcrumb">
                        <li><a href="indexp.jsp">Inicio</a></li>
                        <li class="active"><a href="ayuda.jsp">Ayuda</a></li>                    
                    </ol>
                    <!-- Fin de miga de pan -->

                    <!-- Mensajes de alertas -->
                    <%                        if (request.getParameter("msg") != null && request.getParameter("tipoAlert") != null) {
                    %>
                    <div class="alert alert-<%= request.getParameter("tipoAlert")%>" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <p class="text-center"><%= request.getParameter("msg")%></p>
                    </div>
                    <%
                        }
                    %>            
                    <!-- Fin de mensajes de alertas -->

                    <!-- Contenedor de contenido especifico -->
                    <div class="container-fluid">
                        <legend class="text-center">Preguntas frecuentes</legend> 
                        <div class="row">

                            <div class="col-md-3">
                                <div class="container-fluid">
                                    <img src="../img/ayudaoferta.jpg" class="img-circle " style="max-width: 100%;">
                                </div>
                            </div>
                            <div class="col-md-9">
                                <h2>¿Como ofertar mis productos?</h2>
                                <p>Asegurese de haber asociados unos productos a su usuario, esto lo puede verificar en la sección de mis productos
                                cada uno de los productos en esta sección cuenta con una opcion de oferta y otra de eliminacion si por alguna razon desea eliminarlo
                                en la opcion de oferta, encontrara lo que debe contener su oferta para poder ser publicada en el sistema.
                                Ingrese el precio al cual vendera su producto, seleccion la presentacion a la cual vendera sus productos, tenga en cuenta que los productos
                                seran vendidos segun la presentacion, segun la presentacion se ira descontando de su inventario una vez realize la oferta del producto.</p>
                            </div>                              
                        </div><br>
                        <div class="row">

                            <div class="col-md-3">
                                <div class="container-fluid">
                                    <img src="../img/ayudapedido.jpg" class="img-rounded" style="max-width: 100%;">
                                </div>
                            </div>
                            <div class="col-md-9">
                                <h2>¿Como realizo la compra de productos?</h2>
                                <p>Usted podra comprar los productos que usted desea el cual se encuentren en la sección de ofertas
                                Cada una de las ofertas contiene un producto a vender, usted podra seleccionar uno de estos y realizar la cantidad
                                que uste desea pedir del producto.
                                Tenga en cuenta que usted solo podra realizar 5 pedidos el cual se encuentren activos, usted puede revizar el estdo de sus pedidos
                                en mis pedidos y en el historial de pedidos.
                                Una vez usted halla agregado productos podra verlos en la seccion de cotización, esta seccion le permite hacer un balance de costos
                                permitiendo reducir la cantidad que usted halla indicado de un producto especifico, o podra eliminar el producto de la cotizacion
                                Si esta a gusto con el pedido que va a realizar, puede proceder a realizar el pedido, el cual se notificara a los productos para poder hacer la debida
                                entrega de sus productos en un tiempo limite de 8 días.
                                </p>
                            </div>                              
                        </div><br>
                        <div class="row">

                            <div class="col-md-3">
                                <div class="container-fluid">
                                    <img src="../img/ayudapedido1.jpg" class="img-rounded" style="max-width: 100%;">
                                </div>
                            </div>
                            <div class="col-md-9">
                                <h2>¿Como realizo el pago del pedido que he realizado?</h2>
                                <p>Farmers market no maneja los pagos online, por lo cual usted podra realizar el pago de sus productos
                                una vez se le haga la entrega de los mismos por parte del producto,recuerde que el producto tiene un limite maximo
                                de 8 días para hacer la entrega de los productos que se le han pedido tras las primeras 24 horas de haber realizado el pedido
                                En caso de algun inconveniente puede contactar un administrador para recivir mayor informacion hacer de su pedido</p>
                            </div>                              
                        </div><br>
                        <div class="row">

                            <div class="col-md-3">
                                <div class="container-fluid">
                                    <img src="../img/ayudasoporte.jpg" class="img-circle " style="max-width: 100%;">
                                </div>
                            </div>
                            <div class="col-md-9">
                                <h2>Contactenos</h2>
                                <p>En caso de algun inconveniente o duda que no logre resolver en esta sección por favor contacte a alguno de nuestros administrador
                                podra contactarlo a traves del enlace que se encuentra la parte inferior derecha de la pantalla, por favor describa detalladamente cual 
                                fue su inconveniente y este atento a nuestra respuesta en su correo electronico.</p>
                            </div>                              
                        </div>
                    </div>
                    <!-- Fin de contenedor de contenido especifico -->


                    <!-- Ventanas Modales -->
                    <div class="container-fluid">
                        <!-- Cambiar Contraseña -->
                        <div>
                            <div class="modal fade" id="modalCambiarClave" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                            <h4 class="modal-title text-center" id="myModalLabel">Cambiar Contraseña</h4>
                                        </div>
                                        <div class="modal-body">
                                            <form class="form-horizontal" method="POST" action="../ControladorUsuarios" id="formCambiarClave">
                                                <div class="form-group has-feedback" id="inpClaveAntigua">
                                                    <label for="ccClaveAntigua" class="col-sm-4 control-label">Contraseña Actual</label>
                                                    <div class="col-sm-7">
                                                        <input type="password" class="form-control" 
                                                               id="ccClaveAntigua" placeholder="Ingrese la contraseña actual"
                                                               name="ccClaveAntigua" onblur="validarClaveEnCambiar(this)">
                                                        <!-- Al momento de validar, se le manda la class a la i para agregar icon-->
                                                        <i id="iconFeedbackClaveCambiar"></i>
                                                    </div>
                                                </div>

                                                <div class="form-group has-feedback" id="inpClaveNuevaCambiar">
                                                    <label for="ccClaveNueva" class="col-sm-4 control-label">Nueva Contraseña</label>
                                                    <div class="col-sm-7">                                                        
                                                        <input type="password" class="form-control" 
                                                               id="ccClaveNueva" placeholder="Ingrese una nueva contraseña"
                                                               name="ccClaveNueva" onblur="validarClaveNuevaEnCambiar(this)">
                                                        <!-- Al momento de validar, se le manda la class a la i para agregar icon-->
                                                        <i id="iconFeedbackClaveNuevaCambiar"></i>
                                                    </div>
                                                </div>

                                                <div class="form-group has-feedback" id="inpClaveRepetidaCambiar">
                                                    <label for="ccClaveRepetida" class="col-sm-4 control-label">Confirmar Contraseña</label>
                                                    <div class="col-sm-7">                                                        
                                                        <input type="password" class="form-control" 
                                                               id="ccClaveRepetida" placeholder="Ingrese una nueva contraseña"
                                                               name="ccClaveRepetida" onblur="validarRepetirClaveNuevaEnCambiar(this)">
                                                        <!-- Al momento de validar, se le manda la class a la i para agregar icon-->
                                                        <i id="iconFeedbackClaveNuevaCambiar2"></i>
                                                    </div>
                                                </div>
                                                <input hidden="true" name="ccDocumento" id="ccDocumento" value="<%=actualUsuario.getIdUsuario()%>">                                            
                                                <legend></legend>
                                                <div class="text-right">
                                                    <input type="button" class="btn btn-danger" data-dismiss="modal" value="Cancelar">
                                                    <input type="submit" name="cambiarPass" id="cambiarPass"class="btn btn-success" value="Cambiar contraseña">
                                                </div>
                                            </form>                                                                   
                                        </div>                                       
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- Fin de Cambiar Contraseña -->                        

                        <!-- Formulario de Contáctenos -->
                        <div>
                            <div class="modal fade" id="modalContactenos" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                            <h4 class="modal-title text-center" id="myModalLabel">Contáctenos | Farmer's Market</h4>
                                        </div>
                                        <div class="modal-body">
                                            <form class="form-horizontal" method="POST" action="../ControladorContacto" id="formContactenos">
                                                <div class="form-group" id="dmAsunto">
                                                    <label for="mcNombre" class="col-sm-2 control-label">Asunto</label>
                                                    <div class="col-sm-10">
                                                        <input type="text" class="form-control" name="mcAsunto"
                                                               id="mcAsunto">
                                                    </div>
                                                </div>                                

                                                <div class="form-group" id="dmMensaje">
                                                    <label for="inputPassword3" class="col-sm-2 control-label">Mensaje</label>
                                                    <div class="col-sm-10">
                                                        <textarea name="mcMensaje" id="mcMensaje" class="form-control" rows="4" placeholder="Ingrese su mensaje para la compañía Farmer's Market"></textarea>
                                                    </div>
                                                </div>
                                                <input hidden="true" name="mcViene" value="">
                                                <input hidden="true" name="usuario" value="<%=actualUsuario.getNombres() + " " + actualUsuario.getApellidos()%>">
                                                <input hidden="true" name="correo" value="<%=actualUsuario.getCorreo()%>">
                                                <div class="text-right">
                                                    <input type="submit" name="contactarAdmin" id="contactarAdmin"class="btn btn-success" value="Enviar Mensaje">
                                                </div>
                                            </form>
                                        </div>                                    
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- Fin de formulario de Contáctenos -->
                        <!--Modal detalle pedido cliente-->
                        <div class="modal fade" id="modalPedidocliente" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                        <h3 class="modal-title text-center">Detalle del pedido</h3>
                                    </div>
                                    <div class="modal-body" id="tablaDetalleCliente">

                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-primary" data-dismiss="modal">Cerrar</button>

                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--Fin modal pedido cliente-->
                        <!-- modal pedido productor-->
                        <div class="modal fade" id="modalPedidoProductor" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                        <h3 class="modal-title text-center">Detalle del pedido</h3>
                                    </div>
                                    <div class="modal-body" id="tablaDetalleProductor">

                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-primary" data-dismiss="modal">Cerrar</button>

                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--Fin modal pedido productor-->
                    </div>

                </div>
                <!-- Contenedor de Segundo-->
            </div>
            <!-- Fin de contenedor Principal de la Página -->
            <p></p>
            <!-- Footer (Nota: Escribir el código que permita que esto quede abajo fijo) -->
            <div class="row">
                <div class="col-md-13">
                    <!-- Footer (Nota: Escribir el código que permita que esto quede abajo fijo) -->
                    <ol class="breadcrumb container-fluid">                        
                        <em class="pull-right"><a href="#" data-toggle="modal" data-target="#modalContactenos">Contactar un Administrador</a></em>
                    </ol>

                </div>
            </div>
            <!-- Fin del Footer -->
        </div>
    </body>
</html>
<%

        }
    }
%>