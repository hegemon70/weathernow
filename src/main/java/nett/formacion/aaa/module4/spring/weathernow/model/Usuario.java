package nett.formacion.aaa.module4.spring.weathernow.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the usuarios database table.
 * 
 */
@Entity
@Table(name="usuarios")
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_usu")
	private int idUsu;

	@Column(name="clave_usu")
	private String claveUsu;

	@Column(name="correo_usu")
	private String correoUsu;

	@Column(name="nombre_usu")
	private String nombreUsu;

	@Column(name="privilegio_usu")
	private String privilegioUsu;
/*
	//bi-directional many-to-one association to Registro
	@OneToMany(mappedBy="usuario")
	private List<Registro> registros;
*/
	public Usuario() {
	}

	public int getIdUsu() {
		return this.idUsu;
	}

	public void setIdUsu(int idUsu) {
		this.idUsu = idUsu;
	}

	public String getClaveUsu() {
		return this.claveUsu;
	}

	public void setClaveUsu(String claveUsu) {
		this.claveUsu = claveUsu;
	}

	public String getCorreoUsu() {
		return this.correoUsu;
	}

	public void setCorreoUsu(String correoUsu) {
		this.correoUsu = correoUsu;
	}

	public String getNombreUsu() {
		return this.nombreUsu;
	}

	public void setNombreUsu(String nombreUsu) {
		this.nombreUsu = nombreUsu;
	}

	public String getPrivilegioUsu() {
		return this.privilegioUsu;
	}

	public void setPrivilegioUsu(String privilegioUsu) {
		this.privilegioUsu = privilegioUsu;
	}
/*
	public List<Registro> getRegistros() {
		return this.registros;
	}

	public void setRegistros(List<Registro> registros) {
		this.registros = registros;
	}

	public Registro addRegistro(Registro registro) {
		getRegistros().add(registro);
		registro.setUsuario(this);

		return registro;
	}

	public Registro removeRegistro(Registro registro) {
		getRegistros().remove(registro);
		registro.setUsuario(null);

		return registro;
	}
*/
}