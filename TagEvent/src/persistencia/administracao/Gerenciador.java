package persistencia.administracao;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperPrint;
import propriedades.Propriedades;
import swing.graficos.graficoBarra.GraficoBarra;
import swing.graficos.graficoLinearCategoria.GraficoLinearCategoria;
import swing.mensagens.MensagemBox;
import utils.ArquivoUtil;
import utils.IntegerUtil;
import utils.LongUtil;
import utils.data.Data;
import br.com.mresolucoes.atta.persistencia.administracao.actionScript.ArquivoModulo;
import br.com.mresolucoes.atta.persistencia.administracao.actionScript.GeraActionScript;
import br.com.mresolucoes.atta.persistencia.administracao.actionScript.PathASBase;
import br.com.mresolucoes.atta.persistencia.administracao.comparaBases.ComparaBases;
import br.com.mresolucoes.atta.persistencia.administracao.comparaServices.ComparaServices;
import br.com.mresolucoes.atta.persistencia.administracao.flySpray.GeraBurndownFlySpray;
import br.com.mresolucoes.atta.persistencia.administracao.flySpray.GeraCartoesFlySpray;
import br.com.mresolucoes.atta.persistencia.administracao.flySpray.GeraHorasFlySpray;
import br.com.mresolucoes.atta.persistencia.administracao.flySpray.GeraRelatorioColaboradorFlySpray;
import br.com.mresolucoes.atta.persistencia.administracao.flySpray.GeraRelatorioFlySpray;
import br.com.mresolucoes.atta.persistencia.administracao.flySpray.GeraResumoFlySpray;
import br.com.mresolucoes.atta.persistencia.administracao.flySpray.TarefaFlySpray;
import br.com.mresolucoes.atta.persistencia.administracao.flySpray.componentes.GraficoRelatorio;
import br.com.mresolucoes.atta.persistencia.administracao.flySpray.componentes.HTMLViewer;
import br.com.mresolucoes.atta.persistencia.administracao.flySpray.componentes.JasperViewerMRE;
import br.com.mresolucoes.atta.persistencia.administracao.flySpray.graficos.jas.FlySprayGraficoItem;
import br.com.mresolucoes.atta.persistencia.administracao.geraConstantes.GeraConstantes;
import br.com.mresolucoes.atta.persistencia.administracao.geraOMFlex.GeraOMFlex;
import br.com.mresolucoes.atta.persistencia.administracao.geraOMJava.GeraOM;
import br.com.mresolucoes.atta.persistencia.administracao.geraOMJava.estruturas.Tabela;
import br.com.mresolucoes.atta.persistencia.administracao.geraTabelas.GeraTabelas;
import br.com.mresolucoes.atta.persistencia.administracao.popula.ExecutarPopulas;
import br.com.mresolucoes.atta.persistencia.administracao.popula.utils.BuscaPopula;
import br.com.mresolucoes.atta.persistencia.utils.PgAdminUtil;
import br.com.mresolucoes.atta.relatorios.Relatorio;
import criptografia.CriptografiaRC4;

/**
 * Esta classe possui apenas um metodo "MAIN" que deve ser executado
 * quando se deseja refazer as estruturas do banco de dados, ele apaga (drop)
 * todas as tabelas e as cria novamente
 */
public class Gerenciador extends GerenciadorTela
{
	/*-*-*-* Contantes Privadas *-*-*-*/
	private static final long serialVersionUID = 1L;
	
	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	private List<ArquivoModulo> arquivosModulos;
	private static boolean checkMode = false;
	
	private List<ArquivoModulo> arquivosConstantes;
	private static boolean checkModeConstantes = false;
	
	private List<ArquivoModulo> arquivosPopula;
	private static boolean checkModePopula = false;

	private static boolean checkModeOMs = false;
	
	
	/*-*-*-* Construtores *-*-*-*/
	/**
	 * Construtor
	 * @param projeto nome do projeto onde este gerenciador esta sendo executado
	 * @param permiteGeraTabela permissao para gerar as tabelas do banco de dados, utilziado para impedir os modulos
	 */
	public Gerenciador(String projeto, boolean permiteGeraTabela)
	{
		super(projeto, permiteGeraTabela);
		carregarConfiguracoes();
		carregarComboPerfilModulos();
		cbPerfilModulos.setSelectedItem("");
		buscarOMs();
		escutaRadios(null);
	}
	
	/**
	 * Metodo chamado quando antes da interface ser fechada
	 */
	@Override
	public void sairGerenciador() 
	{
		gravarConfiguracoes();
	}

	/**
	 * Armazena as configuracoes do gerenciador
	 */
	public void gravarConfiguracoes()
	{
		try
		{
			Propriedades prop = new Propriedades(pathEspecifico + "/.mainGerenciador");
			prop.set("config_comparaBaseAtual", ctBaseAtual.getMComponente().getText());
			prop.set("config_comparaBaseNova", ctBaseNova.getMComponente().getText());
			prop.set("config_gerarTabelasDrop", Boolean.toString(ckDropCreate.isSelected()));
			prop.set("config_gerarTabelasBase", ctBaseGeraTabela.getText());
			prop.set("config_compararBasesCTRLV", Boolean.toString(chCTRLVSQL.getMComponente().isSelected()));
			prop.set("config_compararBasesSCRIPT", Boolean.toString(chComparaSQL.getMComponente().isSelected()));
			prop.set("config_compararBasesFKSEPARADA", Boolean.toString(chFKsSeparadaSQL.getMComponente().isSelected()));
			prop.set("config_populaBase", ctBasePopula.getText());
			prop.set("config_flyspray_url", ctFlySprayURL.getMComponente().getText());
			prop.set("config_flyspray_base", ctFlySprayBase.getMComponente().getText());
			prop.set("config_flyspray_porta", ctFlySprayPorta.getMComponente().getText());
			prop.set("config_flyspray_login", ctFlySprayLogin.getMComponente().getText());
			prop.set("config_flyspray_senha", CriptografiaRC4.criptografar(csFlySpraySenha.getMComponente().getText(), "chavecriptflyspray"));
			prop.set("config_flyspray_semborda", Boolean.toString(ckSemBorda.getMComponente().isSelected()));
			prop.set("config_flyspray_2coluna", Boolean.toString(ckIniciar2Coluna.getMComponente().isSelected()));
			prop.set("config_flyspray_tipotarefas", LongUtil.toString(grupoRadioFSTarefas.getSelectedID()));
			prop.set("config_flyspray_opcao_relatorio", (String)cbOpcoesRelatorio.getMComponente().getSelectedItem());
			prop.set("config_flyspray_tarefasID", ctTarefasID.getMComponente().getText());
			prop.set("config_flyspray_tarefasSprint", ctTarefasSprint.getMComponente().getText());
			prop.set("config_flyspray_datalimite", (cdLimiteRealizado.getData()==null ? "" : cdLimiteRealizado.getData().toString("dd/MM/yyyy")));
			prop.set("config_flyspray_tipograficohoras", LongUtil.toString(grupoRadioTipoGraficoHoras.getSelectedID()));
			prop.set("config_flyspray_modograficohoras", LongUtil.toString(grupoRadioModoGraficoHoras.getSelectedID()));
			prop.set("config_flyspray_duplicartarefarelacionar", Boolean.toString(ckFlySprayDuplicarTarefaLink.getMComponente().isSelected()));
			prop.set("config_flyspray_duplicartarefanovosprint", ctFlySprayDuplicarTarefaNovoSprint.getMComponente().getText());
			prop.set("config_flyspray_resumotarefassprint", Boolean.toString(ckFlySprayResumoTarefaSprint.getMComponente().isSelected()));
			prop.set("config_flyspray_resumotarefassuportes", Boolean.toString(ckFlySprayResumoTarefaSuporte.getMComponente().isSelected()));
		}
		catch (Exception e) 
		{ e.printStackTrace(); }
	}
	
