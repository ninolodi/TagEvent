package beans.modulos.instagram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import logs.Logs;
import persistencia.dao.InstagramDAO;
import persistencia.om.Evento;
import persistencia.om.InstagramFoto;
import utils.BDConstantes;
import utils.LongUtil;
import utils.SConstantes;
import beans.aplicacao.Contexto;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;

@ManagedBean(name="GaleriaInstagramBean")
@ViewScoped
public class GaleriaInstagramBean implements Serializable
{
	private static final long serialVersionUID = 5516846833366281393L;
	
	/**
	 * Parametros de url
	 */
	private String hashtag = null;	
	private Long pkEvento = null;
	
	private Evento eventoSelecionado = null;
	
	private List<InstagramFoto> fotosInstagram = new ArrayList<InstagramFoto>();
	private int offset = 0;
	private int limit = 20;
	private BaseJDBC baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
		
	public GaleriaInstagramBean() 
	{
		try
		{
			offset = 0;
			limit = 20;
			fotosInstagram.clear();	
		}
		catch (Exception e)
		{
			Logs.addError("GaleriaInstagramBean - Construtor", e);
		}
	}
	
	public void atualizar() 
	{
		try
		{
			if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("h")!=null && (hashtag == null || hashtag.equals((String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("h")) == false))
			{
				hashtag = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("h");
				pkEvento = null;
				
				offset = 0;
				limit = 20;
				fotosInstagram.clear();
			}
			else if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("e") != null && (pkEvento == null || pkEvento != LongUtil.toLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("e"))))
			{
				pkEvento = LongUtil.toLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("e"));
				hashtag = null;
				
				offset = 0;
				limit = 20;
				fotosInstagram.clear();
			}
			else if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("h")==null && FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("e") == null)
			{
				Evento eventoSessao = (Evento)new Contexto().getSessionObject(SConstantes.EVENTO_SELECIONADO);
				if(eventoSessao!=null && (pkEvento == null || pkEvento != eventoSessao.getPkEvento()))
				{
					pkEvento = eventoSessao.getPkEvento();
					hashtag = null;
					
					offset = 0;
					limit = 20;
					fotosInstagram.clear();
				}
			}
			
			getMorePhotos();
		}
		catch (Exception e)
		{
			Logs.addError("GaleriaInstagramBean - Atualizar", e);
		}
	}	
	
	public void getMorePhotos() throws Exception
	{
		System.out.println("getMorePhotos");
		if(hashtag!=null && hashtag.length()>0)
		{
			fotosInstagram.addAll(new InstagramDAO().getFotosInstagramHashtag(baseJDBC, hashtag, new int[] {BDConstantes.STATUS_ATIVO_LIBERADO}, 0, limit, offset));
		}
		else if(pkEvento!=null && pkEvento>0)
		{
			fotosInstagram.addAll(new InstagramDAO().getFotosInstagramEvento(baseJDBC, pkEvento, new int[] {BDConstantes.STATUS_ATIVO_LIBERADO}, 0, limit, offset));
		}
		else
		{
			eventoSelecionado = (Evento)new Contexto().getSessionObject(SConstantes.EVENTO_SELECIONADO);
			if(eventoSelecionado!=null)
			{ pkEvento = eventoSelecionado.getPkEvento(); }
			fotosInstagram.addAll(new InstagramDAO().getFotosInstagramEvento(baseJDBC, pkEvento, new int[] {BDConstantes.STATUS_ATIVO_LIBERADO}, 0, limit, offset));
		}
		offset = fotosInstagram.size();	
	}

	/*-*-* Getters and Setters *-*-*/
	public List<InstagramFoto> getFotosInstagram() {
		return fotosInstagram;
	}

	public void setFotosInstagram(List<InstagramFoto> fotosInstagram) {
		this.fotosInstagram = fotosInstagram;
	}

	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public Long getPkEvento() {
		return pkEvento;
	}

	public void setPkEvento(Long pkEvento) {
		this.pkEvento = pkEvento;
	}
	

}
