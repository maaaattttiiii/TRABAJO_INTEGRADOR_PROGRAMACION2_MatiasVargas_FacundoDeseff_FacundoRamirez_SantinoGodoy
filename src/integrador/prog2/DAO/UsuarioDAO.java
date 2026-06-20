package integrador.prog2.DAO;

import integrador.prog2.config.DatabaseConnectionPool;
import integrador.prog2.entities.Usuario;
import integrador.prog2.enums.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO implements GenericDAO<Usuario> {

    // --- CREAR AL USUARIO ---
    @Override
    public void crear(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre, apellido, mail, celular, contrasenia, rol) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getCelular());
            ps.setString(5, usuario.getContrasenia());
            ps.setString(6, usuario.getRol().name());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) usuario.setId(rs.getLong(1));
            }

        } catch (SQLException e) {
            System.err.println("Error al crear al Usuario: " + e.getMessage());
        }
    }

    // --- BUSCAR USUARIO POR ID ---
    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        String sql = "SELECT * FROM usuario WHERE id = ? AND eliminado = false";

        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getLong("id"));
                    u.setEliminado(rs.getBoolean("eliminado"));
                    u.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    u.setNombre(rs.getString("nombre"));
                    u.setApellido(rs.getString("apellido"));
                    u.setMail(rs.getString("mail"));
                    u.setCelular(rs.getString("celular"));
                    u.setContrasenia(rs.getString("contrasenia"));
                    u.setRol(Rol.valueOf(rs.getString("rol")));
                    return Optional.of(u);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar al Usuario: " + e.getMessage());
        }
        return Optional.empty();
    }

    // --- LISTAR A TODOS LOS USUARIOS ---
    @Override
    public List<Usuario> listar() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE eliminado = false";

        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getLong("id"));
                u.setEliminado(rs.getBoolean("eliminado"));
                u.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setMail(rs.getString("mail"));
                u.setCelular(rs.getString("celular"));
                u.setContrasenia(rs.getString("contrasenia"));
                u.setRol(Rol.valueOf(rs.getString("rol")));
                usuarios.add(u);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar a los Usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    // --- ACTUALIZAR USUARIO ---
    @Override
    public void actualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre = ?, apellido = ?, mail = ?, celular = ?, contrasenia = ?, rol = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getCelular());
            ps.setString(5, usuario.getContrasenia());
            ps.setString(6, usuario.getRol().name());
            ps.setLong(7, usuario.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar al Usuario: " + e.getMessage());
        }
    }

    // --- ELIMINAR USUARIO ---
    @Override
    public void eliminar(Long id) {
        String sql = "UPDATE usuario SET eliminado = true WHERE id = ?";

        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar al Usuario: " + e.getMessage());
        }
    }

    // --- VERIFICAR SI EL MAIL EXISTE ---
    public boolean existeMail(String mail) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE mail = ? AND eliminado = false";

        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mail);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar mail: " + e.getMessage());
        }
        return false;
    }
}
