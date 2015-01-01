package periodico;

import java.util.List;

import logs.Logs;
import persistencia.dao.EventoDAO;
import persistencia.om.Evento;
import utils.BDConstantes;
import utils.data.Data;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.periodico.baseTarefa.TarefaPeriodica;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;

public class GestorEvento extends TarefaPeriodica
{
	private int intervalo = 5;
	private BaseJDBC baseJDBC = null;
	
	/*-*-*-* Construtores *-*-*-*/
	/**
	 * Construtor
	 * {@link TarefaPeriodica#TarefaPeriodica(int, int, int, int, int, String, boolean)}
	 * @param pkSerieIndicador indica em qual serie os lancamentos dever ser inseridos (necessario cadastrar previamente o indicador e a serie)
	 */
	public GestorEvento(int intervaloTempo)
	{
		super(TEMPORAL, intervaloTempo, true);
		this.intervalo = intervaloTempo;
		
		baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
	}
	
	/*-*-*-* Metodos Publicos *-*-*-*/
	@Override
	public void executarTarefa()
	{
		System.out.println("/***** GestorEvento " + new Data().toString("dd/MM/yyyy HH:mm"));
		try 
		{
			EventoDAO EventoDAO = new EventoDAO();
			Data atual = new Data().clone();
			Data anterior = atual.clone().subMinuto(intervalo);
			Data proxima = atual.clone().addMinuto(intervalo);
			System.out.println("Anterior - " + ((anterior.getHora() * 60) + anterior.getMinuto()) + " == " + ((atual.getHora() * 60) + atual.getMinuto()) + " == " + "Proxima - " + ((proxima.getHora() * 60) + proxima.getMinuto()));

			List<Evento> eventos = null;
			Evento evento = null;

			//Bloqueia todos os eventos agendados dentre os 5 minutos anteriores
			eventos = EventoDAO.getTodosEventosBloquear(baseJDBC, anterior, ((anterior.getHora() * 60) + anterior.getMinuto()), 0);
			for (int i = 0; i < eventos.size(); i++) 
			{
				evento = eventos.get(i);
				System.out.println("BLOQUEAR - " + evento.getNome());
				evento.setStatus(BDConstantes.STATUS_EVENTO_BLOQUEADO);
				EventoDAO.salvar(baseJDBC, evento);
			}
			
			//Libera todos os eventos agendados dentre os proximos 5 minutos
			eventos = EventoDAO.getTodosEventosLiberar(baseJDBC, proxima, ((proxima.getHora() * 60) + proxima.getMinuto()), 0);
			for (int i = 0; i < eventos.size(); i++) 
			{
				evento = eventos.get(i);
				System.out.println("LIBERAR - " + evento.getNome());				
				evento.setStatus(BDConstantes.STATUS_EVENTO_LIBERADO);
				EventoDAO.salvar(baseJDBC, evento);
			}
			
			baseJDBC.liberarConexao(true);
		} 
		catch(Exception e) 
		{ 
			baseJDBC.liberarConexao(false);
			Logs.addFatal("Falha enviando lembrete de tarefa", e); 
		}
	}
}