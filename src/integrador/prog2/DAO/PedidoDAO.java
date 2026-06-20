package integrador.prog2.DAO;

import integrador.prog2.config.DatabaseConnectionPool;
import integrador.prog2.entities.DetallePedido;
import integrador.prog2.entities.Pedido;
import integrador.prog2.entities.Usuario;
import integrador.prog2.enums.Estado;
import integrador.prog2.enums.FormaPago;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoDAO implements GenericDAO<Pedido> {

    private final DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO();

    // --- CREAR PEDIDO ---
    @Override
    public void crear(Pedido pedido) {
        String sql = "INSERT INTO pedido (fecha, estado, total, forma_pago, id_usuario) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(pedido.getFecha()));
            ps.setString(2, pedido.getEstado().name());
            ps.setDouble(3, pedido.getTotal());
            ps.setString(4, pedido.getFormaPago().name());
            ps.setLong(5, pedido.getUsuario().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) pedido.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            System.err.println("Error al crear pedido: " + e.getMessage());
        }
    }

    // --- BUSCAR PEDIDO POR ID ---
    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        String sql = "SELECT * FROM pedido WHERE id = ? AND eliminado = false";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pedido p = new Pedido();
                    p.setId(rs.getLong("id"));
                    p.setEliminado(rs.getBoolean("eliminado"));
                    p.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    p.setFecha(rs.getDate("fecha").toLocalDate());
                    p.setEstado(Estado.valueOf(rs.getString("estado")));
                    p.setTotal(rs.getDouble("total"));
                    p.setFormaPago(FormaPago.valueOf(rs.getString("forma_pago")));
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getLong("id_usuario"));
                    p.setUsuario(usuario);
                    return Optional.of(p);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar pedido: " + e.getMessage());
        }
        return Optional.empty();
    }

    // --- LISTAR PEDIDOS ---
    @Override
    public List<Pedido> listar() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE eliminado = false";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getLong("id"));
                p.setEliminado(rs.getBoolean("eliminado"));
                p.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                p.setFecha(rs.getDate("fecha").toLocalDate());
                p.setEstado(Estado.valueOf(rs.getString("estado")));
                p.setTotal(rs.getDouble("total"));
                p.setFormaPago(FormaPago.valueOf(rs.getString("forma_pago")));
                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("id_usuario"));
                p.setUsuario(usuario);
                pedidos.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar pedidos: " + e.getMessage());
        }
        return pedidos;
    }

    // --- ACTUALIZAR PEDIDO ---
    @Override
    public void actualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET fecha = ?, estado = ?, total = ?, forma_pago = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(pedido.getFecha()));
            ps.setString(2, pedido.getEstado().name());
            ps.setDouble(3, pedido.getTotal());
            ps.setString(4, pedido.getFormaPago().name());
            ps.setLong(5, pedido.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar pedido: " + e.getMessage());
        }
    }

    // --- ELIMINAR PEDIDO ---
    @Override
    public void eliminar(Long id) {
        String sql = "UPDATE pedido SET eliminado = true WHERE id = ?";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
        }
    }

    // --- CREAR PEDIDO COMPLETO (TRANSACCIÓN) ---
    public void crearPedidoCompleto(Pedido pedido) throws SQLException {
        String sqlPedido = "INSERT INTO pedido (fecha, estado, total, forma_pago, id_usuario) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnectionPool.getConnection();

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, Date.valueOf(pedido.getFecha()));
                ps.setString(2, pedido.getEstado().name());
                ps.setDouble(3, pedido.getTotal());
                ps.setString(4, pedido.getFormaPago().name());
                ps.setLong(5, pedido.getUsuario().getId());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) pedido.setId(rs.getLong(1));
                }
            }

            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.getPedido().setId(pedido.getId());
                detallePedidoDAO.crear(detalle, conn);
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;

        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    // --- LISTAR PEDIDOS POR USUARIO ---
    public List<Pedido> listarPorUsuario(Long idUsuario) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE id_usuario = ? AND eliminado = false";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setId(rs.getLong("id"));
                    p.setEliminado(rs.getBoolean("eliminado"));
                    p.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    p.setFecha(rs.getDate("fecha").toLocalDate());
                    p.setEstado(Estado.valueOf(rs.getString("estado")));
                    p.setTotal(rs.getDouble("total"));
                    p.setFormaPago(FormaPago.valueOf(rs.getString("forma_pago")));
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getLong("id_usuario"));
                    p.setUsuario(usuario);
                    pedidos.add(p);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar pedidos por usuario: " + e.getMessage());
        }
        return pedidos;
    }

}