package persistencia.popula.palavras;

import logs.Logs;
import persistencia.dao.AnalisadorPalavraDAO;
import persistencia.om.AnalisadorPalavras;
import utils.BDConstantes;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;

public class PopulaPalavras 
{
	public PopulaPalavras(BaseJDBC baseJDBC) throws Exception
	{
		new AnalisadorPalavraDAO().apagarBasePalavras(baseJDBC, null);

		//Palavras negativas
		add(baseJDBC, "ruim", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "horrível", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "horrivel", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "péssimo", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "pessimo", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "tosco", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "bosta", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "merda", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "lixo", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "desgraça", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "cagada", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "vergonha", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "ferrar", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "fdp", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "puta", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "negativo", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "negativa", BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO);
		
		
		
		//Palavras positivos
		add(baseJDBC, "lindo", BDConstantes.TIPO_POST_POSITIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "gostoso", BDConstantes.TIPO_POST_POSITIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "maravilhoso", BDConstantes.TIPO_POST_POSITIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "fantástico", BDConstantes.TIPO_POST_POSITIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "fantastico", BDConstantes.TIPO_POST_POSITIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "bom", BDConstantes.TIPO_POST_POSITIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "demais", BDConstantes.TIPO_POST_POSITIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "top", BDConstantes.TIPO_POST_POSITIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "positivo", BDConstantes.TIPO_POST_POSITIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "positiva", BDConstantes.TIPO_POST_POSITIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "parabéns", BDConstantes.TIPO_POST_POSITIVO, BDConstantesAtta.STATUS_ATIVO);
		add(baseJDBC, "parabens", BDConstantes.TIPO_POST_POSITIVO, BDConstantesAtta.STATUS_ATIVO);
	}
	
	public boolean add(BaseJDBC baseJDBC, String palavra, int classificacao, int status)
	{
		try
		{
			AnalisadorPalavras registro = new AnalisadorPalavras();
			registro.setPalavra(palavra);
//			registro.setEvento(null);
			registro.setClassificacao(classificacao);
			registro.setStatus(status);
			
			new AnalisadorPalavraDAO().inserir(baseJDBC, registro);
			
			return true;
		}
		catch(Exception e)
		{
			Logs.addError(e);
			return false;
		}
	}
}