	/**
	 * Carrega as configuracoes do gerenciador
	 */
	public void carregarConfiguracoes()
	{
		try
		{
			Propriedades prop = new Propriedades(pathEspecifico + "/.mainGerenciador");
			ctBaseAtual.getMComponente().setText(prop.get("config_comparaBaseAtual"));
			ctBaseNova.getMComponente().setText(prop.get("config_comparaBaseNova"));
			if(prop.get("config_gerarTabelasBase")!=null && prop.get("config_gerarTabelasBase").length()>0) { ctBaseGeraTabela.setText(prop.get("config_gerarTabelasBase")); }
			if(prop.get("config_populaBase")!=null && prop.get("config_populaBase").length()>0) { ctBasePopula.setText(prop.get("config_populaBase")); }
			ckDropCreate.setSelected(prop.getBool("config_gerarTabelasDrop")==null ? false : prop.getBool("config_gerarTabelasDrop"));
			chCTRLVSQL.getMComponente().setSelected(prop.getBool("config_compararBasesCTRLV")==null ? true : prop.getBool("config_compararBasesCTRLV"));
			chComparaSQL.getMComponente().setSelected(prop.getBool("config_compararBasesSCRIPT")==null ? true : prop.getBool("config_compararBasesSCRIPT"));
			chFKsSeparadaSQL.getMComponente().setSelected(prop.getBool("config_compararBasesFKSEPARADA")==null ? false : prop.getBool("config_compararBasesFKSEPARADA"));
			
			ctFlySprayURL.getMComponente().setText(prop.get("config_flyspray_url"));
			ctFlySprayBase.getMComponente().setText(prop.get("config_flyspray_base"));
			ctFlySprayPorta.getMComponente().setText(prop.get("config_flyspray_porta"));
			ctFlySprayLogin.getMComponente().setText(prop.get("config_flyspray_login"));
			csFlySpraySenha.getMComponente().setText(CriptografiaRC4.criptografar(prop.get("config_flyspray_senha"), "chavecriptflyspray"));
			ckSemBorda.getMComponente().setSelected(Boolean.parseBoolean(prop.get("config_flyspray_semborda")));
			ckIniciar2Coluna.getMComponente().setSelected(Boolean.parseBoolean(prop.get("config_flyspray_2coluna")));
			grupoRadioFSTarefas.setSelectedID(LongUtil.toLong(prop.get("config_flyspray_tipotarefas")));
			cbOpcoesRelatorio.getMComponente().setSelectedItem(prop.get("config_flyspray_opcao_relatorio"));
			ctTarefasID.getMComponente().setText(prop.get("config_flyspray_tarefasID"));
			ctTarefasSprint.getMComponente().setText(prop.get("config_flyspray_tarefasSprint"));
			cdLimiteRealizado.setData(prop.get("config_flyspray_datalimite")==null || (prop.get("config_flyspray_datalimite").equals("")) ? null : new Data(prop.get("config_flyspray_datalimite")));
			grupoRadioTipoGraficoHoras.setSelectedID(LongUtil.toLong(prop.get("config_flyspray_tipograficohoras")));
			grupoRadioModoGraficoHoras.setSelectedID(LongUtil.toLong(prop.get("config_flyspray_modograficohoras")));
			ckFlySprayDuplicarTarefaLink.getMComponente().setSelected(Boolean.parseBoolean(prop.get("config_flyspray_duplicartarefarelacionar")));
			ctFlySprayDuplicarTarefaNovoSprint.getMComponente().setText(prop.get("config_flyspray_duplicartarefanovosprint"));
			ckFlySprayResumoTarefaSprint.getMComponente().setSelected(Boolean.parseBoolean(prop.get("config_flyspray_resumotarefassprint")));
			ckFlySprayResumoTarefaSuporte.getMComponente().setSelected(Boolean.parseBoolean(prop.get("config_flyspray_resumotarefassuportes")));
		}
		catch (Exception e) 
		{ e.printStackTrace(); }
	}
	
	
	/*-*-*-* Listeners *-*-*-*/
	/**
	 * Escuta eventos em botoes
	 * @param actionEvent
	 */
	public void escutaBotoes(ActionEvent actionEvent)
	{
		try
		{
			if(actionEvent.getSource()==btTabelas)		 					{ gerarTabelas(); }
			else if(actionEvent.getSource()==btBuscarModulos)				{ buscarModulos(); }
			else if(actionEvent.getSource()==btCheck)						{ checkTodos(); }
			else if(actionEvent.getSource()==btGerar)						{ gerarArquivoActionScript(); }
			else if(actionEvent.getSource()==btComparar)					{ compararBases(); }
			else if(actionEvent.getSource()==btService)						{ compararServices(); }
			else if(actionEvent.getSource()==btCheckPopula)					{ checkTodosPopula(); }
			else if(actionEvent.getSource()==btGerarPopula)					{ popularTabelas(); }
			else if(actionEvent.getSource()==btBuscarPopula)				{ buscarPopulas(); }
			else if(actionEvent.getSource()==btSalvarPerfilModulo)			{ salvarPerfilModulos(); }
			else if(actionEvent.getSource()==btCarregarPerfilModulo)		{ carregarPerfilModulos(); }
			else if(actionEvent.getSource()==btBuscarConstantes)			{ buscarConstantes(); }
			else if(actionEvent.getSource()==btCheckConstantes)				{ checkTodosConstantes(); }
			else if(actionEvent.getSource()==btGerarConstantes)				{ gerarArquivosConstantes(); }
			else if(actionEvent.getSource()==btCheckOMs)					{ checkTodosOMs(); }
			else if(actionEvent.getSource()==btGerarOMsJava)				{ gerarOMJava(); }
			else if(actionEvent.getSource()==btGerarOMsFlex)				{ gerarOMFlex(); }
			else if(actionEvent.getSource()==btFlySprayCartao)				{ gerarCartoesFlySpray(); }
			else if(actionEvent.getSource()==btFlySprayRelatorio)			{ gerarRelatorioFlySpray(); }
			else if(actionEvent.getSource()==btFlySprayResumo)				{ gerarResumoFlySpray(); }
			else if(actionEvent.getSource()==btFlySprayGraficoBD)			{ gerarGraficoBDFlySpray(); }
			else if(actionEvent.getSource()==btFlySprayGraficoBDImprimir)	{ gerarGraficoBDImprimirFlySpray(); }
			else if(actionEvent.getSource()==btFlySprayGraficoHoras)		{ gerarGraficoHorasFlySpray(); }
			else if(actionEvent.getSource()==btFlySprayGraficoHorasImprimir){ gerarGraficoHorasImprimirFlySpray(); }
			else if(actionEvent.getSource()==btFlySprayDuplicarTarefa)		{ duplicarTarefaFlySpray(); }
			else if(actionEvent.getSource()==btFlySprayRelatorioColaborador){ gerarRelatorioColaboradorFlySpray(); }
			
		}
		catch(Exception e) 
		{ e.printStackTrace(); }
	}
	
