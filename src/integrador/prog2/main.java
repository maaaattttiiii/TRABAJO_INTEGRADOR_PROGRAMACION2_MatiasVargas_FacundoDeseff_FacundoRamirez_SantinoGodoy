package integrador.prog2;

import integrador.prog2.entities.Producto;
import integrador.prog2.entities.Categoria;
import integrador.prog2.exception.ValidacionNegocioException;
import integrador.prog2.services.ProductoService;

public class main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("🧪 Testeando el Servicio de Productos...");
        System.out.println("=========================================");

        ProductoService productoService = new ProductoService();

        // 1. Creamos un producto VÁLIDO
        Categoria catMaxi = new Categoria();
        catMaxi.setNombre("Papas Fritas");

        Producto papasOk = new Producto();
        papasOk.setNombre("Papas Lay's");
        papasOk.setPrecio(2500.0);
        papasOk.setCategoria(catMaxi);

        // 2. Creamos un producto INVÁLIDO (Sin categoría y precio en cero)
        Producto papasTruchas = new Producto();
        papasTruchas.setNombre("Papas Sueltas Anonimas");
        papasTruchas.setPrecio(0.0); // 🔴 Rompe regla de precio <= 0

        // 3. Probamos validar ambos en un try-catch
        try {
            // El primero tendría que pasar sin problemas
            productoService.validarProducto(papasOk);

            System.out.println("-----------------------------------------");
            System.out.println("⚠️ Ahora intentamos pasar el producto trucho...");

            // El segundo tendría que hacer saltar la excepción de negocio
            productoService.validarProducto(papasTruchas);

        } catch (ValidacionNegocioException e) {
            System.out.println("\n⚠️ ¡Excepción capturada en el main con éxito!");
            System.out.println("Mensaje de error: " + e.getMessage());
        }

        System.out.println("=========================================");
        System.out.println("🏁 Fin del test de productos.");
        System.out.println("=========================================");
    }
}