package com.example.pedido_db.service.impl;

import com.example.pedido_db.entity.Pedido;
import com.example.pedido_db.dto.Cliente;
import com.example.pedido_db.dto.Producto;
import com.example.pedido_db.entity.DetallePedido;
import com.example.pedido_db.feign.ClienteFeign;
import com.example.pedido_db.feign.ProductoFeign;
import com.example.pedido_db.repository.PedidoRepository;
import com.example.pedido_db.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteFeign clienteFeign;

    @Autowired
    private ProductoFeign productoFeign;

    @Override
    public List<Pedido> listar() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        // Cargar las relaciones cliente y productos de manera similar a como se hace en listarPorId
        for (Pedido pedido : pedidos) {
            // Llamada a ClienteFeign para obtener el cliente
            Cliente cliente = clienteFeign.listById(pedido.getClienteId()).getBody();
            pedido.setCliente(cliente);

            // Cargar los detalles del pedido y los productos
            List<DetallePedido> detallePedidos = pedido.getDetalle().stream().map(detallePedido -> {
                // Llamada a ProductoFeign para obtener el producto
                Producto producto = productoFeign.listById(detallePedido.getProductoId()).getBody();
                detallePedido.setProducto(producto);
                return detallePedido;
            }).collect(Collectors.toList());

            pedido.setDetalle(detallePedidos);
        }

        return pedidos;
    }

    @Override
    public Optional<Pedido> listarPorId(Integer id) {
        try {
            Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            // Cargar Cliente
            Cliente cliente = clienteFeign.listById(pedido.getClienteId()).getBody();
            pedido.setCliente(cliente);

            // Cargar Detalles
            List<DetallePedido> pedidoDetalles = pedido.getDetalle().stream().map(detallePedido -> {
                Producto producto = productoFeign.listById(detallePedido.getProductoId()).getBody();
                detallePedido.setProducto(producto);
                return detallePedido;
            }).collect(Collectors.toList());

            pedido.setDetalle(pedidoDetalles);
            return Optional.of(pedido);
        } catch (Exception e) {
            // Log de error para detectar la causa del fallo
            System.err.println("Error al procesar el pedido con ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }



    // Guardar un nuevo pedido
    @Override
    public Pedido guardar(Pedido pedido) {
        // Guardamos el pedido en la base de datos
        return pedidoRepository.save(pedido);
    }

    // Actualizar un pedido existente
    @Override
    public Pedido actualizar(Pedido pedido) {
        // Actualizamos el pedido en la base de datos
        return pedidoRepository.save(pedido);
    }

    // Eliminar un pedido por ID
    @Override
    public void eliminar(Integer id) {
        // Eliminamos el pedido por su ID
        pedidoRepository.deleteById(id);
    }
}