/* 
 * Cargar Ciudades en Pos de un Departamento
 */

var xmlHttp;
function getSubcategorias(idDepartamento) {

    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "ajax/cargarCiudades.jsp?idDepartamento=" + idDepartamento;
    xmlHttp.onreadystatechange = resultadoCiudades;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoCiudades() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("ruCiudad").innerHTML = xmlHttp.responseText;
    }
}
/* 
 *  Fin de cargar Ciudades en Pos de un Departamento
 */

/* 
 * Cargar Permisos en Pos de un Rol
 */

var xmlHttp;
function getPermisos(idRol) {

    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/cargarPermisos.jsp?idRol=" + idRol;
    xmlHttp.onreadystatechange = resultadoPermisos;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoPermisos() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("sesionPermisos").innerHTML = xmlHttp.responseText;
    }
}
/* 
 *  Fin de cargar Permisos en Pos de un Rol
 */

/* 
 * Cargar Formulario de Ofertar Producto
 */

var xmlHttp;
function getFormOfertar(idProducoAso) {

    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/cargarPublicarOferta.jsp?idProducoAso=" + idProducoAso;
    xmlHttp.onreadystatechange = resultadoFormOfertar;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoFormOfertar() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("formularioPublicarOferta").innerHTML = xmlHttp.responseText;
    }
}
/* 
 *  Fin de cargar ofertar
 */




// Realizar Pedido
var xmlHttp;
function getPedido(idOferta) {

    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/cargarPedido.jsp?idOferta=" + idOferta;
    xmlHttp.onreadystatechange = resultadoPedido;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoPedido() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("formularioRealizarPedido").innerHTML = xmlHttp.responseText;
    }
}

var xmlHttp;
function getNumeroOfertas(idProducto, idProdAso, idProductor) {

    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/validarOferta.jsp?idProducto=" + idProducto + "&idProdAso=" + idProdAso + "&idProductor=" + idProductor;
    xmlHttp.onreadystatechange = resultadonumeroOferta;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadonumeroOferta() {
    if (xmlHttp.readyState === 4) {
        if (xmlHttp.responseText !== null) {
            document.getElementById("modalOferta").innerHTML = xmlHttp.responseText;

        }

    }
}

var xmlHttp;
function getAbastecimiento(idOferta) {

    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/cargarPedido.jsp?idOfer=" + idOferta;
    xmlHttp.onreadystatechange = resultadoAbastecer;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoAbastecer() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("formAbastecimiento").innerHTML = xmlHttp.responseText;
    }
}


var xmlHttp;
function getDetalleCliente(idPedido) {

    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/cargarDetalle.jsp?idpc=" + idPedido;
    xmlHttp.onreadystatechange = resultadoDetalleCliente;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoDetalleCliente() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("tablaDetalleCliente").innerHTML = xmlHttp.responseText;
    }
}

var xmlHttp;
function getDetalleProductor(idPedido, idUsuario) {

    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/cargarDetalle.jsp?idpp=" + idPedido + "&idus=" + idUsuario;
    xmlHttp.onreadystatechange = resultadoDetalleProductor;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoDetalleProductor() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("tablaDetalleProductor").innerHTML = xmlHttp.responseText;
    }
}

var xmlHttp;
function getHistorialProductor(idPedido, idUsuario) {

    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/cargarHistorial.jsp?idpp=" + idPedido + "&idus=" + idUsuario;
    xmlHttp.onreadystatechange = resultadoHistorialProductor;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoHistorialProductor() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("tablaHistorialProductor").innerHTML = xmlHttp.responseText;
    }
}

var xmlHttp;
function getHistorialCliente(idPedido) {

    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/cargarDetalle.jsp?idpc=" + idPedido;
    xmlHttp.onreadystatechange = resultadoHistorialCliente;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoHistorialCliente() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("tablaHistorialCliente").innerHTML = xmlHttp.responseText;
    }
}

var xmlHttp;
function getEliminar(idProducto) {


    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/eliminarProducto.jsp?idproducto=" + idProducto;
    xmlHttp.onreadystatechange = resultadoEliminar;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoEliminar() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("modalEliminarProducto").innerHTML = xmlHttp.responseText;
    }
}

var xmlHttp;
function getCancelarPedido(idPedido) {


    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/eliminarProducto.jsp?idPedido=" + idPedido;
    xmlHttp.onreadystatechange = resultadoCancelarPedido;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoCancelarPedido() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("cancelarPedidomodal").innerHTML = xmlHttp.responseText;
    }
}

var xmlHttp;
function getUsuarioAdmin(idUsuario) {


    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/AdministrarPermisos.jsp?idUsuario=" + idUsuario;
    xmlHttp.onreadystatechange = resultadoUsuarioAdmin;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoUsuarioAdmin() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("didUsuarioCambio").innerHTML = xmlHttp.responseText;
    }
}

var xmlHttp;
function getSuspencion(idUsuario, estado) {


    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/AdministrarPermisos.jsp?idUsuarioSus=" + idUsuario + "&estado=" + estado;
    xmlHttp.onreadystatechange = resultadoSuspencion;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoSuspencion() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("didUsuario").innerHTML = xmlHttp.responseText;
    }
}

var xmlHttp;
function getHistorialVentas() {


    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/cargaReportes.jsp?tblHistorial=1";
    xmlHttp.onreadystatechange = resultadoHistorial;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoHistorial() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("tablaReporte").innerHTML = xmlHttp.responseText;
    }
}


var xmlHttp;
function getVentasCiudad() {


    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert("El navegador no soporta Ajax!");
        return;
    }

    var url = "../ajax/cargaReportes.jsp?tblVentas=1";
    xmlHttp.onreadystatechange = resultadoCiudad;
    xmlHttp.open("GET", url, true);
    xmlHttp.send(null);

}

function resultadoCiudad() {
    if (xmlHttp.readyState === 4) {
        document.getElementById("tablaReporte").innerHTML = xmlHttp.responseText;
    }
}