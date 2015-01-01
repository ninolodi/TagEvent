package persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.Type;
import utils.data.Data;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;
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
	private String CPF;
	private String RG;
	private String RGOrgaoEmissor;
	@Type(type="timestamp")
	private Data nascimento;
	private String apelido;
	private String nacionalidade;
	private String profissao;
	private Integer formacaoEscolar;
	private Integer estadoCivil;
	@Type(type="timestamp")
	private Data dataRegime;
	private Integer regime;

	@ManyToOne @JoinColumn(name="fkEstadoRGOrgaoEmissor")
	private Estado estadoRGOrgaoEmissor;

	@ManyToOne @JoinColumn(name="fkPessoaConjuge")
	private PessoaFisica pessoaConjuge;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@OneToMany(mappedBy="pessoaConjuge")
	private List<PessoaFisica> pessoaFisicas;


	/*-*-*-* Construtores *-*-*-*/
	public PessoaFisica() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkPessoaFisica() { return pkPessoaFisica; }
	public void setPkPessoaFisica(long pkPessoaFisica) { this.pkPessoaFisica = pkPessoaFisica; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getCPF() { return CPF; }
	public void setCPF(String CPF) { this.CPF = CPF; }

	public String getRG() { return RG; }
	public void setRG(String RG) { this.RG = RG; }

	public String getRGOrgaoEmissor() { return RGOrgaoEmissor; }
	public void setRGOrgaoEmissor(String RGOrgaoEmissor) { this.RGOrgaoEmissor = RGOrgaoEmissor; }

	public Data getNascimento() { return nascimento; }
	public void setNascimento(Data nascimento) { this.nascimento = nascimento; }

	public String getApelido() { return apelido; }
	public void setApelido(String apelido) { this.apelido = apelido; }

	public String getNacionalidade() { return nacionalidade; }
	public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }

	public String getProfissao() { return profissao; }
	public void setProfissao(String profissao) { this.profissao = profissao; }

	public Integer getFormacaoEscolar() { return formacaoEscolar; }
	public void setFormacaoEscolar(Integer formacaoEscolar) { this.formacaoEscolar = formacaoEscolar; }

	public Integer getEstadoCivil() { return estadoCivil; }
	public void setEstadoCivil(Integer estadoCivil) { this.estadoCivil = estadoCivil; }

	public Data getDataRegime() { return dataRegime; }
	public void setDataRegime(Data dataRegime) { this.dataRegime = dataRegime; }

	public Integer getRegime() { return regime; }
	public void setRegime(Integer regime) { this.regime = regime; }

	public Estado getEstadoRGOrgaoEmissor() { return estadoRGOrgaoEmissor; }
	public void setEstadoRGOrgaoEmissor(Estado estadoRGOrgaoEmissor) { this.estadoRGOrgaoEmissor = estadoRGOrgaoEmissor; }

	public PessoaFisica getPessoaConjuge() { return pessoaConjuge; }
	public void setPessoaConjuge(PessoaFisica pessoaConjuge) { this.pessoaConjuge = pessoaConjuge; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public List<PessoaFisica> getPessoaFisicas() { if(pessoaFisicas==null) { pessoaFisicas = new ArrayList<PessoaFisica>(); } return pessoaFisicas; }
	public void setPessoaFisicas(List<PessoaFisica> pessoaFisicas) { this.pessoaFisicas = pessoaFisicas; }
}