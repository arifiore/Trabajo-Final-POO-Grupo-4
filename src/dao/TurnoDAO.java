package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import businessEntity.Turno;

public class TurnoDAO implements IBaseDAO<Turno>{
    private final Connection conn;

    public TurnoDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean insertar(Turno turno) throws Exception {
        String sql = "INSERT INTO Turno (nombre, descripcion, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, turno.getNombre());
            ps.setString(2, turno.getDescripcion());
            ps.setTime(3, Time.valueOf(turno.getHoraInicio()));
            ps.setTime(4, Time.valueOf(turno.getHoraFin()));
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public boolean actualizar(Turno turno) throws Exception {
        String sql = "UPDATE Turno SET nombre=?, descripcion=?, hora_inicio=?, hora_fin=? WHERE id_turno=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, turno.getNombre());
            ps.setString(2, turno.getDescripcion());
            ps.setTime(3, Time.valueOf(turno.getHoraInicio()));
            ps.setTime(4, Time.valueOf(turno.getHoraFin()));
            ps.setInt(5, turno.getIdTurno());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws Exception {
        String sql = "DELETE FROM Turno WHERE id_turno=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public Turno obtenerPorId(int id) throws Exception {
        String sql = "SELECT id_turno, nombre, descripcion, hora_inicio, hora_fin FROM Turno WHERE id_turno=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Turno tur = new Turno();
                    tur.setIdTurno(rs.getInt("id_turno"));
                    tur.setNombre(rs.getString("nombre"));
                    tur.setDescripcion(rs.getString("descripcion"));
                    tur.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                    tur.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                    return tur;
                }
            }
        }
        return null;
    }

    @Override
    public List<Turno> obtenerTodos() throws Exception {
        List<Turno> lista = new ArrayList<>();
        String sql = "SELECT id_turno, nombre, descripcion, hora_inicio, hora_fin FROM Turno";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Turno tur = new Turno();
                tur.setIdTurno(rs.getInt("id_turno"));
                tur.setNombre(rs.getString("nombre"));
                tur.setDescripcion(rs.getString("descripcion"));
                tur.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                tur.setHoraFin(rs.getTime("hora_fin").toLocalTime());

                lista.add(tur);
            }
        }
        return lista;
    }
}
