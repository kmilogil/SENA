/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulo.usuarios.dao;

import modulo.usuarios.dto.RolDto;
import modulo.usuarios.dto.UsuarioDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modulo.usuarios.dto.PermisoDto;

/**
 *
 * @author kennross
 */
public class UsuarioDao {

    PreparedStatement pstm = null;
    ResultSet rs = null;
    String mensaje = "";
    int rtdo = 0;
    String sqlTemp = "";

    public String insertUsuarioProcedimiento(UsuarioDto nuevoUsuario, RolDto suRol, Connection unaConexion) {
        try {
            String sqlInsert = "CALL ProceRegistrarUsuarioCompleto(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            pstm = unaConexion.prepareStatement(sqlInsert);

            pstm.setLong(1, nuevoUsuario.getIdUsuario());
            pstm.setString(2, nuevoUsuario.getNombres());
            pstm.setString(3, nuevoUsuario.getApellidos());
            pstm.setString(4, nuevoUsuario.getClave());
            pstm.setString(5, nuevoUsuario.getCorreo());
            pstm.setString(6, nuevoUsuario.getFechaNacimiento());
            pstm.setString(7, nuevoUsuario.getDireccion());
            pstm.setInt(8, nuevoUsuario.getIdCiudad());
            pstm.setString(9, nuevoUsuario.getImagen());
            pstm.setInt(10, nuevoUsuario.getEstado());
            pstm.setInt(11, suRol.getIdRol());
            pstm.setInt(12, nuevoUsuario.getNotifaciones());

            rtdo = pstm.executeUpdate();

            if (rtdo != 0) {
                mensaje = "ok";
            } else {
                mensaje = "okno";
            }
        } catch (SQLException sqle) {
            mensaje = "Error, detalle " + sqle.getMessage();
        }
        return mensaje;
    }

    public String actualizarUsuario(UsuarioDto usuarioActualizado, Connection unaConexion) {
        try {
            String sqlInsert = "UPDATE `usuarios` SET `idUsuario` = ? `Nombres` = ?, "
                    + "`Apellidos` = ?, `Clave` = ?, `Correo` = ?, `FechaNacimiento` = ?, "
                    + "`Direccion` = ?, `idCiudad` = ?, `Imagen` = ?, "
                    + "`idEstado` = ? WHERE `idUsuario` = ?";
            pstm = unaConexion.prepareStatement(sqlInsert);

            pstm.setLong(1, usuarioActualizado.getIdUsuario());
            pstm.setString(2, usuarioActualizado.getNombres());
            pstm.setString(3, usuarioActualizado.getApellidos());
            pstm.setString(4, usuarioActualizado.getClave());
            pstm.setString(5, usuarioActualizado.getCorreo());
            pstm.setString(6, usuarioActualizado.getFechaNacimiento());
            pstm.setString(7, usuarioActualizado.getDireccion());
            pstm.setInt(8, usuarioActualizado.getIdCiudad());
            pstm.setString(9, usuarioActualizado.getImagen());
            pstm.setInt(10, usuarioActualizado.getEstado());
            pstm.setLong(11, usuarioActualizado.getIdUsuario());

            rtdo = pstm.executeUpdate();

            if (rtdo != 0) {
                mensaje = "ok";
            } else {
                mensaje = "okno";
            }
        } catch (SQLException sqle) {
            mensaje = "Error, detalle " + sqle.getMessage();
        }
        return mensaje;
    }

    public StringBuilder validarExistenciaDocumento(long idUsuario, Connection unaConexion) {
        StringBuilder salidaValidar = new StringBuilder("");
        sqlTemp = "SELECT `idUsuario` FROM `usuarios` WHERE `idUsuario` = ?";
        try {
            pstm = unaConexion.prepareStatement(sqlTemp);
            pstm.setLong(1, idUsuario);
            rs = pstm.executeQuery();

            if (rs.next()) {
                salidaValidar.append("existe");
            } else {
                salidaValidar.append("noexiste");
            }
        } catch (SQLException ex) {
            mensaje = "Error, detalle: " + ex.getMessage();
        }
        return salidaValidar;
    }

    public StringBuilder validarExistenciaCorreo(String correo, Connection unaConexion) {
        StringBuilder salidaValidar = new StringBuilder("");
        sqlTemp = "SELECT `Correo` FROM `usuarios` WHERE `Correo` = ?";
        try {
            pstm = unaConexion.prepareStatement(sqlTemp);
            pstm.setString(1, correo);
            rs = pstm.executeQuery();

            if (rs.next()) {
                salidaValidar.append("existe");
            } else {
                salidaValidar.append("noexiste");
            }
        } catch (SQLException ex) {
            mensaje = "Error, detalle: " + ex.getMessage();
        }
        return salidaValidar;
    }

