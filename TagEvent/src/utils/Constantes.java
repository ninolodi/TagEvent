package utils;

import imagens.Imagens;
import br.com.mresolucoes.atta.utils.ConstantesAtta;


public class Constantes extends ConstantesAtta
{
	//Caminho do projeto
	public static String PATH = "TagEvent";

	public static final String CRIP_KEY_ACESS="vNOC2014MREseACbtij";
	
	public static void inicializar()
	{
		//Versao
		VERSAO = "1.14.12.22.1";
		
		//Nome
		NOME_EMPRESA = "TagEvent";
		
		//Relatorios
		RELATORIO_RODAPE_PADRAO = "TagEvent";
		RELATORIO_LOGO = Imagens.LOGO_RELATORIO_CABECALHO;

		//Caminho do projeto
		PATH = "TagEvent";	
	}
}
