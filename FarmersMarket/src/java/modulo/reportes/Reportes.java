/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulo.reportes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utilidades.Conexion;

/**
 *
 * @author User
 */
public class Reportes {

    private Connection cnn = null;
    private ResultSet rs = null;
    private PreparedStatement pstm = null;

    public Reportes() {
        cnn = Conexion.getInstance();
    }

    public List historicoVentas() {
        ArrayList<HistoricoVentas> historico = new ArrayList();
        try {
            pstm = cnn.prepareStatement("Select concat(up.nombres, ' ', up.apellidos) as 'Productor', pr.nombres, coalesce(m1, 0) as 'Mes1', coalesce(m2, 0) as 'Mes2'\n"
                    + "from usuarios up\n"
                    + "inner join productoasociado pa on up.idUsuario = pa.idUsuario\n"
                    + "inner join productos pr on pa.idProducto = pr.idProducto\n"
                    + "inner join ofertas of on pa.idProdAsoc = of.idProdAsoc\n"
                    + "left outer join (select sum(car.cantidad) as 'm1', pro.idProducto as 'idProd'\n"
                    + "					from productos pro\n"
                    + "					inner join productoasociado pas on pro.idProducto = pas.idProducto\n"
                    + "					inner join ofertas ofe on pas.idProdAsoc = ofe.idProdAsoc\n"
                    + "					inner join carrito car on ofe.idOferta = car.idOferta\n"
                    + "					inner join pedidos ped on car.idPedido = ped.idPedido\n"
                    + "					where ped.fechaPedido between '2015/05/01' and '2015/05/10'\n"
                    + "					group by idProd) v1\n"
                    + "                    on pr.idProducto = v1.idProd\n"
                    + "left outer join (select sum(car.cantidad) as 'm2', pro.idProducto as 'idProd'\n"
                    + "					from productos pro\n"
                    + "					inner join productoasociado pas on pro.idProducto = pas.idProducto\n"
                    + "					inner join ofertas ofe on pas.idProdAsoc = ofe.idProdAsoc\n"
                    + "					inner join carrito car on ofe.idOferta = car.idOferta\n"
                    + "					inner join pedidos ped on car.idPedido = ped.idPedido\n"
                    + "					where ped.fechaPedido between '2015/05/11' and '2015/05/30'\n"
                    + "					group by idProd) v2\n"
                    + "                    on pr.idProducto = v2.idProd\n"
                    + "group by up.idUsuario, pr.idProducto;");
            rs = pstm.executeQuery();
            while (rs.next()) {
                HistoricoVentas historial = new HistoricoVentas();
                historial.setProductor(rs.getString("Productor"));
                historial.setProducto(rs.getString("nombres"));
                historial.setPrimerMes(rs.getString("mes1"));
                historial.setSegundoMes(rs.getString("mes2"));
                historico.add(historial);
            }
        } catch (SQLException ex) {

        }
        return historico;
    }

    public List ventaCiudad() {
        ArrayList<VentaCiudad> ventas = new ArrayList();
        try {
            pstm = cnn.prepareStatement("select concat(up.nombres, ' ', up.apellidos) as 'Productor', ci.Nombre, monthname(pe.fechaPedido) as 'Mes', round(avg(ca.cantidad*((1 - pro.detalle)*of.precioVenta)), 2) as 'Total'\n"
                    + "from usuarios up\n"
                    + "inner join productoasociado pa on up.idUsuario = pa.idUsuario\n"
                    + "inner join ofertas of on pa.idProdAsoc = of.idProdAsoc\n"
                    + "inner join promociones pro on of.idPromocion = pro.idPromocion\n"
                    + "inner join carrito ca on of.idOferta = ca.idOferta\n"
                    + "inner join pedidos pe on ca.idPedido = pe.idPedido\n"
                    + "inner join usuarios uc on pe.idCliente = uc.idUsuario\n"
                    + "inner join ciudades ci on uc.idCiudad = ci.idCiudad\n"
                    + "group by up.idUsuario, ci.idCiudad;");
            rs = pstm.executeQuery();
            while (rs.next()) {
                VentaCiudad venta = new VentaCiudad();
                venta.setProductor(rs.getString("Productor"));
                venta.setCiudad(rs.getString("Nombre"));
                venta.setMes(rs.getString("Mes"));
                venta.setTotal(rs.getString("Total"));
                ventas.add(venta);
            }
        } catch (SQLException ex) {

        }
        return ventas;
    }
}
