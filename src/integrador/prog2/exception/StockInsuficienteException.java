package integrador.prog2.exception;

/**
 * Se lanza cuando se intenta realizar una operación
 * con una cantidad superior al stock disponible.
 */
public class StockInsuficienteException extends Exception{
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
