package com.upeu.serviciocompra.feign;

import com.upeu.serviciocompra.dto.ProveedorDto;
import org.springframework.stereotype.Component;

@Component
public class ProveedorFallback implements ProveedorClient {

    @Override
    public ProveedorDto getProveedorById(Integer id) {
        // En caso de que el servicio de proveedor no esté disponible,
        // devolvemos un proveedor ficticio o algún valor por defecto.
        ProveedorDto proveedor = new ProveedorDto();
        proveedor.setId(id);
        proveedor.setNombre("Proveedor no disponible");
        proveedor.setTelefono("N/A");
        proveedor.setDireccion("N/A");
        proveedor.setEmail("N/A");
        return proveedor;
    }
}

