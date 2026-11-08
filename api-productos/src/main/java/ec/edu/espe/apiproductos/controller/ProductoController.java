package ec.edu.espe.apiproductos.controller;

import ec.edu.espe.apiproductos.model.Producto;
import ec.edu.espe.apiproductos.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Recibe las peticiones HTTP y delega la lógica en ProductoService.
 * No contiene reglas de negocio ni bloques try/catch.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    // Inyección por constructor: Spring entrega el bean ProductoService, nunca se usa new
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Producto> registrar(@RequestBody Producto producto) {
        Producto creado = productoService.registrar(producto);
        return ResponseEntity.created(URI.create("/api/productos/" + creado.getId())).body(creado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
