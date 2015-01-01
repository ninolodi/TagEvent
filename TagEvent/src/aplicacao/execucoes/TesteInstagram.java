package aplicacao.execucoes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import logs.Logs;
import persistencia.dao.EventoDAO;
import persistencia.dao.InstagramDAO;
import persistencia.om.Evento;
import persistencia.om.Instagram;
import persistencia.om.InstagramFoto;
import utils.BDConstantes;
import utils.Constantes;
import aplicacao.MainTagEvent;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TesteInstagram 
{
	private static String idClienteInstagram = "2b1d8578faaf464784a6dfb1c5145b80";
	private static String accessTokenInstagram = "33511459.2b1d857.8b6aa01266414d83b870efe665339252";

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		//Inicializando constantes
		Constantes.inicializar();
				
		//Carrega as configuracoes do sistema
		Configuracoes.carregar(MainTagEvent.class.getResourceAsStream("configuracoes/configuracoes.properties"), MainTagEvent.class.getResourceAsStream("configuracoes/log4j.properties"));
				
		//Inicia a conexao com a base de dados
		BaseJDBC baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
		
		try
		{
			InstagramDAO instagramDAO = new InstagramDAO();
			List<Instagram> instragrams;
	    	List<Evento> eventos = new EventoDAO().getTodosEventos(baseJDBC, BDConstantesAtta.STATUS_ATIVO, 0);
	    	String tagSearch;

			boolean monitoramentoEvento;
			int statusMonitoramento;
			
	    	boolean proximaPagina = true;
	    	Evento evento = null;
	    	Instagram instagram;
	    	for(int i = 0; i<eventos.size(); i++)
	    	{
	    		evento = eventos.get(i);
	    		
	    		//Comecar a pegar as fotos de um novo evento
	    		proximaPagina = true;

	    		//Informacoes do evento
	    		long fkEvento = evento.getPkEvento();

	    		//Pega todos os foursquares de um determinado evento
	    		instragrams = instagramDAO.getInstagramsEvento(baseJDBC, fkEvento, BDConstantesAtta.STATUS_ATIVO, -1);
	    		
		    	for(int j = 0; j<instragrams.size(); j++)
		    	{
		    		instagram = instragrams.get(j);
		    		
		    		tagSearch = instagram.getHashtag();
		    		monitoramentoEvento = instagram.getMonitoramento();
		    		statusMonitoramento = (monitoramentoEvento==true ? -1 : 0);
		    		
		    		//URL de imagens do Instagram
		    		String getImagesURL = "https://api.instagram.com/v1/tags/"+ tagSearch +"/media/recent?client_id="+ idClienteInstagram +"&count=200";
		    		
		    		//Busca pelas fotos ate encontrar uma ja existente
			    	while(proximaPagina)
			    	{
			    		URL url = new URL(getImagesURL);
						// cria um stream de entrada do conteúdo
						InputStreamReader inputReader = new InputStreamReader( url.openStream() );
						BufferedReader bufferedReader = new BufferedReader( inputReader );
			 
						String linha = "";
						StringBuffer sb = new StringBuffer();
						while((linha = bufferedReader.readLine()) != null)
						{
							System.out.println(linha);
							sb.append(linha);
						}
						
						JsonParser parser = new JsonParser();
				        JsonElement element = parser.parse(sb.toString());
				        if (element.isJsonObject()) 
				        {
				            JsonObject jsons = element.getAsJsonObject();
	
				            JsonElement infosInstagram =  jsons.get("pagination");
	
				    		//URL da proxima pagina
				    		if (infosInstagram!=null && infosInstagram.getAsJsonObject().has("next_url") && infosInstagram.getAsJsonObject().get("next_url")!=null) 
				    		{ getImagesURL = infosInstagram.getAsJsonObject().get("next_url").getAsString(); }
				    		else
				    		{ getImagesURL = "Sem URL de Requisição"; proximaPagina = false; }
				            
				            
				            JsonArray fotosInstagram =  jsons.get("data").getAsJsonArray();
				   
				            JsonObject fotoInstagram;
				            InstagramFoto fotoExistente = null;
				    		//Loop passando por todas as fotos que o Instagram retornou
				    		for(int k=0; k<fotosInstagram.size(); k++)
				    		{
				    			fotoInstagram = fotosInstagram.get(k).getAsJsonObject();
				    			
				    			int statusFoto = statusMonitoramento;
				    			
				    			//ID da foto do Instagram
				    			String idFotoInstagram = fotoInstagram.get("id").getAsString();
	
				    			//Busca a foto no sistema TagEvent
				    			int[] status = new int[] {0, 998, 999}; 
				    			if(monitoramentoEvento==true)
				    			{ status = new int[] {0, -1, 998, 999}; }
				    			fotoExistente = new InstagramDAO().getFotoIdInstagram(baseJDBC, idFotoInstagram, instagram.getPkInstagram(), status, -1);
				    					
				    			//Se nao existir inseri e vai pra proxima foto, caso exista, para o loop e vai para o proximo evento
				    			if(fotoExistente==null)
				    			{
	//			    				String username = fotoInstagram.get("user").getAsJsonObject().get("username").getAsString();
	//			    				boolean liberado = $Monitoramentos->getMonitoramentosEvento(username, idEvento);
	//			    				if(statusMonitoramento == -1 && liberado!=null)
	//			    				{ statusFoto = 0; } 
				    				boolean insercao = new InstagramDAO().addInstagramFoto(baseJDBC, fotoInstagram, null, BDConstantes.TIPO_POST_INDEFINIDO, statusFoto, instagram);			    				 
				    			}
		//	 	    			else
		//	 	    			{ $proximaPagina = false; break; }
				    		}	
				    	}
			    	}
		    	}
	    	}
	    	
	    	baseJDBC.liberarConexao(true);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
	    	baseJDBC.liberarConexao(false);
			Logs.addError(e);
		}
		System.exit(0);
	}

}