	/**
	 * Escuta enventos em radios
	 * @param actionEvent
	 */
	public void escutaRadios(ActionEvent actionEvent) 
	{ 
		try
		{
			if(grupoRadioFSTarefas.getSelectedID()!=null && grupoRadioFSTarefas.getSelectedID()==2)
			{
				ctTarefasID.setHabilitado(false);
				ctTarefasSprint.setHabilitado(true);
				btFlySprayGraficoBD.setEnabled(true);
				btFlySprayGraficoBDImprimir.setEnabled(true);
				cdLimiteRealizado.setHabilitado(true);
			}
			else
			{
				ctTarefasID.setHabilitado(true);
				ctTarefasSprint.setHabilitado(false);
				btFlySprayGraficoBD.setEnabled(false);
				btFlySprayGraficoBDImprimir.setEnabled(false);
				cdLimiteRealizado.setHabilitado(false);
			}
		}
		catch(Exception e) 
		{ e.printStackTrace(); }
	}
	
	/**
	 * Gera os OMs do java (A partir do desenho .erm)
	 */
	public void gerarOMJava()
	{
		System.out.println("Gerando Arquivos OMs Java");
		
		List<PathASBase> pathsModulosIn = new ArrayList<PathASBase>();
		for(int i=0; i<defaultTableModelOMs.getRowCount(); i++)
		{
			if((Boolean)defaultTableModelOMs.getValueAt(i, 0))
			{
				pathsModulosIn.add(pathModulos.get(i));
			}
		}
		
		try
		{
			String pathModulo;
			String modulo;
			String moduloSimples = "";
			String pathERM;
			String pathJavaOMs;
			for (int i=0; i<pathsModulosIn.size(); i++) 
			{
				pathModulo = pathsModulosIn.get(i).getPath();
				modulo = pathModulo.substring(pathModulo.lastIndexOf("/")+1, pathModulo.length());
				
				if(modulo.equalsIgnoreCase("atta"))
				{
					modulo = "atta/";
					moduloSimples = "Atta";
				}
				else if(modulo.startsWith("Atta"))
				{
					moduloSimples = modulo.substring(4);
					modulo = "atta/" + moduloSimples.substring(0, 1).toLowerCase() + moduloSimples.substring(1);
					modulo = modulo + "/";
					if(modulo.equalsIgnoreCase("atta/rH/")) { modulo = modulo.toLowerCase(); }
				}
				else
				{
					moduloSimples = modulo;
					modulo = "";
				}
				
				pathERM = pathModulo + "/src/" + modulo + "persistencia/modelagem.erm";
				pathJavaOMs = pathModulo + "/src/" + modulo + "persistencia/om";
				
				System.out.println(moduloSimples + "...");
				List<Tabela> tabelas = GeraOM.obterTabelas(pathERM);
				GeraOM.gerarOMs(tabelas, pathJavaOMs, GeraOM.obterPacote(pathJavaOMs));
			}
		}
		catch(Exception e) 
		{ e.printStackTrace(); }
		
		System.out.println("Arquivos Gerados com sucesso");
	}
	
	/**
	 * Gera os OMs do flex (A partir das classes OM Java)
	 */
	public void gerarOMFlex() throws Exception
	{
		System.out.println("Gerando Arquivos OMs Java");
		
		List<PathASBase> pathsModulosIn = new ArrayList<PathASBase>();
		for(int i=0; i<defaultTableModelOMs.getRowCount(); i++)
		{
			if((Boolean)defaultTableModelOMs.getValueAt(i, 0))
			{
				pathsModulosIn.add(pathModulos.get(i));
			}
		}
		
		try
		{
			String pathModulo;
			String modulo;
			String moduloSimples = "";
			String pathJavaOMs;
			String pathFlexOMs;
			for (int i=0; i<pathsModulosIn.size(); i++) 
			{
				pathModulo = pathsModulosIn.get(i).getPath();
				modulo = pathModulo.substring(pathModulo.lastIndexOf("/")+1, pathModulo.length());
				
				if(modulo.equalsIgnoreCase("atta"))
				{
					modulo = "atta/";
					moduloSimples = "Atta";
				}
				else if(modulo.startsWith("Atta"))
				{
					moduloSimples = modulo.substring(4);
					modulo = "atta/" + moduloSimples.substring(0, 1).toLowerCase() + moduloSimples.substring(1);
					modulo = modulo + "/";
					if(modulo.equalsIgnoreCase("atta/rH/")) { modulo = modulo.toLowerCase(); }
				}
				else
				{
					moduloSimples = modulo;
					modulo = "";
				}
				
				pathJavaOMs = pathModulo + "/src/" + modulo + "persistencia/om";
				pathFlexOMs = pathModulo + "/flex_src/" + modulo + "comunicacao/om";
				
				System.out.println(moduloSimples + "...");
				List<Class<?>> classes = GeraTabelas.obterClasses(new File(pathJavaOMs), GeraOM.obterPacote(pathJavaOMs));
				GeraOMFlex.flexOMs(classes, pathFlexOMs, GeraOM.obterPacote(pathFlexOMs));
			}
		}
		catch(Exception e) 
		{ e.printStackTrace(); }
		
		System.out.println("Arquivos Gerados com sucesso");
	}
	
	/**
	 * Gera as tabelas do banco de dados
	 */
	public void gerarTabelas() throws Exception
	{
		if(usuario==USUARIO_ROOT || baseURL.equalsIgnoreCase("localhost") || baseURL.equalsIgnoreCase("127.0.0.1"))
		{
			String base = ctBaseGeraTabela.getText();
			if(ckDropCreate.isSelected())
			{
				System.out.println("Drop na base: " + base);
				PgAdminUtil.gerenciaTabela(PgAdminUtil.DROPDB, baseURL, base, basePorta, baseLogin, baseSenha);
				System.out.println("Criando base: " + base);
				PgAdminUtil.gerenciaTabela(PgAdminUtil.CREATEDB, baseURL, base, basePorta, baseLogin, baseSenha);
			}
			System.out.println("Gerando Tabelas");
			GeraTabelas.criarTabelas(baseURL, base, basePorta, baseLogin, baseSenha, pathOMs);
			System.out.println("Tabelas Geradas com Sucesso");
		}
		else
		{ MensagemBox.mensagemOk("Voce nao pode criar tabelas em um endereco diferente de localhost", baseURL, MensagemBox.ICONE_ERRO, null); }
	}
	
	/**
	 * Compara bases do banco de dados
	 */
	public void compararBases() throws Exception
	{
		System.out.println("------------------------------ Comparando Bases -------------------------------");
		System.out.println(ComparaBases.comparar(ctBaseNova.getMComponente().getText(), ctBaseAtual.getMComponente().getText(), chComparaSQL.getMComponente().isSelected(), chCTRLVSQL.getMComponente().isSelected(), chFKsSeparadaSQL.getMComponente().isSelected()));
	}
	
