import java.io.BufferedReader;


public class AnalisadorLexico {
	
   TabelaSimbolos simbolos = new TabelaSimbolos();

   String lexema = "";

   public boolean dev = false;

   Simbolo simbolo;

   boolean id = false, constante = false;

   char c;

   public int linha = 1;

   public boolean comentario = false;

   public boolean EOF = false;
	
   public Simbolo analisar(Boolean devolver, BufferedReader bf) throws Exception {
      int estadoInicial = 0;
      int estadoFinal = 5;
      lexema ="";
      while (estadoInicial != estadoFinal) {
         switch (estadoInicial) {
            case 0: 
               {
                  if(devolver == false){			
                     c = (char)bf.read();
                  }
                  if (c == '\n') {
                     linha++;
                     devolver = false;
                     dev = false;
                  }
                  else if (c == '\r' || c == ' ' || c == '\t') {
                     devolver = false;
                     dev = true;
                  }
                  else if (((c=='+'  || c == '-')&& dev) ||  c == '(' || c == ')' || c == ',' ||c == ';' ||c == '{' || c == '}' ||c == '*' ||c == '[' ||c == ']' ||c == '%' ||c == '=') {
                     estadoInicial = estadoFinal;
                     lexema += c;
                     dev = false;
                  }
                  else if (ehDigito(c)) {
                     if (c == '0') {
                        estadoInicial = 1;
                        lexema += c;
                        dev = false;
                     }
                     else {
                        estadoInicial = 6;
                        lexema += c;
                        dev = false;
                     }
                  
                  }
                  else if (c == '+' || c == '-') {
                     estadoInicial = 7;
                     lexema += c;
                     dev = false;
                  }
                  else if (c == '_' || c == '.') {
                     estadoInicial = 15;
                     lexema += c;
                     dev = false;
                  }
                  else if (ehLetra(c)) {
                     estadoInicial = 9;
                     lexema += c;
                     dev = false;
                  }
                  else if (c == '/') {
                     estadoInicial = 10;
                     lexema += c;
                     dev = false;
                  }
                  else if (c == '>') {
                     estadoInicial = 13;
                     lexema += c;
                     dev = false;
                  }
                  else if (c == '<') {
                     estadoInicial = 13;
                     lexema += c;
                     dev = false;
                  }
                  else if ((int)c == 39 ) {
                     estadoInicial = 16;
                     lexema += c;
                     dev = false;
                  }
                  else if (c == '"') {
                     estadoInicial = 8;
                     lexema += c;
                     dev = false;
                  }
                  else if(c == -1 || c == 65535){
                     estadoInicial = estadoFinal;
                     EOF = true;
                     dev = false;
                     bf.close();
                  } else {
                     System.err.println(linha + ":Caractere invalido");
                     EOF = true;
                     System.exit(0);
                  }
                  break;
               }
            case 1: 
               {
                  c = (char)bf.read();
                  if (c == 'x'|| c == 'X') {
                     estadoInicial = 2;
                     lexema += c;
                     dev = false;
                  }
                  else if (ehDigito(c)) {
                     estadoInicial = 6;
                     lexema += c;
                     dev = false;
                  }
                  else {
                     estadoInicial = estadoFinal;
                     devolver = true;
                     dev = true;
                  }
                  break;
               }
            case 2: 
               {
                  c = (char)bf.read();
                  if (ehDigito(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
                     estadoInicial = 3;
                     lexema += c;
                     dev = false;
                  } else {
                     System.err.println(linha + ":Caractere invalido");
                     EOF = true;
                     System.exit(0);
                  }
                  break;
               }
            case 3: 
               {
                  c = (char)bf.read();
                  if (ehDigito(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
                     estadoInicial = 3;
                     lexema += c;
                     dev = false;
                  }
                  else {
                     estadoInicial = estadoFinal;
                     devolver = true;
                     dev = true;
                  }
               
                  break;
               }
            case 6: 
               {
                  c = (char)bf.read();
                  if (ehDigito(c)) {
                     estadoInicial = 6;
                     lexema += c;
                     dev = false;
                  } else {
                     estadoInicial = estadoFinal;
                     dev = true;
                     devolver = true;
                  }
                  break; 
               }
            case 7: 
               {
                  c = (char)bf.read();
                  if (ehDigito(c)) {
                     estadoInicial = 6;
                     lexema += c;
                     dev = false;
                  } else {
                     estadoInicial = estadoFinal;
                     devolver = true;
                     dev = true;
                  }
                  break;
               }
            case 8: 
               {
                  c = (char)bf.read();
                  if (c == '$') {
                     EOF = true;
                     System.err.println(linha + ":Lexema não esperado: " + lexema);
                     System.exit(0);
                  }
                  if (c != '"') {
                     estadoInicial = 8;
                     lexema += c;
                     dev = false;
                  } else {
                     estadoInicial = estadoFinal;
                     lexema += c;
                     dev = false;
                  }
                  break;
               }
            case 9: 
               {
                  c = (char)bf.read();
                  if (ehDigito(c) || ehLetra(c)|| c == '_' || c == '.') {
                     estadoInicial = 9;
                     lexema += c;
                     dev = false;
                  } else {
                     estadoInicial = estadoFinal;
                     devolver = true;
                     dev = true;
                  }
                  break;
               }
            case 10: 
               {
                  c = (char)bf.read();
                  if (c == '*') {
                     estadoInicial = 11;
                     lexema += c;
                     dev = false;
                  } else {
                     estadoInicial = estadoFinal;
                     dev = true;
                     devolver = true;
                  }
                  break;
               }
            case 11: 
               {
                  c = (char)bf.read();
                  if(c == '*'){
                     estadoInicial = 12;
                  }else if(c == 13){
                     estadoInicial = 11;
                     linha ++;
                  }else if(c == -1 || c == 65535){
                     EOF = true;
                     System.err.println(linha + ":Fim de arquivo não esperado");
                     System.exit(0);
                  }else{
                     estadoInicial = 11;
                  }
                  break;
               }
            case 12: 
               {
                  c = (char)bf.read();
                  if(c == '/'){
                     estadoInicial = 0;
                     lexema = "";
                     comentario = false;
                  }else if(c == '*'){
                     estadoInicial = 12;
                  }else if(c == -1 || c == 65535){
                     EOF = true;
                     System.err.println(linha + ":Fim de arquivo não esperado");
                     System.exit(0);
                  }else{
                     estadoInicial = 11;
                  }
                  break;
               }
            case 13: 
               {
                  c = (char)bf.read();
                  if (c == '=') {
                     estadoInicial = estadoFinal;
                     lexema += c;
                     dev = false;
                  } else if (c == '>') {
                     estadoInicial = estadoFinal;
                     lexema += c;
                     dev = false;
                  }else{
                     estadoInicial = estadoFinal;
                     devolver = true;
                     dev = true;
                  }
                  break;
               }
            case 14: 
               {
                  c = (char)bf.read();
                  if (c == '=') {
                     estadoInicial = estadoFinal;
                     lexema += c;
                     dev = false;
                  }
                  if (c == '>') {
                     estadoInicial = estadoFinal;
                     lexema += c;
                     dev = false;
                  } else {
                     estadoInicial = estadoFinal;
                     devolver = true;
                     dev = true;
                  }
                  break;
               }
            case 15: 
               {
                  c = (char)bf.read();
                  if (ehDigito(c) || ehLetra(c)) {
                     estadoInicial = 9;
                     lexema += c;
                     dev = false;
                  } else {
                     System.err.println(linha + ":Caractere invalido");
                     EOF = true;
                     System.exit(0);
                  }
                  break;
               }
            case 16: 
               {
                  c = (char)bf.read();
                  if ((int)c != 39  ) {
                     estadoInicial = 17;
                     lexema += c;
                     dev = false;
                  } 
               }
            case 17: 
               {
                  c = (char)bf.read();
                  if ((int)c == 39  ) {
                     estadoInicial = estadoFinal;
                     lexema += c;
                     dev = false;
                  }else {
                     System.err.println(linha + ":Caractere invalido");
                     EOF = true;
                     System.exit(0);
                  }
               }
         }
      	
      }
      if(!EOF){
         if(simbolos.tabela.get(lexema.toLowerCase()) != null){
            simbolo = simbolos.tabela.get(lexema.toLowerCase());
         } else {
            if (lexema.toLowerCase().equals("true") || lexema.toLowerCase().equals("false") ) {
               simbolo = simbolos.inserirConstante(lexema, "tipo_logico");
            }
            else if(ehLetra(lexema.charAt(0)) || lexema.charAt(0) == '_'|| lexema.charAt(0) == '.'){
               simbolo = simbolos.inserirID(lexema);
            } 				
            else if(ehDigito(lexema.charAt(0)) || lexema.charAt(0) == '-'|| lexema.charAt(0) == '+' || lexema.charAt(0) == '"' || (int)lexema.charAt(0) == 39){
               if(ehDigito(lexema.charAt(0)) || lexema.charAt(0) == '-'|| lexema.charAt(0) == '+' ){
                  if(lexema.charAt(0) == '0'){
                     if(lexema.length() > 1){
                        if(lexema.charAt(1) == 'x'){
                           if(lexema.length() == 4){
                              for(int i = 2; i < lexema.length(); i++){
                                 if((lexema.charAt(i)<'A' || lexema.charAt(i)>'F') && !(ehDigito(lexema.charAt(i)))){
                                    System.err.println(linha + ":Lexema não esperado: " + lexema);
                                    System.exit(0);
                                 }
                              }
                              simbolo = simbolos.inserirConstante(lexema, "caractere");
                           } else {
                              System.err.println(linha + ":Lexema não esperado: " + lexema);
                              System.exit(0);
                           }
                        									
                        }else if(lexema.length() <= 5){
                           for(char l : lexema.toCharArray()){
                              if(!ehDigito(l)){
                                 System.err.println(linha + ":Lexema não esperado: " + lexema);
                                 System.exit(0);
                              }
                           }
                           simbolo = simbolos.inserirConstante(lexema, "inteiro");
                        }
                     }else if(lexema.length() == 1){
                        simbolo = simbolos.inserirConstante(lexema, "inteiro");
                     }
                  }else{
                     if(lexema.length() <= 5){
                        int index = 0;
                        for(char l : lexema.toCharArray()){
                           if(index == 0) {
                              if (!ehDigito(l) && l != '+' && l != '-') {
                                 System.err.println(linha + ":Caracter inválido.");
                                 System.exit(0);
                              }
                           }
                           else if(!ehDigito(l)){
                              System.err.println(linha + ":Caracter inválido.");
                              System.exit(0);
                           }
                           index++;
                        }
                        int lex = 0;
                        lex = Integer.parseInt(lexema);
                        if(lex >= 0 && lex <= 255){
                           simbolo = simbolos.inserirConstante(lexema, "inteiro");
                        }else{
                           simbolo = simbolos.inserirConstante(lexema, "inteiro");
                        }
                     }else{
                        System.err.println(linha + ":Lexema não identificado: " + lexema);
                        System.exit(0);
                     }
                  }
               }else if(lexema.charAt(0) == '"' && lexema.charAt(lexema.length() - 1) == '"'){
                  simbolo = simbolos.inserirConstante(lexema, "caractere", lexema.length()); // string
               	
               }else if((int)lexema.charAt(0) == 39 && (int) lexema.charAt(lexema.length() - 1) == 39){
                  int lex = 0;
                  lex = Integer.parseInt((int)lexema.charAt(1)+"");
                  if(lex >= 0 && lex <= 255){
                     simbolo = simbolos.inserirConstante(lex+"", "caractere");
                  }
               }else{
                  System.err.println(linha + ":Lexema não identificado: " + lexema);
                  System.exit(0);
               }
            }else{
               System.err.println(linha + ":Lexema não identificado: " + lexema);
               System.exit(0);
            }
         }
      }
      return simbolo;
   }
   public boolean ehDigito(char c) {
      return (c >= '0' && c <= '9');
   }
   public boolean ehLetra(char c) {
      return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
   }
}
