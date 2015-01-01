package persistencia.administracao;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import swing.componentes.mCaixa.caixaCampoSenha.MCaixaCampoSenha;
import swing.componentes.mCaixa.caixaCampoTexto.MCaixaCampoTexto;
import swing.componentes.mCaixa.caixaCheck.MCaixaCheck;
import swing.componentes.mCaixa.caixaCombo.MCaixaComboBox;
import swing.componentes.mCaixa.caixaData.MCaixaData;
import swing.componentes.mComponente.campoTexto.MCampoTexto;
import swing.componentes.mComponente.campoTexto.mascaras.MCampoTextoMascaraData;
import swing.componentes.mComponentesEspeciais.mGrupoRadioDuplo.MGrupoRadioDuplo;
import swing.eventos.action.EscutaBotoes;
import swing.eventos.action.EscutaRadios;
import br.com.mresolucoes.atta.imagens.Imagens;
import br.com.mresolucoes.atta.persistencia.administracao.actionScript.PathASBase;
import br.com.mresolucoes.atta.persistencia.administracao.comparaBases.ComparaBases;


public abstract class GerenciadorTela extends JFrame
{
	/*-*-*-* Variaveis de Configuracao *-*-*-*/
	public static String pathWorkspace = "D:/Programacao/Flex/Atta/";
	protected static String pathEspecifico = null;
	protected static String nomeEspecifico = null;
	protected static String pathAplication = null;
		
	/*-*-*-* Contantes Publicas de configuracao *-*-*-*/
	public static List<PathASBase> pathModulos = new ArrayList<PathASBase>();
	public static List<String> pathOMs = new ArrayList<String>();
	public static String baseURL = null;
	public static String baseBase = null;
	public static int basePorta = 0;
	public static String baseLogin = null;
	public static String baseSenha = null;


	/*-*-*-* Contantes Privadas *-*-*-*/
	private static final long serialVersionUID = 1L;
	public static final int USUARIO_NORMAL = 0;
	public static final int USUARIO_ROOT = 101;
	public static final List<Color> coresZebra = new ArrayList<Color>(Arrays.asList(new Color(255, 190, 190), new Color(250, 190, 255), new Color(190, 190, 255), new Color(190, 255, 250), new Color(205, 255, 190), new Color(255, 255, 190), new Color(255, 225, 190)));
	
	
	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	protected static Gerenciador instance = null;
	protected static int usuario = USUARIO_NORMAL;

	protected JTabbedPane tabbedPane = new JTabbedPane();
	protected JPanel painelTab1 = new JPanel();
	protected JPanel painelTab2 = new JPanel();
	protected JPanel painelTab3 = new JPanel();
	protected JPanel painelTab4 = new JPanel();
	protected JPanel painelTab5 = new JPanel();
	protected JPanel painelTab6 = new JPanel();
	protected JPanel painelTab7 = new JPanel();
	
	//Tab 1 - OMs
	protected JPanel painelBotoesOMs = new JPanel();
	protected JButton btCheckOMs = new JButton("Check");
	protected JButton btGerarOMsJava = new JButton("Java");
	protected JButton btGerarOMsFlex = new JButton("Flex");
	protected DefaultTableModel defaultTableModelOMs;
	protected JTable tabelaOMs;
	protected JScrollPane scrollPaneOMs;

	
	//Tab 2 - Tabelas
	protected JPanel painelGeraTabelas = new JPanel();
	protected JCheckBox ckDropCreate = new JCheckBox("Drop/Create da Base");
	protected JButton btTabelas = new JButton("Gerar Tabelas");
	protected JPanel painelURLGeraTabela = new JPanel();
	protected MCampoTexto ctBaseGeraTabela = new MCampoTexto(MCampoTexto.TIPO_LIVRE);
	protected JLabel labelGeraTabela = new JLabel();

	protected JPanel painelComparaTabelas = new JPanel();
	protected MCaixaCampoTexto ctBaseAtual = new MCaixaCampoTexto("ATUAL", SwingConstants.TOP, MCaixaCampoTexto.TIPO_LIVRE, MCaixaCampoTexto.FORMATO_TEXTO_LIVRE);
	protected MCaixaCampoTexto ctBaseNova = new MCaixaCampoTexto("NOVA", SwingConstants.TOP, MCaixaCampoTexto.TIPO_LIVRE, MCaixaCampoTexto.FORMATO_TEXTO_LIVRE);
	protected MCaixaCheck chComparaSQL = new MCaixaCheck("Apenas SQL", SwingConstants.RIGHT);
	protected MCaixaCheck chCTRLVSQL = new MCaixaCheck("CTRL+C", SwingConstants.RIGHT);
	protected MCaixaCheck chFKsSeparadaSQL = new MCaixaCheck("FKs Separadas", SwingConstants.RIGHT);
	protected JButton btComparar = new JButton("Comparar");
	
	protected JPanel painelTabelasService = new JPanel();
	protected JButton btService = new JButton("Comparar Services");
	
	
	//Tab 3 - Actionscript
	protected JPanel painelBotoesModulos = new JPanel();
	protected JButton btBuscarModulos = new JButton("Buscar Módulos");
	protected JButton btCheck = new JButton("Check");
	protected JButton btGerar = new JButton("Gerar");
	protected JPanel painelPerfilModulos = new JPanel();
	protected JButton btSalvarPerfilModulo = new JButton("Salvar");
	protected JButton btCarregarPerfilModulo = new JButton("Carregar");
	@SuppressWarnings("rawtypes")
	protected JComboBox cbPerfilModulos = new JComboBox();
	protected DefaultTableModel defaultTableModel;
	protected JTable tabela;
	protected JScrollPane scrollPane;


	//Tab 4 - Popula
	protected JPanel painelBotoesPopula = new JPanel();
	protected JButton btCheckPopula = new JButton("Check");
	protected JButton btBuscarPopula = new JButton("Buscar");
	protected JButton btGerarPopula = new JButton("Popular");
	protected JPanel painelURLPopula = new JPanel();
	protected JLabel labelPopulaURL = new JLabel();
	protected MCampoTexto ctBasePopula = new MCampoTexto(MCampoTexto.TIPO_LIVRE);
	protected DefaultTableModel defaultTableModelPopula;
	protected JTable tabelaPopula;
	protected JScrollPane scrollPanePopula;


