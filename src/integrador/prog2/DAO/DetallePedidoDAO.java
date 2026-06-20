package integrador.prog2.DAO;

import integrador.prog2.config.DatabaseConnectionPool;
import integrador.prog2.entities.DetallePedido;
import integrador.prog2.entities.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DetallePedidoDAO implements GenericDAO<DetallePedido> {

    // --- CREAR DETALLE PEDIDO ---
    @Override
    public void crear(DetallePedido detalle) {
        String sql = "INSERT INTO detalle_pedido (cantidad, subtotal, id_pedido, id_producto) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, detalle.getCantidad());
            ps.setDouble(2, detalle.getSubtotal());
            ps.setLong(3, detalle.getPedido().getId());
            ps.setLong(4, detalle.getProducto().getId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) detalle.setId(rs.getLong(1));
            }

        } catch (SQLException e) {
            System.err.println("Error al crear detalle de pedido: " + e.getMessage());
        }
    }

    // --- CREAR DETALLE PEDIDO (CONEXIÓN EXTERNA PARA TRANSACCIONES) ---
    public void crear(DetallePedido detalle, Connection conn) throws SQLException {
        String sql = "INSERT INTO detalle_pedido (cantidad, subtotal, id_pedido, id_producto) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, detalle.getCantidad());
            ps.setDouble(2, detalle.getSubtotal());
            ps.setLong(3, detalle.getPedido().getId());
            ps.setLong(4, detalle.getProducto().getId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) detalle.setId(rs.getLong(1));
            }
        }
    }

    // --- BUSCAR DETALLE PEDIDO POR ID ---
    @Override
    public Optional<DetallePedido> buscarPorId(Long id) {
        String sql = "SELECT * FROM detalle_pedido WHERE id = ? AND eliminado = false";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DetallePedido d = new DetallePedido();
                    d.setId(rs.getLong("id"));
                    d.setEliminado(rs.getBoolean("eliminado"));
                    d.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setSubtotal(rs.getDouble("subtotal"));
                    Producto prod = new Producto();
                    prod.setId(rs.getLong("id_producto"));
                    d.setProducto(prod);
                    return Optional.of(d);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar detalle de pedido: " + e.getMessage());
        }
        return Optional.empty();
    }

    // --- LISTAR TODOS LOS DETALLE PEDIDO ---
    @Override
    public List<DetallePedido> listar() {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_pedido WHERE eliminado = false";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DetallePedido d = new DetallePedido();
                d.setId(rs.getLong("id"));
                d.setEliminado(rs.getBoolean("eliminado"));
                d.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                d.setCantidad(rs.getInt("cantidad"));
                d.setSubtotal(rs.getDouble("subtotal"));
                Producto prod = new Producto();
                prod.setId(rs.getLong("id_producto"));
                d.setProducto(prod);
                detalles.add(d);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar detalles de pedido: " + e.getMessage());
        }
        return detalles;
    }

    // --- ACTUALIZAR DETALLE PEDIDO ---
    @Override
    public void actualizar(DetallePedido detalle) {
        String sql = "UPDATE detalle_pedido SET cantidad = ?, subtotal = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detalle.getCantidad());
            ps.setDouble(2, detalle.getSubtotal());
            ps.setLong(3, detalle.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar detalle de pedido: " + e.getMessage());
        }
    }

    // --- ELIMINAR DETALLE PEDIDO ---
    @Override
    public void eliminar(Long id) {
        String sql = "UPDATE detalle_pedido SET eliminado = true WHERE id = ?";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar detalle de pedido: " + e.getMessage());
        }
    }

    // --- LISTAR DETALLES PEDIDO POR PEDIDO ---
    public List<DetallePedido> listarPorPedido(Long idPedido) {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_pedido WHERE id_pedido = ? AND eliminado = false";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedido d = new DetallePedido();
                    d.setId(rs.getLong("id"));
                    d.setEliminado(rs.getBoolean("eliminado"));
                    d.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setSubtotal(rs.getDouble("subtotal"));
                    Producto prod = new Producto();
                    prod.setId(rs.getLong("id_producto"));
                    d.setProducto(prod);
                    detalles.add(d);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar detalles por pedido: " + e.getMessage());
        }
        return detalles;
    }

}