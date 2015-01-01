package aplicacao.execucoes;

import java.util.Locale;

import aplicacao.MainTagEvent;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.persistencia.conexao.BancoDeDados;
import utils.Constantes;
import utils.PostUtil;

public class AnalisadorPost {

	public static void main(String[] args) 
	{
		Locale.setDefault(new Locale("pt", "BR"));

		//Inicializando constantes
		Constantes.inicializar();
		
		//Carrega as configuracoes do sistema
		Configuracoes.carregar(MainTagEvent.class.getResourceAsStream("configuracoes/configuracoes.properties"), MainTagEvent.class.getResourceAsStream("configuracoes/log4j.properties"));
		
		//Inicia a conexao com a base de dados
		BancoDeDados.conectar();

		PostUtil.inicializar();
		
		String word = "#MaisQueBostaDeJogo #VoleiAmil";

		
		System.out.println(PostUtil.analisador(word));
	}

}
