package beans.modulos.instagram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import logs.Logs;
import persistencia.dao.InstagramDAO;
import persistencia.om.Evento;
import persistencia.om.Instagram;
import persistencia.om.InstagramConfiguracao;
import utils.SConstantes;
import beans.aplicacao.Contexto;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;

@ManagedBean(name="ConfiguracaoInstagramBean")
@ViewScoped
public class ConfiguracaoInstagramBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private static final long CHECK_MONITORAMENTO = 1010L;

	private Evento eventoSelecionado = null;
	
	private Map<Long, Boolean> checks = new HashMap<Long, Boolean>();
	private List<Instagram> instagrams = new ArrayList<Instagram>();
	private InstagramConfiguracao configuracao = null;
	private BaseJDBC baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
	
	public ConfiguracaoInstagramBean() 
	{
		try
		{
			checks.put(CHECK_MONITORAMENTO, false);
		}
		catch (Exception e)
		{
			Logs.addError("ConfiguracaoInstagramBean - Construtor", e);
//			redirecionarErro(e);
		}
	}
	
	public void atualizar()
	{
		try
		{
			Evento eventoSelecionado = (Evento)new Contexto().getSessionObject(SConstantes.EVENTO_SELECIONADO);
			if(eventoSelecionado!=null)
			{
				instagrams = new InstagramDAO().getInstagramsEvento(baseJDBC, eventoSelecionado.getPkEvento(), BDConstantesAtta.STATUS_ATIVO, 0);
				configuracao = new InstagramDAO().getInstagramConfiguracaoEvento(baseJDBC, eventoSelecionado.getPkEvento(), BDConstantesAtta.STATUS_ATIVO, 0);
				
				if(configuracao!=null)
				{ checks.put(CHECK_MONITORAMENTO, configuracao.getMonitoramento()); }
			}			
		}
		catch (Exception e)
		{
			Logs.addError("ConfiguracaoInstagramBean - Atualizar", e);
//			redirecionarErro(e);
		}
	
	}
	
	public boolean remover(long pk)
	{
		try
		{
			return true;
		}
		catch(Exception e)
		{
			Logs.addError("ConfiguracaoInstagramBean - remover pk=" + pk, e);
		}
		return false;
	}

	public boolean editar(long pk) 
	{
		try
		{
			return true;
		}
		catch(Exception e)
		{
			Logs.addError("ConfiguracaoInstagramBean - editar pk=" + pk, e);
		}
		return false;
	}

	public boolean salvarConfiguracao()
	{
		System.out.println("salvarConfiguracao");
		try
		{
			System.out.println(configuracao.getJanela());
			configuracao.setMonitoramento(checks.get(CHECK_MONITORAMENTO));
			boolean valor = (new InstagramDAO().salvar(baseJDBC, configuracao) != null ? true : false);
			baseJDBC.commit();
			
			return valor;
			
		}
		catch(Exception e)
		{
			Logs.addError("ConfiguracaoInstagramBean - salvarConfiguracao " + configuracao.getPkInstagramConfiguracao(), e);
		}
		return false;
	}
	
	/*-*-* Getters and Setters *-*-*/

	public Evento getEventoSelecionado() {
		return eventoSelecionado;
	}

	public void setEventoSelecionado(Evento eventoSelecionado) {
		this.eventoSelecionado = eventoSelecionado;
	}

	public List<Instagram> getInstagrams() {
		return instagrams;
	}

	public void setInstagrams(List<Instagram> instagrams) {
		this.instagrams = instagrams;
	}

	public InstagramConfiguracao getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(InstagramConfiguracao configuracao) {
		this.configuracao = configuracao;
	}

	public Map<Long, Boolean> getChecks() {
		return checks;
	}

	public void setChecks(Map<Long, Boolean> checks) {
		this.checks = checks;
	}	

	

}
