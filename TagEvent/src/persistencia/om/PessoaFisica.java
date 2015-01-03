package persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.Type;
import utils.data.Data;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;

@Entity
public class PessoaFisica implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkPessoaFisica;
	private String nome;
	private String apelido;
	private String CPF;
	private String RG;
	@Type(type="timestamp")
	private Data nascimento;
	private String telefone;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;

	@ManyToOne @JoinColumn(name="fkEndereco")
	private Endereco endereco;

	@OneToMany(mappedBy="pessoaFisica")
	private List<Contato> contatos;


	/*-*-*-* Construtores *-*-*-*/
	public PessoaFisica() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkPessoaFisica() { return pkPessoaFisica; }
	public void setPkPessoaFisica(long pkPessoaFisica) { this.pkPessoaFisica = pkPessoaFisica; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getApelido() { return apelido; }
	public void setApelido(String apelido) { this.apelido = apelido; }

	public String getCPF() { return CPF; }
	public void setCPF(String CPF) { this.CPF = CPF; }

	public String getRG() { return RG; }
	public void setRG(String RG) { this.RG = RG; }

	public Data getNascimento() { return nascimento; }
	public void setNascimento(Data nascimento) { this.nascimento = nascimento; }

	public String getTelefone() { return telefone; }
	public void setTelefone(String telefone) { this.telefone = telefone; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public Endereco getEndereco() { return endereco; }
	public void setEndereco(Endereco endereco) { this.endereco = endereco; }

	public List<Contato> getContatos() { if(contatos==null) { contatos = new ArrayList<Contato>(); } return contatos; }
	public void setContatos(List<Contato> contatos) { this.contatos = contatos; }
}