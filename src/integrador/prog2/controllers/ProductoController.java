package integrador.prog2.controllers;

import integrador.prog2.entities.Producto;
import integrador.prog2.exception.EntidadNoEncontradaException;
import integrador.prog2.exception.ValidacionNegocioException;
import integrador.prog2.services.ProductoService;

import java.util.List;
import java.util.Optional;

public class ProductoController {
    private ProductoService productoService;

    public ProductoController() {
        this.productoService = new ProductoService();
    }

    public void registrarProducto(Producto producto) {
        try {
            System.out.println("\n[ProductoController] Peticion recibida para registrar un producto...");
            productoService.guardarProducto(producto);
            System.out.println("[ProductoController] Producto registrado con exito. ID: " + producto.getId());
        } catch (ValidacionNegocioException e) {
            System.out.println("[ProductoController] Error al validar producto:");
            System.out.println("   -> " + e.getMessage());
        }
    }

    public List<Producto> listarProductos() {
        return productoService.listar();
    }

    public List<Producto> listarPorCategoria(Long idCategoria) {
        return productoService.listarPorCategoria(idCategoria);
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoService.buscarPorId(id);
    }

    public void actualizarProducto(Producto producto) {
        try {
            productoService.actualizar(producto);
            System.out.println("[ProductoController] Producto actualizado con exito.");
        } catch (ValidacionNegocioException e) {
            System.out.println("[ProductoController] Error al actualizar producto:");
            System.out.println("   -> " + e.getMessage());
        } catch (EntidadNoEncontradaException e) {
            System.out.println("[ProductoController] " + e.getMessage());
        }
    }

    public void eliminarProducto(Long id) {
        try {
            productoService.eliminar(id);
            System.out.println("[ProductoController] Producto eliminado con exito.");
        } catch (ValidacionNegocioException e) {
            System.out.println("[ProductoController] Error al eliminar producto:");
            System.out.println("   -> " + e.getMessage());
        } catch (EntidadNoEncontradaException e) {
            System.out.println("[ProductoController] " + e.getMessage());
        }
    }
}
