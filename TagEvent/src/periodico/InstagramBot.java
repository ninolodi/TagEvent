package periodico;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import logs.Logs;
import persistencia.dao.EventoDAO;
import persistencia.dao.InstagramDAO;
import persistencia.dao.MonitoramentoAutomaticoDAO;
import persistencia.om.Evento;
import persistencia.om.Instagram;
import persistencia.om.InstagramFoto;
import persistencia.om.MonitoramentoAutomatico;
import persistencia.om.UserInstagram;
import utils.BDConstantes;
import utils.PostUtil;
import utils.data.Data;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.periodico.baseTarefa.TarefaPeriodica;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class InstagramBot extends TarefaPeriodica
{
	private String idCliente = "2b1d8578faaf464784a6dfb1c5145b80";
//	private String accessToken = "33511459.2b1d857.8b6aa01266414d83b870efe665339252";
	private BaseJDBC baseJDBC = null;
	
	/*-*-*-* Construtores *-*-*-*/
	/**
	 * Construtor
	 * {@link TarefaPeriodica#TarefaPeriodica(int, int, int, int, int, String, boolean)}
	 * @param pkSerieIndicador indica em qual serie os lancamentos dever ser inseridos (necessario cadastrar previamente o indicador e a serie)
	 */
	public InstagramBot(int intervaloTempo)
	{
		super(TEMPORAL, intervaloTempo, true);
		
		//Inicia a conexao com a base de dados
		baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
	}
	
	/*-*-*-* Metodos Publicos *-*-*-*/
	@Override
	public void executarTarefa()
	{
		System.out.println("/***** InstagramBot " + new Data().toString("dd/MM/yyyy HH:mm"));
		try 
		{
			InstagramDAO instagramDAO = new InstagramDAO();
			List<Instagram> instragrams;
			String tagSearch;
			int capacidadeRepetidas = 1;
			String getImagesURL;
			URL url;
	    	InputStreamReader inputReader;
	    	BufferedReader bufferedReader;
	    	String linha;
	    	StringBuffer sb;
	    	JsonParser parser;
	        JsonElement element;
			boolean monitoramentoEvento;
			int statusMonitoramento;
			int statusFoto;
			String username = null;
			
			MonitoramentoAutomatico monitoramentoLiberado = null;
			MonitoramentoAutomaticoDAO monitoramentoDAO = new MonitoramentoAutomaticoDAO();
			
			List<Evento> eventos = new EventoDAO().getTodosEventos(baseJDBC, BDConstantesAtta.STATUS_ATIVO, 0);

			Data data = new Data();
			String post = null;
			int classificacao = BDConstantes.TIPO_GENERO_INDEFINIDO;
			JsonObject jsons;
            JsonElement infosInstagram;
            JsonArray fotosInstagram;
            JsonObject fotoInstagram;
            JsonObject user;
            InstagramFoto fotoExistente = null;
            UserInstagram userInstagram = null;
            String id;
						
	    	boolean proximaPagina = true;
	    	int fotosRepetidasEncontradas = 0;
	    	Evento evento = null;
	    	Instagram instagram;
	    	for(int i = 0; i<eventos.size(); i++)
	    	{
	    		evento = eventos.get(i);
	    		
	    		//Comecar a pegar as fotos de um novo evento
	    		proximaPagina = true;
	    		
	    		//Informacoes do evento
	    		long fkEvento = evento.getPkEvento();

	    		//Pega todos os instagramns de um determinado evento
	    		instragrams = instagramDAO.getInstagramsEvento(baseJDBC, fkEvento, BDConstantesAtta.STATUS_ATIVO, -1);
	    		
		    	for(int j = 0; j<instragrams.size(); j++)
		    	{
		    		instagram = instragrams.get(j);
		    		
		    		tagSearch = instagram.getHashtag();
		    		monitoramentoEvento = instagram.getMonitoramento();
		    		statusMonitoramento = (monitoramentoEvento==true ? BDConstantes.STATUS_MONITORAMENTO : BDConstantes.STATUS_ATIVO_LIBERADO);
		    		proximaPagina = true;
		    		capacidadeRepetidas = (instagram.getJanela()==0 ? 1 : instagram.getJanela());
		    		fotosRepetidasEncontradas = 0;
		    		
		    		//URL de imagens do Instagram
		    		getImagesURL = "https://api.instagram.com/v1/tags/"+ tagSearch +"/media/recent?client_id="+ idCliente +"&count=200";
		    		
		    		//Busca pelas fotos ate encontrar uma ja existente
			    	while(proximaPagina)
			    	{
			    		url = new URL(getImagesURL);
						// cria um stream de entrada do conteúdo
						inputReader = new InputStreamReader( url.openStream() );
						bufferedReader = new BufferedReader( inputReader );
			 
						linha = "";
						sb = new StringBuffer();
						while((linha = bufferedReader.readLine()) != null)
						{ sb.append(linha); }
						
						parser = new JsonParser();
				        element = parser.parse(sb.toString());
				        if (element.isJsonObject()) 
				        {
				            jsons = element.getAsJsonObject();

				            infosInstagram =  jsons.get("pagination");

				    		//URL da proxima pagina
				    		if (infosInstagram!=null && infosInstagram.getAsJsonObject().has("next_url") && infosInstagram.getAsJsonObject().get("next_url")!=null) 
				    		{ getImagesURL = infosInstagram.getAsJsonObject().get("next_url").getAsString(); }
				    		else
				    		{ getImagesURL = "Sem URL de Requisição"; proximaPagina = false; }
				            
				            
				            fotosInstagram =  jsons.get("data").getAsJsonArray();
				   
				    		//Loop passando por todas as fotos que o Instagram retornou
				    		for(int k=0; k<fotosInstagram.size(); k++)
				    		{
				    			fotoInstagram = fotosInstagram.get(k).getAsJsonObject();
				    			
				    			statusFoto = statusMonitoramento;
				    			monitoramentoLiberado = null;
				    			userInstagram = null;
				    			
				    			//ID da foto do Instagram
				    			String idFotoInstagram = fotoInstagram.get("id").getAsString();

				    			//Busca a foto no sistema TagEvent
				    			int[] status = new int[] {BDConstantesAtta.STATUS_ATIVO, BDConstantes.STATUS_ATIVO_LIBERADO, BDConstantes.STATUS_BLOQUEADO, BDConstantesAtta.STATUS_REMOVIDO}; 
				    			if(monitoramentoEvento==true)
				    			{ status = new int[] {BDConstantesAtta.STATUS_ATIVO, BDConstantes.STATUS_ATIVO_LIBERADO, BDConstantes.STATUS_MONITORAMENTO, BDConstantes.STATUS_BLOQUEADO, BDConstantesAtta.STATUS_REMOVIDO}; }
				    			fotoExistente = instagramDAO.getFotoIdInstagram(baseJDBC, idFotoInstagram, instagram.getPkInstagram(), status, -1);
				    					
				    			//Se nao existir inseri e vai pra proxima foto, caso exista, para o loop e vai para o proximo evento
				    			if(fotoExistente==null)
				    			{
				    				username = fotoInstagram.get("user").getAsJsonObject().get("username").getAsString();
				    				monitoramentoLiberado = monitoramentoDAO.getUserMonitoramento(baseJDBC, username, instagram, BDConstantesAtta.STATUS_ATIVO, -1);
				    				if(statusMonitoramento == BDConstantes.STATUS_MONITORAMENTO && monitoramentoLiberado!=null)
				    				{ statusFoto = BDConstantes.STATUS_ATIVO_LIBERADO; } 
				    				
				    				user = null;
				    				id = null;
					        		if(fotoInstagram.has("user") && fotoInstagram.get("user").isJsonNull()==false)
					        		{ 
					        			user = fotoInstagram.get("user").getAsJsonObject();
					        			id = (user.has("id") && user.get("id").isJsonNull()==false ? user.get("id").getAsString() : null);
					        			
					        			if(id!=null)
					        			{
					        				userInstagram = instagramDAO.getUserIdInstagram(baseJDBC, id, BDConstantesAtta.STATUS_ATIVO, -1);
						    				if(userInstagram==null)
						        			{ userInstagram = instagramDAO.addUserInstagram(baseJDBC, data, id, user.get("full_name").getAsString(), user.get("username").getAsString(), user.get("profile_picture").getAsString(), BDConstantes.TIPO_GENERO_INDEFINIDO, BDConstantesAtta.STATUS_ATIVO, null); }
					        			}					        			
					        		}				    				
				    				
				    				if(fotoInstagram.has("caption") && fotoInstagram.get("caption").isJsonNull()==false
				    		    		&& fotoInstagram.get("caption").getAsJsonObject().has("text") 
				    		    		&& fotoInstagram.get("caption").getAsJsonObject().get("text").isJsonNull()==false)
				    		    	{ post = fotoInstagram.get("caption").getAsJsonObject().get("text").getAsString(); }
				    		    		    
				    				classificacao = PostUtil.analisador(post);
					        		
				    				boolean insercao = instagramDAO.addInstagramFoto(baseJDBC, fotoInstagram, userInstagram, classificacao, statusFoto, instagram);
				    				if(insercao==false)
				    				{
				    					throw new Exception("InstagramBot - Falha ao inserir uma foto do Instagram no banco de dados.");
				    				}
				    			}
				    			else
				    			{ fotosRepetidasEncontradas++; }
				    			
				    			if(fotosRepetidasEncontradas==capacidadeRepetidas)
			 	    			{ proximaPagina = false; break; }
				    		}
				    		baseJDBC.commit();
				    	}
			    	}
		    	}
	    		
	    	}
	    	
	    	baseJDBC.liberarConexao(true);
		}
		catch (UnknownHostException e) 
		{
			baseJDBC.liberarConexao(false);
			System.out.println("/***** [SEM CONEXÃO COM A INTERNET] InstagramBot " + new Data().toString("dd/MM/yyyy HH:mm"));
		}
		catch (SocketTimeoutException e) 
		{
			baseJDBC.liberarConexao(false);
			System.out.println("/***** [SEM CONEXÃO] InstagramBot " + new Data().toString("dd/MM/yyyy HH:mm"));
		}
		catch(Exception e) 
		{
			baseJDBC.liberarConexao(false);
			Logs.addFatal("IntagramBot - Falha atualizando fotos do Instagram", e); 
		}
	}
}