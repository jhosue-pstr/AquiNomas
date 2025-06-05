package com.upeu.serviciocliente.repostory;

import com.upeu.serviciocliente.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    // Aquí puedes agregar consultas personalizadas si es necesario

    // Ejemplo de consulta personalizada:
    Cliente findByDni(String dni);

    Cliente findByEmail(String email);
}