    public UsuarioDto validarSesion(long documento, Connection unaConexion) {
        UsuarioDto usuario = null;
        sqlTemp = "SELECT `idUsuario`, `Clave` FROM `usuarios` WHERE `idUsuario` = ?";
        try {
            pstm = unaConexion.prepareStatement(sqlTemp);
            pstm.setLong(1, documento);
            rs = pstm.executeQuery();

            usuario = new UsuarioDto();
            while (rs.next()) {
                usuario.setIdUsuario(rs.getLong("idUsuario"));
                usuario.setClave(rs.getString("Clave"));
            }
        } catch (SQLException ex) {
            System.out.println("Error, detalle: " + ex.getMessage());
        }
        return usuario;
    }

    public UsuarioDto obtenerUsuarioPorId(long idUsuario, Connection unaConexion) {
        sqlTemp = "select u.idUsuario, nombres,apellidos,clave, correo, fechaNac,direccion,idCiudad,fechaSistema,imagen,estado, rl.idRol\n"
                + "from usuarios u join rolesusuario rl on u.idUsuario = rl.idUsuario where u.idUsuario = ?;";
        UsuarioDto salidaUsuario = new UsuarioDto();
        try {
            pstm = unaConexion.prepareStatement(sqlTemp);
            pstm.setLong(1, idUsuario);
            rs = pstm.executeQuery();

            while (rs.next()) {
                salidaUsuario.setIdUsuario(rs.getLong("idUsuario"));
                salidaUsuario.setNombres(rs.getString("nombres"));
                salidaUsuario.setApellidos(rs.getString("apellidos"));
                salidaUsuario.setClave(rs.getString("clave"));
                salidaUsuario.setCorreo(rs.getString("correo"));
                salidaUsuario.setFechaNacimiento(rs.getString("fechaNac"));
                salidaUsuario.setDireccion(rs.getString("direccion"));
                salidaUsuario.setIdCiudad(rs.getInt("idCiudad"));
                salidaUsuario.setFechaSistema(rs.getString("fechaSistema"));
                salidaUsuario.setImagen(rs.getString("imagen"));
                salidaUsuario.setEstado(rs.getInt("estado"));
                salidaUsuario.getRol().setIdRol(rs.getInt("idRol"));
            }
        } catch (SQLException ex) {
            System.out.println("Error, detalle: " + ex.getMessage());
        }
        return salidaUsuario;
    }

    public UsuarioDto obtenerUsuarioPorCorreo(String correo, Connection unaConexion) {
        sqlTemp = "select idUsuario, nombres,apellidos,clave, correo, fechaNac,direccion,idCiudad,fechaSistema,imagen,estado "
                + "from usuarios where correo = ?;";
        UsuarioDto salidaUsuario = new UsuarioDto();
        try {
            pstm = unaConexion.prepareStatement(sqlTemp);
            pstm.setString(1, correo);
            rs = pstm.executeQuery();

            while (rs.next()) {
                salidaUsuario.setIdUsuario(rs.getLong("idUsuario"));
                salidaUsuario.setNombres(rs.getString("nombres"));
                salidaUsuario.setApellidos(rs.getString("apellidos"));
                salidaUsuario.setClave(rs.getString("clave"));
                salidaUsuario.setCorreo(rs.getString("correo"));
                salidaUsuario.setFechaNacimiento(rs.getString("fechaNac"));
                salidaUsuario.setDireccion(rs.getString("direccion"));
                salidaUsuario.setIdCiudad(rs.getInt("idCiudad"));
                salidaUsuario.setFechaSistema(rs.getString("fechaSistema"));
                salidaUsuario.setImagen(rs.getString("imagen"));
                salidaUsuario.setEstado(rs.getInt("estado"));
            }
        } catch (SQLException ex) {
            System.out.println("Error, detalle: " + ex.getMessage());
        }
        return salidaUsuario;
    }

    public String actualizarClave(String nuevaClave, long documento, Connection unaConexion) {
        try {
            String sqlInsert = "UPDATE usuarios SET clave = ? WHERE idUsuario = ?";
            pstm = unaConexion.prepareStatement(sqlInsert);

            pstm.setString(1, nuevaClave);
            pstm.setLong(2, documento);

            rtdo = pstm.executeUpdate();

            if (rtdo != 0) {
                mensaje = "ok";
            } else {
                mensaje = "okno";
            }
        } catch (SQLException sqle) {
            mensaje = "Error, detalle " + sqle.getMessage();
        }
        return mensaje;
    }

