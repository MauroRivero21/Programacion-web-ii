package edu.desarollo.comercial.model.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.desarollo.comercial.model.dao.ClienteDAO;
import edu.desarollo.comercial.model.entity.Cliente;

@Service
public class ComercialService implements ComercialServiceIface{

    @Autowired
    private ClienteDAO clienteDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarClientesTodos() {
        return clienteDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> buscarClientesTodos(Pageable pageable) {
        return clienteDAO.findAll(pageable);
    }
    
    @Override
    @Transactional
    public void guardarCliente(Cliente cliente) {
        clienteDAO.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarClientePorId(Long id) {
        return clienteDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminarClientePorId(Long id) {
        clienteDAO.deleteById(id);
    }
    
}
