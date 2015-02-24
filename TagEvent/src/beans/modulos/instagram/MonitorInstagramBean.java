package beans.modulos.instagram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import logs.Logs;
import persistencia.dao.InstagramDAO;
import persistencia.om.Evento;
import persistencia.om.InstagramFoto;
import utils.SConstantes;
import beans.aplicacao.Contexto;
import beans.estrututra.him.FotoInstagramHIM;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;

@ManagedBean(name="MonitorInstagramBean")
@ViewScoped
public class MonitorInstagramBean implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Parametros de url
	 */
	private String hashtag = null;	
	private Long pkEvento = null;
	
	private List<FotoInstagramHIM> fotosInstagramHIM = new ArrayList<FotoInstagramHIM>();
	private BaseJDBC baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
	
	public MonitorInstagramBean() 
	{
		try
		{
			
		}
		catch (Exception e)
		{
			Logs.addError("MonitorInstagramBean - Construtor", e);
//			redirecionarErro(e);
		}
	}
	
	public void atualizar()
	{

		try
		{
			List<InstagramFoto> fotosInstagram = new ArrayList<InstagramFoto>();
			
			if(hashtag!=null && hashtag.length()>0)
			{
				fotosInstagram.addAll(new InstagramDAO().getFotosInstagramHashtag(baseJDBC, hashtag, new int[] {BDConstantesAtta.STATUS_ATIVO}, 0, null, null));
			}
			else if(pkEvento!=null && pkEvento>0)
			{
				fotosInstagram.addAll(new InstagramDAO().getFotosInstagramEvento(baseJDBC, pkEvento, new int[] {BDConstantesAtta.STATUS_ATIVO}, 0, null, null));
			}
			else
			{
				Evento eventoSelecionado = (Evento)new Contexto().getSessionObject(SConstantes.EVENTO_SELECIONADO);
				if(eventoSelecionado!=null)
				{ pkEvento = eventoSelecionado.getPkEvento(); }
				fotosInstagram.addAll(new InstagramDAO().getFotosInstagramEvento(baseJDBC, pkEvento, new int[] {BDConstantesAtta.STATUS_ATIVO}, 0, null, null));
			}
			
			InstagramFoto instagramFoto;
			FotoInstagramHIM fotoInstagramHIM;
			for (int i = 0; i < fotosInstagram.size(); i++) 
			{
				instagramFoto = fotosInstagram.get(i);
				
				fotoInstagramHIM = new FotoInstagramHIM();
				fotoInstagramHIM.setPk(instagramFoto.getPkInstagramFoto());
				fotoInstagramHIM.setSelecionado(false);
				fotoInstagramHIM.setInstagramFoto(instagramFoto);
				
				fotosInstagramHIM.add(fotoInstagramHIM);
			}
		}
		catch (Exception e)
		{
			Logs.addError("MonitorInstagramBean - Atualizar", e);
//			redirecionarErro(e);
		}
	
	}
	
	public boolean remover(long pk)
	{
		boolean removeu = false;
		FotoInstagramHIM foto;
		for (int i = (fotosInstagramHIM.size()-1); i >= 0; i--) 
		{
			foto = fotosInstagramHIM.get(i);
			if(foto.getPk() == pk)
			{
				fotosInstagramHIM.remove(i);
				removeu = true;
				break;
			}
		}
		return removeu;
	}

	public void liberar(long pk) 
	{
		try
		{
			System.out.println("liberar = "+pk);
			if(remover(pk))
			{
				InstagramFoto foto = new InstagramFoto();
				foto.setPkInstagramFoto(pk);
//				new InstagramDAO().setStatus(baseJDBC, foto, BDConstantes.STATUS_ATIVO_LIBERADO);
			}
		}
		catch(Exception e)
		{
			Logs.addError("MonitorInstagramBean - liberar foto pk=" + pk, e);
		}
	}
	
	public void bloquear(long pk) 
	{
		try
		{
			System.out.println("bloquear = "+pk);
			if(remover(pk))
			{
				InstagramFoto foto = new InstagramFoto();
				foto.setPkInstagramFoto(pk);
//				new InstagramDAO().setStatus(baseJDBC, foto, BDConstantes.STATUS_BLOQUEADO);
			}
		}
		catch(Exception e)
		{
			Logs.addError("MonitorInstagramBean - bloquear foto pk=" + pk, e);
		}
	}

	public void bloquearTodos() 
	{
		try
		{
			System.out.println("bloquearTodos");
			
			FotoInstagramHIM foto;
			for (int i = (fotosInstagramHIM.size()-1); i >= 0; i--) 
			{
				foto = fotosInstagramHIM.get(i);
//				if(foto.isSelecionado())
//				{ 
					bloquear(foto.getPk()); 
//				}
			}
		}
		catch(Exception e)
		{
			Logs.addError("MonitorInstagramBean - bloquearTodos", e);
		}
	}
	
	public void liberarTodos() 
	{
		try
		{
			System.out.println("liberarTodos");
			
			FotoInstagramHIM foto;
			for (int i = (fotosInstagramHIM.size()-1); i >= 0; i--) 
			{
				foto = fotosInstagramHIM.get(i);
//				if(foto.isSelecionado())
//				{ 
					liberar(foto.getPk()); 
//				}
			}
		}
		catch(Exception e)
		{
			Logs.addError("MonitorInstagramBean - liberarTodos", e);
		}
	}

	public boolean check(long pk) 
	{
		boolean check = false;
		try
		{
			System.out.println("check = "+pk);
			
			FotoInstagramHIM foto;
			for (int i = (fotosInstagramHIM.size()-1); i >= 0; i--) 
			{
				foto = fotosInstagramHIM.get(i);
				if(foto.getPk() == pk)
				{
					check = !foto.isSelecionado();
					foto.setSelecionado(check);
					break;
				}
			}
			return check;
		}
		catch(Exception e)
		{
			Logs.addError("MonitorInstagramBean - check foto pk=" + pk, e);
			return check;
		}
	}
	
	public void getMorePhotos() throws Exception
	{
		System.out.println("getMorePhotos");
	}
	/*-*-* Getters and Setters *-*-*/
	public List<FotoInstagramHIM> getFotosInstagramHIM() {
		return fotosInstagramHIM;
	}

	public void setFotosInstagramHIM(List<FotoInstagramHIM> fotosInstagram) {
		this.fotosInstagramHIM = fotosInstagram;
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
