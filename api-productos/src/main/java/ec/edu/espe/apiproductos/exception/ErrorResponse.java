package ec.edu.espe.apiproductos.exception;

import java.time.LocalDateTime;

/**
 * Cuerpo JSON uniforme para todas las respuestas de error de la API.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        String ruta) {
}
