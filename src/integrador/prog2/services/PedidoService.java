    package integrador.prog2.services;

    import integrador.prog2.entities.DetallePedido;
    import integrador.prog2.entities.Pedido;
    import integrador.prog2.enums.Estado;
    import integrador.prog2.exception.ValidacionNegocioException;

    public class PedidoService {

        // 💡 CONEXIÓN: El servicio de pedidos ahora conoce a los otros servicios
        private UsuarioService usuarioService = new UsuarioService();
        private ProductoService productoService = new ProductoService();

        public void procesarPedido(Pedido pedido) throws ValidacionNegocioException {
            System.out.println("🔍 [Service] Iniciando validación profunda del pedido Nro: " + pedido.getId() + "...");

            // 1. Validar el Usuario que hizo la compra
            if (pedido.getUsuario() == null) {
                throw new ValidacionNegocioException("🔴 Error de Negocio: El pedido no tiene un usuario asignado.");
            }
            // Usás tu propio servicio para validar al cliente
            usuarioService.validarUsuario(pedido.getUsuario());

            // 2. Validar que tenga productos
            if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
                throw new ValidacionNegocioException("🔴 Error de Negocio: No se puede procesar un pedido sin renglones.");
            }

            // 3. Validar cada Producto que está adentro de la lista
            for (DetallePedido detalle : pedido.getDetalles()) {
                if (detalle.getProducto() == null) {
                    throw new ValidacionNegocioException("🔴 Error de Negocio: Hay un renglón con un producto inexistente.");
                }
                // Usás el servicio de productos para validar precio, categoría, etc.
                productoService.validarProducto(detalle.getProducto());
            }

            // 4. Si todo el mapa de objetos es legal, se confirma
            pedido.setEstado(Estado.CONFIRMADO);
            System.out.println("✅ [Service] ¡Pedido e integrantes verificados con éxito! Estado: " + pedido.getEstado());
        }
    }