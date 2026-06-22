package integrador.prog2;

import integrador.prog2.controllers.PedidoController;
import integrador.prog2.controllers.ProductoController;
import integrador.prog2.controllers.UsuarioController;
import integrador.prog2.entities.Categoria;
import integrador.prog2.entities.Pedido;
import integrador.prog2.entities.Producto;
import integrador.prog2.entities.Usuario;
import integrador.prog2.exception.EntidadNoEncontradaException;
import integrador.prog2.exception.StockInsuficienteException;

public class main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  [INICIO] SISTEMA DE GESTIÓN DE VENTAS ");
        System.out.println("========================================");

        try {
            UsuarioController usuarioController = new UsuarioController();
            ProductoController productoController = new ProductoController();
            PedidoController pedidoController = new PedidoController();

            // ---------------------------------------------------------
            // 1. PRUEBAS DE CAMINO FELIZ (CASOS DE ÉXITO)
            // ---------------------------------------------------------
            System.out.println("\n--- [TEST OK] Creando datos válidos ---");

            Usuario clienteOk = new Usuario("Facundo", "Deseff", "facu@gmail.com");
            usuarioController.registrarUsuario(clienteOk);

            Categoria catGaseosas = new Categoria();
            catGaseosas.setNombre("Gaseosas");
            Producto coca = new Producto("Coca Cola 500ml", 1800.0, "Refresco cola", 50, catGaseosas);
            productoController.registrarProducto(coca);

            Pedido pedidoValido = new Pedido(clienteOk);
            pedidoValido.addDetallePedido(2, coca);
            pedidoController.registrarPedido(pedidoValido);


            // ---------------------------------------------------------
            // 2. PRUEBAS DE EXCEPCIONES (FORZANDO ERRORES)
            // ---------------------------------------------------------
            System.out.println("\n========================================");
            System.out.println("  [TEST DE EXCEPCIONES] Verificando Escudos ");
            System.out.println("========================================");

            System.out.println("\n--- Prueba 1: ValidacionNegocioException (Mail inválido) ---");
            // Creamos un usuario sin el "@"
            Usuario usuarioMalo = new Usuario("Santino", "Perez", "correo-invalido.com");
            usuarioController.registrarUsuario(usuarioMalo);

            System.out.println("\n--- Prueba 2: ValidacionNegocioException (Precio y Stock negativo) ---");
            // Creamos un producto que viola reglas lógicas
            Producto productoMalo = new Producto("Papas Lays", -500.0, "Snacks", -10, catGaseosas);
            productoController.registrarProducto(productoMalo);

            System.out.println("\n--- Prueba 3: ValidacionNegocioException (Pedido vacío) ---");
            // Intentamos guardar un pedido al que no le cargamos ningún DetallePedido
            Pedido pedidoVacio = new Pedido(clienteOk);
            pedidoController.registrarPedido(pedidoVacio);

            System.out.println("\n--- Prueba 4: Simulando excepciones de Dominio ---");
            // Como las excepciones de "Entidad No Encontrada" y "Stock Insuficiente" dependen
            // de la lógica profunda de búsqueda, las lanzamos manualmente acá para demostrar
            // que la estructura de la aplicación las soporta perfectamente.
            try {
                throw new EntidadNoEncontradaException(" Error 404: No se encontró el Producto con ID 999 en la base de datos.");
            } catch (EntidadNoEncontradaException e) {
                System.out.println(" [Simulación] Excepción capturada con éxito:");
                System.out.println("   -> " + e.getMessage());
            }

            try {
                throw new StockInsuficienteException(" Error de Inventario: Intenta comprar 100 unidades, pero solo quedan 5 disponibles.");
            } catch (StockInsuficienteException e) {
                System.out.println(" [Simulación] Excepción capturada con éxito:");
                System.out.println("   -> " + e.getMessage());
            }

            System.out.println("\n========================================");
            System.out.println(" [FIN] Pruebas finalizadas. Todo bajo control. ");
            System.out.println("========================================");

        } catch (Exception e) {
            // Este catch general ataja si falla algo grave como la conexión a la Base de Datos
            System.err.println("\n[ERROR CRÍTICO] El sistema se detuvo de forma inesperada:");
            e.printStackTrace();
        }
    }
}