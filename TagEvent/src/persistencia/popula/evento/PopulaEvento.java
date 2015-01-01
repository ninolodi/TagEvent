package persistencia.popula.evento;

import logs.Logs;
import persistencia.dao.EventoDAO;
import persistencia.dao.InstagramDAO;
import persistencia.om.Evento;
import persistencia.om.Instagram;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;

public class PopulaEvento 
{
	public PopulaEvento(BaseJDBC baseJDBC)
	{
		add(baseJDBC, "TagEvent","TagEvent", true, BDConstantesAtta.STATUS_ATIVO);
	}
	
	public boolean add(BaseJDBC baseJDBC, String nome, String hashtag, boolean monitoramento, int status)
	{
		try
		{
			Evento evento = new Evento();
			evento.setNome(nome);
			evento.setStatus(status);
			new EventoDAO().inserir(baseJDBC, evento);
			
			Instagram instagram = new Instagram();
			instagram.setEvento(evento);
			instagram.setStatus(status);
			instagram.setHashtag(hashtag);
			instagram.setMonitoramento(monitoramento);
			new InstagramDAO().inserir(baseJDBC, instagram);
			
			return true;
		}
		catch(Exception e)
		{
			Logs.addError(e);
			return false;
		}
	}
}
