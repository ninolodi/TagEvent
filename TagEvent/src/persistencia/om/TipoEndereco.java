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
public class TipoEndereco implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkTipoEndereco;
	private String nome;
	private int tipo; //ENDERECO_ENTREGA, ENDERECO_COBRANCA, ENDERECO_RESIDENCIAL, ENDERECO_COMERCIAL

	@OneToMany(mappedBy="tipoEndereco")
	private List<Endereco> enderecos;


	/*-*-*-* Construtores *-*-*-*/
	public TipoEndereco() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkTipoEndereco() { return pkTipoEndereco; }
	public void setPkTipoEndereco(long pkTipoEndereco) { this.pkTipoEndereco = pkTipoEndereco; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public int getTipo() { return tipo; }
	public void setTipo(int tipo) { this.tipo = tipo; }

	public List<Endereco> getEnderecos() { if(enderecos==null) { enderecos = new ArrayList<Endereco>(); } return enderecos; }
	public void setEnderecos(List<Endereco> enderecos) { this.enderecos = enderecos; }
}