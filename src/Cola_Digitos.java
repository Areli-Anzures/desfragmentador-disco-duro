import java.awt.Color;

//clase para 
public class Cola_Digitos {
	public final Color color;
	public int digito;
	Lugares lugar;
	
	public Cola_Digitos(Color color, int digito, int x, int y) {
		this.color=color;
		this.digito = digito;
		lugar = new Lugares(x,y);
	}
	
	public Cola_Digitos(Color color, int digito, int j) {
		this.color=color;
		this.digito = digito;
		lugar = new Lugares(0,0);
	}
	
}
