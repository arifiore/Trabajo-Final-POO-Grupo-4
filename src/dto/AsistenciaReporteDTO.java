
package dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AsistenciaReporteDTO {
    private LocalDate fecha;
    private LocalTime horaEsperadaInicio;
    private LocalTime horaEsperadaFin;
    private LocalTime horaRegistro;
    private String estado;
    private String justificacion; 

    public AsistenciaReporteDTO() {
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraEsperadaInicio() {
        return horaEsperadaInicio;
    }

    public void setHoraEsperadaInicio(LocalTime horaEsperadaInicio) {
        this.horaEsperadaInicio = horaEsperadaInicio;
    }

    public LocalTime getHoraEsperadaFin() {
        return horaEsperadaFin;
    }

    public void setHoraEsperadaFin(LocalTime horaEsperadaFin) {
        this.horaEsperadaFin = horaEsperadaFin;
    }

    public LocalTime getHoraRegistro() {
        return horaRegistro;
    }

    public void setHoraRegistro(LocalTime horaRegistro) {
        this.horaRegistro = horaRegistro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }
    
}
