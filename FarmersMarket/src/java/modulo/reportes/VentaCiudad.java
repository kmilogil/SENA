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
public class VentaCiudad {
    private String productor = "";
    private String ciudad = "";
    private String mes = "";
    private String total = "";

    @Override
    public String toString() {
        return "VentaCiudad{" + "productor=" + productor + ", ciudad=" + ciudad + ", mes=" + mes + ", total=" + total + '}';
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
     * @return the ciudad
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * @param ciudad the ciudad to set
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * @return the mes
     */
    public String getMes() {
        return mes;
    }

    /**
     * @param mes the mes to set
     */
    public void setMes(String mes) {
        this.mes = mes;
    }

    /**
     * @return the total
     */
    public String getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(String total) {
        this.total = total;
    }
}