	/**
	 * Compara chamadas de services do Flex com Services existentes no Java indicando os erros no console
	 */
	public void compararServices()
	{
		System.out.println("------------------------------ Comparando Services -------------------------------");
		ComparaServices.comparar(pathModulos, Arrays.asList(pathWorkspace));
	}
	
	/**
	 * Popula as tabelas do banco de dados
	 */
	public void popularTabelas()
	{
		if(usuario==USUARIO_ROOT || baseURL.equalsIgnoreCase("localhost") || baseURL.equalsIgnoreCase("127.0.0.1"))
		{
			System.out.println("Populando Tabelas");
			List<ArquivoModulo> populasBanco = new ArrayList<ArquivoModulo>();
			for(int i=0; i<defaultTableModelPopula.getRowCount(); i++)
			{
				if((Boolean)defaultTableModelPopula.getValueAt(i, 0)) { populasBanco.add(arquivosPopula.get(i)); }
			}
			ExecutarPopulas.popular(populasBanco, Gerenciador.baseURL, ctBasePopula.getText(), Gerenciador.basePorta, Gerenciador.baseLogin, Gerenciador.baseSenha);
			System.out.println("Tabelas Populadas com Sucesso");
		}
		else
		{ MensagemBox.mensagemOk("Voce nao pode popular tabelas em um endereco diferente de localhost", baseURL, MensagemBox.ICONE_ERRO, null); }
	}
	
	/**
	 * Salva o perfil dos modulos selecionados no properties
	 */
	public void salvarPerfilModulos()
	{
		try
		{
			String comboText = (String)cbPerfilModulos.getSelectedItem();
			if(comboText!=null && comboText.trim().length()>0)
			{
				List<ArquivoModulo> arquivosModulosIn = new ArrayList<ArquivoModulo>();
				for(int i=0; i<defaultTableModel.getRowCount(); i++)
				{
					if((Boolean)defaultTableModel.getValueAt(i, 0)) { arquivosModulosIn.add(arquivosModulos.get(i)); }
				}

				String conteudo = GeraActionScript.gerarActionScriptModulos(arquivosModulosIn, pathAplication);
				Propriedades prop = new Propriedades(pathEspecifico + "/.mainGerenciador");
				prop.set("perfilModulo_" + comboText, conteudo);
				
				carregarComboPerfilModulos();
			}
			else
			{ MensagemBox.mensagemOk("Defina um nome para o perfil", "Perfil Módulos", MensagemBox.ICONE_INFORMACAO, this); }
		}
		catch (Exception e) 
		{ e.printStackTrace(); }
	}
	
	/**
	 * Carrega no combo os perfis de modulos disponiveis
	 */
	@SuppressWarnings("unchecked")
	public void carregarComboPerfilModulos()
	{
		try
		{
			cbPerfilModulos.removeAllItems();
			Propriedades prop = new Propriedades(pathEspecifico + "/.mainGerenciador");
			String nome;
			for(Iterator<Object> iterator = prop.getProperties().keySet().iterator(); iterator.hasNext();)
			{
				nome = (String)iterator.next();
				if(nome!=null && nome.trim().indexOf("perfilModulo_")==0) { cbPerfilModulos.addItem(nome.substring(13)); }
			}
		}
		catch (Exception e) 
		{ e.printStackTrace(); }
		
	}
	
	/**
	 * Carrega o perfil dos modulos selecionados no properties
	 */
	public void carregarPerfilModulos()
	{
		try
		{
			String comboText = (String)cbPerfilModulos.getSelectedItem();
			if(comboText!=null && comboText.trim().length()>0)
			{
				Propriedades prop = new Propriedades(pathEspecifico + "/.mainGerenciador");
				String conteudo = prop.get("perfilModulo_" + comboText);
				
				while(defaultTableModel.getRowCount()>0) { defaultTableModel.removeRow(0); }
				
				if(conteudo!=null && conteudo.trim().length()>0)
				{
					String modulo;
					boolean contem;
					for(int i=0; i<arquivosModulos.size(); i++)
					{
						modulo = arquivosModulos.get(i).getArquivo().getAbsolutePath();
						modulo = modulo.substring(modulo.indexOf("atta")+5);
						modulo = modulo.substring(0, modulo.indexOf(System.getProperty("file.separator")));
						if(modulo.equals("rogramacao") || modulo.equals("rs")) 	{ modulo = "Específico"; }
						else if(modulo.equals("modulos")) 	{ modulo = "Atta"; }
						
						contem = conteudo.contains(arquivosModulos.get(i).getArquivo().getAbsolutePath().substring(arquivosModulos.get(i).getArquivo().getAbsolutePath().indexOf("flex_src")).replace(System.getProperty("file.separator"), "/"));
						defaultTableModel.addRow(new Object[]{contem, modulo, arquivosModulos.get(i).getArquivo().getName()});
					}
				}
				else
				{ MensagemBox.mensagemOk("Perfil nao encontrado", "Perfil Módulos", MensagemBox.ICONE_INFORMACAO, this); }
			}
			else
			{ MensagemBox.mensagemOk("Digite um nome de perfil", "Perfil Módulos", MensagemBox.ICONE_INFORMACAO, this); }
		}
		catch (Exception e) 
		{ e.printStackTrace(); }
	}
	
	/**
	 * Metodo que busca os modulos dos projetos
	 */
	public void buscarModulos() throws Exception
	{
		System.out.println("Iniciando Busca de arquivos de Interface Flex");
		while(defaultTableModel.getRowCount()>0) { defaultTableModel.removeRow(0); }
		arquivosModulos = GeraActionScript.obtemModulos(pathModulos);
		
		Collections.sort(arquivosModulos, new Comparator<ArquivoModulo>()
		{
			@Override
			public int compare(ArquivoModulo o1, ArquivoModulo o2) 
			{
				String modulo1;
				modulo1 = o1.getArquivo().getAbsolutePath();
				modulo1 = modulo1.substring(modulo1.indexOf("atta")+5);
				modulo1 = modulo1.substring(0, modulo1.indexOf(System.getProperty("file.separator")));
				if(modulo1.equals("rogramacao") || modulo1.equals("rs")) 	{ modulo1 = "Específico"; }
				else if(modulo1.equals("modulos")) 	{ modulo1 = "Atta"; }
				
				String modulo2;
				modulo2 = o2.getArquivo().getAbsolutePath();
				modulo2 = modulo2.substring(modulo2.indexOf("atta")+5);
				modulo2 = modulo2.substring(0, modulo2.indexOf(System.getProperty("file.separator")));
				if(modulo2.equals("rogramacao") || modulo2.equals("rs")) 	{ modulo2 = "Específico"; }
				else if(modulo2.equals("modulos")) 	{ modulo2 = "Atta"; }
				
				int compara = modulo1.compareToIgnoreCase(modulo2);
				if(compara==0)
				{
					compara = o1.getArquivo().getName().compareToIgnoreCase(o2.getArquivo().getName());
				}
				else
				{
					if(modulo1.equalsIgnoreCase("Específico")) 		{ return -1; }
					else if(modulo2.equalsIgnoreCase("Específico")) { return 1; }
					else if(modulo1.equalsIgnoreCase("Atta")) 		{ return -1; }
					else if(modulo2.equalsIgnoreCase("Atta")) 		{ return 1; }
				}
				return compara;
			}
		});
		
		String conteudo = ArquivoUtil.getString(pathEspecifico + "/.actionScriptProperties");
		String modulo;
		boolean contem;
		for(int i=0; i<arquivosModulos.size(); i++)
		{
			modulo = arquivosModulos.get(i).getArquivo().getAbsolutePath();
			modulo = modulo.substring(modulo.indexOf("atta")+5);
			modulo = modulo.substring(0, modulo.indexOf(System.getProperty("file.separator")));
			if(modulo.equals("rogramacao") || modulo.equals("rs")) 	{ modulo = "Específico"; }
			else if(modulo.equals("modulos")) 	{ modulo = "Atta"; }
			
			contem = conteudo.contains(arquivosModulos.get(i).getArquivo().getAbsolutePath().substring(arquivosModulos.get(i).getArquivo().getAbsolutePath().indexOf("flex_src")).replace(System.getProperty("file.separator"), "/"));
			defaultTableModel.addRow(new Object[]{contem, modulo, arquivosModulos.get(i).getArquivo().getName()});
		}
		btCheck.setEnabled(true);
		btGerar.setEnabled(true);
		cbPerfilModulos.setEnabled(true);
		btSalvarPerfilModulo.setEnabled(true);
		btCarregarPerfilModulo.setEnabled(true);
		System.out.println("Fim da Busca");
	}
	
