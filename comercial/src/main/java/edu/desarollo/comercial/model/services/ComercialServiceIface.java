package edu.desarollo.comercial.model.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.desarollo.comercial.model.entity.Cliente;

public interface ComercialServiceIface {
	// servicios para Cliente
	public List<Cliente> buscarClientesTodos();
	public Page<Cliente> buscarClientesTodos(Pageable pageable);
	public void guardarCliente(Cliente cliente);
	public Cliente buscarClientePorId(Long id);
	public void eliminarClientePorId(Long id);    
}
