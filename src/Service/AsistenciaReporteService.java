package Service;

import businessEntity.Asistencia;
import businessEntity.Horario;
import dao.AsistenciaDAO;
import dao.HorarioDAO;
import dto.AsistenciaReporteDTO;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AsistenciaReporteService {
    private final HorarioDAO horarioDAO;
    private final AsistenciaDAO asistenciaDAO;

    public AsistenciaReporteService(Connection conn) {
        this.horarioDAO = new HorarioDAO(conn);
        this.asistenciaDAO = new AsistenciaDAO(conn);
    }

    public List<AsistenciaReporteDTO> generarReporteMensual(int idEmpleado, int anio, int mes) throws Exception {
        List<AsistenciaReporteDTO> reporte = new ArrayList<>();
        List<Horario> horarios = horarioDAO.obtenerPorEmpleado(idEmpleado).stream()
                .filter(h -> h.getFecha().getYear() == anio && h.getFecha().getMonthValue() == mes)
                .sorted(Comparator.comparing(Horario::getFecha))
                .toList();
        List<Asistencia> asistencias = asistenciaDAO.obtenerPorEmpleado(idEmpleado).stream()
                .filter(a -> a.getFechaHora().getYear() == anio && a.getFechaHora().getMonthValue() == mes)
                .toList();

        for (Horario horario : horarios) {
            LocalDate fecha = horario.getFecha();
            AsistenciaReporteDTO registro = new AsistenciaReporteDTO();
            registro.setFecha(fecha);
            registro.setHoraEsperadaInicio(horario.getHoraInicio());
            registro.setHoraEsperadaFin(horario.getHoraFin());

            Optional<Asistencia> asistencia = asistencias.stream()
                    .filter(a -> a.getFechaHora().toLocalDate().equals(fecha))
                    .findFirst();

            if (asistencia.isPresent()) {
                LocalTime horaRegistro = asistencia.get().getFechaHora().toLocalTime();
                registro.setHoraRegistro(horaRegistro);
                
                if (horaRegistro.isAfter(horario.getHoraInicio().plusMinutes(15))) {
                    registro.setEstado("Tardanza");
                } else {
                    registro.setEstado("Asistió");
                }
                registro.setJustificacion(asistencia.get().getJustificacion());
            } else {
                registro.setEstado("Falta");
            }
            
            reporte.add(registro);
        }
        
        return reporte;
    }
}
