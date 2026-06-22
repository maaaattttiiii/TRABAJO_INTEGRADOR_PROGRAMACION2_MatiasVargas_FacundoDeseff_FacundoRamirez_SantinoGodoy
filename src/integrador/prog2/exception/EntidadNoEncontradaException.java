package integrador.prog2.exception;

/**
 * Se lanza cuando se intenta buscar una entidad
 * que no existe en el sistema.
 */
public class EntidadNoEncontradaException extends Exception {
    public EntidadNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
