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

@Entity
public class Cidade implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkCidade;
	private String nome;
	private String efkCidade;

	@ManyToOne @JoinColumn(name="fkEstado")
	private Estado estado;

	@OneToMany(mappedBy="cidade")
	private List<Endereco> enderecos;


	/*-*-*-* Construtores *-*-*-*/
	public Cidade() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkCidade() { return pkCidade; }
	public void setPkCidade(long pkCidade) { this.pkCidade = pkCidade; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getEfkCidade() { return efkCidade; }
	public void setEfkCidade(String efkCidade) { this.efkCidade = efkCidade; }

	public Estado getEstado() { return estado; }
	public void setEstado(Estado estado) { this.estado = estado; }

	public List<Endereco> getEnderecos() { if(enderecos==null) { enderecos = new ArrayList<Endereco>(); } return enderecos; }
	public void setEnderecos(List<Endereco> enderecos) { this.enderecos = enderecos; }
}