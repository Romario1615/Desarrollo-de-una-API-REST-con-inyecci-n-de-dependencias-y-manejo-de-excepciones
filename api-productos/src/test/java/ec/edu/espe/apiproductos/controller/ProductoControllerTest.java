package ec.edu.espe.apiproductos.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración de los ocho casos exigidos en el taller.
 * Cada prueba arranca con los tres productos iniciales.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("1. Consulta de todos los productos -> 200")
    void consultarTodos() throws Exception {
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(3)));
    }

    @Test
    @DisplayName("2. Consulta de un producto existente -> 200")
    void consultarExistente() throws Exception {
        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Teclado mecánico"));
    }

    @Test
    @DisplayName("3. Consulta de un producto inexistente -> 404")
    void consultarInexistente() throws Exception {
        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensaje").value("No existe un producto con id 99"));
    }

    @Test
    @DisplayName("4. Registro de un producto válido -> 201")
    void registrarValido() throws Exception {
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Audífonos USB\",\"precio\":25.75,\"stock\":12}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/productos/4"))
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    @DisplayName("5. Registro con precio inválido -> 400")
    void registrarPrecioInvalido() throws Exception {
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Cable HDMI\",\"precio\":0,\"stock\":5}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El precio debe ser mayor que cero"));
    }

    @Test
    @DisplayName("6. Registro con stock negativo -> 400")
    void registrarStockNegativo() throws Exception {
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Parlante\",\"precio\":30.0,\"stock\":-3}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El stock no puede ser negativo"));
    }

    @Test
    @DisplayName("7. Eliminación de un producto existente -> 204")
    void eliminarExistente() throws Exception {
        mockMvc.perform(delete("/api/productos/2"))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/productos/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("8. Eliminación de un producto inexistente -> 404")
    void eliminarInexistente() throws Exception {
        mockMvc.perform(delete("/api/productos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No existe un producto con id 99"));
    }

    @Test
    @DisplayName("Extra. Ruta inexistente -> 404 de ruta, no de producto")
    void rutaInexistente() throws Exception {
        mockMvc.perform(get("/api/productoss"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("La ruta solicitada no existe"));
    }
}
