package com.example.pedido_db.feign;

import com.example.pedido_db.dto.Cliente;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "servicio-cliente", path = "/clientes")
public interface ClienteFeign {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "clienteCircuitBreaker", fallbackMethod = "fallbackClienteById")
    ResponseEntity<Cliente> listById(@PathVariable Integer id);

    // Método fallback para cuando el servicio cliente no responde
    default ResponseEntity<Cliente> fallbackClienteById(Integer id, Throwable e) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNombre("Cliente no disponible");
        cliente.setApellido("Apellido no disponible");
        cliente.setDni("DNI no encontrado");
        cliente.setRuc("RUC no encontrado");
        cliente.setTelefono("Teléfono no disponible");
        cliente.setEmail("Email no disponible");
        cliente.setDireccion("Dirección no disponible");
        cliente.setDescuento(BigDecimal.ZERO);
        cliente.setFechaRegistro(null);

        return ResponseEntity.ok(cliente);
    }
}
