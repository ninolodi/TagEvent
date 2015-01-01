package beans.modulos.instagram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import logs.Logs;
import persistencia.dao.InstagramDAO;
import persistencia.om.Instagram;
import persistencia.om.InstagramConfiguracao;
import persistencia.om.InstagramFoto;
import utils.BDConstantes;
import utils.data.Data;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;

@ManagedBean(name="SlideshowInstagramBean")
@ViewScoped
public class SlideshowInstagramBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long pkEvento = 10;
	private List<InstagramFoto> fotosInstagram = new ArrayList<InstagramFoto>();
	private List<Instagram> instagrams = new ArrayList<Instagram>();
	private BaseJDBC baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
	private InstagramDAO instagramDAO = new InstagramDAO();
	private InstagramConfiguracao configuracao = null;
	private int tempoAtualizacaoSegundos = 15;
	
	public SlideshowInstagramBean() 
	{
		try
		{
			configuracao = instagramDAO.getInstagramConfiguracaoEvento(baseJDBC, pkEvento, BDConstantesAtta.STATUS_ATIVO, 0);
			
			fotosInstagram.clear();
			fotosInstagram.addAll(new InstagramDAO().getFotosInstagramEvento(baseJDBC, pkEvento, new int[] {BDConstantes.STATUS_ATIVO_LIBERADO}, 0, configuracao.getJanela(), 0));
			
			instagrams = new InstagramDAO().getInstagramsEvento(baseJDBC, pkEvento, BDConstantesAtta.STATUS_ATIVO, 0);
		}
		catch (Exception e)
		{
			Logs.addError("GaleriaInstagramBean - Construtor", e);
//			redirecionarErro(e);
		}
	}
	
	public void getAutoMorePhotos() throws Exception
	{
		System.out.println(" ********* getAutoMorePhotos *********");
		int nrFotosAntigas = fotosInstagram.size();
		System.out.println("nrFotosAntigas = " + nrFotosAntigas);
		fotosInstagram.addAll(0, instagramDAO.getFotosInstagramEventoData(baseJDBC, pkEvento, new Data().subSegundo(tempoAtualizacaoSegundos), new int[] {BDConstantes.STATUS_ATIVO_LIBERADO}, 0));
		int nrFotosNovas = (fotosInstagram.size() - nrFotosAntigas);
		System.out.println("nrFotosNovas = " + nrFotosNovas);
		int nrFotosRemovidas = 0;
		int nrTotalFotos = fotosInstagram.size();
		for (int i = 0; i < fotosInstagram.size(); i++) 
		{ System.out.println(i + " = " + fotosInstagram.get(i).getPkInstagramFoto()); }
		
		if(nrFotosNovas > 0) 
		{
			if(nrFotosNovas >= configuracao.getJanela())
			{ nrFotosRemovidas = configuracao.getJanela(); }
			else
			{ nrFotosRemovidas = nrFotosNovas; }
		}
		System.out.println("nrFotosRemovidas = " + nrFotosRemovidas);
		if(nrFotosRemovidas > 0)
		{
			System.out.println("i = " + (nrTotalFotos - 1) + " :: i >= " + (nrTotalFotos - nrFotosRemovidas));
			for (int i = (nrTotalFotos - 1); i >= (nrTotalFotos - nrFotosRemovidas); i--) 
			{ fotosInstagram.remove(i); System.out.println("Remover = " + i);}	
		}		
	}

	/*-*-* Getters and Setters *-*-*/
	public List<InstagramFoto> getFotosInstagram() {
		return fotosInstagram;
	}

	public void setFotosInstagram(List<InstagramFoto> fotosInstagram) {
		this.fotosInstagram = fotosInstagram;
	}

	public List<Instagram> getInstagrams() {
		return instagrams;
	}

	public void setInstagrams(List<Instagram> instagrams) {
		this.instagrams = instagrams;
	}
	

}
