 /** PRACTICA 1 Inteligencia Artificial 
  * 			Simulador de un desfragmentador de Disco duro con 3 niveles diferentes
  *	Nombre: Areli Anzures Villarreal   2202800380
  * Fecha: Jueves 10/Septiembre/2020  
 **/

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.Timer;

class DiscoD extends JFrame implements KeyListener, ActionListener, ItemListener {
	private JTextField[][] Casilla;
	private Object[][][] Tablero;
	//botones de salir, conenzar y nuevo desfragmentado
	private JButton jButton1;
	private JButton jButton2;
	private JButton jButton3;
	//botones salir y aceptar
	private JButton aceptar, salir;
	//seleccion de nivel
	private JRadioButton nivel1, nivel2, nivel3;
	//etiqueta de dificultad
	private JLabel etiqueta;
	//para los menos
	private JMenuBar menuBar; //barra para menu
	private JMenu menu; //menu
	private JMenuItem subMenu1, subMenu2, subMenu3; //submenus
	//segunda ventana de opciones
	private JDialog opcion;
	//paneles para ventana principal y ventana de opciones
	private JPanel Panel_Matriz, panelOp;
	private  Color[] colores;
	//private Timer timer;
	private int n,m; //tamanio de cuadricula
	private int nivel = 0; //seleccion de nivel
	private Timer timer;
	//Para guardar la informacion de nuestro tablero y 
	//asi poder ordenarla de manera correcta
	private Cola_Digitos fragmento; 
	
	//ArrayList para la informacion desordenada
	LinkedList<Cola_Digitos> DisDesordenado;
	
	private LinkedList<Lugares> LugaresVacios = new LinkedList<>();
	//constructor 
	
	public DiscoD(int n, int m) {
		this.n=n;
		this.m=m;
		//Declaramos y llenamos el tablero auxiliar con una configuracion aleatoria
		//el tablero va a ser una matriz tridimensional, con 2 capas
		//la primera capa van a ser los fragmentos de cada archivo
		//la segunda capa va a ser el color
		Tablero=new Object[2][n][m];
		//inicializamos los colores de los archivos
		colores=new Color[5];
		colores[0]=Color.YELLOW;
		colores[1]=Color.RED;
		colores[2]=Color.ORANGE;
		colores[3]=Color.LIGHT_GRAY;
		colores[4]=Color.BLUE;
		//iniciamos los componentes graficos de la aplicacion
		iniciaComponentes();
	}

