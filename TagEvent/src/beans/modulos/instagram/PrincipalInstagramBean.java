package beans.modulos.instagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import logs.Logs;
import persistencia.dao.InstagramDAO;
import persistencia.om.Evento;
import persistencia.om.Instagram;
import utils.BDConstantes;
import utils.DoubleUtil;
import utils.SConstantes;
import beans.aplicacao.Contexto;
import beans.estrututra.him.TopHashtagHIM;
import beans.estrututra.him.TopUserHIM;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.persistencia.utils.ResultSQL;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;
import br.com.mresolucoes.atta.utils.LogsUtil;

@ManagedBean(name="PrincipalInstagramBean")
@RequestScoped
public class PrincipalInstagramBean
{
	private Long pkEvento = null;
	private Evento eventoSelecionado = null;
	
	private StringBuffer dadosHistograma = new StringBuffer();
	private StringBuffer datasHistograma = new StringBuffer();
	private HashMap<String, Integer> hashDatasHistogramaInicial = new HashMap<>();
	
	private long nrTotalMidias = 0;
	private long nrTotalFotos = 0;
	private long nrTotalVideos = 0;
	private long nrTotalUsers = 0;

	private StringBuffer dadosDistribuicaoMidia = new StringBuffer();
	private StringBuffer dadosSaudeMarca = new StringBuffer();

	private List<TopUserHIM> topUsersPost = new ArrayList<>();
	private List<TopHashtagHIM> topHashtagsPost = new ArrayList<>();
	
	public PrincipalInstagramBean() 
	{
		try
		{
			atualizar();
		}
		catch (Exception e)
		{
			Logs.addError("PrincipalInstagramBean - Construtor", e);
//			redirecionarErro(e);
		}
	}
	

	public void atualizar() 
	{
		try
		{
			eventoSelecionado = (Evento)new Contexto().getSessionObject(SConstantes.EVENTO_SELECIONADO);
			
			carregarEvento();
		}
		catch (Exception e)
		{
			Logs.addError("PrincipalInstagramBean - Atualizar", e);
		}
	}	
	
