package integrador.prog2.services;

import integrador.prog2.DAO.ProductoDAO;
import integrador.prog2.entities.Producto;
import integrador.prog2.exception.ValidacionNegocioException;

public class ProductoService {

    private ProductoDAO productoDAO;

    public ProductoService() {
        this.productoDAO = new ProductoDAO();
    }

    public void validarProducto(Producto producto) throws ValidacionNegocioException {
        System.out.println(" Validando consistencia del producto: " + producto.getNombre() + "...");

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: El producto debe tener un nombre obligatorio.");
        }
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: El precio del producto '" + producto.getNombre() + "' debe ser mayor a $0.");
        }
        if (producto.getCategoria() == null) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: El producto '" + producto.getNombre() + "' debe tener una categoría asignada obligatoriamente.");
        }
        if (producto.getStock() != null && producto.getStock() < 0) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: El stock del producto '" + producto.getNombre() + "' no puede ser menor a 0.");
        }
        System.out.println(" El producto '" + producto.getNombre() + "' pasó todas las validaciones.");
    }

    public void guardarProducto(Producto producto) {
        System.out.println(" [ProductoService] Enviando producto a la base de datos...");
        productoDAO.crear(producto);
        System.out.println(" [ProductoService] Producto guardado exitosamente con ID: " + producto.getId());
    }
}