package integrador.prog2.controllers;

import integrador.prog2.entities.Pedido;
import integrador.prog2.exception.ValidacionNegocioException;
import integrador.prog2.services.PedidoService;

public class PedidoController {
    private PedidoService pedidoService;

    public PedidoController() {
        this.pedidoService = new PedidoService();
    }

    public void registrarPedido(Pedido pedido) {
        try {
            System.out.println("\n📦 [PedidoController] Peticion recibida para registrar un pedido...");
            pedidoService.procesarPedido(pedido);
            System.out.println("📦 [PedidoController] Respuesta enviada con exito.");
        } catch (ValidacionNegocioException e) {
            System.out.println("📦 [PedidoController] Error capturado desde el servicio:");
            System.out.println("   -> " + e.getMessage());
        }
    }
}