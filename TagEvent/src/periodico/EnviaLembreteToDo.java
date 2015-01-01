package periodico;

import logs.Logs;
import utils.data.Data;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.periodico.baseTarefa.TarefaPeriodica;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;

public class EnviaLembreteToDo extends TarefaPeriodica
{
	private int intervalo = 0;
	
	/*-*-*-* Construtores *-*-*-*/
	/**
	 * Construtor
	 * {@link TarefaPeriodica#TarefaPeriodica(int, int, int, int, int, String, boolean)}
	 * @param pkSerieIndicador indica em qual serie os lancamentos dever ser inseridos (necessario cadastrar previamente o indicador e a serie)
	 */
	public EnviaLembreteToDo(int intervaloTempo)
	{
		super(TEMPORAL, intervaloTempo, true);
		this.intervalo = intervaloTempo;
	}
	
	/*-*-*-* Metodos Publicos *-*-*-*/
	@Override
	public void executarTarefa()
	{
//		BaseJDBC baseJDBC = BancoDeDados.getConexao(null);
		BaseJDBC baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
		
		try 
		{
			Data atual = new Data().clone();
//			Data proxima = atual.clone().addMinuto(intervalo);
			
			System.out.println("Executando " + atual.toString("HH:mm"));
			
			baseJDBC.liberarConexao(true);
		} 
		catch(Exception e) 
		{ 
			baseJDBC.liberarConexao(false);
			Logs.addFatal("Falha enviando lembrete de tarefa", e); 
		}
	}
}