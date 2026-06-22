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
            System.out.println("\n[ProductoController] Peticion recibida para registrar un producto...");
            productoService.validarProducto(producto);
            System.out.println("[ProductoController] Producto validado correctamente.");

            productoService.guardarProducto(producto);
            System.out.println("[ProductoController] Proceso de registro finalizado con éxito.");

        } catch (ValidacionNegocioException e) {
            System.out.println("[ProductoController] Error al validar producto:");
            System.out.println("   -> " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[ProductoController] Error inesperado al registrar el producto:");
            System.out.println("   -> " + e.getMessage());
        }
    }
}