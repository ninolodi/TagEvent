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
import utils.DoubleUtil;
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

	public String printCarossel(long qtdFotos)
	{
		StringBuffer print = new StringBuffer();
		InstagramFoto foto = null;
		for (int i = 0; i < fotosInstagram.size(); i++) 
		{
			foto = fotosInstagram.get(i);
			
			if(i==0) 
			{ 
				print.append(newItemGallery(true));
				print.append(newRow());
			}
			else if(i%qtdFotos==0) 
			{
				print.append(close());
				print.append(close());
				print.append(newItemGallery(false));
				print.append(newRow());
			}
			
			print.append(newPhoto(foto, qtdFotos));
            
			if(i==(fotosInstagram.size()-1))
			{
				print.append(close());
				print.append(close());
			}
		}
		
		return print.toString();
	}
	
	public String newItemGallery(boolean valor)
	{ return "<div class='item gallery"+(valor ? " active" : "")+"'>"; }

	public String newRow()
	{ return "<div class='row'>"; }
	
	public String newPhoto(InstagramFoto foto, long qtdFoto)
	{ return "<div "+(sizePhoto(qtdFoto))+" class='col-sm-"+(int)(12/qtdFoto)+" middle-box text-center'><img alt='image' class='img-responsive' src='"+foto.getUrlImagemGrande()+"'></div>"; }

	public String close()
	{ return "</div>"; }

	public String sizePhoto(long qtdFoto)
	{
		if(qtdFoto==1) 	{ return "style='width: 636px; height: 636px;'"; }
		if(qtdFoto==2) 	{ return "style='width: 512px; height: 512px;'"; }
		if(qtdFoto==6) 	{ return "style='width: 318px; height: 318px;'"; }
		if(qtdFoto==12) { return "style='width: 212px; height: 212px;'"; }
		return null;
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
