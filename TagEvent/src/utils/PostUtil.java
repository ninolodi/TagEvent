package utils;

import java.util.HashMap;
import java.util.List;

import persistencia.dao.AnalisadorPalavraDAO;
import persistencia.om.AnalisadorPalavras;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;

public class PostUtil 
{
	public static HashMap<String, Integer> positivas = new HashMap<>();
	public static HashMap<String, Integer> negativas = new HashMap<>();
	
	public static void inicializar()
	{
		BaseJDBC baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
		List<AnalisadorPalavras> palavras = null;
		try 
		{
			palavras = new AnalisadorPalavraDAO().getPalavrasClassificacao(baseJDBC, BDConstantes.TIPO_POST_POSITIVO, BDConstantesAtta.STATUS_ATIVO, -1);
			for (AnalisadorPalavras analisadorPalavras : palavras) 
			{
				if(positivas.containsKey(analisadorPalavras.getPalavra())==false)
				{ positivas.put(analisadorPalavras.getPalavra(), BDConstantes.TIPO_POST_POSITIVO); }
			}
			
			palavras = new AnalisadorPalavraDAO().getPalavrasClassificacao(baseJDBC, BDConstantes.TIPO_POST_NEGATIVO, BDConstantesAtta.STATUS_ATIVO, -1);
			for (AnalisadorPalavras analisadorPalavras : palavras) 
			{
				if(negativas.containsKey(analisadorPalavras.getPalavra())==false)
				{ negativas.put(analisadorPalavras.getPalavra(), BDConstantes.TIPO_POST_NEGATIVO); }
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static int analisador(String post)
	{
        String[] words = post.toLowerCase().split(" ");
        int i = 0;
        String registro = null;
        Object[] array = null;
        
        //Analisar post
        for (String word : words) 
        {
        	if(negativas.size()>0)
        	{
        		if(negativas.containsKey(word)) 
                { return negativas.get(word); }
                else
                {
              	  array = negativas.keySet().toArray();
              	  for(i=0; i < array.length; i++)
              	  {
              		  registro = (String)array[i];
              		  if(word.indexOf(registro) != -1)
              		  { return BDConstantes.TIPO_POST_NEGATIVO; }
              	  }
                }
        	}
        	
        	if(positivas.size()>0)
        	{
        		if(positivas.containsKey(word)) 
                { return positivas.get(word); }
                else
                {
              	  array = positivas.keySet().toArray();
              	  for(i=0; i < array.length; i++)
              	  {
              		  registro = (String)array[i];
              		  if(word.indexOf(registro) != -1)
              		  { return BDConstantes.TIPO_POST_POSITIVO; }
              	  }
                }
        	}
        }
   
		return BDConstantes.TIPO_POST_INDEFINIDO;
	}
}
