import java.util.HashMap;

public class TabelaSimbolos {
	public HashMap<String, Simbolo> tabela = new HashMap<>();
	public static int end = -1;
	
	public final byte CONST = 0;
	public final byte VAR = 1;
	public final byte INTEGER = 2;
	public final byte CHAR = 3;
	public final byte FOR = 4;
	public final byte IF = 5;
	public final byte ELSE = 6;
	public final byte AND = 7;
	public final byte OR = 8;
	public final byte NOT = 9;
	public final byte THEN = 10;
	public final byte READ = 11;
	public final byte READLN = 12;
	public final byte STEP = 13;
	public final byte WRITE = 14;
	public final byte WRITELN = 15;
	public final byte DO = 16;
	public final byte VIRGULA = 17;
	public final byte PONTOVIRGULA = 18;
	public final byte ABPAR = 19;
	public final byte FCPAR = 20;
	public final byte ABCOLC = 21;
	public final byte FCCOLC = 22;
	public final byte ABCHAV = 23;
	public final byte FCCHAV = 24;
	public final byte MAIS = 25;
	public final byte MENOS = 26;
	public final byte DIV = 27;
	public final byte MOD = 28;
	public final byte MAIOR = 29;
	public final byte MENOR = 30;
	public final byte IGUAL = 31;
	public final byte MAIORIGUAL = 32;
	public final byte MENORIGUAL = 33;
	public final byte DIFERENTE = 34;
	public final byte MULT = 35;
	public final byte TRUE = 36;
	public final byte FALSE = 37;
	public final byte STRING = 38;
	public final byte TO = 39;
	public final byte APOSTRO = 40;
   public final byte CONSTANTES = 42;
	public final byte ID = 43;
	public TabelaSimbolos() {
		tabela.put("const", new Simbolo(CONST,"const", 0,++end));
      tabela.put("constantes", new Simbolo(CONSTANTES,"constantes", 0,++end));
		tabela.put("var", new Simbolo(VAR,"var", 0,++end));
		tabela.put("integer", new Simbolo(INTEGER,"integer", 0,++end));
		tabela.put("char", new Simbolo(CHAR,"char", 0,++end));
		tabela.put("for", new Simbolo(FOR,"for", 0,++end));
		tabela.put("if", new Simbolo(IF,"if", 0,++end));
		tabela.put("else", new Simbolo(ELSE,"else", 0,++end));
		tabela.put("and", new Simbolo(AND,"and", 0,++end));
		tabela.put("or", new Simbolo(OR,"or", 0,++end));
		tabela.put("not", new Simbolo(NOT,"not", 0,++end));
		tabela.put("then", new Simbolo(THEN,"then", 0,++end));
		tabela.put("read", new Simbolo(READ,"read", 0,++end));
		tabela.put("readln", new Simbolo(READLN,"readln", 0,++end));
		tabela.put("step", new Simbolo(STEP,"step", 0,++end));
		tabela.put("write", new Simbolo(WRITE,"write", 0,++end));
		tabela.put("writeln", new Simbolo(WRITELN,"writeln", 0,++end));
		tabela.put("do", new Simbolo(DO,"do", 0,++end));
		tabela.put(",", new Simbolo(VIRGULA,",", 0,++end));
		tabela.put(";", new Simbolo(PONTOVIRGULA,";", 0,++end));
		tabela.put("(", new Simbolo(ABPAR,"(", 0,++end));
		tabela.put(")", new Simbolo(FCPAR,")", 0,++end));
		tabela.put("[", new Simbolo(ABCOLC,"[", 0,++end));
		tabela.put("]", new Simbolo(FCCOLC,"]", 0,++end));
		tabela.put("{", new Simbolo(ABCHAV,"{", 0,++end));
		tabela.put("}", new Simbolo(FCCHAV,"}", 0,++end));
		tabela.put("+", new Simbolo(MAIS,"+", 0,++end));
		tabela.put("-", new Simbolo(MENOS,"-", 0,++end));
		tabela.put("/", new Simbolo(DIV,"/", 0,++end));
		tabela.put("%", new Simbolo(MOD,"%", 0,++end));
		tabela.put(">", new Simbolo(MAIOR,">", 0,++end));
		tabela.put("<", new Simbolo(MENOR,"<", 0,++end));
		tabela.put("=", new Simbolo(IGUAL,"=", 0,++end));
		tabela.put(">=", new Simbolo(MAIORIGUAL,">=", 0,++end));
		tabela.put("<=", new Simbolo(MENORIGUAL,"<=", 0,++end));
		tabela.put("<>", new Simbolo(DIFERENTE,"<>", 0,++end));
		tabela.put("*", new Simbolo(MULT,"*", 0,++end)); 
		tabela.put("to", new Simbolo(TO,"to", 0,++end));
		
	}
	
	public Simbolo buscaSimbolo(String lexema){
		lexema = lexema.toLowerCase();
		return tabela.get(lexema);
	}
	
	public Simbolo inserirID(String lexema){
		lexema = lexema.toLowerCase();
		Simbolo simbolo = new Simbolo(ID,lexema, 0);
		tabela.put(lexema, simbolo);
		return tabela.get(lexema);
	}

	public Simbolo inserirConstante(String lexema, String tipo){
		lexema = lexema.toLowerCase();
		Simbolo simbolo = new Simbolo(CONSTANTES, lexema, tipo, 0);
		tabela.put(lexema, simbolo);
		return tabela.get(lexema);

	}
   	public Simbolo inserirConstante(String lexema, String tipo, int tamanho){
		lexema = lexema.toLowerCase();
      System.out.println(tamanho);
		Simbolo simbolo = new Simbolo(CONSTANTES, lexema, tipo, tamanho);
		tabela.put(lexema, simbolo);
		return tabela.get(lexema);

	}
}