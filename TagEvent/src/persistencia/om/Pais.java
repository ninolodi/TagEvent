package persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;

@Entity
public class Pais implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkPais;
	private String nome;
	private String sigla;
	private String efkPais;

	@OneToMany(mappedBy="pais")
	private List<Estado> estados;


	/*-*-*-* Construtores *-*-*-*/
	public Pais() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkPais() { return pkPais; }
	public void setPkPais(long pkPais) { this.pkPais = pkPais; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getSigla() { return sigla; }
	public void setSigla(String sigla) { this.sigla = sigla; }

	public String getEfkPais() { return efkPais; }
	public void setEfkPais(String efkPais) { this.efkPais = efkPais; }

	public List<Estado> getEstados() { if(estados==null) { estados = new ArrayList<Estado>(); } return estados; }
	public void setEstados(List<Estado> estados) { this.estados = estados; }
}