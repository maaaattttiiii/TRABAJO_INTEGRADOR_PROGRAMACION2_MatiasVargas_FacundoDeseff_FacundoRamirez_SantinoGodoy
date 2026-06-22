package integrador.prog2.services;

import integrador.prog2.DAO.PedidoDAO;
import integrador.prog2.entities.DetallePedido;
import integrador.prog2.entities.Pedido;
import integrador.prog2.entities.Producto;
import integrador.prog2.enums.Estado;
import integrador.prog2.enums.FormaPago;
import integrador.prog2.exception.EntidadNoEncontradaException;
import integrador.prog2.exception.PersistenciaException;
import integrador.prog2.exception.StockInsuficienteException;
import integrador.prog2.exception.ValidacionNegocioException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PedidoService {

    private UsuarioService usuarioService = new UsuarioService();
    private ProductoService productoService = new ProductoService();
    private PedidoDAO pedidoDAO = new PedidoDAO();

    public void procesarPedido(Pedido pedido) throws ValidacionNegocioException, StockInsuficienteException, PersistenciaException, EntidadNoEncontradaException {
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
            Optional<Producto> prodDB = productoService.buscarPorId(detalle.getProducto().getId());
            if (prodDB.isEmpty()) {
                throw new EntidadNoEncontradaException("🔴 Error: El producto con ID " +
                        detalle.getProducto().getId() + " no existe en la base de datos.");
            }
            if (prodDB.get().getStock() < detalle.getCantidad()) {
                throw new StockInsuficienteException("🔴 Error de Inventario: Stock insuficiente para '" +
                        prodDB.get().getNombre() + "'. Disponible: " + prodDB.get().getStock() +
                        ", solicitado: " + detalle.getCantidad());
            }
        }

        pedido.calcularTotal();
        pedido.setEstado(Estado.CONFIRMADO);
        System.out.println("[Service] Pedido e integrantes verificados con exito! Estado: " + pedido.getEstado());

        try {
            System.out.println(" [Service] Enviando transaccion a la base de datos...");
            pedidoDAO.crearPedidoCompleto(pedido);
            System.out.println(" [Service] El pedido se impacto correctamente en la base de datos!");
        } catch (SQLException e) {
            throw new PersistenciaException("Error Critico: Fallo el guardado del pedido. Motivo: " + e.getMessage(), e);
        }
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoDAO.buscarPorId(id);
    }

    public List<Pedido> listar() {
        return pedidoDAO.listar();
    }

    public List<Pedido> listarPorUsuario(Long idUsuario) {
        return pedidoDAO.listarPorUsuario(idUsuario);
    }

    public void actualizarEstado(Long idPedido, Estado nuevoEstado) throws EntidadNoEncontradaException {
        Optional<Pedido> pedidoOpt = pedidoDAO.buscarPorId(idPedido);
        if (pedidoOpt.isEmpty()) {
            throw new EntidadNoEncontradaException("🔴 Error: El pedido con ID " + idPedido + " no existe.");
        }
        Pedido pedido = pedidoOpt.get();
        pedido.setEstado(nuevoEstado);
        pedidoDAO.actualizar(pedido);
    }

    public void actualizarFormaPago(Long idPedido, FormaPago nuevaForma) throws EntidadNoEncontradaException {
        Optional<Pedido> pedidoOpt = pedidoDAO.buscarPorId(idPedido);
        if (pedidoOpt.isEmpty()) {
            throw new EntidadNoEncontradaException("🔴 Error: El pedido con ID " + idPedido + " no existe.");
        }
        Pedido pedido = pedidoOpt.get();
        pedido.setFormaPago(nuevaForma);
        pedidoDAO.actualizar(pedido);
    }

    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Optional<Pedido> existente = pedidoDAO.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new EntidadNoEncontradaException("🔴 Error: El pedido con ID " + id + " no existe.");
        }
        pedidoDAO.eliminar(id);
    }
}
