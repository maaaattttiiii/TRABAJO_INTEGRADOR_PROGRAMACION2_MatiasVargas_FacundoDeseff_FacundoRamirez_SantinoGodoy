package integrador.prog2.exception;

/**
 * Se lanza cuando una regla de negocio
 * no se cumple durante una operación.
 */
public class ValidacionNegocioException extends Exception {
    public ValidacionNegocioException(String mensaje) {
        super(mensaje);
    }
}