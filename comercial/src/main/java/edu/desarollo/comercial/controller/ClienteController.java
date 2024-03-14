package edu.desarollo.comercial.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.desarollo.comercial.model.entity.Cliente;
import edu.desarollo.comercial.model.services.ComercialServiceIface;
import edu.desarollo.comercial.utils.paginator.PageRender;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/comercial")
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private ComercialServiceIface comercialService;

	@GetMapping("/clienteslistar")
	public String clientesListar(@RequestParam(name = "pag", defaultValue = "0") int pag, Model model) {

		Pageable pagina = PageRequest.of(pag, 5);
		Page<Cliente> clientes = comercialService.buscarClientesTodos(pagina);
		System.out.println("\n*** Resumen del page<objeto>");
		System.out.println("-".repeat(50));
		System.out.println("Total de páginas recuperadas: " + clientes.getTotalPages());
		System.out.println("Total de registros recuperados: " + clientes.getTotalElements());
		System.out.println("Página actual: " + clientes.getNumber());
		System.out.println("Número de elementos por página: " + clientes.getSize());
		System.out.println("Número de elementos de la página actual: " + clientes.getNumberOfElements());
		System.out.println("Hay una página anterior ? " + clientes.hasPrevious());
		System.out.println("Hay una página siguiente ? " + clientes.hasNext());
		System.out.println("Es la primera página ? " + clientes.isFirst());
		System.out.println("Es la última página ? " + clientes.isLast());

		// List<Cliente> clientes = comercialService.buscarClientesTodos();
		PageRender<Cliente> pageRender = new PageRender<>("/comercial/clienteslistar", clientes);
		model.addAttribute("pageRender", pageRender);
		model.addAttribute("titulo", "Listado de clientes activos");
		model.addAttribute("clientes", clientes);
		return "cliente/listado_clientes";
	}

	@GetMapping("/clientenuevo")
	public String clienteFormNuevo(Model model) {
		model.addAttribute("titulo", "Nuevo cliente");
		model.addAttribute("accion", "Crear");
		model.addAttribute("cliente", new Cliente());
		return "cliente/formulario_cliente";
	}

	@PostMapping("/clienteguardar")
	public String clienteGuardar(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result,
			Model model, SessionStatus status, RedirectAttributes flash,
			@RequestParam(name = "file") MultipartFile imagen) {

		String accion = (cliente.getId() == null) ? "Crear" : "Modificar";
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Nuevo cliente");
			model.addAttribute("accion", accion);
			model.addAttribute("info", "Complemente o corrija la información de los campos del formulario");
			return "cliente/formulario_cliente";
		}

		if (!imagen.isEmpty()) {

			if(cliente.getId() != null && cliente.getId() > 0 && cliente.getImagen() != null && cliente.getImagen().length() > 0){
				Path rutaAbsUploads = Paths.get("uploads").resolve(cliente.getImagen()).toAbsolutePath();
				File archivo = rutaAbsUploads.toFile();

				if(archivo.exists() && archivo.canRead()){
					archivo.delete();
				}
			}

			String nombreUnico = UUID.randomUUID().toString().substring(0, 8) + "_ " + imagen.getOriginalFilename();
			Path rutaUploads = Paths.get("uploads").resolve(nombreUnico);
			Path rutaAbsUploads = rutaUploads.toAbsolutePath();

			System.out.println("Ruta Relativa " + rutaUploads.toString());
			System.out.println("Ruta Absoluta" + rutaAbsUploads.toString());

			try {
				Files.copy(imagen.getInputStream(), rutaAbsUploads);
				cliente.setImagen(nombreUnico);
				flash.addFlashAttribute("info", "El archivo " + nombreUnico + " fue cargado");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		comercialService.guardarCliente(cliente);
		status.setComplete();
		flash.addFlashAttribute("success",
				"El registro fue " + (cliente.getId() == null ? "agregado" : "modificado") + " con éxito");
		return "redirect:/comercial/clienteslistar";
	}

	@GetMapping("/clienteconsultar/{id}")
	public String clienteConsultar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model) {
		Cliente cliente = comercialService.buscarClientePorId(id);
		if (cliente == null) {
			flash.addFlashAttribute("warning", "El registro no fue hallado en la base de datos");
			return "redirect:/comercial/clienteslistar";
		}
		System.out.println(cliente);
		model.addAttribute("titulo", "Consulta del cliente: " + cliente.getNombreCompleto());
		model.addAttribute("cliente", cliente);
		return "cliente/consulta_cliente";
	}

	@GetMapping("/clientemodificar/{id}")
	public String clienteFormModificar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = comercialService.buscarClientePorId(id);
			if (cliente == null) {
				flash.addFlashAttribute("warning", "El registro no fue hallado en la base de datos");
				return "redirect:/comercial/clienteslistar";
			}
		} else {
			flash.addFlashAttribute("error", "Error, el ID no es válido !!");
			return "redirect:/comercial/clienteslistar";
		}
		model.addAttribute("accion", "Modificar");
		model.addAttribute("titulo", "Modificar cliente");
		model.addAttribute("cliente", cliente);
		return "cliente/formulario_cliente";
	}

	@GetMapping("/clienteeliminar/{id}")
	public String clienteEliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente cliente = comercialService.buscarClientePorId(id);
			if (cliente != null) {
				Path rutaAbsUploads = Paths.get("uploads").resolve(cliente.getImagen()).toAbsolutePath();
				File archivo = rutaAbsUploads.toFile();

				if(archivo.exists() && archivo.canRead()){
					if(archivo.delete()){
						flash.addFlashAttribute("info", "El archivo " + cliente.getImagen() + " fue eliminado");
					}
				}
			
				comercialService.eliminarClientePorId(id);
				flash.addFlashAttribute("success", "El registro fue eliminado de la base de datos");
			}
		} else {
			flash.addFlashAttribute("error", "Error, el ID no es válido !!");
		}
		return "redirect:/comercial/clienteslistar";
	}
} // fin clase ClienteController
