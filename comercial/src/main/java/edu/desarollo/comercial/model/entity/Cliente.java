package edu.desarollo.comercial.model.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@Entity
@Table(name = "clientes")
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "tipo_identificacion", nullable = false, length = 50)
	@NotEmpty
	private String tipoIdentificacion;
	
	@NotNull
	private Long identificacion;
	
	@NotEmpty
	@Column(name = "nombre_completo", length = 60, nullable = false)
	private String nombreCompleto;
	
	@NotEmpty
	private String direccion;
	
	@NotEmpty
	private String telefono;
	
	@Column(name = "correo_electronico", length = 60)
	@NotEmpty
	@Email
	private String correoElectronico;
	
	@Column(name = "fecha_ingreso")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	@NotNull
	@Past
	private Date fechaIngreso;
	
	@Column(name = "capacidad_credito", nullable = false)
	@NotNull
	@Min(value = 0)
	@Max(value = 9000000)
	private Integer capacidadCredito;

    private String imagen;

    public Cliente() {
    }

    public Cliente(Long id, String tipoIdentificacion, Long identificacion, String nombreCompleto, String direccion,
            String telefono, String correoElectronico, Date fechaIngreso, Integer capacidadCredito) {
        this.id = id;
        this.tipoIdentificacion = tipoIdentificacion;
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.fechaIngreso = fechaIngreso;
        this.capacidadCredito = capacidadCredito;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public Long getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(Long identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getCapacidadCredito() {
        return capacidadCredito;
    }

    public void setCapacidadCredito(Integer capacidadCredito) {
        this.capacidadCredito = capacidadCredito;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "{identificacion: " + identificacion + ", nombre completo: " + nombreCompleto + ", capacidad crédito: "
                + capacidadCredito + " ...}";
    }
}