	//Tab 5 - Constantes
	protected JPanel painelBotoesConstantes = new JPanel();
	protected JButton btCheckConstantes = new JButton("Check");
	protected JButton btBuscarConstantes = new JButton("Buscar");
	protected JButton btGerarConstantes = new JButton("Gerar Arquivos");
	protected DefaultTableModel defaultTableModelConstantes;
	protected JTable tabelaConstantes;
	protected JScrollPane scrollPaneConstantes;
	
	
	//Tab 6 - FlySpray
	protected JPanel painelFlySprayConexao = new JPanel();
	protected MCaixaCampoTexto ctFlySprayURL = new MCaixaCampoTexto("URL", SwingConstants.LEFT, MCaixaCampoTexto.TIPO_LIVRE, MCaixaCampoTexto.FORMATO_TEXTO_LIVRE);
	protected MCaixaCampoTexto ctFlySprayBase = new MCaixaCampoTexto("Base", SwingConstants.LEFT, MCaixaCampoTexto.TIPO_LIVRE, MCaixaCampoTexto.FORMATO_TEXTO_LIVRE);
	protected MCaixaCampoTexto ctFlySprayPorta = new MCaixaCampoTexto("Porta", SwingConstants.LEFT, MCaixaCampoTexto.TIPO_NUMERO_NATURAL, MCaixaCampoTexto.FORMATO_TEXTO_LIVRE);
	protected MCaixaCampoTexto ctFlySprayLogin = new MCaixaCampoTexto("Login", SwingConstants.LEFT, MCaixaCampoTexto.TIPO_LIVRE, MCaixaCampoTexto.FORMATO_TEXTO_LIVRE);
	protected MCaixaCampoSenha csFlySpraySenha = new MCaixaCampoSenha("Senha", SwingConstants.LEFT);
	
	protected JPanel painelFlySprayTarefas = new JPanel();
	protected MCaixaCampoTexto ctTarefasID = new MCaixaCampoTexto("Tarefas IDs", SwingConstants.LEFT, MCaixaCampoTexto.TIPO_LIVRE, MCaixaCampoTexto.FORMATO_TEXTO_LIVRE);
	protected MCaixaCampoTexto ctTarefasSprint = new MCaixaCampoTexto("Tarefas Sprint", SwingConstants.LEFT, MCaixaCampoTexto.TIPO_LIVRE, MCaixaCampoTexto.FORMATO_TEXTO_LIVRE);
	protected MGrupoRadioDuplo grupoRadioFSTarefas = new MGrupoRadioDuplo("", 1, "", 2, SwingConstants.VERTICAL, 5);
	protected MCaixaComboBox cbOpcoesRelatorio = new MCaixaComboBox("Considerar", SwingConstants.LEFT);
	
	protected JPanel painelFlySprayCartoes = new JPanel();
	protected JButton btFlySprayCartao = new JButton("Gerar Cartões");
	protected MCaixaCheck ckSemBorda = new MCaixaCheck("Sem Borda", SwingConstants.RIGHT, 5);
	protected MCaixaCheck ckIniciar2Coluna = new MCaixaCheck("2ª Coluna", SwingConstants.RIGHT, 5);
	
	protected JPanel painelFlySprayDuplicarTarefa = new JPanel();
	protected JButton btFlySprayDuplicarTarefa = new JButton("Duplicar");
	protected MCaixaCampoTexto ctFlySprayDuplicarTarefaID = new MCaixaCampoTexto("Tarefa ID", SwingConstants.RIGHT, MCaixaCampoTexto.TIPO_NUMERO_NATURAL, MCaixaCampoTexto.FORMATO_TEXTO_LIVRE);
	protected MCaixaCheck ckFlySprayDuplicarTarefaLink = new MCaixaCheck("Relacionar", SwingConstants.RIGHT, 5);
	protected MCaixaCampoTexto ctFlySprayDuplicarTarefaNovoSprint = new MCaixaCampoTexto("Novo Sprint", SwingConstants.TOP, MCaixaCampoTexto.TIPO_LIVRE, MCaixaCampoTexto.FORMATO_TEXTO_LIVRE);
	
	//Tab 6 - FlySpray Resultados
	protected JPanel painelFlySprayRelatorio = new JPanel();
	protected JButton btFlySprayRelatorio = new JButton("Gerar Relatório");
	
	protected JPanel painelFlySprayResumo = new JPanel();
	protected MCaixaCheck ckFlySprayResumoTarefaSprint = new MCaixaCheck("Tarefas Sprint", SwingConstants.RIGHT, 5);
	protected MCaixaCheck ckFlySprayResumoTarefaSuporte = new MCaixaCheck("Tarefas/Suportes", SwingConstants.RIGHT, 5);
	protected JButton btFlySprayResumo = new JButton("Gerar Resumo");
	
	protected JPanel painelFlySprayGraficoBurndown = new JPanel();
	protected JButton btFlySprayGraficoBD = new JButton("Visualizar");
	protected JButton btFlySprayGraficoBDImprimir = new JButton("Imprimir");
	protected MCaixaData cdLimiteRealizado = new MCaixaData("Limite Realizado", SwingConstants.TOP, 0, "dd/mm/aaaa", new MCampoTextoMascaraData(), Imagens.BOTAO_CALENDARIO, Imagens.BOTAO_CALENDARIO, Imagens.BOTAO_CALENDARIO, "Data limite Realizadas", this);

	protected JPanel painelFlySprayGraficoHoras = new JPanel();
	protected JButton btFlySprayGraficoHoras = new JButton("Visualizar");
	protected JButton btFlySprayGraficoHorasImprimir = new JButton("Imprimir");
	protected MGrupoRadioDuplo grupoRadioModoGraficoHoras = new MGrupoRadioDuplo("Tarefa", 1, "Diário", 2, SwingConstants.VERTICAL, 1);
	protected MGrupoRadioDuplo grupoRadioTipoGraficoHoras = new MGrupoRadioDuplo("Barra", 1, "Linha", 2, SwingConstants.VERTICAL, 1);
	
	protected JPanel painelFlySprayRelatorioColaborador = new JPanel();
	protected JButton btFlySprayRelatorioColaborador = new JButton("Gerar Relatório Colaborador");
	
