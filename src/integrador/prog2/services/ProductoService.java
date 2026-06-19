package integrador.prog2.services;

import integrador.prog2.entities.Producto;
import integrador.prog2.exception.ValidacionNegocioException;

public class ProductoService {

    // Método para validar las condiciones de un producto antes de guardarlo en el sistema
    public void validarProducto(Producto producto) throws ValidacionNegocioException {
        System.out.println("🔍 Validando consistencia del producto: " + producto.getNombre() + "...");

        // REGLA DE NEGOCIO: El producto debe tener un nombre válido
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: El producto debe tener un nombre obligatorio.");
        }

        // REGLA DE NEGOCIO: El precio no puede ser 0 ni negativo
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: El precio del producto '" + producto.getNombre() + "' debe ser mayor a $0.");
        }

        // REGLA DE NEGOCIO: Todo producto debe tener una categoría asignada
        if (producto.getCategoria() == null) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: El producto '" + producto.getNombre() + "' debe tener una categoría asignada obligatoriamente.");
        }

        System.out.println("✅ El producto '" + producto.getNombre() + "' pasó todas las validaciones de negocio.");
    }
}