package ec.edu.espe.apiproductos.service;

import ec.edu.espe.apiproductos.exception.ProductoInvalidoException;
import ec.edu.espe.apiproductos.exception.ProductoNoEncontradoException;
import ec.edu.espe.apiproductos.model.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Reglas de negocio de los productos. Spring crea una única instancia (singleton)
 * y la inyecta donde se necesite. Los datos viven en memoria, sin base de datos.
 */
@Service
public class ProductoService {

    private final Map<Long, Producto> productos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();

    public ProductoService() {
        registrar(new Producto(null, "Teclado mecánico", 45.90, 15));
        registrar(new Producto(null, "Mouse inalámbrico", 18.50, 40));
        registrar(new Producto(null, "Monitor 24 pulgadas", 159.99, 8));
    }

    public List<Producto> listar() {
        return new ArrayList<>(productos.values());
    }

    public Producto buscarPorId(Long id) {
        Producto producto = productos.get(id);
        if (producto == null) {
            throw new ProductoNoEncontradoException(id);
        }
        return producto;
    }

    public Producto registrar(Producto producto) {
        validar(producto);
        producto.setId(secuencia.incrementAndGet());
        productos.put(producto.getId(), producto);
        return producto;
    }

    public void eliminar(Long id) {
        if (productos.remove(id) == null) {
            throw new ProductoNoEncontradoException(id);
        }
    }

    private void validar(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new ProductoInvalidoException("El nombre del producto es obligatorio");
        }
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new ProductoInvalidoException("El precio debe ser mayor que cero");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new ProductoInvalidoException("El stock no puede ser negativo");
        }
    }
}
