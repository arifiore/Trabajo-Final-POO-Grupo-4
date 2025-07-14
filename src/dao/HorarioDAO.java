package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import businessEntity.Horario;

public class HorarioDAO implements IBaseDAO<Horario>{
    private final Connection conn;

    public HorarioDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean insertar(Horario horario) throws Exception {
        String sql = "INSERT INTO Horario (id_empleado, id_turno, hora_inicio, hora_fin, fecha, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, horario.getIdEmpleado());
            ps.setInt(2, horario.getIdTurno());
            ps.setTime(3, Time.valueOf(horario.getHoraInicio()));
            ps.setTime(4, Time.valueOf(horario.getHoraFin()));
            ps.setDate(5, Date.valueOf(horario.getFecha()));
            ps.setBoolean(6, horario.isEstado());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public boolean actualizar(Horario horario) throws Exception {
        String sql = "UPDATE Horario SET id_empleado=?, id_turno=?, hora_inicio=?, hora_fin=?, fecha=?, estado=? WHERE id_horario=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, horario.getIdEmpleado());
            ps.setInt(2, horario.getIdTurno());
            ps.setTime(3, Time.valueOf(horario.getHoraInicio()));
            ps.setTime(4, Time.valueOf(horario.getHoraFin()));
            ps.setDate(5, Date.valueOf(horario.getFecha()));
            ps.setBoolean(6, horario.isEstado());
            ps.setInt(7, horario.getIdHorario());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws Exception {
        String sql = "DELETE FROM Horario WHERE id_horario=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public Horario obtenerPorId(int id) throws Exception {
        String sql = "SELECT id_horario, id_empleado, id_turno, hora_inicio, hora_fin, fecha, estado FROM Horario WHERE id_horario=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Horario hor = new Horario();
                    hor.setIdHorario(rs.getInt("id_horario"));
                    hor.setIdEmpleado(rs.getInt("id_empleado"));
                    hor.setIdTurno(rs.getInt("id_turno"));
                    hor.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                    hor.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                    hor.setFecha(rs.getDate("fecha").toLocalDate());
                    hor.setEstado(rs.getBoolean("estado"));
                    return hor;
                }
            }
        }
        return null;
    }

    @Override
    public List<Horario> obtenerTodos() throws Exception {
        List<Horario> lista = new ArrayList<>();
        String sql = "SELECT id_horario, id_empleado, id_turno, hora_inicio, hora_fin, fecha, estado FROM Horario";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Horario hor = new Horario();
                hor.setIdHorario(rs.getInt("id_horario"));
                hor.setIdEmpleado(rs.getInt("id_empleado"));
                hor.setIdTurno(rs.getInt("id_turno"));
                hor.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                hor.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                hor.setFecha(rs.getDate("fecha").toLocalDate());
                hor.setEstado(rs.getBoolean("estado"));

                lista.add(hor);
            }
        }
        return lista;
    }
}
