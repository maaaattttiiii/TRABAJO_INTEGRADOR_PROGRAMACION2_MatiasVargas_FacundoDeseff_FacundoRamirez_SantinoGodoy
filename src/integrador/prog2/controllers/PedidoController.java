package integrador.prog2.controllers;

import integrador.prog2.entities.Pedido;
import integrador.prog2.exception.ValidacionNegocioException;
import integrador.prog2.services.PedidoService;

public class PedidoController {

    // El controlador necesita conectarse con el servicio para mandarle el laburo
    private PedidoService pedidoService;

    // Constructor donde inicializamos el servicio
    public PedidoController() {
        this.pedidoService = new PedidoService();
    }

    // Acción que expone el controlador para que la use el "frente" de la app
    public void registrarPedido(Pedido pedido) {
        try {
            System.out.println("\n🎛️ [Controller] Petición recibida para registrar un pedido...");

            // Le delegamos la responsabilidad al servicio
            pedidoService.procesarPedido(pedido);

            System.out.println("🎛️ [Controller] Respuesta enviada con éxito.");

        } catch (ValidacionNegocioException e) {
            // Si el servicio falla, el controlador captura el error y decide cómo mostrarlo
            System.out.println("🎛️ [Controller] Error capturado desde el servicio:");
            System.out.println("👉 " + e.getMessage());
        }
    }
}