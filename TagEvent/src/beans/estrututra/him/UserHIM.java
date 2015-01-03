package beans.estrututra.him;

import java.io.Serializable;

public class UserHIM implements Serializable
{
	private static final long serialVersionUID = -961964456584206072L;

	private String login;
	private String nome;
	private String tipo;
	private String nascimento;
	private String telefone;
	private String cadastrado;
	private String ultimoAcesso;
	private long nrAcessos;
	private int status;

	public UserHIM() {}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNascimento() {
		return nascimento;
	}

	public void setNascimento(String nascimento) {
		this.nascimento = nascimento;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCadastrado() {
		return cadastrado;
	}

	public void setCadastrado(String cadastrado) {
		this.cadastrado = cadastrado;
	}

	public String getUltimoAcesso() {
		return ultimoAcesso;
	}

	public void setUltimoAcesso(String ultimoAcesso) {
		this.ultimoAcesso = ultimoAcesso;
	}

	public long getNrAcessos() {
		return nrAcessos;
	}

	public void setNrAcessos(long nrAcessos) {
		this.nrAcessos = nrAcessos;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
}