	/**
	 * Metodo que busca os modulos dos projetos
	 */
	public void buscarPopulas() throws Exception
	{
		System.out.println("Iniciando Busca de Populas");
		while(defaultTableModelConstantes.getRowCount()>0) { defaultTableModelConstantes.removeRow(0); }
		arquivosPopula = BuscaPopula.obtemPopulas(pathModulos);
		
		Collections.sort(arquivosPopula, new Comparator<ArquivoModulo>()
		{
			@Override
			public int compare(ArquivoModulo o1, ArquivoModulo o2) 
			{
				String modulo1;
				modulo1 = o1.getArquivo().getAbsolutePath();
				modulo1 = modulo1.substring(modulo1.indexOf("atta")+5);
				modulo1 = modulo1.substring(0, modulo1.indexOf(System.getProperty("file.separator")));
				if(modulo1.equals("rogramacao") || modulo1.equals("rs")) 	{ modulo1 = "Específico"; }
				else if(modulo1.equals("persistencia")) 	{ modulo1 = "Atta"; }
				
				String modulo2;
				modulo2 = o2.getArquivo().getAbsolutePath();
				modulo2 = modulo2.substring(modulo2.indexOf("atta")+5);
				modulo2 = modulo2.substring(0, modulo2.indexOf(System.getProperty("file.separator")));
				if(modulo2.equals("rogramacao") || modulo2.equals("rs")) 	{ modulo2 = "Específico"; }
				else if(modulo2.equals("persistencia")) 	{ modulo2 = "Atta"; }
				
				int compara = modulo1.compareToIgnoreCase(modulo2);
				if(compara==0)
				{
					compara = o1.getArquivo().getName().compareToIgnoreCase(o2.getArquivo().getName());
				}
				else
				{
					if(modulo1.equalsIgnoreCase("Específico")) 		{ return -1; }
					else if(modulo2.equalsIgnoreCase("Específico")) { return 1; }
					else if(modulo1.equalsIgnoreCase("Atta")) 		{ return -1; }
					else if(modulo2.equalsIgnoreCase("Atta")) 		{ return 1; }
				}
				return compara;
			}
		});
		
		String modulo;
		for(int i=0; i<arquivosPopula.size(); i++)
		{
			modulo = arquivosPopula.get(i).getArquivo().getAbsolutePath();
			modulo = modulo.substring(modulo.indexOf("atta")+5);
			modulo = modulo.substring(0, modulo.indexOf(System.getProperty("file.separator")));
			if(modulo.equals("rogramacao") || modulo.equals("rs")) 	{ modulo = "Específico"; }
			else if(modulo.equals("persistencia")) 	{ modulo = "Atta"; }
			
			defaultTableModelPopula.addRow(new Object[]{false, modulo, arquivosPopula.get(i).getArquivo().getName()});
		}
		btCheckPopula.setEnabled(true);
		btGerarPopula.setEnabled(true);
		System.out.println("Fim da Busca");
	}
	
	/**
	 * Metodo que busca os modulos dos projetos
	 */
	public void buscarConstantes() throws Exception
	{
		System.out.println("Iniciando Busca de Constantes");
		while(defaultTableModelConstantes.getRowCount()>0) { defaultTableModelConstantes.removeRow(0); }
		arquivosConstantes = GeraConstantes.obtemConstantes(pathModulos);
		
		Collections.sort(arquivosConstantes, new Comparator<ArquivoModulo>()
		{
			@Override
			public int compare(ArquivoModulo o1, ArquivoModulo o2) 
			{
				String modulo1;
				modulo1 = o1.getArquivo().getAbsolutePath();
				modulo1 = modulo1.substring(modulo1.indexOf("atta")+5);
				modulo1 = modulo1.substring(0, modulo1.indexOf(System.getProperty("file.separator")));
				if(modulo1.equals("rogramacao") || modulo1.equals("rs")) 	{ modulo1 = "Específico"; }
				else if(modulo1.equals("utils")) 	{ modulo1 = "Atta"; }
				
				String modulo2;
				modulo2 = o2.getArquivo().getAbsolutePath();
				modulo2 = modulo2.substring(modulo2.indexOf("atta")+5);
				modulo2 = modulo2.substring(0, modulo2.indexOf(System.getProperty("file.separator")));
				if(modulo2.equals("rogramacao") || modulo2.equals("rs")) 	{ modulo2 = "Específico"; }
				else if(modulo2.equals("utils")) 	{ modulo2 = "Atta"; }
				
				int compara = modulo1.compareToIgnoreCase(modulo2);
				if(compara==0)
				{
					compara = o1.getArquivo().getName().compareToIgnoreCase(o2.getArquivo().getName());
				}
				else
				{
					if(modulo1.equalsIgnoreCase("Específico")) 		{ return -1; }
					else if(modulo2.equalsIgnoreCase("Específico")) { return 1; }
					else if(modulo1.equalsIgnoreCase("Atta")) 		{ return -1; }
					else if(modulo2.equalsIgnoreCase("Atta")) 		{ return 1; }
				}
				return compara;
			}
		});
		
		String modulo;
		for(int i=0; i<arquivosConstantes.size(); i++)
		{
			modulo = arquivosConstantes.get(i).getArquivo().getAbsolutePath();
			modulo = modulo.substring(modulo.indexOf("atta")+5);
			modulo = modulo.substring(0, modulo.indexOf(System.getProperty("file.separator")));
			if(modulo.equals("rogramacao") || modulo.equals("rs")) 	{ modulo = "Específico"; }
			else if(modulo.equals("utils")) 	{ modulo = "Atta"; }
			
			defaultTableModelConstantes.addRow(new Object[]{false, modulo, arquivosConstantes.get(i).getArquivo().getName()});
		}
		btCheckConstantes.setEnabled(true);
		btGerarConstantes.setEnabled(true);
		System.out.println("Fim da Busca");
	}
	
