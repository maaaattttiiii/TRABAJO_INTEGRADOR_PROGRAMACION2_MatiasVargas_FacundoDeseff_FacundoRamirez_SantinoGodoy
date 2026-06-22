package integrador.prog2.DAO;

import integrador.prog2.config.DatabaseConnectionPool;
import integrador.prog2.entities.Categoria;
import integrador.prog2.entities.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoDAO implements GenericDAO<Producto> {

    // --- CREAR PRODUCTO ---
    @Override
    public void crear(Producto producto) {
        String sql = "INSERT INTO producto (nombre ,precio, descripcion, stock, image, disponible, id_categoria) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setString(3, producto.getDescripcion());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getImage());
            ps.setBoolean(6, producto.isDisponible());
            ps.setLong(7, producto.getCategoria().getId());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) producto.setId(rs.getLong(1));
            }
        }
        catch (SQLException e) {
            System.err.println("Error al crear el Producto: " + e.getMessage());
        }
    }

    // --- BUSCAR PRODUCTO POR ID ---
    @Override
    public Optional<Producto> buscarPorId(Long id) {
        String sql = "SELECT p.*, c.nombre AS cat_nombre, c.descripcion AS cat_descripcion " +
                     "FROM producto p INNER JOIN categoria c ON p.id_categoria = c.id " +
                     "WHERE p.id = ? AND p.eliminado = false";

        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getLong("id"));
                    p.setEliminado(rs.getBoolean("eliminado"));
                    p.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setStock(rs.getInt("stock"));
                    p.setImage(rs.getString("image"));
                    p.setDisponible(rs.getBoolean("disponible"));
                    Categoria cat = new Categoria();
                    cat.setId(rs.getLong("id_categoria"));
                    cat.setNombre(rs.getString("cat_nombre"));
                    cat.setDescripcion(rs.getString("cat_descripcion"));
                    p.setCategoria(cat);
                    return Optional.of(p);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar el Producto: " + e.getMessage());
        }
        return Optional.empty();
    }

    // --- LISTAR PRODUCTOS ---
    @Override
    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS cat_nombre, c.descripcion AS cat_descripcion " +
                     "FROM producto p INNER JOIN categoria c ON p.id_categoria = c.id " +
                     "WHERE p.eliminado = false";

        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getLong("id"));
                p.setEliminado(rs.getBoolean("eliminado"));
                p.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setStock(rs.getInt("stock"));
                p.setImage(rs.getString("image"));
                p.setDisponible(rs.getBoolean("disponible"));
                Categoria cat = new Categoria();
                cat.setId(rs.getLong("id_categoria"));
                cat.setNombre(rs.getString("cat_nombre"));
                cat.setDescripcion(rs.getString("cat_descripcion"));
                p.setCategoria(cat);
                productos.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar los Productos: " + e.getMessage());
        }
        return productos;
    }

    // --- ACTUALIZAR PRODUCTOS ---
    @Override
    public void actualizar(Producto producto) {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, descripcion = ?, stock = ?, image = ?, disponible = ?, id_categoria = ? WHERE id = ?";

        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setString(3, producto.getDescripcion());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getImage());
            ps.setBoolean(6, producto.isDisponible());
            ps.setLong(7, producto.getCategoria().getId());
            ps.setLong(8, producto.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el Producto: " + e.getMessage());
        }
    }

    // --- ELIMINAR PRODUCTO ---
    @Override
    public void eliminar(Long id) {
        String sql = "UPDATE producto SET eliminado = true WHERE id = ?";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
    }

    // --- LISTAR PRODUCTOS POR CATEGORIA ---
    public List<Producto> listarPorCategoria(Long idCategoria) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS cat_nombre, c.descripcion AS cat_descripcion " +
                     "FROM producto p INNER JOIN categoria c ON p.id_categoria = c.id " +
                     "WHERE p.id_categoria = ? AND p.eliminado = false";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getLong("id"));
                    p.setEliminado(rs.getBoolean("eliminado"));
                    p.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setStock(rs.getInt("stock"));
                    p.setImage(rs.getString("image"));
                    p.setDisponible(rs.getBoolean("disponible"));
                    Categoria cat = new Categoria();
                    cat.setId(rs.getLong("id_categoria"));
                    cat.setNombre(rs.getString("cat_nombre"));
                    cat.setDescripcion(rs.getString("cat_descripcion"));
                    p.setCategoria(cat);
                    productos.add(p);
                }

            }

        } catch (SQLException e) {
            System.err.println("Error al listar productos por categoría: " + e.getMessage());
        }
        return productos;
    }

}
