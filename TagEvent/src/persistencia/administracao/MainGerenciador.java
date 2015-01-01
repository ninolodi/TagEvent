package persistencia.administracao;

import aplicacao.MainTagEvent;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.persistencia.administracao.comparaBases.ComparaBases;

public class MainGerenciador
{
	public static void main(String[] args)
	{
		try
		{
			//Carrega as configuracoes do sistema
			Configuracoes.carregar(MainTagEvent.class.getResourceAsStream("configuracoes/configuracoes.properties"), MainTagEvent.class.getResourceAsStream("configuracoes/log4j.properties"));

			Gerenciador.addModulo("TagEvent");
//			Gerenciador.addModulo("Atta");
					
			//Gerar Tabelas
			Gerenciador.baseURL = Configuracoes.propriedades.get("baseUrl");
			Gerenciador.baseBase = Configuracoes.propriedades.get("baseBase");
			Gerenciador.basePorta = Configuracoes.propriedades.getInt("basePorta");
			Gerenciador.baseLogin = Configuracoes.propriedades.get("baseLogin");
			Gerenciador.baseSenha = Configuracoes.propriedades.get("baseSenha");
			
			//Configuracoes o comparador de Bases
			ComparaBases.baseURLAtual = Gerenciador.baseURL;
			ComparaBases.baseLoginAtual = Gerenciador.baseLogin;
			ComparaBases.baseSenhaAtual = Gerenciador.baseSenha;
			ComparaBases.basePortaAtual = Gerenciador.basePorta;
			
			ComparaBases.baseURLNova = Gerenciador.baseURL;
			ComparaBases.baseLoginNova = Gerenciador.baseLogin;
			ComparaBases.baseSenhaNova = Gerenciador.baseSenha;
			ComparaBases.basePortaNova = Gerenciador.basePorta;

			Gerenciador.getInstance(args, "TagEvent", true).abrir();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
