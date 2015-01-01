package beans.estrututra.him;

import java.io.Serializable;

public class TopUserHIM implements Serializable
{
	private static final long serialVersionUID = -961964456584206072L;

	private long ranking;
	private String nome;
	private String login;
	private String urlImagem;
	private long nrPost;

	public TopUserHIM() {}

	public long getRanking() {
		return ranking;
	}

	public void setRanking(long ranking) {
		this.ranking = ranking;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getUrlImagem() {
		return urlImagem;
	}

	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}

	public long getNrPost() {
		return nrPost;
	}

	public void setNrPost(long nrPost) {
		this.nrPost = nrPost;
	}

	
}
