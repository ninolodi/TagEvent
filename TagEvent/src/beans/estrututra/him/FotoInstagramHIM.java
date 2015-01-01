package beans.estrututra.him;

import java.io.Serializable;

import persistencia.om.InstagramFoto;

public class FotoInstagramHIM implements Serializable
{
	private static final long serialVersionUID = -961964456584206072L;
	
	private Long pk;
	private boolean selecionado;
	
	private InstagramFoto instagramFoto;
	
	public FotoInstagramHIM() {}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public InstagramFoto getInstagramFoto() {
		return instagramFoto;
	}

	public void setInstagramFoto(InstagramFoto instagramFoto) {
		this.instagramFoto = instagramFoto;
	}

	
}
