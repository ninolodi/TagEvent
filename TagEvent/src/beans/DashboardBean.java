/**
 * 
 */
package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import logs.Logs;
import persistencia.dao.EventoDAO;
import persistencia.om.Evento;
import utils.SConstantes;
import beans.aplicacao.Contexto;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;
import br.com.mresolucoes.atta.utils.LogsUtil;

/**
 * @author Nino
 *
 */

@ManagedBean(name="DashboardBean")
@SessionScoped
public class DashboardBean implements Serializable
{
	private static final long serialVersionUID = 111L;
	private long pkUsuario = 1;
	private List<Evento> eventos = new ArrayList<Evento>();
	private Evento eventoSelecionado = null;
	
	public DashboardBean() 
	{
		try
		{
			BaseJDBC baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
//			BaseJDBC baseJDBC = Pool.getConexao();
			
			eventos = new EventoDAO().getTodosEventosUsuario(baseJDBC, pkUsuario, BDConstantesAtta.STATUS_ATIVO, 0);
			
			eventoSelecionado = (Evento)new Contexto().getSessionObject(SConstantes.EVENTO_SELECIONADO);
			if(eventos.size()>0 && eventoSelecionado==null)
			{
				selecionarEvento(eventos.get(0).getPkEvento());
			}
			
			baseJDBC.liberarConexao(false);
		}
		catch (Exception e)
		{
			Logs.addError("DashboardBean - Construtor", e);
		}
	}
	
	/**
	 * Carrega um registro na global para ser editado
	 * @param pk
	 * @return
	 * @throws Exception
	 */
	public void selecionarEvento(Long pkEvento) throws Exception
	{
		BaseJDBC baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
		eventoSelecionado = new EventoDAO().getEventoUsuario(baseJDBC, pkEvento, null, BDConstantesAtta.STATUS_ATIVO, 0);
		
		System.out.println("selecionarEvento ::: " + LogsUtil.classeToString(eventoSelecionado));
		new Contexto().putSessionObject(SConstantes.EVENTO_SELECIONADO, eventoSelecionado);
		System.out.println("+++ " + LogsUtil.classeToString(eventoSelecionado));
	}
	
	/*-*-* Getters and Setters *-*-*/
	public long getPkUsuario() {
		return pkUsuario;
	}

	public void setPkUsuario(long pkUsuario) {
		this.pkUsuario = pkUsuario;
	}

	public List<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	public Evento getEventoSelecionado() {
		return eventoSelecionado;
	}

	public void setEventoSelecionado(Evento eventoSelecionado) {
		this.eventoSelecionado = eventoSelecionado;
	}

}
