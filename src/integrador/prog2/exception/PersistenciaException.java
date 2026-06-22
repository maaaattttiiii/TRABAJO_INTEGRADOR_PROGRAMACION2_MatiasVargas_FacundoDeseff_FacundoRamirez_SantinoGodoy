package integrador.prog2.exception;

/**
 * Se lanza cuando ocurre un error al acceder
 * o guardar información en la base de datos.
 */
public class PersistenciaException extends Exception {

    public PersistenciaException(String mensaje) {
        super(mensaje);
    }

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}