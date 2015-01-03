package persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;

@Entity
public class PessoaJuridica implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkPessoaJuridica;
	private String nomeFantasia;
	private String CNPJ;
	private String razaoSocial;
	private String inscricaoEstadual;
	private String inscricaoMunicipal;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;

	@ManyToOne @JoinColumn(name="fkEndereco")
	private Endereco endereco;

	@OneToMany(mappedBy="pessoaJuridica")
	private List<Contato> contatos;


	/*-*-*-* Construtores *-*-*-*/
	public PessoaJuridica() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkPessoaJuridica() { return pkPessoaJuridica; }
	public void setPkPessoaJuridica(long pkPessoaJuridica) { this.pkPessoaJuridica = pkPessoaJuridica; }

	public String getNomeFantasia() { return nomeFantasia; }
	public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }

	public String getCNPJ() { return CNPJ; }
	public void setCNPJ(String CNPJ) { this.CNPJ = CNPJ; }

	public String getRazaoSocial() { return razaoSocial; }
	public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

	public String getInscricaoEstadual() { return inscricaoEstadual; }
	public void setInscricaoEstadual(String inscricaoEstadual) { this.inscricaoEstadual = inscricaoEstadual; }

	public String getInscricaoMunicipal() { return inscricaoMunicipal; }
	public void setInscricaoMunicipal(String inscricaoMunicipal) { this.inscricaoMunicipal = inscricaoMunicipal; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public Endereco getEndereco() { return endereco; }
	public void setEndereco(Endereco endereco) { this.endereco = endereco; }

	public List<Contato> getContatos() { if(contatos==null) { contatos = new ArrayList<Contato>(); } return contatos; }
	public void setContatos(List<Contato> contatos) { this.contatos = contatos; }
}