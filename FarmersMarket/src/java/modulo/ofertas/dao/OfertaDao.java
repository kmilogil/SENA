/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulo.ofertas.dao;

import java.sql.CallableStatement;
import modulo.ofertas.dto.OfertaDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import modulo.pedidos.dto.CarritoDto;
import utilidades.Exceptions;

/**
 *
 * @author kennross
 */
public class OfertaDao {

    PreparedStatement pstm = null;
    int rtdo;
    ResultSet rs = null;
    String mensaje = "";
    String sqlTemp = "";
    CallableStatement ctm = null;

    public List obtenerOfertas(long idUsuario, String producto, int categoria, int presentaciones, Connection unaConexion) throws Exceptions {
        ArrayList<OfertaDto> ofertas = new ArrayList();
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("select o.idOferta as oferta, concat(u.nombres, ' ',u.apellidos) as productor, u.idUsuario,  o.precioVenta as precio, o.fechaFin as vence \n"
                    + ", i.cantidad as cantidad, pre.descripcion as presentacion, p.nombres as producto\n"
                    + "from ofertas as o \n"
                    + "join inventario as i on i.idOferta = o.idOferta\n"
                    + "join presentaciones as pre on pre.idPresentacion = o.idPresentacion   \n"
                    + "join productoasociado as pa on pa.idProdAsoc = o.idProdAsoc\n"
                    + "join productos as p on p.idProducto = pa.idProducto\n"
                    + "join usuarios as u on u.idUsuario = pa.idUsuario \n"
                    + "where i.cantidad  > 0 and o.fechaFin > curdate() \n"
                    + "and o.idOferta not in (select carrito.idOferta from ofertas join carrito on ofertas.idOferta = carrito.idOferta "
                    + "join pedidos on pedidos.idPedido = carrito.idPedido where pedidos.estadoPedido = 1 and pedidos.idCliente = ?) ");
            if (producto != null) {
                sb.append(" and p.nombres like '").append(producto).append("%' ");
            }
            if (categoria != 0) {
                sb.append(" and p.idCategoria like '").append(categoria).append("%' ");
            }
            if (presentaciones != 0) {
                sb.append(" and o.idPresentacion like '").append(presentaciones).append("%' ");
            }
            pstm = unaConexion.prepareStatement(sb.toString());
            pstm.setLong(1, idUsuario);
            rs = pstm.executeQuery();
            while (rs.next()) {
                OfertaDto oferta = new OfertaDto();
                oferta.setIdOferta(rs.getInt("oferta"));
                oferta.getProAso().getUsDto().setNombres(rs.getString("productor"));
                oferta.getProAso().getUsDto().setIdUsuario(rs.getLong("idUsuario"));
                oferta.getProAso().getProDto().setNombres(rs.getString("producto"));
                oferta.getInDto().setCantidad(rs.getInt("cantidad"));
                oferta.setPrecioVenta(rs.getLong("precio"));
                oferta.setFechaFin(rs.getString("vence"));
                oferta.getPreDto().setDescripcion(rs.getString("presentacion"));
                ofertas.add(oferta);
            }
        } catch (SQLException ex) {
            throw new Exceptions((ex.getMessage()));
        }
        return ofertas;
    }

    public OfertaDto obtenerOfertaPorId(int idOferta, Connection unaConexion) {
        OfertaDto oferta = new OfertaDto();
        try {
            pstm = unaConexion.prepareStatement("select o.idOferta as oferta, concat(u.nombres, ' ',u.apellidos) as productor, o.precioVenta as precio"
                    + ", o.fechaFin as vence , i.cantidad as cantidad, pre.descripcion as presentacion, p.nombres as producto, pro.descripcion as promocion\n"
                    + "from ofertas as o \n"
                    + "join inventario as i on i.idOferta = o.idOferta\n"
                    + "join presentaciones as pre on pre.idPresentacion = o.idPresentacion    \n"
                    + "join promociones as pro on pro.idPromocion = o.idPromocion\n"
                    + "join productoasociado as pa on pa.idProdAsoc = o.idProdAsoc\n"
                    + "join productos as p on p.idProducto = pa.idProducto\n"
                    + "join usuarios as u on u.idUsuario = pa.idUsuario where o.idOferta = ? and curdate() < o.fechaFin;");
            pstm.setInt(1, idOferta);
            rs = pstm.executeQuery();
            while (rs.next()) {
                oferta.setIdOferta(rs.getInt("oferta"));
                oferta.getProAso().getUsDto().setNombres(rs.getString("productor"));
                oferta.getProAso().getProDto().setNombres(rs.getString("producto"));
                oferta.getInDto().setCantidad(rs.getInt("cantidad"));
                oferta.setPrecioVenta(rs.getLong("precio"));
                oferta.setFechaFin(rs.getString("vence"));
                oferta.getPreDto().setDescripcion(rs.getString("presentacion"));
                oferta.getProDto().setDescripcion(rs.getString("promocion"));
            }
        } catch (SQLException ex) {

        }
        return oferta;
    }

    public String actualizarCantidad(int idOferta, Connection unaConexion) {
        String cantidad = "";
        try {
            pstm = unaConexion.prepareStatement("select i.cantidad as cantidad from ofertas as o "
                    + "join inventario as i on o.idOferta = i.idOferta where o.idOferta = ?;");
            pstm.setInt(1, idOferta);
            rs = pstm.executeQuery();
            while (rs.next()) {
                cantidad = rs.getString("cantidad");
            }
        } catch (SQLException ex) {

        }

        return cantidad;
    }

    public int ofertarProducto(OfertaDto ofDto, Connection unaConexion) {
        int salida = 4;
        try {
            ctm = unaConexion.prepareCall("call sp_crearOferta(?,?,?,?,?,?);");
            ctm.setInt(1, ofDto.getIdProdAsoc());
            ctm.setFloat(2, ofDto.getPrecioVenta());
            ctm.setInt(3, ofDto.getIdPromocion());
            ctm.setInt(4, ofDto.getIdPresentacion());
            ctm.setLong(5, ofDto.getInDto().getCantidad());
            ctm.registerOutParameter(6, Types.INTEGER);
            ctm.execute();
            salida = ctm.getInt(6);

        } catch (SQLException ex) {
            ex.getMessage();
        }

        return salida;
    }

    public int obtenerNumeroOfertasConProducto(int idProAso, long idUsuario, Connection unaConexion) {

        try {
            pstm = unaConexion.prepareStatement("select count(idOferta) as oferta from ofertas as o join productoasociado as pa on o.idProdAsoc = pa.idProdAsoc "
                    + "join productos as p on pa.idProducto = p.idProducto \n"
                    + "join usuarios as u on u.idUsuario = pa.idUsuario where p.idProducto = ? and estadoOferta = 1 and u.idUsuario = ? "
                    + "and curdate() < o.fechaFin;");
            pstm.setInt(1, idProAso);
            pstm.setLong(2, idUsuario);
            rs = pstm.executeQuery();
            while (rs.next()) {
                rtdo = rs.getInt("oferta");
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return rtdo;
    }

    public int obtenerProductoOfertado(int idProAso, Connection unaConexion) {
        try {
            pstm = unaConexion.prepareStatement("select o.idProdAsoc from ofertas as o "
                    + "join productoasociado as pa on o.idProdAsoc = pa.idProdAsoc where o.idProdAsoc = ?");
            pstm.setInt(1, idProAso);
            rs = pstm.executeQuery();
            while (rs.next()) {
                rtdo = rs.getInt("idProdAsoc");
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return rtdo;
    }

    public List obtenerOfertasPorProductor(long idUsuario, Connection unaConexion) {
        ArrayList<OfertaDto> ofertas = new ArrayList();

        try {
            pstm = unaConexion.prepareStatement("select o.idOferta as oferta,  o.precioVenta as precio, o.fechaFin as vence , i.cantidad as cantidad"
                    + ", pre.descripcion as presentacion, p.nombres as producto, pro.descripcion as promocion, p.imagen as imagen\n"
                    + "from ofertas as o \n"
                    + "join inventario as i on i.idOferta = o.idOferta\n"
                    + "join presentaciones as pre on pre.idPresentacion = o.idPresentacion   \n"
                    + "join promociones as pro on pro.idPromocion = o.idPromocion\n"
                    + "join productoasociado as pa on pa.idProdAsoc = o.idProdAsoc\n"
                    + "join productos as p on p.idProducto = pa.idProducto\n"
                    + "join usuarios as u on u.idUsuario = pa.idUsuario where u.idUsuario = ? and o.estadoOferta = 1 and o.fechaFin >= curdate();");
            pstm.setLong(1, idUsuario);
            rs = pstm.executeQuery();
            while (rs.next()) {
                OfertaDto oferta = new OfertaDto();
                oferta.setIdOferta(rs.getInt("oferta"));
                oferta.getProAso().getProDto().setNombres(rs.getString("producto"));
                oferta.getInDto().setCantidad(rs.getInt("cantidad"));
                oferta.setPrecioVenta(rs.getLong("precio"));
                oferta.setFechaFin(rs.getString("vence"));
                oferta.getPreDto().setDescripcion(rs.getString("presentacion"));
                oferta.getProDto().setDescripcion(rs.getString("promocion"));
                oferta.getProAso().getProDto().setImagen(rs.getString("imagen"));
                ofertas.add(oferta);
            }
        } catch (SQLException ex) {
//           throw new Exceptions(ex.getMessage());
        }
        return ofertas;
    }

    public int obtenerOfertasOfertadas(long idProductor, Connection unaConexion) {
        try {
            pstm = unaConexion.prepareStatement("select count(of.idOferta) as ofertas from ofertas of\n"
                    + "join ProductoAsociado pa on of.idProdAsoc = pa.idProdAsoc\n"
                    + "where pa.idUsuario = ? and of.estadoOferta = 1;");
            pstm.setLong(1, idProductor);
            rs = pstm.executeQuery();
            while (rs.next()) {
                rtdo = rs.getInt("ofertas");
            }

        } catch (SQLException ex) {
            ex.getMessage();
        }
        return rtdo;
    }

    public List rankingProductosVendidosProductor(long idUsuario, Connection unaConexion) throws Exceptions {
        ArrayList<CarritoDto> ranking = new ArrayList();
        try {
            pstm = unaConexion.prepareStatement("select pr.nombres as producto, pre.descripcion as unidad, sum((ca.cantidad*of.precioVenta) - ((ca.cantidad*of.precioVenta)*pro.detalle)) as vendido \n"
                    + "from carrito ca \n"
                    + "join ofertas of on ca.idOferta = of.idOferta \n"
                    + "join promociones pro on of.idPromocion = pro.idPromocion \n"
                    + "join presentaciones pre on of.idPresentacion = pre.idPresentacion \n"
                    + "join productoasociado pa on of.idProdAsoc = pa.idProdAsoc \n"
                    + "join productos pr on pa.idProducto = pr.idProducto \n"
                    + "where pa.idUsuario = ? \n"
                    + "group by pr.idProducto, pre.idPresentacion \n"
                    + "order by vendido desc;");
            pstm.setLong(1, idUsuario);
            rs = pstm.executeQuery();
            while (rs.next()) {
                CarritoDto vendidos = new CarritoDto();
                vendidos.getOfDto().getProAso().getProDto().setNombres(rs.getString("producto"));
                vendidos.getOfDto().getPreDto().setDescripcion(rs.getString("unidad"));
                vendidos.getPedDto().setTotal(rs.getLong("vendido"));
                ranking.add(vendidos);
            }
        } catch (SQLException ex) {
            throw new Exceptions(ex.getMessage());
        }
        return ranking;
    }

    public List rankingProductosVendidos(Connection unaConexion) throws Exceptions {
        ArrayList<CarritoDto> ranking = new ArrayList();
        try {
            pstm = unaConexion.prepareStatement("select pr.nombres as producto, pre.descripcion as unidad, sum(ca.cantidad) as 'Total'\n"
                    + "from carrito ca\n"
                    + "join ofertas of on ca.idOferta = of.idOferta\n"
                    + "join promociones pro on of.idPromocion = pro.idPromocion\n"
                    + "join presentaciones pre on of.idPresentacion = pre.idPresentacion\n"
                    + "join productoasociado pa on of.idProdAsoc = pa.idProdAsoc\n"
                    + "join productos pr on pa.idProducto = pr.idProducto\n"
                    + "group by pr.idProducto, pre.idPresentacion\n"
                    + "order by 'Total' desc\n"
                    + "limit 5;");

            rs = pstm.executeQuery();
            while (rs.next()) {
                CarritoDto vendidos = new CarritoDto();
                vendidos.getOfDto().getProAso().getProDto().setNombres(rs.getString("producto"));
                vendidos.getOfDto().getPreDto().setDescripcion(rs.getString("unidad"));
                vendidos.getPedDto().setTotal(rs.getLong("Total"));
                ranking.add(vendidos);
            }
        } catch (SQLException ex) {
            throw new Exceptions(ex.getMessage());
        }
        return ranking;
    }
    
    public OfertaDto obtenerUltimaOfertaPorProductor(long idUsuario, Connection unaConexion) {
        OfertaDto oferta = new OfertaDto();
        try {
            pstm = unaConexion.prepareStatement("select productos.nombres as producto, presentaciones.descripcion as presentacion, ofertas.fechaFin as vence, ofertas.precioVenta as precio, inventario.cantidad, promociones.descripcion as promocion\n"
                    + "from ofertas\n"
                    + "join inventario on inventario.idOferta = ofertas.idOferta\n"
                    + "inner join productoasociado on ofertas.idProdAsoc = productoasociado.idProdAsoc\n"
                    + "inner join productos on productos.idProducto = productoasociado.idProducto\n"
                    + "inner join presentaciones on ofertas.idPresentacion= presentaciones.idPresentacion\n"
                    + "inner join promociones on ofertas.idPromocion = promociones.idPromocion\n"
                    + "inner join usuarios on usuarios.idUsuario = productoasociado.idUsuario\n"
                    + "where ofertas.idOferta=(select ofertas.idOferta from ofertas order by ofertas.fechaInicio desc limit 1) and usuarios.idUsuario=?;");
            pstm.setLong(1, idUsuario);
            rs = pstm.executeQuery();
            while (rs.next()) {
                oferta.getProAso().getProDto().setNombres(rs.getString("producto"));
                oferta.getInDto().setCantidad(rs.getInt("cantidad"));
                oferta.setPrecioVenta(rs.getLong("precio"));
                oferta.setFechaFin(rs.getString("vence"));
                oferta.getPreDto().setDescripcion(rs.getString("presentacion"));
                oferta.getProDto().setDescripcion(rs.getString("promocion"));
            }
        } catch (SQLException ex) {

        }
        return oferta;
    }

}