	//inicializa los componentes parwa la creacion de la ventana principal
	public void iniciaComponentes() {
		//ponemos estilo libre mediante la instruccion setLayout(null)
		setLayout(null);
		Font fuente = new Font("Courier New", Font.BOLD, 12);
		//inicializamos la matriz que sera nuestro tablero
		Casilla =  new JTextField[n][m];
		//declaramos el panel que contendra el tablero
		Panel_Matriz= new JPanel();
		//agregamos la fuente de letra al panel
		Panel_Matriz.setFont(fuente);
		//hacemos el panel visible
		Panel_Matriz.setVisible(true);
		//dependiendo de las dimensiones del tablero el panel crece o decrece
		Panel_Matriz.setBorder(BorderFactory.createTitledBorder("Desfragmentador"));
		if(n==5)
			Panel_Matriz.setBounds(20,20,520,300);
		else if(n==10)
			Panel_Matriz.setBounds(20,20,760,440);
		else
			Panel_Matriz.setBounds(20,20,1000,580);
		//dividimos el panel en una matriz de n x m para pintar el tablero
		Panel_Matriz.setLayout(new GridLayout(n,m));
			
		//se crea el JMenuBar y se agrega al panel
		menuBar=new JMenuBar();
		this.setJMenuBar(menuBar);
		//creamos el menuy lo agregamos al menuBar
		menu= new JMenu("Simulador");
		menuBar.add(menu);
		//se crean los submenus y se agregan al Jmenu
		subMenu1= new JMenuItem("Nueva Simulacion (F2)");
		subMenu2= new JMenuItem("Opciones (F3)");
		subMenu3= new JMenuItem("Salir");
		menu.add(subMenu1);
		menu.add(subMenu2);
		menu.add(subMenu3);
		//finalmente se agregan sus respectios Action Listener
		subMenu1.addActionListener(this);
		subMenu2.addActionListener(this);
		subMenu3.addActionListener(this);
			
		//inicializamos el tablero
		iniciaTablero(n,m); //desfragmentador Normal por default
		//finalemente agregamos el panel al JFrame que creamos al inicio
		getContentPane().add(Panel_Matriz);
			
		//nuevamente declaramos un nuevo tio de letra para los demas elementos
		fuente = new Font("Courier New", Font.BOLD, 16);
		//declaramos los botones
		jButton1 = new JButton("Comenzar");
		jButton1.setFont(fuente);
		jButton1.addActionListener(this);
			
		jButton3 = new JButton("Salir");
		jButton3.setFont(fuente);
		jButton3.addActionListener(this);
			
		fuente = new Font("Courier New", Font.BOLD, 12);
		jButton2 = new JButton("Nuevo Desfragmentado");
		jButton2.setFont(fuente);
		jButton2.addActionListener(this);
			
			
		//dependiendo de las dimensiones del tablero el JFrame se ajusta
		if(n==5) {
			setBounds(0,0,580,460);
			jButton1.setBounds(25,340,160,30);
			jButton2.setBounds(215,340,180,30);
			jButton3.setBounds(425,340,110,30);
		}
		else if(n==10){
			setBounds(0,0,800,600);
			jButton1.setBounds(25,480,160,30);
			jButton2.setBounds(300,480,180,30);
			jButton3.setBounds(620,480,110,30);
		} else {
			setBounds(0,0,1050,780);
			jButton1.setBounds(25,620,160,30);
			jButton2.setBounds(430,620,180,30);
			jButton3.setBounds(880,620,110,30);
		}
		
		this.add(jButton1);
		this.add(jButton2);
		this.add(jButton3);
			
			
		//agregamos un metodo de alida por default
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//pnemos en modo visible nuestra ventana
		setVisible(true);
		//hacemos que la ventana se localice en el centro de la pantalla
		setLocationRelativeTo(null);
	}
	
	//se inicializa el tablero de acuerdo al nivel elegido
	private void iniciaTablero(int n, int m) {
		//seleccionamos una nueva fuente para los numeros del tablero
		for(int i=0; i<n; i++) {				
			for(int j=0; j<m; j++) {
				Tablero[0][i][j] = null;
			}
		}
		Font fuente = new Font("Courier New", Font.BOLD, 12);
		//empieza la declaracion del tablero mediante un for doble
		for(int i=0; i<n; i++) {				
			for(int j=0; j<m; j++) {
				Casilla[i][j]=new JTextField("");
				//anniadimos el tipo de letra a las casillas del tablero
				Casilla[i][j].setFont(fuente);
				//Establecer un colo para el fondo de la casilla
				Casilla[i][j].setBackground(Color.WHITE);
				//establecer un color para el texto de la casilla
				Casilla[i][j].setForeground(Color.BLACK);
				//agregamos un concierge de eventos
			//	Casilla[i][j].addActionListener(this);
				//agregamos cada casilla creada al panel
				Panel_Matriz.add(Casilla[i][j]);
			}
		}	
			
		generaTablero();
	}
	
	public void limpiaTablero() {
		Font fuente = new Font("Courier New", Font.BOLD, 14);
		Panel_Matriz.removeAll();
		//empieza la declaracion del tablero mediante un for doble
		for(int i=0; i<n; i++) {				
			for(int j=0; j<m; j++) {
				Casilla[i][j]=new JTextField("");
				//anniadimos el tipo de letra a las casillas del tablero
				Casilla[i][j].setFont(fuente);
				Casilla[i][j].setText("");
				//Establecer un colo para el fondo de la casilla
				Casilla[i][j].setBackground(Color.WHITE);
				//establecer un color para el texto de la casilla
				Casilla[i][j].setForeground(Color.BLACK);
				//agregamos un concierge de eventos
			}
		}
	}
	
