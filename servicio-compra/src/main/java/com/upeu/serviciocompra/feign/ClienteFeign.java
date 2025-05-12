package com.upeu.serviciocompra.feign;

import com.upeu.serviciocompra.dto.ClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-cliente", path = "/clientes")
public interface ClienteFeign {
    @GetMapping("/{id}")
    ClienteDTO obtenerClientePorId(@PathVariable Integer id);
}
