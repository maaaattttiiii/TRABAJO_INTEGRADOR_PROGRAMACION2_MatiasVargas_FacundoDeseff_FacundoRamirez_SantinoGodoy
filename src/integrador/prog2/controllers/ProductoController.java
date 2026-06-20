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
            System.out.println("\n🛒 [ProductoController] Peticion recibida para registrar un producto...");
            productoService.validarProducto(producto);
            System.out.println("🛒 [ProductoController] Producto validado y listo para el DAO.");
        } catch (ValidacionNegocioException e) {
            System.out.println("🛒 [ProductoController] Error al validar producto:");
            System.out.println("   -> " + e.getMessage());
        }
    }
}