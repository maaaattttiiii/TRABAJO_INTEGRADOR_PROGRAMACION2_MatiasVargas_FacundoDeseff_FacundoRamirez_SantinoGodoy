package integrador.prog2.services;

import integrador.prog2.entities.Pedido;
import integrador.prog2.enums.Estado;
import integrador.prog2.exception.ValidacionNegocioException;

public class PedidoService {

    // Método para validar y procesar un pedido antes de cocinarlo
    public void procesarPedido(Pedido pedido) throws ValidacionNegocioException {
        System.out.println("🔍 Validando el pedido Nro: " + pedido.getId() + "...");

        // REGLA DE NEGOCIO: No se puede procesar un pedido que no tenga productos cargados
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: No se puede procesar un pedido sin renglones (detalles).");
        }

        // REGLA DE NEGOCIO: Si el total es 0 o negativo, también es inválido
        if (pedido.getTotal() == null || pedido.getTotal() <= 0) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: El total del pedido debe ser mayor a $0.");
        }

        // Si pasó todas las validaciones, le cambiamos el estado a CONFIRMADO (según enum del grupo)
        pedido.setEstado(Estado.CONFIRMADO);
        System.out.println("✅ ¡Pedido aprobado con éxito! Estado actual: " + pedido.getEstado());
    }
}