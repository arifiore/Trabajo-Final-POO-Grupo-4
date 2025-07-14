package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import businessEntity.Usuario;

public class UsuarioDAO implements IBaseDAO<Usuario>{
    private final Connection conn;

    public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean insertar(Usuario usuario) throws Exception {
        String sql = "INSERT INTO Usuario (id_empleado, username, password, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuario.getIdEmpleado());
            ps.setString(2, usuario.getUsername());
            ps.setString(3, usuario.getPassword());
            ps.setBoolean(4, usuario.isEstado());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public boolean actualizar(Usuario usuario) throws Exception {
        String sql = "UPDATE Usuario SET id_empleado=?, username=?, password=?, estado=? WHERE id_usuario=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuario.getIdEmpleado());
            ps.setString(2, usuario.getUsername());
            ps.setString(3, usuario.getPassword());
            ps.setBoolean(4, usuario.isEstado());
            ps.setInt(5, usuario.getIdUsuario());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws Exception {
        String sql = "DELETE FROM Usuario WHERE id_usuario=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    @Override
    public Usuario obtenerPorId(int id) throws Exception {
        String sql = "SELECT id_usuario, id_empleado, username, password, estado FROM Usuario WHERE id_usuario=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usu = new Usuario();
                    usu.setIdUsuario(rs.getInt("id_usuario"));
                    usu.setIdEmpleado(rs.getInt("id_empleado"));
                    usu.setUsername(rs.getString("username"));
                    usu.setPassword(rs.getString("password"));
                    usu.setEstado(rs.getBoolean("estado"));
                    return usu;
                }
            }
        }
        return null;
    }

    @Override
    public List<Usuario> obtenerTodos() throws Exception {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, id_empleado, username, password, estado FROM Usuario";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario usu = new Usuario();
                usu.setIdUsuario(rs.getInt("id_usuario"));
                    usu.setIdEmpleado(rs.getInt("id_empleado"));
                    usu.setUsername(rs.getString("username"));
                    usu.setPassword(rs.getString("password"));
                    usu.setEstado(rs.getBoolean("estado"));

                lista.add(usu);
            }
        }
        return lista;
    }
}
