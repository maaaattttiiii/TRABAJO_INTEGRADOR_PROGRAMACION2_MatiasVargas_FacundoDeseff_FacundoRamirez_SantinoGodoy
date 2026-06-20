package integrador.prog2.controllers;

import integrador.prog2.entities.Producto;
import integrador.prog2.exception.ValidacionNegocioException;
import integrador.prog2.services.ProductoService;

public class ProductoController {

    private ProductoService productoService;

    public ProductoController() {
        this.productoService = new ProductoService();
    }

    public void registrarProducto(Producto producto) {
        try {
            System.out.println("\n🎛️ [Controller] Petición recibida para registrar un producto...");

            // Delegamos la validación al servicio de productos
            productoService.validarProducto(producto);

            System.out.println("🎛️ [Controller] Producto validado y listo para el DAO.");
        } catch (ValidacionNegocioException e) {
            System.out.println("🎛️ [Controller] Error de negocio al validar producto:");
            System.out.println("👉 " + e.getMessage());
        }
    }
}