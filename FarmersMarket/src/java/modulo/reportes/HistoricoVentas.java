/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modulo.reportes;

/**
 *
 * @author User
 */
public class HistoricoVentas {
    private String productor = "";
    private String producto = "";
    private String primerMes = "";
    private String segundoMes = "";

    @Override
    public String toString() {
        return "HistoricoVentas{" + "productor=" + productor + ", producto=" + producto + ", primerMes=" + primerMes + ", segundoMes=" + segundoMes + '}';
    }

    /**
     * @return the productor
     */
    public String getProductor() {
        return productor;
    }

    /**
     * @param productor the productor to set
     */
    public void setProductor(String productor) {
        this.productor = productor;
    }

    /**
     * @return the producto
     */
    public String getProducto() {
        return producto;
    }

    /**
     * @param producto the producto to set
     */
    public void setProducto(String producto) {
        this.producto = producto;
    }

    /**
     * @return the primerMes
     */
    public String getPrimerMes() {
        return primerMes;
    }

    /**
     * @param primerMes the primerMes to set
     */
    public void setPrimerMes(String primerMes) {
        this.primerMes = primerMes;
    }

    /**
     * @return the segundoMes
     */
    public String getSegundoMes() {
        return segundoMes;
    }

    /**
     * @param segundoMes the segundoMes to set
     */
    public void setSegundoMes(String segundoMes) {
        this.segundoMes = segundoMes;
    }
    
}