	public Boolean carregarEvento()
	{
		System.out.println("carregarEvento - " + LogsUtil.classeToString(eventoSelecionado));
		try 
		{
			BaseJDBC baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
			
			if(eventoSelecionado!=null)
			{
				InstagramDAO instagramDAO = new InstagramDAO();
				pkEvento = eventoSelecionado.getPkEvento();
				/** Resumo **/
				nrTotalMidias = instagramDAO.getNrPostTipoEvento(baseJDBC, pkEvento, null);
				nrTotalFotos = instagramDAO.getNrPostTipoEvento(baseJDBC, pkEvento, BDConstantes.TIPO_FOTO);
				nrTotalVideos = instagramDAO.getNrPostTipoEvento(baseJDBC, pkEvento, BDConstantes.TIPO_VIDEO);
				nrTotalUsers = instagramDAO.getTopPostUserTipoEvento(baseJDBC, pkEvento, null, null).size();					
				/** FIM - Resumo **/
				
				/** Histograma **/
	    		ResultSQL result;
	    		int nrRegistros = 0;
				StringBuffer values = null;
				
				//Loop responsavel por armazenar todas as datas do histograma 
				result = instagramDAO.getHistogramaEvento(baseJDBC, pkEvento, "DD/MM/YYYY - HH");
	    		for (int i = 0; i < result.size(); i++) 
				{
					if(hashDatasHistogramaInicial.containsKey((String)result.get(i).get(0)))
					{
						nrRegistros = hashDatasHistogramaInicial.get((String)result.get(i).get(0));
					}
					else
					{
						nrRegistros++;
						hashDatasHistogramaInicial.put((String)result.get(i).get(0), nrRegistros);
						datasHistograma.append("["+ nrRegistros +", \""+ (String)result.get(i).get(0) +":00\"]");

						if(i!=result.size()-1)
						{ datasHistograma.append(","); }				    		
					}
				}
	    		
	    		//Loop responsavel por passar por todas as configuracoes de instagram
	    		List<Instagram> instragrams = instagramDAO.getInstagramsEvento(baseJDBC, pkEvento, BDConstantesAtta.STATUS_ATIVO, -1);
	    		Instagram instagram;
	    		datasHistograma = new StringBuffer();
	    		dadosHistograma = new StringBuffer();
	    		for(int i = 0; i<instragrams.size(); i++)
		    	{
		    		instagram = instragrams.get(i);
		    		
		    		/** Dados Histograma **/
		    		if(i!=0)
					{ dadosHistograma.append("\n");	}
					
		    		result = instagramDAO.getHistogramaInstagram(baseJDBC, instagram.getPkInstagram(), "DD/MM/YYYY - HH");
		    		values = new StringBuffer();
					for (int j = 0; j < result.size(); j++) 
					{
						if(hashDatasHistogramaInicial.containsKey((String)result.get(j).get(1)))
						{ nrRegistros = hashDatasHistogramaInicial.get((String)result.get(j).get(1)); }
						else
						{ throw new Exception("PrincipalInstagramBean - Data não encontrada no hash do Histograma..."); }
						
						values.append("["+nrRegistros+", "+result.get(j).get(2)+"]");

						if(j!=result.size()-1)
						{ values.append(","); }
					}
		    		
		    		dadosHistograma.append("{ label:\"#"+ instagram.getHashtag() +"\", data : [" + values.toString() + "], bars : { show : true, barWidth : 0.20"+ (instragrams.size()>1 ? ", order : "+ i +"}" : "}") +"}");
		    		
					if(i!=instragrams.size()-1)
					{ dadosHistograma.append(","); }
					/** FIM - Dados Histograma **/
		    	}
	    		
	    		System.out.println(datasHistograma.toString());
				System.out.println(dadosHistograma.toString());
				/** FIM - Histograma **/
								
				/** Dados Distribuicao de Midia **/
				result = instagramDAO.getDistribuicaoMidiaEvento(baseJDBC, pkEvento);
				dadosDistribuicaoMidia = new StringBuffer();
				for (int i = 0; i < result.size(); i++) 
				{				
					if(i!=0)
					{ dadosDistribuicaoMidia.append("\n");	}
					
					dadosDistribuicaoMidia.append("{ value : "+ DoubleUtil.arredonda((double)((long)result.get(i).get(2)/(double)nrTotalMidias)*100, 2) +", label : '"+ result.get(i).get(0)+" - "+ ((Integer)result.get(i).get(1) == BDConstantes.TIPO_FOTO ? "Fotos":"Vídeos") +"' }");
					
					if(i!=result.size()-1)
					{ dadosDistribuicaoMidia.append(","); }
				}
				System.out.println(dadosDistribuicaoMidia.toString());
				/** FIM - Dados Distribuicao de Midia **/

				/** Top Users **/
				topUsersPost.clear();
				result = instagramDAO.getTopPostUserTipoEvento(baseJDBC, pkEvento, null, 5);
				Vector<Object> registro = null;
				TopUserHIM himUser = null;
				for (int i = 0; i < result.size(); i++) 
				{
					registro = result.get(i);
					
					himUser = new TopUserHIM();
					himUser.setRanking((i+1));
					himUser.setNome((String)registro.get(0));
					himUser.setLogin((String)registro.get(1));
					himUser.setUrlImagem((String)registro.get(2));
					himUser.setNrPost((Long)registro.get(3));
					
					topUsersPost.add(himUser);
				}
				/** FIM - Top Users **/
				
				/** Top Hashtags**/
				topHashtagsPost.clear();
				result = instagramDAO.getTopUsersTipoHashtag(baseJDBC, pkEvento, null, null);
				registro = null;
				TopHashtagHIM himHashtag = null;
				for (int i = 0; i < result.size(); i++) 
				{
					registro = result.get(i);
					
					himHashtag = new TopHashtagHIM();
					himHashtag.setRanking((i+1));
					himHashtag.setHashtag((String)registro.get(0));
					himHashtag.setNrPost((Long)registro.get(1));
					
					topHashtagsPost.add(himHashtag);
				}
				/** FIM - Top Hashtags **/
				
				/** Dados Saude da Marca **/
				result = instagramDAO.getSaudeMarcaEvento(baseJDBC, pkEvento, null);
				dadosSaudeMarca = new StringBuffer();
				for (int i = 0; i < result.size(); i++) 
				{				
					if(i!=0)
					{ dadosSaudeMarca.append("\n");	}
					
					dadosSaudeMarca.append("{ value : "+ DoubleUtil.arredonda((double)((long)result.get(i).get(1)/(double)nrTotalMidias)*100, 2) +", label : '"+ ((Integer)result.get(i).get(0) == BDConstantes.TIPO_POST_POSITIVO ? "Positivo":((Integer)result.get(i).get(0) == BDConstantes.TIPO_POST_NEGATIVO ? "Negativo" :"Indefinido")) +"' }");
					
					if(i!=result.size()-1)
					{ dadosSaudeMarca.append(","); }
				}
				System.out.println(dadosSaudeMarca.toString());
				/** FIM - Dados Saude da Marca **/
			}
			return true;
		}
		catch (Exception e) 
		{
			return false;
		}	
	}
	