	/**
	 * Metodo que busca os modulos dos projetos
	 */
	public void buscarOMs()
	{
		try
		{
			while(defaultTableModelOMs.getRowCount()>0) { defaultTableModelOMs.removeRow(0); }
			
			String modulo;
			for(int i=0; i<pathModulos.size(); i++)
			{
				modulo = pathModulos.get(i).getPath();
				modulo = modulo.substring(modulo.lastIndexOf("/")+1, modulo.length());
				defaultTableModelOMs.addRow(new Object[]{false, modulo});
			}
			btCheckOMs.setEnabled(true);
			btCheckOMs.setEnabled(true);
		}
		catch (Exception e) 
		{ e.printStackTrace(); }
	}
	
	/**
	 * Metodo que gera o arquivo actionScript de acordo com os modulos selecionados na tabela
	 * @throws Exception
	 */
	public void gerarArquivoActionScript() throws Exception
	{
		List<ArquivoModulo> arquivosModulosIn = new ArrayList<ArquivoModulo>();
		for(int i=0; i<defaultTableModel.getRowCount(); i++)
		{
			if((Boolean)defaultTableModel.getValueAt(i, 0))
			{
				arquivosModulosIn.add(arquivosModulos.get(i));
			}
		}
		
		File fileAction = new File(pathEspecifico + "/.actionScriptProperties");
		String conteudo = GeraActionScript.gerarActionScript(arquivosModulosIn, fileAction, pathAplication);
		FileOutputStream fos = new FileOutputStream(fileAction);
		fos.write(conteudo.getBytes());
		fos.flush();
		fos.close();
		MensagemBox.mensagemOk("Arquivo gerado com sucesso", "actionScriptProperties", MensagemBox.ICONE_INFORMACAO, this);
	}
	
	/**
	 * Metodo que gera os cartoes de projeto do flyspray
	 * @throws Exception
	 */
	public void gerarCartoesFlySpray() throws Exception
	{
		if(grupoRadioFSTarefas.getSelectedID()==1)
		{
			String[] tarefas = ctTarefasID.getMComponente().getText().replace(" ", "").split(",");
			if(tarefas!=null && tarefas.length>0)
			{
				int[] tarefasIDs = new int[tarefas.length];
				for(int i=0, id=0; i<tarefas.length; i++) 
				{
					id = IntegerUtil.toInteger(tarefas[i]);
					if(id>0){ tarefasIDs[i] = id; }
					else 	{ System.out.println("Falha tentando converter tarefa:" + tarefas[i]); }
				}
				GeraCartoesFlySpray.gerarCartoes(ctFlySprayURL.getMComponente().getText(), ctFlySprayBase.getMComponente().getText(), IntegerUtil.toInteger(ctFlySprayPorta.getMComponente().getText()), ctFlySprayLogin.getMComponente().getText(), csFlySpraySenha.getMComponente().getText(), tarefasIDs, (String)cbOpcoesRelatorio.getMComponente().getSelectedItem(), ckSemBorda.getMComponente().isSelected(), ckIniciar2Coluna.getMComponente().isSelected());
			}
		}
		else if(grupoRadioFSTarefas.getSelectedID()==2)
		{
			String[] projetoSprint = ctTarefasSprint.getMComponente().getText().split(",");
			if(projetoSprint!=null && projetoSprint.length==2)
			{
				GeraCartoesFlySpray.gerarCartoesProjetoSprint(ctFlySprayURL.getMComponente().getText(), ctFlySprayBase.getMComponente().getText(), IntegerUtil.toInteger(ctFlySprayPorta.getMComponente().getText()), ctFlySprayLogin.getMComponente().getText(), csFlySpraySenha.getMComponente().getText(), ckSemBorda.getMComponente().isSelected(), (String)cbOpcoesRelatorio.getMComponente().getSelectedItem(), ckIniciar2Coluna.getMComponente().isSelected(), projetoSprint[0], projetoSprint[1]);
			}
		}
	}
	
	/**
	 * Metodo que gera os cartoes de projeto do flyspray
	 * @throws Exception
	 */
	public void gerarRelatorioFlySpray() throws Exception
	{
		String conteudo = null;
		if(grupoRadioFSTarefas.getSelectedID()==1)
		{
			String[] tarefas = ctTarefasID.getMComponente().getText().replace(" ", "").split(",");
			if(tarefas!=null && tarefas.length>0)
			{
				int[] tarefasIDs = new int[tarefas.length];
				for(int i=0, id=0; i<tarefas.length; i++) 
				{
					id = IntegerUtil.toInteger(tarefas[i]);
					if(id>0){ tarefasIDs[i] = id; }
					else 	{ System.out.println("Falha tentando converter tarefa:" + tarefas[i]); }
				}
				conteudo = GeraRelatorioFlySpray.gerarRelatorio(ctFlySprayURL.getMComponente().getText(), ctFlySprayBase.getMComponente().getText(), IntegerUtil.toInteger(ctFlySprayPorta.getMComponente().getText()), ctFlySprayLogin.getMComponente().getText(), csFlySpraySenha.getMComponente().getText(), tarefasIDs, (String)cbOpcoesRelatorio.getMComponente().getSelectedItem(), false);
			}
		}
		else if(grupoRadioFSTarefas.getSelectedID()==2)
		{
			String[] projetoSprint = ctTarefasSprint.getMComponente().getText().split(",");
			if(projetoSprint!=null && projetoSprint.length==2)
			{
				conteudo = GeraRelatorioFlySpray.gerarRelatorioProjetoSprint(ctFlySprayURL.getMComponente().getText(), ctFlySprayBase.getMComponente().getText(), IntegerUtil.toInteger(ctFlySprayPorta.getMComponente().getText()), ctFlySprayLogin.getMComponente().getText(), csFlySpraySenha.getMComponente().getText(), (String)cbOpcoesRelatorio.getMComponente().getSelectedItem(), projetoSprint[0], projetoSprint[1], false);
			}
		}
		
		if(conteudo!=null)
		{
			HTMLViewer.getInstance().setHTML(conteudo, "Atividades");
			HTMLViewer.getInstance().setVisible(true);
		}
	}
	
	/**
	 * Metodo que gera o relatorio por colaborador
	 * @throws Exception
	 */
	public void gerarRelatorioColaboradorFlySpray() throws Exception
	{
		String conteudo = null;
		if(grupoRadioFSTarefas.getSelectedID()==1)
		{
			String[] tarefas = ctTarefasID.getMComponente().getText().replace(" ", "").split(",");
			if(tarefas!=null && tarefas.length>0)
			{
				int[] tarefasIDs = new int[tarefas.length];
				for(int i=0, id=0; i<tarefas.length; i++) 
				{
					id = IntegerUtil.toInteger(tarefas[i]);
					if(id>0){ tarefasIDs[i] = id; }
					else 	{ System.out.println("Falha tentando converter tarefa:" + tarefas[i]); }
				}
				conteudo = GeraRelatorioColaboradorFlySpray.gerarRelatorio(ctFlySprayURL.getMComponente().getText(), ctFlySprayBase.getMComponente().getText(), IntegerUtil.toInteger(ctFlySprayPorta.getMComponente().getText()), ctFlySprayLogin.getMComponente().getText(), csFlySpraySenha.getMComponente().getText(), tarefasIDs, (String)cbOpcoesRelatorio.getMComponente().getSelectedItem(), false);
			}
		}
		else if(grupoRadioFSTarefas.getSelectedID()==2)
		{
			String[] projetoSprint = ctTarefasSprint.getMComponente().getText().split(",");
			if(projetoSprint!=null && projetoSprint.length==2)
			{
				conteudo = GeraRelatorioColaboradorFlySpray.gerarRelatorioProjetoSprint(ctFlySprayURL.getMComponente().getText(), ctFlySprayBase.getMComponente().getText(), IntegerUtil.toInteger(ctFlySprayPorta.getMComponente().getText()), ctFlySprayLogin.getMComponente().getText(), csFlySpraySenha.getMComponente().getText(), (String)cbOpcoesRelatorio.getMComponente().getSelectedItem(), projetoSprint[0], projetoSprint[1], false);
			}
		}
		
		if(conteudo!=null)
		{
			HTMLViewer.getInstance().setHTML(conteudo, "Atividades Colaboradores");
			HTMLViewer.getInstance().setVisible(true);
		}
	}
	
