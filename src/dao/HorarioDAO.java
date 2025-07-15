package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import businessEntity.Horario;
import java.time.LocalDate;
import java.time.YearMonth;

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
    
    public boolean eliminarPorMes(int anio, int mes) throws SQLException {
        String sql = "DELETE FROM horario WHERE EXTRACT(YEAR FROM fecha) = ? AND EXTRACT(MONTH FROM fecha) = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean existeParaMes(int anio, int mes) throws SQLException {
        String sql = "SELECT COUNT(*) FROM horario WHERE EXTRACT(YEAR FROM fecha) = ? AND EXTRACT(MONTH FROM fecha) = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, anio);
            stmt.setInt(2, mes);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } 
    }

    public List<Horario> obtenerPorEmpleado(int idEmpleado) throws Exception {
        List<Horario> lista = new ArrayList<>();
        String sql = "SELECT * FROM horario WHERE id_empleado = ? ORDER BY fecha ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Horario h = new Horario();
                    h.setIdHorario(rs.getInt("id_horario"));
                    h.setIdEmpleado(rs.getInt("id_empleado"));
                    h.setIdTurno(rs.getInt("id_turno"));
                    h.setFecha(rs.getDate("fecha").toLocalDate());
                    h.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                    h.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                    h.setEstado(rs.getBoolean("estado"));
                    lista.add(h);
                }
            }
        }
        return lista;
    }
    
    public boolean generarHorariosDesdeMesBase(int anioBase, int mesBase, int anioDestino, int mesDestino) throws SQLException {
        if (!existeParaMes(anioBase, mesBase)) {
            throw new IllegalArgumentException("No existen horarios para el mes base especificado");
        }
        eliminarPorMes(anioDestino, mesDestino);
        String sqlSelect = "SELECT id_empleado, id_turno, hora_inicio, hora_fin, estado " +
                          "FROM horario WHERE EXTRACT(YEAR FROM fecha) = ? AND EXTRACT(MONTH FROM fecha) = ?";
        String sqlInsert = "INSERT INTO horario (id_empleado, id_turno, hora_inicio, hora_fin, fecha, estado) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement psSelect = conn.prepareStatement(sqlSelect);
             PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {

            psSelect.setInt(1, anioBase);
            psSelect.setInt(2, mesBase);

            ResultSet rs = psSelect.executeQuery();

            while (rs.next()) {
                for (int dia = 1; dia <= YearMonth.of(anioDestino, mesDestino).lengthOfMonth(); dia++) {
                    LocalDate fechaDestino = LocalDate.of(anioDestino, mesDestino, dia);

                    psInsert.setInt(1, rs.getInt("id_empleado"));
                    psInsert.setInt(2, rs.getInt("id_turno"));
                    psInsert.setTime(3, rs.getTime("hora_inicio"));
                    psInsert.setTime(4, rs.getTime("hora_fin"));
                    psInsert.setDate(5, Date.valueOf(fechaDestino));
                    psInsert.setBoolean(6, rs.getBoolean("estado"));
                    psInsert.addBatch();
                }
            }
            psInsert.executeBatch();
            return true;
        }
    }
}