	/*-*-* Getters and Setters *-*-*/
	public String getDadosHistograma() {
		return dadosHistograma.toString();
	}	
	
	public String getDatasHistograma() {
		return datasHistograma.toString();
	}

	public String getDadosDistribuicaoMidia() {
		return dadosDistribuicaoMidia.toString();
	}

	public String getDadosSaudeMarca() {
		return dadosSaudeMarca.toString();
	}

	public long getPkEvento() {
		return pkEvento;
	}

	public void setPkEvento(long pkEvento) {
		this.pkEvento = pkEvento;
	}

	public HashMap<String, Integer> getHashDatasHistogramaInicial() {
		return hashDatasHistogramaInicial;
	}

	public void setHashDatasHistogramaInicial(
			HashMap<String, Integer> hashDatasHistogramaInicial) {
		this.hashDatasHistogramaInicial = hashDatasHistogramaInicial;
	}

	public void setDadosHistograma(StringBuffer dadosHistograma) {
		this.dadosHistograma = dadosHistograma;
	}

	public void setDatasHistograma(StringBuffer datasHistograma) {
		this.datasHistograma = datasHistograma;
	}

	public void setDadosDistribuicaoMidia(StringBuffer dadosDistribuicaoMidia) {
		this.dadosDistribuicaoMidia = dadosDistribuicaoMidia;
	}
	
	public long getNrTotalMidias() {
		return nrTotalMidias;
	}

	public void setNrTotalMidias(long nrTotalMidias) {
		this.nrTotalMidias = nrTotalMidias;
	}

	public long getNrTotalFotos() {
		return nrTotalFotos;
	}

	public void setNrTotalFotos(long nrTotalFotos) {
		this.nrTotalFotos = nrTotalFotos;
	}

	public long getNrTotalVideos() {
		return nrTotalVideos;
	}

	public void setNrTotalVideos(long nrTotalVideos) {
		this.nrTotalVideos = nrTotalVideos;
	}

	public long getNrTotalUsers() {
		return nrTotalUsers;
	}

	public void setNrTotalUsers(long nrTotalUsers) {
		this.nrTotalUsers = nrTotalUsers;
	}

	public List<TopUserHIM> getTopUsersPost() {
		return topUsersPost;
	}

	public void setTopUsersPost(List<TopUserHIM> topUsersPost) {
		this.topUsersPost = topUsersPost;
	}

	public List<TopHashtagHIM> getTopHashtagsPost() {
		return topHashtagsPost;
	}

	public void setTopHashtagsPost(List<TopHashtagHIM> topHashtagsPost) {
		this.topHashtagsPost = topHashtagsPost;
	}

	
}