	/*-*-*-* Listeners *-*-*-*/
	protected EscutaBotoes escutaBotoes = new EscutaBotoes(this);
	protected EscutaRadios escutaRadios = new EscutaRadios(this);
	
	
	/*-*-*-* Construtores *-*-*-*/
	/**
	 * Construtor
	 * @param projeto nome do projeto onde este gerenciador esta sendo executado
	 * @param permiteGeraTabela permissao para gerar as tabelas do banco de dados, utilziado para impedir os modulos
	 */
	public GerenciadorTela(String projeto, boolean permiteGeraTabela)
	{
		this.setIconImage(Imagens.LOGO_ATTA_3.getImage());
		this.setVisible(false);
		this.setSize(600, 450);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle("Atta Gerenciador [" + projeto + "]");
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent windowEvent)
			{
				sairGerenciador();
				System.exit(0);
			}
		});
		
		
		//TabbedPane
		painelTab1.setLayout(new BoxLayout(painelTab1, BoxLayout.Y_AXIS));
		painelTab2.setLayout(new BoxLayout(painelTab2, BoxLayout.Y_AXIS));
		painelTab3.setLayout(new BoxLayout(painelTab3, BoxLayout.Y_AXIS));
		painelTab4.setLayout(new BoxLayout(painelTab4, BoxLayout.Y_AXIS));
		painelTab5.setLayout(new BoxLayout(painelTab5, BoxLayout.Y_AXIS));
		painelTab6.setLayout(new BoxLayout(painelTab6, BoxLayout.Y_AXIS));
		painelTab7.setLayout(new BoxLayout(painelTab7, BoxLayout.Y_AXIS));

		tabbedPane.addTab("OMs", null, painelTab1, "Gerar OMs do Java e Flex");
		tabbedPane.addTab("Tabelas", null, painelTab2, "Gerência de bancos de dados");
		tabbedPane.addTab("ActionScript", null, painelTab3, "Define modulos para o flex");
		tabbedPane.addTab("Popula", null, painelTab4, "Popula o banco de dados");
		tabbedPane.addTab("Constantes", null, painelTab5, "Gera arquivos de constantes para o flex");
		tabbedPane.addTab("FlySpray", null, painelTab6, "Informações para Projetos");
		tabbedPane.addTab("FlySpray Resultados", null, painelTab7, "Resultados para Projetos");
		addComponente(tabbedPane);

		 //Tab 1 - OMs *******************************************************************************************
	    painelBotoesOMs.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
	    painelTab1.add(painelBotoesOMs);
	    
  		btCheckOMs.setPreferredSize(new Dimension(70, 20));
  		btCheckOMs.addActionListener(escutaBotoes);
  		painelBotoesOMs.add(btCheckOMs);

  		btGerarOMsJava.setPreferredSize(new Dimension(120, 20));
  		btGerarOMsJava.addActionListener(escutaBotoes);
  		painelBotoesOMs.add(btGerarOMsJava);
  		
  		btGerarOMsFlex.setPreferredSize(new Dimension(120, 20));
  		btGerarOMsFlex.addActionListener(escutaBotoes);
  		painelBotoesOMs.add(btGerarOMsFlex);
  		
  		
  		tabelaOMs = new JTable()
 	    {  
 			private static final long serialVersionUID = 1L;
 			private List<Color> tiposCores = new ArrayList<Color>();
 			private HashMap<String, Color> cores = new HashMap<String, Color>();
 			
 			public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex)
     		{  
 				Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);  
 				String modulo = (String)getValueAt(rowIndex, 1);
     			
 				if(tiposCores==null || tiposCores.size()<=0)
 				{
 					tiposCores.addAll(coresZebra);
 				}
 				
     			if(cores.containsKey(modulo))
     			{
     				c.setBackground(cores.get(modulo));
     			}
     			else
     			{
     				if(tiposCores.size()>0)
     				{
     					cores.put(modulo, tiposCores.remove(0));
     					c.setBackground(cores.get(modulo));
     				}
     				else
     				{ c.setBackground(getBackground()); }
     			}
     			return c;  
     		}
 	    };  
 	    
 	    defaultTableModelOMs = new DefaultTableModel(null, new String[]{"X", "Projeto"})
 	    {
 			private static final long serialVersionUID = 1L;
 			private Class<?>[] types = new Class[]{Boolean.class, String.class};
 			public Class<?> getColumnClass(int columnIndex)
 			{
 			    return types[columnIndex];
 			}
 	    };
 	    tabelaOMs.setModel(defaultTableModelOMs);
 		tabelaOMs.getColumnModel().getColumn(0).setPreferredWidth(10);
 		tabelaOMs.getColumnModel().getColumn(1).setPreferredWidth(220);

 		scrollPaneOMs = new JScrollPane(tabelaOMs);
 		scrollPaneOMs.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
 	    painelTab1.add(scrollPaneOMs);
 	    //Verificando variaveis para pre-configuracao de utilizacao
		ckDropCreate.setEnabled(permiteGeraTabela);
		btTabelas.setEnabled(permiteGeraTabela);

		
		//Tab 2 - Tabelas *******************************************************************************************
		painelComparaTabelas.setLayout(null);
		painelComparaTabelas.setPreferredSize(new Dimension(360, 130));
		painelComparaTabelas.setMaximumSize(new Dimension(360, 130));
		painelComparaTabelas.setAlignmentX(0.5f);
		painelComparaTabelas.setBorder(BorderFactory.createTitledBorder("Tabela"));
		painelTab2.add(painelComparaTabelas);
		
		ctBaseAtual.setLimites(15, 25, 200, 20, 200, 20, 0);
		ctBaseAtual.getMLabel().setText("ATUAL  [" + ComparaBases.baseURLAtual + "]");
		ctBaseAtual.getMLabel().setToolTipText(ComparaBases.baseURLAtual);
		painelComparaTabelas.add(ctBaseAtual);
		
		ctBaseNova.setLimites(15, 70, 200, 20, 200, 20, 0);
		ctBaseNova.getMLabel().setText("NOVA  [" + ComparaBases.baseURLNova + "]");
		ctBaseNova.getMLabel().setToolTipText(ComparaBases.baseURLNova);
		painelComparaTabelas.add(ctBaseNova);

		chCTRLVSQL.setLimites(235, 10, 100, 20, 20, 20, 5);
		chCTRLVSQL.getMComponente().setSelected(true);
		painelComparaTabelas.add(chCTRLVSQL);

		chComparaSQL.setLimites(235, 30, 100, 20, 20, 20, 5);
		chComparaSQL.getMComponente().setSelected(true);
		painelComparaTabelas.add(chComparaSQL);
		
		chFKsSeparadaSQL.setLimites(235, 50, 100, 20, 20, 20, 5);
		chFKsSeparadaSQL.getMComponente().setSelected(true);
		painelComparaTabelas.add(chFKsSeparadaSQL);

		btComparar.setBounds(233, 75, 110, 35);
		btComparar.addActionListener(escutaBotoes);
		painelComparaTabelas.add(btComparar);
		
		painelGeraTabelas.setLayout(new BoxLayout(painelGeraTabelas, BoxLayout.Y_AXIS));
		painelGeraTabelas.setBorder(BorderFactory.createTitledBorder("Tabelas"));
		painelGeraTabelas.setPreferredSize(new Dimension(360, 130));
		painelGeraTabelas.setMaximumSize(new Dimension(360, 130));
		painelGeraTabelas.setAlignmentX(0.5f);
		painelTab2.add(painelGeraTabelas);
		
		painelGeraTabelas.add(Box.createRigidArea(new Dimension(240, 5)));
		
		ckDropCreate.setMaximumSize(new Dimension(320, 20));
		ckDropCreate.setPreferredSize(new Dimension(320, 20));
		ckDropCreate.setAlignmentX(0.5f);
		ckDropCreate.setSelected(false);
		painelGeraTabelas.add(ckDropCreate);
		
		painelGeraTabelas.add(Box.createRigidArea(new Dimension(340, 5)));
		
		painelURLGeraTabela.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		painelGeraTabelas.add(painelURLGeraTabela);

		labelGeraTabela.setText(baseURL!=null ? (baseURL + "     >     ") : "");
		painelURLGeraTabela.add(labelGeraTabela);
		
		ctBaseGeraTabela.setText(baseBase!=null ? baseBase : "");
		ctBaseGeraTabela.setPreferredSize(new Dimension(120, 20));
		painelURLGeraTabela.add(ctBaseGeraTabela);
		
		painelGeraTabelas.add(Box.createRigidArea(new Dimension(340, 5)));
		
		btTabelas.setMaximumSize(new Dimension(320, 40));
		btTabelas.setPreferredSize(new Dimension(320, 40));
		btTabelas.setAlignmentX(0.5f);
		btTabelas.addActionListener(escutaBotoes);
		painelGeraTabelas.add(btTabelas);
		
		painelGeraTabelas.add(Box.createRigidArea(new Dimension(340, 7)));
		
		painelTabelasService.setLayout(new BoxLayout(painelTabelasService, BoxLayout.Y_AXIS));
		painelTabelasService.setBorder(BorderFactory.createTitledBorder("Services"));
		painelTabelasService.setPreferredSize(new Dimension(360, 80));
		painelTabelasService.setMaximumSize(new Dimension(360, 80));
		painelTabelasService.setAlignmentX(0.5f);
		painelTab2.add(painelTabelasService);

		painelTabelasService.add(Box.createRigidArea(new Dimension(340, 15)));
		
		btService.setMaximumSize(new Dimension(320, 40));
		btService.setPreferredSize(new Dimension(320, 40));
		btService.addActionListener(escutaBotoes);
		btService.setAlignmentX(0.5f);
		painelTabelasService.add(btService);
		
		painelTabelasService.add(Box.createRigidArea(new Dimension(340, 10)));
		
		
		//Tab 3 - ActionScript *******************************************************************************************
		painelBotoesModulos.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		painelTab3.add(painelBotoesModulos);
		
		btCheck.setPreferredSize(new Dimension(70, 20));
		btCheck.addActionListener(escutaBotoes);
		btCheck.setEnabled(false);
		painelBotoesModulos.add(btCheck);
		
		btBuscarModulos.setPreferredSize(new Dimension(130, 20));
		btBuscarModulos.addActionListener(escutaBotoes);
		painelBotoesModulos.add(btBuscarModulos);

		btGerar.setPreferredSize(new Dimension(110, 20));
		btGerar.addActionListener(escutaBotoes);
		btGerar.setEnabled(false);
		painelBotoesModulos.add(btGerar);
		
		painelPerfilModulos.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		painelPerfilModulos.setMinimumSize(new Dimension(300, 20));
		painelTab3.add(painelPerfilModulos);
		
		cbPerfilModulos.setPreferredSize(new Dimension(150, 20));
		cbPerfilModulos.setEnabled(false);
		cbPerfilModulos.setEditable(true);
		cbPerfilModulos.setAlignmentY(JComponent.CENTER_ALIGNMENT);
		cbPerfilModulos.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		painelPerfilModulos.add(cbPerfilModulos);
		
		btCarregarPerfilModulo.setPreferredSize(new Dimension(90, 20));
		btCarregarPerfilModulo.addActionListener(escutaBotoes);
		btCarregarPerfilModulo.setEnabled(false);
		painelPerfilModulos.add(btCarregarPerfilModulo);
		
		btSalvarPerfilModulo.setPreferredSize(new Dimension(70, 20));
		btSalvarPerfilModulo.addActionListener(escutaBotoes);
		btSalvarPerfilModulo.setEnabled(false);
		painelPerfilModulos.add(btSalvarPerfilModulo);
		
		painelTab3.add(Box.createVerticalStrut(3));
		
	    tabela = new JTable()
	    {  
			private static final long serialVersionUID = 1L;
			private List<Color> tiposCores = new ArrayList<Color>();
			private HashMap<String, Color> cores = new HashMap<String, Color>();
			
			public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex)
    		{  
				Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);  
				String modulo = (String)getValueAt(rowIndex, 1);
    			
				if(tiposCores==null || tiposCores.size()<=0)
				{
					tiposCores.addAll(coresZebra);
				}
				
    			if(cores.containsKey(modulo))
    			{
    				c.setBackground(cores.get(modulo));
    			}
    			else
    			{
    				if(tiposCores.size()>0)
    				{
    					cores.put(modulo, tiposCores.remove(0));
    					c.setBackground(cores.get(modulo));
    				}
    				else
    				{ c.setBackground(getBackground()); }
    			}
    			return c;  
    		}
	    };  
	    
	    defaultTableModel = new DefaultTableModel(null, new String[]{"X", "Módulo", "Nome"})
	    {
			private static final long serialVersionUID = 1L;
			private Class<?>[] types = new Class[]{Boolean.class, String.class, String.class};
			public Class<?> getColumnClass(int columnIndex)
			{
			    return types[columnIndex];
			}
	    };
		tabela.setModel(defaultTableModel);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(10);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(50);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(200);

		scrollPane = new JScrollPane(tabela);
		scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	    painelTab3.add(scrollPane);
	
	    
	    //Tab 4 - Popula *******************************************************************************************
	    painelBotoesPopula.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
	    painelTab4.add(painelBotoesPopula);
	    
  		btCheckPopula.setPreferredSize(new Dimension(70, 20));
  		btCheckPopula.setEnabled(false);
  		btCheckPopula.addActionListener(escutaBotoes);
  		painelBotoesPopula.add(btCheckPopula);
  		
  		btBuscarPopula.setPreferredSize(new Dimension(80, 20));
  		btBuscarPopula.addActionListener(escutaBotoes);
  		painelBotoesPopula.add(btBuscarPopula);

  		btGerarPopula.setPreferredSize(new Dimension(120, 20));
  		btGerarPopula.setEnabled(false);
  		btGerarPopula.addActionListener(escutaBotoes);
  		painelBotoesPopula.add(btGerarPopula);
  		
  		painelURLPopula.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
  		painelTab4.add(painelURLPopula);
  		
  		labelPopulaURL.setText("Popular:  " + baseURL + "  >  ");
  		painelURLPopula.add(labelPopulaURL);
  		
  		ctBasePopula.setPreferredSize(new Dimension(120, 20));
  		ctBasePopula.setText(baseBase);
  		painelURLPopula.add(ctBasePopula);
  		
  		tabelaPopula = new JTable()
 	    {  
 			private static final long serialVersionUID = 1L;
 			private List<Color> tiposCores = new ArrayList<Color>();
 			private HashMap<String, Color> cores = new HashMap<String, Color>();
 			
 			public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex)
     		{  
 				Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);  
 				String modulo = (String)getValueAt(rowIndex, 1);
     			
 				if(tiposCores==null || tiposCores.size()<=0)
 				{
 					tiposCores.addAll(coresZebra);
 				}
 				
     			if(cores.containsKey(modulo))
     			{
     				c.setBackground(cores.get(modulo));
     			}
     			else
     			{
     				if(tiposCores.size()>0)
     				{
     					cores.put(modulo, tiposCores.remove(0));
     					c.setBackground(cores.get(modulo));
     				}
     				else
     				{ c.setBackground(getBackground()); }
     			}
     			return c;  
     		}
 	    };  
 	    
 	    defaultTableModelPopula = new DefaultTableModel(null, new String[]{"X", "Módulo", "Nome"})
 	    {
 			private static final long serialVersionUID = 1L;
 			private Class<?>[] types = new Class[]{Boolean.class, String.class, String.class};
 			public Class<?> getColumnClass(int columnIndex)
 			{
 			    return types[columnIndex];
 			}
 	    };
 	    tabelaPopula.setModel(defaultTableModelPopula);
 	    tabelaPopula.getColumnModel().getColumn(0).setPreferredWidth(10);
 	    tabelaPopula.getColumnModel().getColumn(1).setPreferredWidth(50);
 	    tabelaPopula.getColumnModel().getColumn(2).setPreferredWidth(200);

 		scrollPanePopula = new JScrollPane(tabelaPopula);
 		scrollPanePopula.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
 		painelTab4.add(scrollPanePopula);

	  	
	  	//Tab 5 - Constantes *******************************************************************************************
	    painelBotoesConstantes.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
	    painelTab5.add(painelBotoesConstantes);
	    
  		btCheckConstantes.setPreferredSize(new Dimension(70, 20));
  		btCheckConstantes.setEnabled(false);
  		btCheckConstantes.addActionListener(escutaBotoes);
  		painelBotoesConstantes.add(btCheckConstantes);
  		
  		btBuscarConstantes.setPreferredSize(new Dimension(80, 20));
  		btBuscarConstantes.addActionListener(escutaBotoes);
  		painelBotoesConstantes.add(btBuscarConstantes);

  		btGerarConstantes.setPreferredSize(new Dimension(120, 20));
  		btGerarConstantes.setEnabled(false);
  		btGerarConstantes.addActionListener(escutaBotoes);
  		painelBotoesConstantes.add(btGerarConstantes);
  		
  		tabelaConstantes = new JTable()
 	    {  
 			private static final long serialVersionUID = 1L;
 			private List<Color> tiposCores = new ArrayList<Color>();
 			private HashMap<String, Color> cores = new HashMap<String, Color>();
 			
 			public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex)
     		{  
 				Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);  
 				String modulo = (String)getValueAt(rowIndex, 1);
     			
 				if(tiposCores==null || tiposCores.size()<=0)
 				{
 					tiposCores.addAll(coresZebra);
 				}
 				
     			if(cores.containsKey(modulo))
     			{
     				c.setBackground(cores.get(modulo));
     			}
     			else
     			{
     				if(tiposCores.size()>0)
     				{
     					cores.put(modulo, tiposCores.remove(0));
     					c.setBackground(cores.get(modulo));
     				}
     				else
     				{ c.setBackground(getBackground()); }
     			}
     			return c;  
     		}
 	    };  
 	    
 	    defaultTableModelConstantes = new DefaultTableModel(null, new String[]{"X", "Módulo", "Nome"})
 	    {
 			private static final long serialVersionUID = 1L;
 			private Class<?>[] types = new Class[]{Boolean.class, String.class, String.class};
 			public Class<?> getColumnClass(int columnIndex)
 			{
 			    return types[columnIndex];
 			}
 	    };
 	    tabelaConstantes.setModel(defaultTableModelConstantes);
 		tabelaConstantes.getColumnModel().getColumn(0).setPreferredWidth(10);
 		tabelaConstantes.getColumnModel().getColumn(1).setPreferredWidth(50);
 		tabelaConstantes.getColumnModel().getColumn(2).setPreferredWidth(200);

 		scrollPaneConstantes = new JScrollPane(tabelaConstantes);
 		scrollPaneConstantes.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
 	    painelTab5.add(scrollPaneConstantes);
	  	
		
		//Tab 6 - FlySpray *******************************************************************************************
 	    painelFlySprayConexao.setLayout(null);
 	    painelFlySprayConexao.setPreferredSize(new Dimension(360, 85));
 	    painelFlySprayConexao.setMaximumSize(new Dimension(360, 85));
 	    painelFlySprayConexao.setAlignmentX(0.5f);
 	    painelFlySprayConexao.setBorder(BorderFactory.createTitledBorder("Conexão FlySpray"));
	    painelTab6.add(painelFlySprayConexao);
	    
	    ctFlySprayURL.setLimites(6, 25, 30, 20, 100, 20, 5);
	    painelFlySprayConexao.add(ctFlySprayURL);
	    
	    ctFlySprayBase.setLimites(143, 25, 40, 20, 80, 20, 5);
	    painelFlySprayConexao.add(ctFlySprayBase);
	    
	    ctFlySprayPorta.setLimites(256, 25, 50, 20, 40, 20, 5);
	    painelFlySprayConexao.add(ctFlySprayPorta);
	    
	    ctFlySprayLogin.setLimites(-4, 50, 40, 20, 100, 20, 5);
	    painelFlySprayConexao.add(ctFlySprayLogin);
	    
	    csFlySpraySenha.setLimites(133, 50, 50, 20, 80, 20, 5);
	    painelFlySprayConexao.add(csFlySpraySenha);
 	    
	    
 	    painelFlySprayTarefas.setLayout(null); //new BoxLayout(painelFlySprayTarefas, BoxLayout.Y_AXIS)
 	    painelFlySprayTarefas.setPreferredSize(new Dimension(360, 100));
 	   	painelFlySprayTarefas.setMaximumSize(new Dimension(360, 100));
	    painelFlySprayTarefas.setAlignmentX(0.5f);
	    painelFlySprayTarefas.setBorder(BorderFactory.createTitledBorder("Tarefas"));
	    painelTab6.add(painelFlySprayTarefas);

	    ctTarefasID.setLimites(-10, 18, 100, 20, 240, 20, 5);
	    ctTarefasID.getMLabel().setToolTipText("Indique as tarefas separando-as por ',' (Virgula)");
	    painelFlySprayTarefas.add(ctTarefasID);

	    ctTarefasSprint.setLimites(-10, 43, 100, 20, 240, 20, 5);
	    ctTarefasSprint.getMLabel().setToolTipText("Indique o Projeto e o Sprint (ex: Almoxarifado,Sprint 013)");
		painelFlySprayTarefas.add(ctTarefasSprint);
	    
		grupoRadioFSTarefas.setLocation(336, 17);
		grupoRadioFSTarefas.setSizeTodosMLabel(20, 19);
		grupoRadioFSTarefas.addEventoListener(escutaRadios);
		painelFlySprayTarefas.add(grupoRadioFSTarefas);
		
		cbOpcoesRelatorio.setLimites(10, 68, 80, 20, 238, 20, 5);
		cbOpcoesRelatorio.getMComponente().addItem("Todas", 1);
		cbOpcoesRelatorio.getMComponente().addItem("Fechadas", 2);
		cbOpcoesRelatorio.getMComponente().addItem("Abertas", 3);
		painelFlySprayTarefas.add(cbOpcoesRelatorio);
		
		
		
		
		painelFlySprayDuplicarTarefa.setLayout(new BoxLayout(painelFlySprayDuplicarTarefa, BoxLayout.X_AXIS));
		painelFlySprayDuplicarTarefa.setBorder(BorderFactory.createTitledBorder("Duplicar Tarefa"));
		painelFlySprayDuplicarTarefa.setPreferredSize(new Dimension(360, 65));
		painelFlySprayDuplicarTarefa.setMaximumSize(new Dimension(360, 65));
		painelFlySprayDuplicarTarefa.setAlignmentX(0.5f);
		painelTab6.add(painelFlySprayDuplicarTarefa);
		
		painelFlySprayDuplicarTarefa.add(Box.createRigidArea(new Dimension(10, 80)));
		
		btFlySprayDuplicarTarefa.setPreferredSize(new Dimension(110, 30));
		btFlySprayDuplicarTarefa.setMaximumSize(new Dimension(110, 30));
		btFlySprayDuplicarTarefa.setMinimumSize(new Dimension(110, 30));
		btFlySprayDuplicarTarefa.addActionListener(escutaBotoes);
		painelFlySprayDuplicarTarefa.add(btFlySprayDuplicarTarefa);
			
		painelFlySprayDuplicarTarefa.add(Box.createRigidArea(new Dimension(15, 80)));
		
		ctFlySprayDuplicarTarefaNovoSprint.setLimites(0, 0, 100, 18, 100, 20, 0);
		ctFlySprayDuplicarTarefaNovoSprint.getMLabel().setToolTipText("Se definido duplica como [To Do, Tarefa Sprint, 0%, Sprint Definido] senão [Nova, Tarefa, 0%, Sem Sprint]");
		ctFlySprayDuplicarTarefaNovoSprint.getMComponente().setAutoSelecionarTexto(true);
		painelFlySprayDuplicarTarefa.add(ctFlySprayDuplicarTarefaNovoSprint);
		
		painelFlySprayDuplicarTarefa.add(Box.createRigidArea(new Dimension(10, 80)));
	
		JPanel painelFlySprayDuplicarTarefaInterno = new JPanel();
		painelFlySprayDuplicarTarefaInterno.setLayout(new BoxLayout(painelFlySprayDuplicarTarefaInterno, BoxLayout.Y_AXIS));
		
		ctFlySprayDuplicarTarefaID.setLimites(0, 0, 60, 20, 40, 20, 3);
		ctFlySprayDuplicarTarefaID.getMComponente().setAutoSelecionarTexto(true);
		ctFlySprayDuplicarTarefaID.getMComponente().setHorizontalAlignment(SwingConstants.CENTER);
		painelFlySprayDuplicarTarefaInterno.add(ctFlySprayDuplicarTarefaID);

		ckFlySprayDuplicarTarefaLink.getMLabel().setToolTipText("Faz com que a tarefa nova seja dependente da tarefa antiga");
		painelFlySprayDuplicarTarefaInterno.add(ckFlySprayDuplicarTarefaLink);
		painelFlySprayDuplicarTarefa.add(painelFlySprayDuplicarTarefaInterno);
		    
		
		
		
		
		
		painelFlySprayCartoes.setLayout(new BoxLayout(painelFlySprayCartoes, BoxLayout.X_AXIS));
		painelFlySprayCartoes.setBorder(BorderFactory.createTitledBorder("Cartões"));
		painelFlySprayCartoes.setPreferredSize(new Dimension(360, 65));
		painelFlySprayCartoes.setMaximumSize(new Dimension(360, 65));
		painelFlySprayCartoes.setAlignmentX(0.5f);
		painelTab6.add(painelFlySprayCartoes);
		
		painelFlySprayCartoes.add(Box.createRigidArea(new Dimension(20, 80)));
		
		btFlySprayCartao.setPreferredSize(new Dimension(200, 30));
		btFlySprayCartao.setMaximumSize(new Dimension(200, 30));
		btFlySprayCartao.setMinimumSize(new Dimension(200, 30));
		btFlySprayCartao.addActionListener(escutaBotoes);
		painelFlySprayCartoes.add(btFlySprayCartao);
			
		painelFlySprayCartoes.add(Box.createRigidArea(new Dimension(20, 80)));
		
		JPanel painelFlySprayRelatorioInterno = new JPanel();
		painelFlySprayRelatorioInterno.setLayout(new BoxLayout(painelFlySprayRelatorioInterno, BoxLayout.Y_AXIS));
		painelFlySprayRelatorioInterno.add(ckSemBorda);
	    ckIniciar2Coluna.getMLabel().setToolTipText("Iniciar impressão pela segunda coluna");
	    painelFlySprayRelatorioInterno.add(ckIniciar2Coluna);
	    painelFlySprayCartoes.add(painelFlySprayRelatorioInterno);
	    
	   		   
	    
	    painelFlySprayRelatorio.setLayout(new BoxLayout(painelFlySprayRelatorio, BoxLayout.X_AXIS));
	    painelFlySprayRelatorio.setBorder(BorderFactory.createTitledBorder("Relatório"));
	    painelFlySprayRelatorio.setPreferredSize(new Dimension(360, 65));
	    painelFlySprayRelatorio.setMaximumSize(new Dimension(360, 65));
	    painelFlySprayRelatorio.setAlignmentX(0.5f);
	    painelTab7.add(painelFlySprayRelatorio);
		
		painelFlySprayRelatorio.add(Box.createRigidArea(new Dimension(20, 20)));

		btFlySprayRelatorio.setPreferredSize(new Dimension(300, 30));
		btFlySprayRelatorio.setMaximumSize(new Dimension(310, 30));
		btFlySprayRelatorio.setMinimumSize(new Dimension(310, 30));
		btFlySprayRelatorio.addActionListener(escutaBotoes);
		painelFlySprayRelatorio.add(btFlySprayRelatorio);
		
		painelFlySprayRelatorio.add(Box.createRigidArea(new Dimension(20, 20)));
		
		painelFlySprayResumo.setLayout(new BoxLayout(painelFlySprayResumo, BoxLayout.X_AXIS));
		painelFlySprayResumo.setBorder(BorderFactory.createTitledBorder("Resumo"));
		painelFlySprayResumo.setPreferredSize(new Dimension(360, 65));
		painelFlySprayResumo.setMaximumSize(new Dimension(360, 65));
		painelFlySprayResumo.setAlignmentX(0.5f);
	    painelTab7.add(painelFlySprayResumo);
		
	    painelFlySprayResumo.add(Box.createRigidArea(new Dimension(20, 20)));

		btFlySprayResumo.setPreferredSize(new Dimension(180, 30));
		btFlySprayResumo.setMaximumSize(new Dimension(180, 30));
		btFlySprayResumo.setMinimumSize(new Dimension(180, 30));
		btFlySprayResumo.addActionListener(escutaBotoes);
		painelFlySprayResumo.add(btFlySprayResumo);
		
		painelFlySprayResumo.add(Box.createRigidArea(new Dimension(15, 20)));
		
		JPanel painelFlySprayResumoOpcoes = new JPanel();
	    painelFlySprayResumoOpcoes.setLayout(new BoxLayout(painelFlySprayResumoOpcoes, BoxLayout.Y_AXIS));
		
	    ckFlySprayResumoTarefaSprint.getMLabel().setToolTipText("Opção válida apenas para buscas realizadas em Sprints");
	    painelFlySprayResumoOpcoes.add(ckFlySprayResumoTarefaSprint);
	    
	    ckFlySprayResumoTarefaSuporte.getMLabel().setToolTipText("Opção válida apenas para buscas realizadas em Sprints");
	    painelFlySprayResumoOpcoes.add(ckFlySprayResumoTarefaSuporte);
		painelFlySprayResumo.add(painelFlySprayResumoOpcoes);
		
		painelFlySprayGraficoBurndown.setLayout(new BoxLayout(painelFlySprayGraficoBurndown, BoxLayout.X_AXIS));
		painelFlySprayGraficoBurndown.setBorder(BorderFactory.createTitledBorder("Gráfico Burndown"));
		painelFlySprayGraficoBurndown.setPreferredSize(new Dimension(360, 70));
		painelFlySprayGraficoBurndown.setMaximumSize(new Dimension(360, 70));
		painelFlySprayGraficoBurndown.setAlignmentX(0.5f);
		painelTab7.add(painelFlySprayGraficoBurndown);
		
		painelFlySprayGraficoBurndown.add(Box.createRigidArea(new Dimension(15, 20)));

		btFlySprayGraficoBD.setPreferredSize(new Dimension(90, 30));
		btFlySprayGraficoBD.setMaximumSize(new Dimension(90, 30));
		btFlySprayGraficoBD.setMinimumSize(new Dimension(90, 30));
		btFlySprayGraficoBD.addActionListener(escutaBotoes);
		painelFlySprayGraficoBurndown.add(btFlySprayGraficoBD);
		
		painelFlySprayGraficoBurndown.add(Box.createRigidArea(new Dimension(15, 20)));
		
		btFlySprayGraficoBDImprimir.setPreferredSize(new Dimension(90, 30));
		btFlySprayGraficoBDImprimir.setMaximumSize(new Dimension(90, 30));
		btFlySprayGraficoBDImprimir.setMinimumSize(new Dimension(90, 30));
		btFlySprayGraficoBDImprimir.addActionListener(escutaBotoes);
		painelFlySprayGraficoBurndown.add(btFlySprayGraficoBDImprimir);
		
		painelFlySprayGraficoBurndown.add(Box.createRigidArea(new Dimension(15, 20)));
		painelFlySprayGraficoBurndown.add(cdLimiteRealizado);
		
		
		
		painelFlySprayGraficoHoras.setLayout(new BoxLayout(painelFlySprayGraficoHoras, BoxLayout.X_AXIS));
		painelFlySprayGraficoHoras.setBorder(BorderFactory.createTitledBorder("Gráfico Horas Estimadas x Horas Realizadas"));
		painelFlySprayGraficoHoras.setPreferredSize(new Dimension(360, 70));
		painelFlySprayGraficoHoras.setMaximumSize(new Dimension(360, 70));
		painelFlySprayGraficoHoras.setAlignmentX(0.5f);
		painelTab7.add(painelFlySprayGraficoHoras);
		
		painelFlySprayGraficoHoras.add(Box.createRigidArea(new Dimension(15, 20)));

		btFlySprayGraficoHoras.setPreferredSize(new Dimension(90, 30));
		btFlySprayGraficoHoras.setMaximumSize(new Dimension(90, 30));
		btFlySprayGraficoHoras.setMinimumSize(new Dimension(90, 30));
		btFlySprayGraficoHoras.addActionListener(escutaBotoes);
		painelFlySprayGraficoHoras.add(btFlySprayGraficoHoras);
		
		painelFlySprayGraficoHoras.add(Box.createRigidArea(new Dimension(15, 20)));
		
		btFlySprayGraficoHorasImprimir.setPreferredSize(new Dimension(90, 30));
		btFlySprayGraficoHorasImprimir.setMaximumSize(new Dimension(90, 30));
		btFlySprayGraficoHorasImprimir.setMinimumSize(new Dimension(90, 30));
		btFlySprayGraficoHorasImprimir.addActionListener(escutaBotoes);
		painelFlySprayGraficoHoras.add(btFlySprayGraficoHorasImprimir);
		
		painelFlySprayGraficoHoras.add(Box.createRigidArea(new Dimension(10, 20)));
		
		grupoRadioModoGraficoHoras.setPreferredSize(new Dimension(65, 45));
		grupoRadioModoGraficoHoras.setMaximumSize(new Dimension(65, 45));
		grupoRadioModoGraficoHoras.setMinimumSize(new Dimension(65, 45));
		grupoRadioModoGraficoHoras.setSizeTodosMLabel(40, 20);
		painelFlySprayGraficoHoras.add(grupoRadioModoGraficoHoras);
				
		grupoRadioTipoGraficoHoras.setPreferredSize(new Dimension(60, 45));
		grupoRadioTipoGraficoHoras.setMaximumSize(new Dimension(60, 45));
		grupoRadioTipoGraficoHoras.setMinimumSize(new Dimension(60, 45));
		grupoRadioTipoGraficoHoras.setSizeTodosMLabel(40, 20);
		painelFlySprayGraficoHoras.add(grupoRadioTipoGraficoHoras);
		
		painelFlySprayRelatorioColaborador.setLayout(new BoxLayout(painelFlySprayRelatorioColaborador, BoxLayout.X_AXIS));
		painelFlySprayRelatorioColaborador.setBorder(BorderFactory.createTitledBorder("Relatório Colaborador"));
		painelFlySprayRelatorioColaborador.setPreferredSize(new Dimension(360, 65));
		painelFlySprayRelatorioColaborador.setMaximumSize(new Dimension(360, 65));
		painelFlySprayRelatorioColaborador.setAlignmentX(0.5f);
	    painelTab7.add(painelFlySprayRelatorioColaborador);
		
	    painelFlySprayRelatorioColaborador.add(Box.createRigidArea(new Dimension(20, 20)));

		btFlySprayRelatorioColaborador.setPreferredSize(new Dimension(300, 30));
		btFlySprayRelatorioColaborador.setMaximumSize(new Dimension(310, 30));
		btFlySprayRelatorioColaborador.setMinimumSize(new Dimension(310, 30));
		btFlySprayRelatorioColaborador.addActionListener(escutaBotoes);
		painelFlySprayRelatorioColaborador.add(btFlySprayRelatorioColaborador);
		
		painelFlySprayRelatorioColaborador.add(Box.createRigidArea(new Dimension(20, 20)));
			
		
  		//Configuracoes
		nomeEspecifico = projeto;
		pathEspecifico = pathWorkspace + nomeEspecifico;
		pathAplication = "flex_src/aplicacao/Main" + nomeEspecifico + ".mxml";
	}
	
	/*-*-*-* Metodos Publicos *-*-*-*/
	/**
	 * Método utilizado para abrir (exibir) esta janela
	 */
	public void abrir()
	{
		this.setVisible(true);
	}
	
	/**
	 * Metodo utilizado para fechar esta janela
	 * @return true caso ela tenha sido fechada com sucesso e false caso não tenha sido fechada
	 */
	public boolean fechar()
	{
		this.setVisible(false);
		return true;
	}
	
	/**
	 * Metodo que instancia uma nova cor
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public Color cor(int r, int g, int b)
	{
		return new Color(r, g, b);
	}

	/**
	 * Adiciona um componente nesta classe com seu zorder "0", ou seja, afrente de todos os outros componentes
	 * @param component a ser adicionado
	 */
	public void addComponente(JComponent component)
	{
		this.getContentPane().add(component);
		this.getContentPane().setComponentZOrder(component, 0);
	}

	/**
	 * Metodo auxiliar para adicionar paths de OMs java e dos modulos, utilziado externamente
	 * @param modulo
	 * @param nome 
	 */
	public static void addModulo(String modulo) { addModulo(modulo, modulo); }
	public static void addModulo(String modulo, String nome)
	{
		pathModulos.add(new PathASBase(pathWorkspace + modulo, nome));

		String nomeModuloSimples = modulo.replace("Atta", "");
		if(nomeModuloSimples.length()>0)
		{
			if(nomeModuloSimples.equalsIgnoreCase(modulo))
			{ pathOMs.add(pathWorkspace + modulo + "/src/persistencia/om"); }
			else
			{
				if(nomeModuloSimples.length()<=2)
				{ nomeModuloSimples = nomeModuloSimples.toLowerCase(); }
				else
				{ nomeModuloSimples = nomeModuloSimples.substring(0, 1).toLowerCase().concat(nomeModuloSimples.substring(1)); }
				
				pathOMs.add(pathWorkspace + modulo + "/src/persistencia/om"); }
		}
		else
		{ pathOMs.add("D:/Programacao/Flex/Atta/Atta/src/persistencia/om"); }
	}
	
	/**
	 * Metodo chamando antes do sistema ser fechado
	 */
	public abstract void sairGerenciador();
}