	//genera el tablero con colores y numeros
	public void generaTablero() {
		Random A = new Random();
		//num aleatoio del 0 al m*n para generar el maximo de casillas
		DisDesordenado = new LinkedList<>();
		int color=0;
		//inidices para las casillas
		int aux1, aux2;
		do {
			int numColor = A.nextInt(n*m/5); // Número de fragmentos para el archivo de color "color"
			//se genera el numero maximo para poner en casillas
			for(int i=1;i<numColor+1; i++) {
				//se generan los indices de manera aleatoria
				do {
					aux1 =A.nextInt(n);//5);
					aux2=A.nextInt(m);//10);

				}while(LugarOcupado(aux2, aux1));
				//se agregan las caracteristicas a las casillas
				Casilla[aux1][aux2].setText(" " + String.valueOf(i));
				Casilla[aux1][aux2].setBackground(colores[color]);
				Casilla[aux1][aux2].setForeground(Color.BLACK);
				//guardamos la informacion de nuestro tablero en un ArrayList
				fragmento = new Cola_Digitos(colores[color], i, aux1, aux2);
				DisDesordenado.add(fragmento);
				Tablero[0][aux1][aux2]=fragmento;
			}
			color++;
		}while(color<5);
	}
	
	//funcion que busca un lugar de acuerdo a las coordenadas dadas
	public boolean LugarOcupado(int coordX, int coordY) {
		if(LugaresVacios.isEmpty()) { //la lista de lugares esta vacia
			LugaresVacios.add(new Lugares(coordX, coordY));
			return false;
		} else { //la lista no esta vacia
			for(int i=0; i<LugaresVacios.size(); i++) { //recorre la lista
				if((LugaresVacios.get(i).x == coordX) && (LugaresVacios.get(i).y == coordY)) {
					return true; //si las coordenadas coiciden, entonces ya hay un lugar ocupado con esas coord
				}
			}
			LugaresVacios.add(new Lugares(coordX, coordY)); //agrega ese lugar para prox referencia
					return false;
		} 
	}
	
	//metodo para la simulacion
	public void ComienzaSimulacion(int ind) {
		Cola_Digitos fragAux;
		//comienza el recorrido en la lista
		int i = ind/m;
		int j = ind%m;
				
		fragmento = DisDesordenado.get(ind);   
		if(Tablero[0][i][j]==null) { //si en la primera pos. no hay nada
			Tablero[0][i][j]=fragmento; //agrega el fragmento
			//donde estaba el fragmento, lo quita
			Tablero[0][fragmento.lugar.x][fragmento.lugar.y]=null;
			//en la nueva casilla pone color y digito 
			Casilla[i][j].setText(" " + String.valueOf(fragmento.digito));
			Casilla[i][j].setBackground(fragmento.color);
			//donde estaba el fragmento pone casilla en blanco
			Casilla[fragmento.lugar.x][fragmento.lugar.y].setText("");
			Casilla[fragmento.lugar.x][fragmento.lugar.y].setBackground(Color.WHITE);
			fragmento.lugar.x=i;
			fragmento.lugar.y=j;
		} else { //si la casilla no esta vacia
			//guarda ese fragmento en uno auxiliar
			fragAux = (Cola_Digitos) Tablero[0][i][j];
			Tablero[0][i][j] = fragmento; //el nuevo fragmento lo coloca
			//coloca el fragmento anterior en el lugar del que se movio
			Tablero[0][fragmento.lugar.x][fragmento.lugar.y]= fragAux;
			fragAux.lugar.x=fragmento.lugar.x;
			fragAux.lugar.y=fragmento.lugar.y;
			//se coloca el fragmento ordenado en el lugar correspondiente
			fragmento.lugar.x=i;
			fragmento.lugar.y=j;
			//digito y color de fragmento que se ordeno
			Casilla[i][j].setText(" " + String.valueOf(fragmento.digito));
			Casilla[i][j].setBackground(fragmento.color);
			//digito y color del fragmento que se movio
			Casilla[fragAux.lugar.x][fragAux.lugar.y].setText(" " + String.valueOf(fragAux.digito));
			Casilla[fragAux.lugar.x][fragAux.lugar.y].setBackground(fragAux.color);
		}
	}