    public String obtenerCorreoPorId(long idUsuario, Connection unaConexion) {
        sqlTemp = "SELECT `correo` FROM `usuarios` WHERE `idUsuario` = ?";
        try {
            pstm = unaConexion.prepareStatement(sqlTemp);
            pstm.setLong(1, idUsuario);
            rs = pstm.executeQuery();

            while (rs.next()) {
                mensaje = rs.getString("correo");
            }
        } catch (SQLException ex) {
            System.out.println("Error, detalle: " + ex.getMessage());
        }
        return mensaje;
    }

    public List obtenerUsuarios(Connection unaConexion) {
        ArrayList<UsuarioDto> usuarios = new ArrayList();
        try {
            pstm = unaConexion.prepareStatement("select ru.idRol, u.idUsuario, u.nombres, u.apellidos, u.estado "
                    + "from usuarios u join rolesusuario ru on u.idUsuario = ru.idUsuario where ru.idRol not in (3);");
            rs = pstm.executeQuery();
            while (rs.next()) {
                UsuarioDto usuario = new UsuarioDto();
                usuario.setIdUsuario(rs.getLong("idUsuario"));
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setEstado(rs.getInt("estado"));
                usuario.getRol().setIdRol(rs.getInt("idRol"));
                usuarios.add(usuario);
            }
        } catch (SQLException ex) {

        }
        return usuarios;
    }

    public List obtenerPermisosUsuario(long idUsuario, Connection unaConexion) {
        ArrayList<PermisoDto> permisos = new ArrayList();
        try {
            pstm = unaConexion.prepareStatement("select p.idPermisos, p.permisos from usuarios u join rolesusuario ru on u.idUsuario = ru.idUsuario "
                    + "join roles r on ru.idRol = r.idRol join permisosrol pr on r.idRol = pr.idRol join permisos p on pr.idPermisos = p.idPermisos "
                    + "where u.idUsuario = ?");
            pstm.setLong(1, idUsuario);
            rs = pstm.executeQuery();
            while (rs.next()) {
                PermisoDto permiso = new PermisoDto();
                permiso.setIdPermiso(rs.getInt("idPermisos"));
                permiso.setPermiso(rs.getString("permisos"));
                permisos.add(permiso);
            }
        } catch (SQLException ex) {

        }
        return permisos;
    }

    public String supenderUsuario(long idUsuario, Connection unaConexion) {
        try {
            pstm = unaConexion.prepareStatement("update usuarios set estado = 3 where idUsuario = ?;");
            pstm.setLong(1, idUsuario);
            rtdo = pstm.executeUpdate();
            if (rtdo != 0) {
                mensaje = "ok";
            } else {
                mensaje = "no";
            }
        } catch (SQLException ex) {
            mensaje = ex.getMessage();
        }
        return mensaje;
    }
    public String activarUsuario(long idUsuario, Connection unaConexion) {
        try {
            pstm = unaConexion.prepareStatement("update usuarios set estado = 1 where idUsuario = ?;");
            pstm.setLong(1, idUsuario);
            rtdo = pstm.executeUpdate();
            if (rtdo != 0) {
                mensaje = "ok";
            } else {
                mensaje = "no";
            }
        } catch (SQLException ex) {
            mensaje = ex.getMessage();
        }
        return mensaje;
    }

    public List obtenerPermisosUsuarioRol(long idUsuario, Connection unaConexion) {

        ArrayList<PermisoDto> permisos = new ArrayList();
        try {
            pstm = unaConexion.prepareStatement("select roles.idRol, permisos.idPermisos, permisos.permisos from permisos\n"
                    + "       inner join permisosrol on permisos.idPermisos = permisosrol.idPermisos\n"
                    + "       inner join roles on permisosrol.idRol = roles.idRol \n"
                    + "       where permisos.idPermisos not in (select permisos.idPermisos from permisos\n"
                    + "       inner join permisosrol on permisos.idPermisos = permisosrol.idPermisos\n"
                    + "       inner join roles on permisosrol.idRol = roles.idRol\n"
                    + "       inner join rolesusuario on roles.idRol = rolesusuario.idRol\n"
                    + "       inner join usuarios on rolesusuario.idUsuario = usuarios.idUsuario\n"
                    + "       where usuarios.idUsuario = ?) and permisos.idPermisos not in (8, 9);");
            pstm.setLong(1, idUsuario);
            rs = pstm.executeQuery();
            while (rs.next()) {
                PermisoDto permiso = new PermisoDto();
                permiso.setIdPermiso(rs.getInt("idPermisos"));
                permiso.setPermiso(rs.getString("permisos"));
                permisos.add(permiso);
            }
        } catch (SQLException ex) {

        }
        return permisos;
    }
}
