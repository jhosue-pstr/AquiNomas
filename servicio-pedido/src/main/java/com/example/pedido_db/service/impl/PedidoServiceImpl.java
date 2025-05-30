package com.example.pedido_db.service.impl;

import com.example.pedido_db.dto.Inventario;
import com.example.pedido_db.dto.InventarioUpdateRequest;
import com.example.pedido_db.entity.Pedido;
import com.example.pedido_db.dto.Cliente;
import com.example.pedido_db.dto.Producto;
import com.example.pedido_db.entity.DetallePedido;
import com.example.pedido_db.feign.ClienteFeign;
import com.example.pedido_db.feign.InventarioFeign;
import com.example.pedido_db.feign.ProductoFeign;
import com.example.pedido_db.repository.PedidoRepository;
import com.example.pedido_db.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private InventarioFeign inventarioFeign;
    @Autowired
    private RestTemplate restTemplate;

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





    @Override
    public Pedido guardar(Pedido pedido) {
        // Obtén la lista completa de inventarios desde el microservicio
        List<Inventario> inventarios = inventarioFeign.obtenerInventarios();

        // Validar que cada producto del pedido exista en inventario y tenga suficiente stock
        for (DetallePedido detalle : pedido.getDetalle()) {
            Inventario inventarioProducto = inventarios.stream()
                    .filter(inv -> inv.getProductoId() != null && inv.getProductoId().equals(detalle.getProductoId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado en inventario: ID " + detalle.getProductoId()));

            if (detalle.getCantidad() > inventarioProducto.getCantidadDisponible()) {
                throw new RuntimeException("Cantidad solicitada (" + detalle.getCantidad() +
                        ") excede la cantidad disponible (" + inventarioProducto.getCantidadDisponible() +
                        ") para el producto ID: " + detalle.getProductoId());
            }
        }

        // Si todo está ok, guarda el pedido
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // Actualiza el inventario descontando las cantidades pedidas
        for (DetallePedido detalle : pedido.getDetalle()) {
            Inventario inventarioProducto = inventarios.stream()
                    .filter(inv -> inv.getProductoId() != null && inv.getProductoId().equals(detalle.getProductoId()))
                    .findFirst()
                    .get();

            int nuevaCantidad = inventarioProducto.getCantidadDisponible() - detalle.getCantidad();

            InventarioUpdateRequest updateRequest = new InventarioUpdateRequest();
            updateRequest.setCantidadDisponible(nuevaCantidad);

            // Formatear la fecha para que el backend la acepte correctamente
            String fechaOriginal = inventarioProducto.getFechaVencimiento();
            String fechaFormateada;
            try {
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(fechaOriginal, DateTimeFormatter.RFC_1123_DATE_TIME);
                fechaFormateada = zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Exception e) {
                fechaFormateada = "9999-12-31";
            }
            updateRequest.setFechaVencimiento(fechaFormateada);

            // Log del JSON que se envía para facilitar debugging
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = mapper.writeValueAsString(updateRequest);
                System.out.println("JSON a enviar a inventario PUT: " + json);
            } catch (Exception e) {
                System.err.println("Error serializando JSON: " + e.getMessage());
            }

            // Realiza la llamada para actualizar el inventario
            try {
                ResponseEntity<Void> response = inventarioFeign.actualizarInventario(inventarioProducto.getId(), updateRequest);
                if (!response.getStatusCode().is2xxSuccessful()) {
                    System.err.println("Error actualizando inventario producto ID " + inventarioProducto.getProductoId() +
                            ": status " + response.getStatusCode());
                }
            } catch (Exception e) {
                System.err.println("Excepción al actualizar inventario producto ID " + inventarioProducto.getProductoId() +
                        ": " + e.getMessage());
            }
        }

        return pedidoGuardado;
    }










   /* public void actualizarInventarioConRestTemplate(Integer inventarioId, InventarioUpdateRequest updateRequest) {
        String url = "http://localhost:5243/inventario/" + inventarioId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<InventarioUpdateRequest> requestEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

        System.out.println("Respuesta actualización inventario con RestTemplate: " + response.getStatusCode());
    }*/


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