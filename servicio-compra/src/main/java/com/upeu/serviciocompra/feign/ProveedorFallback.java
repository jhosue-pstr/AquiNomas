package com.upeu.serviciocompra.feign;

import com.upeu.serviciocompra.dto.ProveedorDto;
import org.springframework.stereotype.Component;

@Component
public class ProveedorFallback implements ProveedorClient {

    @Override
    public ProveedorDto getProveedorById(Integer id) {
        ProveedorDto proveedor = new ProveedorDto();
        proveedor.setId(id);
        proveedor.setNombre("Proveedor no disponible");
        proveedor.setTelefono("N/A");
        proveedor.setDireccion("N/A");
        proveedor.setEmail("N/A");
        return proveedor;
    }
}