	//ventana de opciones
	public void Opciones() {
		//se crea la nueva vetana de opciones
		opcion = new JDialog();
		opcion.setVisible(true);
		opcion.setTitle("Dificultad"); //nombre
		opcion.setSize(500, 150); //tamanio
		opcion.setLocationRelativeTo(this); 
		//se crean los botones
		aceptar =  new JButton("Aceptar");
		salir = new JButton("Salir");
		//se crean los componentes de la ventana y se agregan
		nivel1 = new JRadioButton("Desfragmentador Normal cuadricula de 5 x 10 mosaicos");
		nivel2 = new JRadioButton("Desfragmentador Nightmare cuadricula de 10 x 20 mosaicos");
		nivel3 = new JRadioButton("Desfragmentador Hell cuadricula de 15 x 30 mosaicos");
		//se crea un panel
		panelOp= new JPanel();
		JPanel panelOp1= new JPanel();
		panelOp.setLayout(new GridLayout(3,1)); 
		//se agregan los componentes al panel
		panelOp.add(nivel1);
		panelOp.add(nivel2);
		panelOp.add(nivel3);
		panelOp1.add(aceptar);
		panelOp1.add(salir);
		opcion.getContentPane().add(panelOp, BorderLayout.CENTER);
		opcion.getContentPane().add(panelOp1, BorderLayout.SOUTH);
			
		//se agregan los ActionListener e ItemListener
		//botones
		aceptar.addActionListener(this);
		salir.addActionListener(this);
		//Radiobutton
		nivel1.addItemListener(this);
		nivel2.addItemListener(this);
		nivel3.addItemListener(this);
	}
		
	//ActionListeners
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jButton1) {//boton comenzar
			timer = new Timer(200,  new ActionListener() {
				private int i=0;
				int tamano=DisDesordenado.size();
				public void actionPerformed(ActionEvent evt) {
					if(i>=tamano) {
						timer.stop();
						return;
					}
					ComienzaSimulacion(i);
					i++;
				}
			});
			timer.start();
		}
		else if(e.getSource() == jButton2) {//boton defragmentado nuevo
			this.dispose();
			new DiscoD(n,m);
		}
		else if(e.getSource() == jButton3) //boton salir
			System.exit(0);
		else if(e.getSource() == subMenu1) { //nuevo desfragmentado
			limpiaTablero();
			generaTablero();
		}
		else if(e.getSource() == subMenu2) //ventana opciones
			Opciones();
		else if(e.getSource() == subMenu3)  //salir
			System.exit(0);
		else if(e.getSource() == salir) //boton salir de ventana opciones
			opcion.dispose();
		else if(e.getSource() == aceptar) { //para seleccion de nivel
			switch (nivel) {
				case 1: opcion.dispose();
						this.dispose();
						new DiscoD(5,10);
						//se selecciono radio button nivel1
						break;
				case 2: opcion.dispose();
						this.dispose();
						new DiscoD(10,20);
						//se selecciono radio button nivel2
						break;
				case 3: opcion.dispose();
						this.dispose();
						new DiscoD(15,30);
						//se selecciono radio button nivel3
						break;
				default: System.out.println("Seleccione nivel");
						//no se selecciono nivel 
			}
		}
	}

	//RadioButtons
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == nivel1) //se selecciono nivel 1
			nivel=1;
		else if(e.getSource() == nivel2) //nivel 2
			nivel=2;
		else if (e.getSource() == nivel3) //nivel 3
			nivel=3;
	}
		
	//Para crear una nueva simulacion y/o abrir la ventana de opciones
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_F)
			System.out.println("presione F2");
		else if(e.getKeyCode() == KeyEvent.VK_F3)
			Opciones(); //se abre el menu de opciones
	}
		
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