	/**
	 * Metodo que gera o resumo das tarefas do flyspray
	 * @throws Exception
	 */
	public void gerarResumoFlySpray() throws Exception
	{
		String conteudo = null;
		if(grupoRadioFSTarefas.getSelectedID()==1)
		{
			String[] tarefas = ctTarefasID.getMComponente().getText().replace(" ", "").split(",");
			if(tarefas!=null && tarefas.length>0)
			{
				int[] tarefasIDs = new int[tarefas.length];
				for(int i=0, id=0; i<tarefas.length; i++) 
				{
					id = IntegerUtil.toInteger(tarefas[i]);
					if(id>0){ tarefasIDs[i] = id; }
					else 	{ System.out.println("Falha tentando converter tarefa:" + tarefas[i]); }
				}
				conteudo = GeraResumoFlySpray.gerarRelatorio(ctFlySprayURL.getMComponente().getText(), ctFlySprayBase.getMComponente().getText(), IntegerUtil.toInteger(ctFlySprayPorta.getMComponente().getText()), ctFlySprayLogin.getMComponente().getText(), csFlySpraySenha.getMComponente().getText(), tarefasIDs, (String)cbOpcoesRelatorio.getMComponente().getSelectedItem(), false);
			}
		}
		else if(grupoRadioFSTarefas.getSelectedID()==2)
		{
			String[] projetoSprint = ctTarefasSprint.getMComponente().getText().split(",");
			if(projetoSprint!=null && projetoSprint.length==2)
			{
				conteudo = GeraResumoFlySpray.gerarRelatorioProjetoSprint(ctFlySprayURL.getMComponente().getText(), ctFlySprayBase.getMComponente().getText(), IntegerUtil.toInteger(ctFlySprayPorta.getMComponente().getText()), ctFlySprayLogin.getMComponente().getText(), csFlySpraySenha.getMComponente().getText(), (String)cbOpcoesRelatorio.getMComponente().getSelectedItem(), projetoSprint[0], projetoSprint[1], false, ckFlySprayResumoTarefaSprint.getMComponente().isSelected(), ckFlySprayResumoTarefaSuporte.getMComponente().isSelected());
			}
		}
		
		if(conteudo!=null)
		{
			HTMLViewer.getInstance().setHTML(conteudo, "Resumo");
			HTMLViewer.getInstance().setVisible(true);
		}
	}
	
	
	/**
	 * Metodo que gera os cartoes de projeto do flyspray
	 * @throws Exception
	 */
	public void gerarGraficoBDFlySpray() throws Exception
	{
		if(grupoRadioFSTarefas.getSelectedID()==2)
		{
			String[] projetoSprint = ctTarefasSprint.getMComponente().getText().split(",");
			if(projetoSprint!=null && projetoSprint.length==2)
			{
				GraficoLinearCategoria glcategoria = GeraBurndownFlySpray.gerarGrafico(ctFlySprayURL.getMComponente().getText(), ctFlySprayBase.getMComponente().getText(), IntegerUtil.toInteger(ctFlySprayPorta.getMComponente().getText()), ctFlySprayLogin.getMComponente().getText(), csFlySpraySenha.getMComponente().getText(), (String)cbOpcoesRelatorio.getMComponente().getSelectedItem(), projetoSprint[0], projetoSprint[1], cdLimiteRealizado.getData());
				GraficoRelatorio.getInstance().setGrafico(glcategoria, 10, 30);
				GraficoRelatorio.getInstance().setVisible(true);
			}
		}
	}
	
	/**
	 * Metodo que gera os cartoes de projeto do flyspray
	 * @throws Exception
	 */
	public void gerarGraficoBDImprimirFlySpray() throws Exception
	{
		if(grupoRadioFSTarefas.getSelectedID()==2)
		{
			String[] projetoSprint = ctTarefasSprint.getMComponente().getText().split(",");
			if(projetoSprint!=null && projetoSprint.length==2)
			{
				GraficoLinearCategoria glcategoria = GeraBurndownFlySpray.gerarGrafico(ctFlySprayURL.getMComponente().getText(), ctFlySprayBase.getMComponente().getText(), IntegerUtil.toInteger(ctFlySprayPorta.getMComponente().getText()), ctFlySprayLogin.getMComponente().getText(), csFlySpraySenha.getMComponente().getText(), (String)cbOpcoesRelatorio.getMComponente().getSelectedItem(), projetoSprint[0], projetoSprint[1], cdLimiteRealizado.getData());
				
				List<FlySprayGraficoItem> graficoItens = new ArrayList<FlySprayGraficoItem>();
				FlySprayGraficoItem graficoItem = new FlySprayGraficoItem();
				graficoItem.imagem = glcategoria.criarImagem(800, 550);
				graficoItens.add(graficoItem);
				
				JasperPrint impressaoFinal = Relatorio.gerarRelatorio(graficoItens, GeraBurndownFlySpray.class.getResourceAsStream("graficos/jasper/flySprayGrafico.jasper"));
				JasperViewerMRE.viewReport(impressaoFinal, false);
			}
		}
	}
	
	/**
	 * Metodo que gera os cartoes de projeto do flyspray
	 * @throws Exception
	 */
	public void gerarGraficoHorasFlySpray() throws Exception
	{
		if(grupoRadioFSTarefas.getSelectedID()==2)
		{
			String[] projetoSprint = ctTarefasSprint.getMComponente().getText().split(",");
			if(projetoSprint!=null && projetoSprint.length==2)
			{
				String modo = ((grupoRadioModoGraficoHoras.getSelectedID()==2) ? "DIARIA" : "TAREFA");
				String tipo = ((grupoRadioTipoGraficoHoras.getSelectedID()==2) ? "LINHA" : "BARRA");
				Object glcategoria = GeraHorasFlySpray.gerarGrafico(ctFlySprayURL.getMComponente().getText(), ctFlySprayBase.getMComponente().getText(), IntegerUtil.toInteger(ctFlySprayPorta.getMComponente().getText()), ctFlySprayLogin.getMComponente().getText(), csFlySpraySenha.getMComponente().getText(), (String)cbOpcoesRelatorio.getMComponente().getSelectedItem(), projetoSprint[0], projetoSprint[1], null, modo, tipo);
				GraficoRelatorio.getInstance().setGrafico((JPanel)glcategoria, 10, 30);
				GraficoRelatorio.getInstance().setVisible(true);
			}
		}
	}
	
