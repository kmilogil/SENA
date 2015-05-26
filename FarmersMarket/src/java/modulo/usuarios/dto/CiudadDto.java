package modulo.usuarios.dto;

/**
 * @author Kennit David Ruz Romero Hora de creación: 11:18 a.m. Fecha: 4 de
 * Marzo de 2015
 */
public class CiudadDto {

    private int idCiudad = 0;
    private String nombre = "";
    private int idDepartamento = 0;
    private DepartamentoDto departamento = new DepartamentoDto();

    @Override
    public String toString() {
        return "CiudadDto{" + "idCiudad=" + idCiudad + ", nombre=" + nombre + ", idDepartamento=" + idDepartamento + ", departamento=" + departamento + '}';
    }

    /**
     * @return the idCiudad
     */
    public int getIdCiudad() {
        return idCiudad;
    }

    /**
     * @param idCiudad the idCiudad to set
     */
    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the idDepartamento
     */
    public int getIdDepartamento() {
        return idDepartamento;
    }

    /**
     * @param idDepartamento the idDepartamento to set
     */
    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    /**
     * @return the departamento
     */
    public DepartamentoDto getDepartamento() {
        return departamento;
    }

    /**
     * @param departamento the departamento to set
     */
    public void setDepartamento(DepartamentoDto departamento) {
        this.departamento = departamento;
    }
}
