package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import businessEntity.Empleado;

public class EmpleadoDAO implements IBaseDAO<Empleado> {

    private final Connection conn;

    public EmpleadoDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean insertar(Empleado empleado) throws Exception {
        String sql = "INSERT INTO Empleado (dni, nombres, apellidos, cargo, area, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empleado.getDni());
            ps.setString(2, empleado.getNombres());
            ps.setString(3, empleado.getApellidos());
            ps.setString(4, empleado.getCargo());
            ps.setString(5, empleado.getArea());
            ps.setBoolean(6, empleado.isEstado());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public boolean actualizar(Empleado empleado) throws Exception {
        String sql = "UPDATE Empleado SET dni=?, nombres=?, apellidos=?, cargo=?, area=?, estado=? WHERE id_empleado=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empleado.getDni());
            ps.setString(2, empleado.getNombres());
            ps.setString(3, empleado.getApellidos());
            ps.setString(4, empleado.getCargo());
            ps.setString(5, empleado.getArea());
            ps.setBoolean(6, empleado.isEstado());
            ps.setInt(7, empleado.getIdEmpleado());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws Exception {
        String sql = "DELETE FROM Empleado WHERE id_empleado=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public Empleado obtenerPorId(int id) throws Exception {
        String sql = "SELECT id_empleado, dni, nombres, apellidos, cargo, area, estado FROM Empleado WHERE id_empleado=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empleado emp = new Empleado();
                    emp.setIdEmpleado(rs.getInt("id_empleado"));
                    emp.setDni(rs.getString("dni"));
                    emp.setNombres(rs.getString("nombres"));
                    emp.setApellidos(rs.getString("apellidos"));
                    emp.setCargo(rs.getString("cargo"));
                    emp.setArea(rs.getString("area"));
                    emp.setEstado(rs.getBoolean("estado"));
                    return emp;
                }
            }
        }
        return null;
    }

    @Override
    public List<Empleado> obtenerTodos() throws Exception {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT id_empleado, dni, nombres, apellidos, cargo, area, estado FROM Empleado";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Empleado emp = new Empleado();
                emp.setIdEmpleado(rs.getInt("id_empleado"));
                emp.setDni(rs.getString("dni"));
                emp.setNombres(rs.getString("nombres"));
                emp.setApellidos(rs.getString("apellidos"));
                emp.setCargo(rs.getString("cargo"));
                emp.setArea(rs.getString("area"));
                emp.setEstado(rs.getBoolean("estado"));

                lista.add(emp);
            }
        }
        return lista;
    }
}
