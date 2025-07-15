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
        if (empleado.getDni() == null || empleado.getDni().isEmpty()) {
            throw new IllegalArgumentException("DNI es requerido");
        }
        String sql = "INSERT INTO Empleado (dni, nombres, apellidos, cargo, area, estado, es_lider) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empleado.getDni());
            ps.setString(2, empleado.getNombres());
            ps.setString(3, empleado.getApellidos());
            ps.setString(4, empleado.getCargo());
            ps.setString(5, empleado.getArea());
            ps.setBoolean(6, empleado.isEstado());
            ps.setBoolean(7, empleado.isEsLider());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public boolean actualizar(Empleado empleado) throws Exception {
        String sql = "UPDATE Empleado SET dni=?, nombres=?, apellidos=?, cargo=?, area=?, estado=?, es_lider=? WHERE id_empleado=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empleado.getDni());
            ps.setString(2, empleado.getNombres());
            ps.setString(3, empleado.getApellidos());
            ps.setString(4, empleado.getCargo());
            ps.setString(5, empleado.getArea());
            ps.setBoolean(6, empleado.isEstado());
            ps.setBoolean(7, empleado.isEsLider());
            ps.setInt(8, empleado.getIdEmpleado());
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
        String sql = "SELECT id_empleado, dni, nombres, apellidos, cargo, area, estado, es_lider FROM Empleado WHERE id_empleado=?";
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
                    emp.setEsLider(rs.getBoolean("es_lider"));
                    return emp;
                }
            }
        }
        return null;
    }

    @Override
    public List<Empleado> obtenerTodos() throws Exception {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT id_empleado, dni, nombres, apellidos, cargo, area, estado, es_lider FROM Empleado";

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
                emp.setEsLider(rs.getBoolean("es_lider"));

                lista.add(emp);
            }
        }
        return lista;
    }
    
    public Empleado obtenerPorDNI(String dni) {
        String sql = "SELECT * FROM empleado WHERE dni = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empleado empleado = new Empleado();
                    empleado.setIdEmpleado(rs.getInt("id_empleado"));
                    empleado.setDni(rs.getString("dni"));
                    empleado.setNombres(rs.getString("nombres"));
                    empleado.setApellidos(rs.getString("apellidos"));
                    empleado.setCargo(rs.getString("cargo"));
                    empleado.setArea(rs.getString("area"));
                    empleado.setEstado(rs.getBoolean("estado"));
                    empleado.setEsLider(rs.getBoolean("es_lider"));
                    return empleado;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener empleado por DNI: " + e.getMessage());
        }
        return null; 
    }

    
    public Empleado obtenerEmpleadoPorId(int idEmpleado) throws SQLException {
        String sql = "SELECT * FROM empleado WHERE id_empleado = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empleado empleado = new Empleado();
                    empleado.setIdEmpleado(rs.getInt("id_empleado"));
                    empleado.setDni(rs.getString("dni"));
                    empleado.setNombres(rs.getString("nombres"));
                    empleado.setApellidos(rs.getString("apellidos"));
                    empleado.setCargo(rs.getString("cargo"));
                    empleado.setArea(rs.getString("area"));
                    empleado.setEstado(rs.getBoolean("estado"));
                    empleado.setEsLider(rs.getBoolean("es_lider"));
                    return empleado;
                }
            }
        }
        return null;
    }
    public List<Empleado> obtenerLideres() throws SQLException {
        List<Empleado> lideres = new ArrayList<>();
        String sql = "SELECT * FROM empleado WHERE es_lider = TRUE";

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
                emp.setEsLider(rs.getBoolean("es_lider"));
                lideres.add(emp);
            }
        }
        return lideres;
    }
    public List<Empleado> obtenerLideresPorArea(String area) throws SQLException {
        List<Empleado> lideres = new ArrayList<>();
        String sql = "SELECT * FROM empleado WHERE es_lider = TRUE AND area = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, area);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Empleado emp = new Empleado();
                    emp.setIdEmpleado(rs.getInt("id_empleado"));
                    emp.setDni(rs.getString("dni"));
                    emp.setNombres(rs.getString("nombres"));
                    emp.setApellidos(rs.getString("apellidos"));
                    emp.setCargo(rs.getString("cargo"));
                    emp.setArea(rs.getString("area"));
                    emp.setEstado(rs.getBoolean("estado"));
                    emp.setEsLider(rs.getBoolean("es_lider"));

                    lideres.add(emp);
                }
            }
        }
        return lideres;
    }
    public List<String> obtenerAreasConLideres() throws SQLException {
        List<String> areas = new ArrayList<>();
        String sql = "SELECT DISTINCT area FROM empleado WHERE es_lider = TRUE ORDER BY area";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                areas.add(rs.getString("area"));
            }
        }
        return areas;
    }
}