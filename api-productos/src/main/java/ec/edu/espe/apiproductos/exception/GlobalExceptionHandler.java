package ec.edu.espe.apiproductos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

/**
 * Manejo centralizado de excepciones: ningún endpoint necesita try/catch.
 * Cada excepción se convierte en un ErrorResponse con el código HTTP adecuado.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(ProductoNoEncontradoException ex,
                                                             HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(ProductoInvalidoException.class)
    public ResponseEntity<ErrorResponse> manejarInvalido(ProductoInvalidoException ex,
                                                         HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // JSON mal formado o con tipos incorrectos en el cuerpo de la petición
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonInvalido(HttpMessageNotReadableException ex,
                                                             HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST, "El cuerpo de la petición no es un JSON válido", request);
    }

    // Id con formato incorrecto, por ejemplo /api/productos/abc
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipoInvalido(MethodArgumentTypeMismatchException ex,
                                                             HttpServletRequest request) {
        String mensaje = "El parámetro '" + ex.getName() + "' tiene un valor inválido: " + ex.getValue();
        return construir(HttpStatus.BAD_REQUEST, mensaje, request);
    }

    // La ruta solicitada no está mapeada a ningún endpoint
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> manejarRutaInexistente(NoResourceFoundException ex,
                                                                HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, "La ruta solicitada no existe", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarGeneral(Exception ex, HttpServletRequest request) {
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", request);
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus status, String mensaje,
                                                    HttpServletRequest request) {
        ErrorResponse cuerpo = new ErrorResponse(LocalDateTime.now(), status.value(),
                status.getReasonPhrase(), mensaje, request.getRequestURI());
        return ResponseEntity.status(status).body(cuerpo);
    }
}
