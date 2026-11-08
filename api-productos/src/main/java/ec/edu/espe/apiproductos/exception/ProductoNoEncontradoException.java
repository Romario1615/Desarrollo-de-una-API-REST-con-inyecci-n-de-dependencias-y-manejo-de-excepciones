package ec.edu.espe.apiproductos.exception;

/**
 * Se lanza cuando se consulta o elimina un producto cuyo id no existe.
 * El manejador global la traduce a HTTP 404 Not Found.
 */
public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(Long id) {
        super("No existe un producto con id " + id);
    }
}
