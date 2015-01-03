package persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;
import br.com.mresolucoes.atta.persistencia.annotations.Implementa;

@Entity
public class Estado implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkEstado;
	private String nome;
	private String sigla;
	private String efkEstado;

	@ManyToOne @JoinColumn(name="fkPais")
	private Pais pais;

	@OneToMany(mappedBy="estado")
	private List<Cidade> cidades;


	/*-*-*-* Construtores *-*-*-*/
	public Estado() { }

	/*-*-*-* Metodos Publicos *-*-*-*/
	@Implementa(codigo="nome")
	public String toString()
	{
		return nome;
	}

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkEstado() { return pkEstado; }
	public void setPkEstado(long pkEstado) { this.pkEstado = pkEstado; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getSigla() { return sigla; }
	public void setSigla(String sigla) { this.sigla = sigla; }

	public String getEfkEstado() { return efkEstado; }
	public void setEfkEstado(String efkEstado) { this.efkEstado = efkEstado; }

	public Pais getPais() { return pais; }
	public void setPais(Pais pais) { this.pais = pais; }

	public List<Cidade> getCidades() { if(cidades==null) { cidades = new ArrayList<Cidade>(); } return cidades; }
	public void setCidades(List<Cidade> cidades) { this.cidades = cidades; }
}