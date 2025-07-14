package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import businessEntity.Asistencia;

public class AsistenciaDAO implements IBaseDAO<Asistencia>{
    private final Connection conn;

    public AsistenciaDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean insertar(Asistencia asistencia) throws Exception {
        String sql = "INSERT INTO Asistencia (id_empleado, id_horario, fecha_hora, estado, justificacion) VALUES (?, ?, ?, ?::asistencia_estado, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, asistencia.getIdEmpleado());
            ps.setInt(2, asistencia.getIdHorario());
            ps.setTimestamp(3, Timestamp.valueOf(asistencia.getFechaHora()));
            ps.setString(4, asistencia.getEstado());
            ps.setString(5, asistencia.getJustificacion());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public boolean actualizar(Asistencia asistencia) throws Exception {
        String sql = "UPDATE Asistencia SET id_empleado=?, id_horario=?, fecha_hora=?, estado=?::asistencia_estado, justificacion=? WHERE id_asistencia=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, asistencia.getIdEmpleado());
            ps.setInt(2, asistencia.getIdHorario());
            ps.setTimestamp(3, Timestamp.valueOf(asistencia.getFechaHora()));
            ps.setString(4, asistencia.getEstado());
            ps.setString(5, asistencia.getJustificacion());
            ps.setInt(7, asistencia.getIdAsistencia());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws Exception {
        String sql = "DELETE FROM Asistencia WHERE id_asistencia=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public Asistencia obtenerPorId(int id) throws Exception {
        String sql = "SELECT id_asistencia, id_empleado, id_horario, fecha_hora, estado, justificacion FROM Asistencia WHERE id_asistencia=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Asistencia asi = new Asistencia();
                    asi.setIdAsistencia(rs.getInt("id_asistencia"));
                    asi.setIdEmpleado(rs.getInt("id_empleado"));
                    asi.setIdHorario(rs.getInt("id_horario"));
                    asi.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                    asi.setEstado(rs.getString("estado"));
                    asi.setJustificacion(rs.getString("justificacion"));
                    return asi;
                }
            }
        }
        return null;
    }

    @Override
    public List<Asistencia> obtenerTodos() throws Exception {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT id_asistencia, id_empleado, id_horario, fecha_hora, estado, justificacion FROM Asistencia";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Asistencia asi = new Asistencia();
                asi.setIdAsistencia(rs.getInt("id_asistencia"));
                asi.setIdEmpleado(rs.getInt("id_empleado"));
                asi.setIdHorario(rs.getInt("id_horario"));
                asi.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                asi.setEstado(rs.getString("estado"));
                asi.setJustificacion(rs.getString("justificacion"));

                lista.add(asi);
            }
        }
        return lista;
    }
}
