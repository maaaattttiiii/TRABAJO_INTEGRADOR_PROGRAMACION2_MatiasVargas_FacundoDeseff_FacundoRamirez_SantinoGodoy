package integrador.prog2.controllers;

import integrador.prog2.entities.Pedido;
import integrador.prog2.enums.Estado;
import integrador.prog2.enums.FormaPago;
import integrador.prog2.exception.EntidadNoEncontradaException;
import integrador.prog2.exception.PersistenciaException;
import integrador.prog2.exception.StockInsuficienteException;
import integrador.prog2.exception.ValidacionNegocioException;
import integrador.prog2.services.PedidoService;

import java.util.List;
import java.util.Optional;

public class PedidoController {
    private PedidoService pedidoService;

    public PedidoController() {
        this.pedidoService = new PedidoService();
    }

    public void registrarPedido(Pedido pedido) {
        try {
            System.out.println("\n[PedidoController] Peticion recibida para registrar un pedido...");
            pedidoService.procesarPedido(pedido);
            System.out.println("[PedidoController] Respuesta enviada con exito.");
        } catch (ValidacionNegocioException e) {
            System.out.println("[PedidoController] Error de negocio:");
            System.out.println("   -> " + e.getMessage());
        } catch (StockInsuficienteException e) {
            System.out.println("[PedidoController] Error de inventario:");
            System.out.println("   -> " + e.getMessage());
        } catch (PersistenciaException e) {
            System.out.println("[PedidoController] Error de persistencia:");
            System.out.println("   -> " + e.getMessage());
        } catch (EntidadNoEncontradaException e) {
            System.out.println("[PedidoController] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[PedidoController] Error inesperado al procesar el pedido:");
            System.out.println("   -> " + e.getMessage());
        }
    }

    public List<Pedido> listarPedidos() {
        return pedidoService.listar();
    }

    public List<Pedido> listarPorUsuario(Long idUsuario) {
        return pedidoService.listarPorUsuario(idUsuario);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoService.buscarPorId(id);
    }

    public void actualizarEstado(Long idPedido, Estado nuevoEstado) {
        try {
            pedidoService.actualizarEstado(idPedido, nuevoEstado);
            System.out.println("[PedidoController] Estado actualizado con exito.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("[PedidoController] " + e.getMessage());
        }
    }

    public void actualizarFormaPago(Long idPedido, FormaPago nuevaForma) {
        try {
            pedidoService.actualizarFormaPago(idPedido, nuevaForma);
            System.out.println("[PedidoController] Forma de pago actualizada con exito.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("[PedidoController] " + e.getMessage());
        }
    }

    public void eliminarPedido(Long id) {
        try {
            pedidoService.eliminar(id);
            System.out.println("[PedidoController] Pedido eliminado con exito.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("[PedidoController] " + e.getMessage());
        }
    }
}
