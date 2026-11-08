package ec.edu.espe.apiproductos.exception;

/**
 * Se lanza cuando los datos de un producto incumplen una regla de negocio.
 * El manejador global la traduce a HTTP 400 Bad Request.
 */
public class ProductoInvalidoException extends RuntimeException {

    public ProductoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
