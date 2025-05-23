package com.upeu.serviciocompra.feign;

import com.upeu.serviciocompra.dto.ProveedorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-proveedor")
public interface ProveedorClient {
    @GetMapping("/proveedores/{id}")
    ProveedorDto getProveedorById(@PathVariable("id") Integer id);
}