	/**
	 * Metodo que gera os cartoes de projeto do flyspray
	 * @throws Exception
	 */
	public void gerarGraficoHorasImprimirFlySpray() throws Exception
	{
		if(grupoRadioFSTarefas.getSelectedID()==2)
		{
			String[] projetoSprint = ctTarefasSprint.getMComponente().getText().split(",");
			if(projetoSprint!=null && projetoSprint.length==2)
			{
				String modo = ((grupoRadioModoGraficoHoras.getSelectedID()==2) ? "DIARIA" : "TAREFA");
				String tipo = ((grupoRadioTipoGraficoHoras.getSelectedID()==2) ? "LINHA" : "BARRA");
				Object glcategoria = GeraHorasFlySpray.gerarGrafico(ctFlySprayURL.getMComponente().getText(), ctFlySprayBase.getMComponente().getText(), IntegerUtil.toInteger(ctFlySprayPorta.getMComponente().getText()), ctFlySprayLogin.getMComponente().getText(), csFlySpraySenha.getMComponente().getText(), (String)cbOpcoesRelatorio.getMComponente().getSelectedItem(), projetoSprint[0], projetoSprint[1], null, modo, tipo);
				
				List<FlySprayGraficoItem> graficoItens = new ArrayList<FlySprayGraficoItem>();
				FlySprayGraficoItem graficoItem = new FlySprayGraficoItem();
				graficoItem.imagem = ((glcategoria instanceof GraficoLinearCategoria) ? ((GraficoLinearCategoria)glcategoria).criarImagem(800, 550) : ((GraficoBarra)glcategoria).criarImagem(800, 550));
				graficoItens.add(graficoItem);
				
				JasperPrint impressaoFinal = Relatorio.gerarRelatorio(graficoItens, GeraBurndownFlySpray.class.getResourceAsStream("graficos/jasper/flySprayGrafico.jasper"));
				JasperViewerMRE.viewReport(impressaoFinal, false);
			}
		}
	}
	
	/**
	 * Metodo que duplica uma tarefa do flyspray
	 * @throws Exception
	 */
	public void duplicarTarefaFlySpray() throws Exception
	{
		if(ctFlySprayDuplicarTarefaID.getMComponente().getText().trim().length()>0)
		{
			String[] idsString = ctFlySprayDuplicarTarefaID.getMComponente().getText().trim().split(",");
			
			if(idsString!=null && idsString.length>0)
			{
				int[] ids = new int[idsString.length];
				for(int i=0; i<idsString.length; i++)
				{ ids[i] = IntegerUtil.toInteger(idsString[i].trim()); }
				
				if(ids.length>0)
				{
					int resposta = MensagemBox.mensagemSimNao("<HTML>Você tem certeza que deseja DUPLICAR a(s) tarefa(s) de ID: <B>" + Arrays.toString(ids) + "</B><BR>Esta operação é irreversível.</HTML>", "Duplicar Tarefa", MensagemBox.ICONE_AVISO, this);
					if(resposta==MensagemBox.SIM)
					{
						List<Integer> novosID = new TarefaFlySpray().duplicarTarefa(ctFlySprayURL.getMComponente().getText(), ctFlySprayBase.getMComponente().getText(), IntegerUtil.toInteger(ctFlySprayPorta.getMComponente().getText()), ctFlySprayLogin.getMComponente().getText(), csFlySpraySenha.getMComponente().getText(), ids, ckFlySprayDuplicarTarefaLink.getMComponente().isSelected(), ctFlySprayDuplicarTarefaNovoSprint.getMComponente().getText());
						if(novosID!=null && novosID.size()>0)
						{
							MensagemBox.mensagemOk("<HTML>Tarefa(s) duplicada(s) com sucesso.<BR>ID(s) da(s) nova(s) tarefa(s): <B>" + Arrays.toString(novosID.toArray()) +"</B></HTML>", "Duplicar Tarefa", MensagemBox.ICONE_INFORMACAO, this);
						}
						else
						{
							MensagemBox.mensagemOk("<HTML>Ocorreu um problema duplicando a(s) tarefa(s)</HTML>", "Duplicar Tarefa", MensagemBox.ICONE_ERRO, this);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Metodo que gera os arquivos de Constantes de acordo com os arquivos selecionados na tabela, criando seu equivalente no seu path analogo ao flex
	 * @throws Exception
	 */
	public void gerarArquivosConstantes() throws Exception
	{
		List<ArquivoModulo> arquivosModulosIn = new ArrayList<ArquivoModulo>();
		for(int i=0; i<defaultTableModelConstantes.getRowCount(); i++)
		{
			if((Boolean)defaultTableModelConstantes.getValueAt(i, 0))
			{
				arquivosModulosIn.add(arquivosConstantes.get(i));
			}
		}
		
		try
		{
			for (int i=0; i<arquivosModulosIn.size(); i++) 
			{
				String absolutePath = arquivosModulosIn.get(i).getArquivo().getAbsolutePath();
				String conteudo = GeraConstantes.gerarArquivoConstante(absolutePath);
				String arquivoConstanteFlex = absolutePath.replace("src", "flex_src").replace(".java", ".as");
				
				File file = new File(arquivoConstanteFlex);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(conteudo.getBytes());
				fos.flush();
				fos.close();
			}
		}
		catch(Exception e) 
		{ e.printStackTrace(); }
		MensagemBox.mensagemOk("Arquivo(s) gerado(s) com sucesso", "Constantes", MensagemBox.ICONE_INFORMACAO, this);
	}

	/**
	 * Metodo que intercaladamente define a selecao de todos os itens da lista, iniciando por selecionado
	 */
	public void checkTodos()
	{
		checkMode = !checkMode;
		for(int i=0; i<defaultTableModel.getRowCount(); i++) { defaultTableModel.setValueAt(checkMode, i, 0); }
	}
	
	/**
	 * Metodo que intercaladamente define a selecao de todos os itens da lista, iniciando por selecionado
	 */
	public void checkTodosConstantes()
	{
		checkModeConstantes = !checkModeConstantes;
		for(int i=0; i<defaultTableModelConstantes.getRowCount(); i++) { defaultTableModelConstantes.setValueAt(checkModeConstantes, i, 0); }
	}
	
	/**
	 * Metodo que intercaladamente define a selecao de todos os itens da lista, iniciando por selecionado
	 */
	public void checkTodosOMs()
	{
		checkModeOMs = !checkModeOMs;
		for(int i=0; i<defaultTableModelOMs.getRowCount(); i++) { defaultTableModelOMs.setValueAt(checkModeOMs, i, 0); }
	}
	
	/**
	 * Metodo que intercaladamente define a selecao de todos os itens da lista, iniciando por selecionado
	 */
	public void checkTodosPopula()
	{
		checkModePopula = !checkModePopula;
		for(int i=0; i<defaultTableModelPopula.getRowCount(); i++) { defaultTableModelPopula.setValueAt(checkModePopula, i, 0); }
	}
	
	/**
	 * Metodo statico para obetencao da instancia singleton da classe
	 * @return uma instancia singleton da classe
	 */
	public static Gerenciador getInstance(String[] args, String projeto, boolean permiteGeraTabela)
	{
		if(instance==null)				{ instance = new Gerenciador(projeto, permiteGeraTabela); }
		if(args!=null && args.length>0)	{ usuario = IntegerUtil.toInteger(args[0]); }
		return instance;
	}
}
