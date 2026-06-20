package integrador.prog2.DAO;

import integrador.prog2.entities.Categoria;
import integrador.prog2.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaDAO implements GenericDAO<Categoria> {

    // --- CREAR CATEGORÍA ---
    @Override
    public void crear(Categoria categoria) {
        String sql = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) categoria.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            System.err.println("Error al crear la Categoría: " + e.getMessage());
        }
    }

    // --- BUSCAR CATEGORÍA POR ID ---
    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        String sql = "SELECT * FROM categoria WHERE id = ? AND eliminado = false";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatemen(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    Categoria c = new Categoria();
                    c.setId(rs.getLong("id"));
                    c.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    c.setNombre(rs.getString("nombre"));
                    c.setDescripcion(rs.getString("descripcion"));
                    return Optional.of(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar la Categoría: " + e.getMessage());
        }
        return Optional.empty();
    }

    // --- LISTAR TODAS LAS CATEGORÍAS ---
    @Override
    public List<Categoria> listar() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categoria WHERE eliminado = false";

        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getLong("id"));
                c.setEliminado(rs.getBoolean("eliminado"));
                c.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                c.setNombre(rs.getString("nombre"));
                c.setDescripcion(rs.getString("descripcion"));
                categorias.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar las Categorías: " + e.getMessage());
        }
        return categorias;
    }

    // --- ACTUALIZAR CATEGORÍA ---
    @Override
    public void actualizar(Categoria entidad) {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ?";

        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setLong(3, categoria.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar categoría: " + e.getMessage());
        }
    }

    // --- ELIMINAR CATEGORÍA ---
    @Override
    public void eliminar(Long id) {
        String sql = "UPDATE categoria SET eliminado = true WHERE id = ?";

        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar la Categoría: " + e.getMessage());
        }
    }
}
