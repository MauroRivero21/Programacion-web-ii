package edu.desarollo.comercial.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.desarollo.comercial.model.entity.Cliente;

public interface ClienteDAO extends JpaRepository<Cliente, Long>{
    
}
