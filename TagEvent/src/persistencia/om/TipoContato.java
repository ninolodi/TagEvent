package persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;

@Entity
public class TipoContato implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkTipoContato;
	private String nome;
	private int tipo; //CONTATO_EMAIL, CONTATO_TELEFONE, CONTATO_RADIO, CONTATO_FAX
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@OneToMany(mappedBy="tipoContato")
	private List<Contato> contatos;


	/*-*-*-* Construtores *-*-*-*/
	public TipoContato() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkTipoContato() { return pkTipoContato; }
	public void setPkTipoContato(long pkTipoContato) { this.pkTipoContato = pkTipoContato; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public int getTipo() { return tipo; }
	public void setTipo(int tipo) { this.tipo = tipo; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public List<Contato> getContatos() { if(contatos==null) { contatos = new ArrayList<Contato>(); } return contatos; }
	public void setContatos(List<Contato> contatos) { this.contatos = contatos; }
}