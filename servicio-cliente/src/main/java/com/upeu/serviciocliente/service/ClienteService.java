package com.upeu.serviciocliente.service;

import com.upeu.serviciocliente.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    Cliente createCliente(Cliente cliente);

    Cliente updateCliente(Integer id, Cliente cliente);

    void deleteCliente(Integer id);

    Optional<Cliente> getClienteById(Integer id);

    List<Cliente> getAllClientes();
}
