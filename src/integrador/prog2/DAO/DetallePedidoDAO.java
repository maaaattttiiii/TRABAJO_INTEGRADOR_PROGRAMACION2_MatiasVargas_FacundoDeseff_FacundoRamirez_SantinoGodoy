package integrador.prog2.DAO;

import integrador.prog2.config.DatabaseConnectionPool;
import integrador.prog2.entities.DetallePedido;
import integrador.prog2.entities.Pedido;
import integrador.prog2.entities.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DetallePedidoDAO implements GenericDAO<DetallePedido> {

    @Override
    public void crear(DetallePedido detalle) {
        String sql = "INSERT INTO detalle_pedido (cantidad, precio_unitario, subtotal, id_pedido, id_producto) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, detalle.getCantidad());
            ps.setDouble(2, detalle.getPrecioUnitario());
            ps.setDouble(3, detalle.getSubtotal());
            ps.setLong(4, detalle.getPedido().getId());
            ps.setLong(5, detalle.getProducto().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) detalle.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            System.err.println("Error al crear detalle de pedido: " + e.getMessage());
        }
    }

    public void crear(DetallePedido detalle, Connection conn) throws SQLException {
        String sql = "INSERT INTO detalle_pedido (cantidad, precio_unitario, subtotal, id_pedido, id_producto) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, detalle.getCantidad());
            ps.setDouble(2, detalle.getPrecioUnitario());
            ps.setDouble(3, detalle.getSubtotal());
            ps.setLong(4, detalle.getPedido().getId());
            ps.setLong(5, detalle.getProducto().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) detalle.setId(rs.getLong(1));
            }
        }
    }

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
                    if(rs.getTimestamp("createdAt") != null) d.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecioUnitario(rs.getDouble("precio_unitario"));

                    Producto prod = new Producto();
                    prod.setId(rs.getLong("id_producto"));
                    d.setProducto(prod);

                    Pedido ped = new Pedido();
                    ped.setId(rs.getLong("id_pedido"));
                    d.setPedido(ped);

                    return Optional.of(d);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar detalle de pedido: " + e.getMessage());
        }
        return Optional.empty();
    }

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
                if(rs.getTimestamp("createdAt") != null) d.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                d.setCantidad(rs.getInt("cantidad"));
                d.setPrecioUnitario(rs.getDouble("precio_unitario"));

                Producto prod = new Producto();
                prod.setId(rs.getLong("id_producto"));
                d.setProducto(prod);

                Pedido ped = new Pedido();
                ped.setId(rs.getLong("id_pedido"));
                d.setPedido(ped);

                detalles.add(d);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar detalles de pedido: " + e.getMessage());
        }
        return detalles;
    }

    @Override
    public void actualizar(DetallePedido detalle) {
        String sql = "UPDATE detalle_pedido SET cantidad = ?, precio_unitario = ?, subtotal = ? WHERE id = ?";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detalle.getCantidad());
            ps.setDouble(2, detalle.getPrecioUnitario());
            ps.setDouble(3, detalle.getSubtotal());
            ps.setLong(4, detalle.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar detalle de pedido: " + e.getMessage());
        }
    }

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
                    if(rs.getTimestamp("createdAt") != null) d.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecioUnitario(rs.getDouble("precio_unitario"));

                    Producto prod = new Producto();
                    prod.setId(rs.getLong("id_producto"));
                    d.setProducto(prod);

                    Pedido ped = new Pedido();
                    ped.setId(rs.getLong("id_pedido"));
                    d.setPedido(ped);

                    detalles.add(d);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar detalles por pedido: " + e.getMessage());
        }
        return detalles;
    }
}