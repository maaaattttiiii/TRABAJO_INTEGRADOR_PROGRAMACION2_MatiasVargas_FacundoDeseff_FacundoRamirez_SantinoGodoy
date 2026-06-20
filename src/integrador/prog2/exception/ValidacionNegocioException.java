package integrador.prog2.exception;

public class ValidacionNegocioException extends Exception {
    // Constructor para pasarle el mensaje de error personalizado
    public ValidacionNegocioException(String mensaje) {
        super(mensaje);
    }
}