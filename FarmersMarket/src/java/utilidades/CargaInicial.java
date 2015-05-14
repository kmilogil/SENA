/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author FamiliaGil
 */
public class CargaInicial {

    private Connection cnn = null;
    private PreparedStatement pstm = null;
    private String mensaje = "";
    private int rtdo = 0;

    public CargaInicial() {
        cnn = Conexion.getInstance();
    }

    public String ejecutar(String sql) {
        try {
            pstm = cnn.prepareStatement(sql);
            rtdo = pstm.executeUpdate();
            if (rtdo != 0) {
                mensaje = "Insertado";
            } else {
                mensaje = "No";
            }
        } catch (SQLException ex) {
            mensaje = ex.getMessage();
        }
        return mensaje;
    }

    public int cargar(String direccion) throws FileNotFoundException, IOException {
        int contador = 1;
        try {
            String ubicacion = direccion;
            FileReader fr = new FileReader(ubicacion);
            BufferedReader entradaArchivo = new BufferedReader(fr);
            String linea = entradaArchivo.readLine();

            while (linea != null) {
                System.out.println(linea);
                ejecutar(linea);
                contador++;
                linea = entradaArchivo.readLine();
            }
            System.out.println(contador);

        } catch (IOException ex) {
            System.out.println("Error en la apertura del archivo " + ex.toString());
        }
        return contador;
    }
}
