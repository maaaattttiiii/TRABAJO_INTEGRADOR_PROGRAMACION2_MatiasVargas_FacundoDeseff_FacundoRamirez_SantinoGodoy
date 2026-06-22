package integrador.prog2.services;

import integrador.prog2.DAO.PedidoDAO;
import integrador.prog2.entities.DetallePedido;
import integrador.prog2.entities.Pedido;
import integrador.prog2.enums.Estado;
import integrador.prog2.exception.ValidacionNegocioException;
import java.sql.SQLException;

public class PedidoService {

    private UsuarioService usuarioService = new UsuarioService();
    private ProductoService productoService = new ProductoService();
    private PedidoDAO pedidoDAO = new PedidoDAO();

    public void procesarPedido(Pedido pedido) throws ValidacionNegocioException {
        System.out.println("[Service] Iniciando validación profunda del pedido Nro: " + pedido.getId() + "...");

        if (pedido.getUsuario() == null) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: El pedido no tiene un usuario asignado.");
        }
        usuarioService.validarUsuario(pedido.getUsuario());

        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: No se puede procesar un pedido sin renglones.");
        }

        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle.getProducto() == null) {
                throw new ValidacionNegocioException("🔴 Error de Negocio: Hay un renglón con un producto inexistente.");
            }
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new ValidacionNegocioException("🔴 Error de Negocio: La cantidad del producto '" +
                        detalle.getProducto().getNombre() + "' debe ser al menos 1.");
            }
            productoService.validarProducto(detalle.getProducto());
        }

        pedido.setEstado(Estado.CONFIRMADO);
        System.out.println("[Service] ¡Pedido e integrantes verificados con éxito! Estado: " + pedido.getEstado());

        try {
            System.out.println(" [Service] Enviando transacción a la base de datos...");
            pedidoDAO.crearPedidoCompleto(pedido);
            System.out.println(" [Service] ¡El pedido se impactó correctamente en la base de datos!");
        } catch (SQLException e) {
            throw new ValidacionNegocioException("Error Crítico: Falló el guardado del pedido. Motivo: " + e.getMessage());
        }
    }
}