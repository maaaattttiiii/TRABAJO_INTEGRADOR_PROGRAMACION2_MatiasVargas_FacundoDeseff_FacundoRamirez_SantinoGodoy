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

    @Override
    public void crear(Pedido pedido) {
        String sql = "INSERT INTO pedido (fecha, estado, total, forma_pago, id_usuario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, Timestamp.valueOf(pedido.getFecha()));
            ps.setString(2, pedido.getEstado().name());
            ps.setDouble(3, pedido.getTotal());
            ps.setString(4, pedido.getFormaPago() != null ? pedido.getFormaPago().name() : null);
            ps.setLong(5, pedido.getUsuario().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) pedido.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            System.err.println("Error al crear pedido: " + e.getMessage());
        }
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        String sql = "SELECT p.*, u.nombre AS usr_nombre, u.apellido AS usr_apellido, u.mail AS usr_mail " +
                     "FROM pedido p INNER JOIN usuario u ON p.id_usuario = u.id " +
                     "WHERE p.id = ? AND p.eliminado = false";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pedido p = new Pedido();
                    p.setId(rs.getLong("id"));
                    p.setEliminado(rs.getBoolean("eliminado"));
                    if(rs.getTimestamp("createdAt") != null) p.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    p.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    p.setEstado(Estado.valueOf(rs.getString("estado")));
                    p.setTotal(rs.getDouble("total"));

                    String fpStr = rs.getString("forma_pago");
                    if (fpStr != null) p.setFormaPago(FormaPago.valueOf(fpStr));

                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getLong("id_usuario"));
                    usuario.setNombre(rs.getString("usr_nombre"));
                    usuario.setApellido(rs.getString("usr_apellido"));
                    usuario.setMail(rs.getString("usr_mail"));
                    p.setUsuario(usuario);
                    return Optional.of(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar pedido: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Pedido> listar() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, u.nombre AS usr_nombre, u.apellido AS usr_apellido, u.mail AS usr_mail " +
                     "FROM pedido p INNER JOIN usuario u ON p.id_usuario = u.id " +
                     "WHERE p.eliminado = false";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getLong("id"));
                p.setEliminado(rs.getBoolean("eliminado"));
                if(rs.getTimestamp("createdAt") != null) p.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                p.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                p.setEstado(Estado.valueOf(rs.getString("estado")));
                p.setTotal(rs.getDouble("total"));

                String fpStr = rs.getString("forma_pago");
                if (fpStr != null) p.setFormaPago(FormaPago.valueOf(fpStr));

                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("id_usuario"));
                usuario.setNombre(rs.getString("usr_nombre"));
                usuario.setApellido(rs.getString("usr_apellido"));
                usuario.setMail(rs.getString("usr_mail"));
                p.setUsuario(usuario);
                pedidos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pedidos: " + e.getMessage());
        }
        return pedidos;
    }

    @Override
    public void actualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET fecha = ?, estado = ?, total = ?, forma_pago = ? WHERE id = ?";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(pedido.getFecha()));
            ps.setString(2, pedido.getEstado().name());
            ps.setDouble(3, pedido.getTotal());
            ps.setString(4, pedido.getFormaPago() != null ? pedido.getFormaPago().name() : null);
            ps.setLong(5, pedido.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar pedido: " + e.getMessage());
        }
    }

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

    public void crearPedidoCompleto(Pedido pedido) throws SQLException {
        String sqlPedido = "INSERT INTO pedido (fecha, estado, total, forma_pago, id_usuario) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnectionPool.getConnection();
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                ps.setTimestamp(1, Timestamp.valueOf(pedido.getFecha()));
                ps.setString(2, pedido.getEstado().name());
                ps.setDouble(3, pedido.getTotal());
                ps.setString(4, pedido.getFormaPago() != null ? pedido.getFormaPago().name() : null);
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

    public List<Pedido> listarPorUsuario(Long idUsuario) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, u.nombre AS usr_nombre, u.apellido AS usr_apellido, u.mail AS usr_mail " +
                     "FROM pedido p INNER JOIN usuario u ON p.id_usuario = u.id " +
                     "WHERE p.id_usuario = ? AND p.eliminado = false";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setId(rs.getLong("id"));
                    p.setEliminado(rs.getBoolean("eliminado"));
                    if(rs.getTimestamp("createdAt") != null) p.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    p.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    p.setEstado(Estado.valueOf(rs.getString("estado")));
                    p.setTotal(rs.getDouble("total"));

                    String fpStr = rs.getString("forma_pago");
                    if (fpStr != null) p.setFormaPago(FormaPago.valueOf(fpStr));

                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getLong("id_usuario"));
                    usuario.setNombre(rs.getString("usr_nombre"));
                    usuario.setApellido(rs.getString("usr_apellido"));
                    usuario.setMail(rs.getString("usr_mail"));
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