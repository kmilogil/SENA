/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulo.pedidos.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import modulo.pedidos.dto.CarritoDto;
import modulo.pedidos.dto.PedidoDto;
import modulo.pedidos.dto.ProductoCarritoDto;
import utilidades.Exceptions;

/**
 *
 * @author User
 */
public class PedidoDao {

    String mensaje = "";
    PreparedStatement pstm = null;
    CallableStatement ctm = null;
    ResultSet rs = null;
    int rtdo = 0;

    public int crearPedido(long idUsuario, int idOferta, int cantidad, Connection unaConexion) {
        try {
            ctm = unaConexion.prepareCall("call sp_pedidoCarrito(?,?,?,?)");
            ctm.setLong(1, idUsuario);
            ctm.setInt(2, idOferta);
            ctm.setInt(3, cantidad);
            ctm.registerOutParameter(4, Types.INTEGER);
            ctm.execute();
            rtdo = ctm.getInt(4);
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return rtdo;
    }

    public String activarPedido(int idPedido, Connection unaConexion) {
        try {
            pstm = unaConexion.prepareStatement("update pedidos set estadoPedido = 2, fechaPedido = curdate(), fechaEntrega = date_add(curdate(), interval 4 day) where idPedido = ?;");
            pstm.setInt(1, idPedido);
            rtdo = pstm.executeUpdate();
            if (rtdo != 0) {
                mensaje = "ok";
            } else {
                mensaje = "okno";
            }
        } catch (SQLException ex) {
            mensaje = ex.getMessage();
        }
        return mensaje;
    }

    public String calcularTotalPedido(int idPedido, Connection unaConexion) {
        String total = "";
        try {
            pstm = unaConexion.prepareStatement("select ceil((sum(c.cantidad * o.precioVenta) - sum((c.cantidad * o.precioVenta)*p.detalle))) as total \n"
                    + "from carrito c\n"
                    + "join pedidos pe on c.idPedido = pe.idPedido\n"
                    + "join ofertas o on c.idOferta = o.idOferta\n"
                    + "join promociones p on o.idPromocion = p.idPromocion\n"
                    + "where pe.idPedido = ? ;");
            pstm.setLong(1, idPedido);
            rs = pstm.executeQuery();
            while (rs.next()) {
                total = rs.getString("total");
            }
        } catch (SQLException ex) {
            mensaje = ex.getMessage();
        }
        return total;
    }

    public List obtenerPedidosCliente(long idCliente, Connection unaConexion) {
        ArrayList<PedidoDto> pedidos = new ArrayList();
        try {
            pstm = unaConexion.prepareStatement("select pe.idPedido ,pe.fechaEntrega as entrega, pe.estadoPedido as estado"
                    + ", ceil((sum(c.cantidad * o.precioVenta)  - sum((c.cantidad * o.precioVenta)*p.detalle))) as 'total'\n"
                    + "from carrito c\n"
                    + "join pedidos pe on c.idPedido = pe.idPedido\n"
                    + "join ofertas o on c.idOferta = o.idOferta\n"
                    + "join promociones p on o.idPromocion = p.idPromocion\n"
                    + "where pe.idCliente = ? and pe.idPedido not in (select ped.idPedido from pedidos as ped where ped.estadoPedido = 1) and pe.estadoPedido = 2"
                    + " group by pe.idPedido;");
            pstm.setLong(1, idCliente);
            rs = pstm.executeQuery();
            while (rs.next()) {
                PedidoDto pedido = new PedidoDto();
                pedido.setIdPedido(rs.getInt("idPedido"));
                pedido.setFechaEntrega(rs.getString("entrega"));
                pedido.setEstadoPedido(rs.getInt("estado"));
                pedido.setTotal(rs.getLong("total"));
                pedidos.add(pedido);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return pedidos;
    }

    public List obtenerHistorialCliente(long idCliente, Connection unaConexion) {
        ArrayList<PedidoDto> pedidos = new ArrayList();
        try {
            pstm = unaConexion.prepareStatement("select pe.idPedido ,pe.fechaEntrega as entrega, pe.estadoPedido as estado"
                    + ", ceil((sum(c.cantidad * o.precioVenta)  - sum((c.cantidad * o.precioVenta)*p.detalle))) as 'total'\n"
                    + "from carrito c\n"
                    + "join pedidos pe on c.idPedido = pe.idPedido\n"
                    + "join ofertas o on c.idOferta = o.idOferta\n"
                    + "join promociones p on o.idPromocion = p.idPromocion\n"
                    + "where pe.idCliente = ? and pe.idPedido not in (select ped.idPedido from pedidos as ped where ped.estadoPedido = 2) and pe.estadoPedido = 3 or pe.estadoPedido = 4"
                    + " group by pe.idPedido;");
            pstm.setLong(1, idCliente);
            rs = pstm.executeQuery();
            while (rs.next()) {
                PedidoDto pedido = new PedidoDto();
                pedido.setIdPedido(rs.getInt("idPedido"));
                pedido.setFechaEntrega(rs.getString("entrega"));
                pedido.setEstadoPedido(rs.getInt("estado"));
                pedido.setTotal(rs.getLong("total"));
                pedidos.add(pedido);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return pedidos;
    }

    public List obtenerDetallePedidosCliente(int idPedido, Connection unaConexion) {
        ArrayList<CarritoDto> pedidos = new ArrayList();
        try {
            pstm = unaConexion.prepareStatement("select concat(u.nombres, ' ' ,u.apellidos) as productor, u.idUsuario , o.idOferta, p.nombres as producto, pre.descripcion as unidad, ca.cantidad as cantidad \n"
                    + "from pedidos as pe join carrito as ca on pe.idPedido = ca.idPedido\n"
                    + "join ofertas as o on ca.idOferta = o.idOferta \n"
                    + "join productoasociado as pa on o.idProdAsoc = pa.idProdAsoc\n"
                    + "join productos as p on pa.idProducto = p.idProducto\n"
                    + "join usuarios u on u.idUsuario = pa.idUsuario \n"
                    + "join presentaciones as pre on o.idPresentacion = pre.idPresentacion\n"
                    + "where pe.idPedido = ? group by o.idOferta;");
            pstm.setInt(1, idPedido);
            rs = pstm.executeQuery();
            while (rs.next()) {
                CarritoDto pedido = new CarritoDto();
                pedido.getOfDto().getProAso().getProDto().setNombres(rs.getString("producto"));
                pedido.getOfDto().getProAso().getUsDto().setNombres(rs.getString("productor"));
                pedido.getOfDto().getProAso().getUsDto().setIdUsuario(rs.getLong("idUsuario"));
                pedido.getOfDto().getPreDto().setDescripcion(rs.getString("unidad"));
                pedido.setCantidad(rs.getInt("cantidad"));
                pedido.setIdOferta(rs.getInt("idOferta"));
                pedidos.add(pedido);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return pedidos;
    }

    public List obtenerHistorialPedidosCliente(int idPedido, Connection unaConexion) {
        ArrayList<CarritoDto> pedidos = new ArrayList();
        try {
            pstm = unaConexion.prepareStatement("select o.idOferta, p.nombres as producto, pre.descripcion as unidad, ca.cantidad as cantidad \n"
                    + "from pedidos as pe join carrito as ca on pe.idPedido = ca.idPedido\n"
                    + "join ofertas as o on ca.idOferta = o.idOferta \n"
                    + "join productoasociado as pa on o.idProdAsoc = pa.idProdAsoc\n"
                    + "join productos as p on pa.idProducto = p.idProducto\n"
                    + "join presentaciones as pre on o.idPresentacion = pre.idPresentacion\n"
                    + "where pe.idPedido = ? group by o.idOferta;");
            pstm.setInt(1, idPedido);
            rs = pstm.executeQuery();
            while (rs.next()) {
                CarritoDto pedido = new CarritoDto();
                pedido.getOfDto().getProAso().getProDto().setNombres(rs.getString("producto"));
                pedido.getOfDto().getPreDto().setDescripcion(rs.getString("unidad"));
                pedido.setCantidad(rs.getInt("cantidad"));
                pedido.setIdOferta(rs.getInt("idOferta"));
                pedidos.add(pedido);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return pedidos;
    }

    public List obtenerPedidosProductor(long idProductor, Connection unaConexion) {
        ArrayList<PedidoDto> pedidos = new ArrayList();
        try {
            pstm = unaConexion.prepareStatement("select concat(u.nombres, ' ' ,u.apellidos) as cliente, u.idUsuario , pe.idPedido ,pe.fechaEntrega as entrega, pe.estadoPedido as estado, ceil((sum(c.cantidad * o.precioVenta)  - sum((c.cantidad * o.precioVenta)*p.detalle))) as 'total'\n"
                    + "from carrito c\n"
                    + "join pedidos pe on c.idPedido = pe.idPedido\n"
                    + "join usuarios as u on u.idUsuario = pe.idCliente\n"
                    + "join ofertas o on c.idOferta = o.idOferta\n"
                    + "join productoasociado as pa on pa.idProdAsoc = o.idProdAsoc\n"
                    + "join usuarios as us on us.idUsuario = pa.idUsuario\n"
                    + "join promociones p on o.idPromocion = p.idPromocion\n"
                    + "where us.idUsuario = ? and pe.idPedido not in (select ped.idPedido from pedidos as ped where ped.estadoPedido = 1) "
                    + "and pe.estadoPedido = 2 group by pe.idPedido;");
            pstm.setLong(1, idProductor);
            rs = pstm.executeQuery();
            while (rs.next()) {
                PedidoDto pedido = new PedidoDto();
                pedido.setIdPedido(rs.getInt("idPedido"));
                pedido.setFechaEntrega(rs.getString("entrega"));
                pedido.setEstadoPedido(rs.getInt("estado"));
                pedido.setTotal(rs.getLong("total"));
                pedido.getUsDto().setNombres(rs.getString("cliente"));
                pedido.getUsDto().setIdUsuario(rs.getLong("idUsuario"));
                pedidos.add(pedido);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }

        return pedidos;
    }

    public List obtenerHistorialProductor(long idProductor, Connection unaConexion) {
        ArrayList<PedidoDto> pedidos = new ArrayList();
        try {
            pstm = unaConexion.prepareStatement("select concat(u.nombres, ' ' ,u.apellidos) as cliente, u.idUsuario,  pe.idPedido ,pe.fechaEntrega as entrega, pe.estadoPedido as estado, ceil((sum(c.cantidad * o.precioVenta)  - sum((c.cantidad * o.precioVenta)*p.detalle))) as 'total'\n"
                    + " from carrito c\n"
                    + " join pedidos pe on c.idPedido = pe.idPedido\n"
                    + " join usuarios as u on u.idUsuario = pe.idCliente\n"
                    + " join ofertas o on c.idOferta = o.idOferta\n"
                    + " join productoasociado as pa on pa.idProdAsoc = o.idProdAsoc\n"
                    + " join usuarios as us on us.idUsuario = pa.idUsuario\n"
                    + " join promociones p on o.idPromocion = p.idPromocion\n"
                    + " where us.idUsuario = ? and pe.idPedido not in (select ped.idPedido from pedidos as ped where ped.estadoPedido = 2) \n"
                    + " and pe.estadoPedido = 4 or pe.estadoPedido = 3 group by pe.idPedido;");
            pstm.setLong(1, idProductor);
            rs = pstm.executeQuery();
            while (rs.next()) {
                PedidoDto pedido = new PedidoDto();
                pedido.setIdPedido(rs.getInt("idPedido"));
                pedido.setFechaEntrega(rs.getString("entrega"));
                pedido.setEstadoPedido(rs.getInt("estado"));
                pedido.setTotal(rs.getLong("total"));
                pedido.getUsDto().setNombres(rs.getString("cliente"));
                pedido.getUsDto().setIdUsuario(rs.getLong("idUsuario"));
                pedidos.add(pedido);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }

        return pedidos;
    }

    public List obtenerDetallePedidosProductor(long idUsuario, int idPedido, Connection unaConexion) {
        ArrayList<CarritoDto> pedidos = new ArrayList();
        try {
            pstm = unaConexion.prepareStatement("select ca.idPedido, pr.nombres as producto, pre.descripcion as unidad, ca.cantidad, us.direccion, ci.Nombre as ciudad from carrito ca\n"
                    + "join ofertas of on ca.idOferta = of.idOferta\n"
                    + "join pedidos pe on ca.idPedido = pe.idPedido\n"
                    + "join usuarios us on pe.idCliente = us.idUsuario\n"
                    + "join ciudades ci on us.idCiudad = ci.idCiudad\n"
                    + "join presentaciones pre on of.idPresentacion = pre.idPresentacion\n"
                    + "join productoasociado pa on of.idProdAsoc = pa.idProdAsoc\n"
                    + "join usuarios usu on pa.idUsuario = usu.idUsuario\n"
                    + "join productos pr on pa.idProducto = pr.idProducto\n"
                    + "where pa.idUsuario = ? and pe.estadoPedido = 2 and pe.idPedido = ? \n"
                    + "group by ca.idPedido, ca.idOferta;");
            pstm.setLong(1, idUsuario);
            pstm.setInt(2, idPedido);
            rs = pstm.executeQuery();
            while (rs.next()) {
                CarritoDto pedido = new CarritoDto();
                pedido.getPedDto().getUsDto().setDireccion(rs.getString("direccion"));
                pedido.getPedDto().getUsDto().getCiDto().setNombre(rs.getString("ciudad"));
                pedido.getOfDto().getProAso().getProDto().setNombres(rs.getString("producto"));
                pedido.getOfDto().getPreDto().setDescripcion(rs.getString("unidad"));
                pedido.setCantidad(rs.getInt("cantidad"));
                pedidos.add(pedido);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return pedidos;
    }

    public List obtenerHistorialPedidosProductor(long idUsuario, int idPedido, Connection unaConexion) {
        ArrayList<CarritoDto> pedidos = new ArrayList();
        try {
            pstm = unaConexion.prepareStatement("select ca.idPedido, pr.nombres as producto, pre.descripcion as unidad, ca.cantidad, us.direccion, ci.Nombre as ciudad from carrito ca\n"
                    + "join ofertas of on ca.idOferta = of.idOferta\n"
                    + "join pedidos pe on ca.idPedido = pe.idPedido\n"
                    + "join usuarios us on pe.idCliente = us.idUsuario\n"
                    + "join ciudades ci on us.idCiudad = ci.idCiudad\n"
                    + "join presentaciones pre on of.idPresentacion = pre.idPresentacion\n"
                    + "join productoasociado pa on of.idProdAsoc = pa.idProdAsoc\n"
                    + "join usuarios usu on pa.idUsuario = usu.idUsuario\n"
                    + "join productos pr on pa.idProducto = pr.idProducto\n"
                    + "where pa.idUsuario = ? and pe.idPedido = ? \n"
                    + "group by ca.idPedido, ca.idOferta;");
            pstm.setLong(1, idUsuario);
            pstm.setInt(2, idPedido);
            rs = pstm.executeQuery();
            while (rs.next()) {
                CarritoDto pedido = new CarritoDto();
                pedido.getPedDto().getUsDto().setDireccion(rs.getString("direccion"));
                pedido.getPedDto().getUsDto().getCiDto().setNombre(rs.getString("ciudad"));
                pedido.getOfDto().getProAso().getProDto().setNombres(rs.getString("producto"));
                pedido.getOfDto().getPreDto().setDescripcion(rs.getString("unidad"));
                pedido.setCantidad(rs.getInt("cantidad"));
                pedidos.add(pedido);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return pedidos;
    }

    public int obtenerEstadoPedido(int idPedido, Connection unaConexion) {
        int estado = 0;
        try {
            pstm = unaConexion.prepareStatement("select estadoPedido from pedidos where idPedido = ?");
            pstm.setInt(1, idPedido);
            rs = pstm.executeQuery();
            while (rs.next()) {
                estado = rs.getInt("estadoPedido");
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return estado;
    }

    public int obtenerPedidosPendientesProductor(long idProductor, Connection unaConexion) {
        try {
            pstm = unaConexion.prepareStatement("select count(distinct pe.idPedido) as pedidos from carrito ca\n"
                    + "join ofertas of on ca.idOferta = of.idOferta\n"
                    + "join pedidos pe on ca.idPedido = pe.idPedido\n"
                    + "join ProductoAsociado pa on of.idProdAsoc = pa.idProdAsoc\n"
                    + "where pa.idUsuario = ? and pe.estadoPedido = 2 ");
            pstm.setLong(1, idProductor);
            rs = pstm.executeQuery();
            while (rs.next()) {
                rtdo = rs.getInt("pedidos");
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return rtdo;
    }

    public int obtenerPedidosPendientesCliente(long idCliente, Connection unaConexion) {
        try {
            pstm = unaConexion.prepareStatement("select count(idPedido) as pedidos from pedidos where idCliente = ? and estadoPedido = 2");
            pstm.setLong(1, idCliente);
            rs = pstm.executeQuery();
            while (rs.next()) {
                rtdo = rs.getInt("pedidos");
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return rtdo;
    }

    public List<ProductoCarritoDto> obtenerUltimopedido(long idCliente, Connection unaConexion) {
        ArrayList<ProductoCarritoDto> pedidos = new ArrayList();
        try {
            pstm = unaConexion.prepareStatement("select productos.nombres, presentaciones.descripcion, carrito.cantidad, pedidos.fechaEntrega, ofertas.precioVenta,\n"
                    + "(carrito.cantidad*ofertas.precioVenta)-(carrito.cantidad*ofertas.precioVenta*promociones.detalle) as 'Total'\n"
                    + "from pedidos\n"
                    + "inner join carrito on pedidos.idPedido=carrito.idPedido\n"
                    + "inner join ofertas on ofertas.idOferta = carrito.idOferta\n"
                    + "inner join productoasociado on ofertas.idProdAsoc = productoasociado.idProdAsoc\n"
                    + "inner join productos on productos.idProducto = productoasociado.idProducto\n"
                    + "inner join presentaciones on ofertas.idPresentacion= presentaciones.idPresentacion\n"
                    + "inner join promociones on ofertas.idPromocion = promociones.idPromocion\n"
                    + "inner join usuarios on usuarios.idUsuario = pedidos.idCliente\n"
                    + "where pedidos.idPedido=(select pedidos.idPedido from pedidos order by pedidos.fechaPedido desc limit 1) and pedidos.idCliente=?");
            pstm.setLong(1, idCliente);
            rs = pstm.executeQuery();
            while (rs.next()) {
                ProductoCarritoDto pedido = new ProductoCarritoDto();
                pedido.setNombre(rs.getString("nombres"));
                pedido.setPresentacion(rs.getString("descripcion"));
                pedido.setCantidad(rs.getLong("cantidad"));
                pedido.setFechaEntrega(rs.getString("fechaEntrega"));
                pedido.setPrecioVenta(rs.getLong("precioVenta"));
                pedido.setTotal(rs.getLong("Total"));
                pedidos.add(pedido);
            }

        } catch (SQLException ex) {
            ex.getMessage();
        }
        return pedidos;

    }

    public StringBuilder buscarCorreos(int idPedido, Connection unaConexion) {
        StringBuilder sb = new StringBuilder("");
        try {
            pstm = unaConexion.prepareStatement("select distinct concat(uc.nombres, ' ', uc.apellidos) as 'Cliente', concat(up.nombres, ' ', up.apellidos) as 'Productor', up.correo\n"
                    + "from usuarios uc\n"
                    + "inner join pedidos pe on uc.idUsuario = pe.idCliente\n"
                    + "inner join carrito ca on pe.idPedido = ca.idPedido\n"
                    + "inner join ofertas of on ca.idOferta = of.idOferta\n"
                    + "inner join productoasociado pa on of.idProdAsoc = pa.idProdAsoc\n"
                    + "inner join usuarios up on pa.idUsuario = up.idUsuario\n"
                    + "where pe.idPedido = ?;");
            pstm.setInt(1, idPedido);
            rs = pstm.executeQuery();
            while (rs.next()) {
                sb.append(rs.getString("correo"));
                if (rs.next()) {
                    sb.append(", ");
                }
            }
        } catch (SQLException ex) {

        }
        return sb;
    }

}
