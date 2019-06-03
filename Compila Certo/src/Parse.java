import java.io.BufferedReader;

public class Parse {
   AnalisadorLexico lexico;

   TabelaSimbolos tabela;

   Simbolo s;

   BufferedReader arquivo;


   Parse(BufferedReader arquivo) { // declarações para executar o Compilador
      try {
         this.arquivo = arquivo; // arquivo com o programa lido
         lexico = new AnalisadorLexico(); // declara o Analisador lexico (automato)
         tabela = new TabelaSimbolos();
         s = lexico.analisar(lexico.dev, arquivo);// Executa a primeira analise do lexico, lendo o primeiro token do
      												// arquivo
      
         if (s == null) { // comentario
            s = lexico.analisar(lexico.dev, arquivo);
         }
      } catch (Exception e) {
         System.out.print(e.getMessage());
      }
   }

   void casaToken(byte token) {
      try {
         if (s != null) {
            if (s.getToken() == token) { 
               s = lexico.analisar(lexico.dev, arquivo);
            } else {
               if (lexico.EOF) {
                  System.err.println(lexico.linha + ":Fim de Arquivo não esperado."); 
                  System.exit(0);
               } else {
                  System.err.println(lexico.linha + ":Token não esperado: " + s.getLexema()); 
                  System.exit(0);
               }
            }
         }
      } catch (Exception e) {
         System.err.println("casaT" + e.toString());
      }
   }

   String tipo() throws Exception {
      if (s.getToken() == tabela.INTEGER) {
         casaToken(tabela.INTEGER);
         s.setTipo("inteiro");
         return "inteiro";
      }  else {
         casaToken(tabela.CHAR);
         s.setTipo("caractere");
         return "caractere";
      }
   }

   void S() {
      try {
         if (lexico.EOF) {
            System.err.println(lexico.linha + ":Fim de arquivo não esperado.");
            System.exit(0);
         }
      	
         if (s != null) {
            casaToken(tabela.VAR);
         
            while (s.getToken() == tabela.INTEGER || s.getToken() == tabela.CHAR || s.getToken() == tabela.CONST) {
               D();
            }
            while ((s.getToken() == tabela.ID || s.getToken() == tabela.IF || s.getToken() == tabela.FOR
            		|| s.getToken() == tabela.READLN || s.getToken() == tabela.WRITE || s.getToken() == tabela.WRITELN
            		|| s.getToken() == tabela.PONTOVIRGULA) && !lexico.EOF) {
               C();
            }
            if(!lexico.EOF){
               System.err.println(lexico.linha + ":Token não esperado: " + s.getLexema());
               System.exit(0);
            }
         }
      } catch (Exception e) {
         System.err.println(e.toString());
      }
   }

   void D() throws Exception {
      if (s.getToken() == tabela.INTEGER || s.getToken() == tabela.CHAR) {
         String Tipo_tipo = tipo();
         if(!s.getClasse().equals("")){
            System.out.println(lexico.linha+": Identificador ja declarado  ["+s.getLexema()+"]");
            System.exit(0);
         }else {
            s.setTipo(Tipo_tipo);
            s.setClasse("classe-var"); 
         }
         
         Simbolo aux = s;
         casaToken(tabela.ID);
         if (s.getToken() == tabela.ABCOLC) {
            casaToken(tabela.ABCOLC);
            Simbolo temp2=s;
            String Exp_tipo = exp();  
            if(!Exp_tipo.equals("inteiro")){
               System.out.println(lexico.linha+": Tipos incompativeis.");
               System.exit(0);
            }
            
            aux.setTamanho(Integer.parseInt(temp2.getLexema()));
            if((aux.getTipo().equals("inteiro") && aux.getTamanho() > 2048) || (!aux.getTipo().equals("inteiro") && aux.getTamanho() > 4096)) {
               System.out.println(lexico.linha+": Tamanho do vetor excede o maximo permitido. ");
               System.exit(0);
            }
            casaToken(tabela.FCCOLC);
         } else if (s.getToken() == tabela.IGUAL) {
         
            casaToken(tabela.IGUAL);
            if (s.getToken() == tabela.MENOS) {
               casaToken(tabela.MENOS);
            } else if(s.getToken() == tabela.MAIS) {
               casaToken(tabela.MAIS);
            }
            String exp_tipo = exp();
            
            
            if(Tipo_tipo != exp_tipo)  {
               System.out.println(lexico.linha+": Tipos incompativeis2");
               System.exit(0);
               
            }
            
            
         }
         while (s.getToken() == tabela.VIRGULA) {
            casaToken(tabela.VIRGULA);
            if(!s.getClasse().equals("")){
               System.out.println(lexico.linha+": Identificador ja declarado  ["+s.getLexema()+"]");
               System.exit(0);
            }else {
               s.setTipo(Tipo_tipo);
               s.setClasse("classe-var"); 
            }
            aux = s;
            casaToken(tabela.ID);
            if (s.getToken() == tabela.ABCOLC) {
               casaToken(tabela.ABCOLC);
               Simbolo temp2=s;
               String Exp_tipo = exp();  
               if(!Exp_tipo.equals("inteiro")){
                  System.out.println(lexico.linha+": Tipos incompativeis.");
                  System.exit(0);
               }
               aux.setTamanho(Integer.parseInt(temp2.getLexema()));
               if((aux.getTipo().equals("inteiro") && aux.getTamanho() > 2048) || (!aux.getTipo().equals("inteiro") && aux.getTamanho() > 4096)) {
                  System.out.println(lexico.linha+": Tamanho do vetor excede o maximo permitido. ");
                  System.exit(0);
               }
               casaToken(tabela.FCCOLC);
            } else if (s.getToken() == tabela.IGUAL) {
               casaToken(tabela.IGUAL);
               if (s.getToken() == tabela.MENOS) {
                  casaToken(tabela.MENOS);
               } else if(s.getToken() == tabela.MAIS) {
                  casaToken(tabela.MAIS);
               }
               
               if(Tipo_tipo != s.getTipo())  {
                  System.out.println(lexico.linha+": Tipos incompativeis");
                  System.exit(0);
               
               }
               
               casaToken(tabela.CONSTANTES);
            }
         }
      } else if (s.getToken() == tabela.CONST) {
         casaToken(tabela.CONST);
         if(!s.getClasse().equals("")){
            System.out.println(lexico.linha+": Identificador ja declarado  ["+s.getLexema()+"]");
            System.exit(0);
         }else {
            s.setClasse("classe-const"); 
         }
         Simbolo aux = s;
         casaToken(tabela.ID);
         
         
         if (s.getToken() == tabela.IGUAL) {
            casaToken(tabela.IGUAL);
            if (s.getToken() == tabela.MENOS) {
               casaToken(tabela.MENOS);
            }
               
         }
               
         aux.setTipo(s.getTipo());
         aux.setTamanho(s.getTamanho());
      
         
         casaToken(tabela.CONSTANTES);
      }
      casaToken(tabela.PONTOVIRGULA);
   }

   void C() throws Exception {
      if (s.getToken() == tabela.ID) {
         Simbolo aux = s;
         if(s.getClasse().equals("")){
            System.out.println(lexico.linha+": Identificador nao declarado  ["+s.getLexema()+"]");
            System.exit(0);
         }
         if(!s.getClasse().equals("classe-var")){
            System.out.println(lexico.linha+": Classe de identificador incompativel ["+s.getLexema()+"]");
            System.exit(0);
         }
         casaToken(tabela.ID);
         if (s.getToken() == tabela.ABCOLC) {
            casaToken(tabela.ABCOLC);
            String Exp_tipo = exp();  
            if(!Exp_tipo.equals("inteiro")){
               System.out.println(lexico.linha+": Tipos incompativeis.");
               System.exit(0);
            }
            casaToken(tabela.FCCOLC);
         }
         casaToken(tabela.IGUAL); 
         String Exp_tipo = exp();
         
         if(!Exp_tipo.equals(aux.getTipo()) ) {
            System.out.println(lexico.linha+": Tipos incompativeis.");
            System.exit(0);
         }
         casaToken(tabela.PONTOVIRGULA);
      
      } else if (s.getToken() == tabela.FOR) { 
         casaToken(tabela.FOR);
         if(s.getClasse().equals("")){
            System.out.println(lexico.linha+": Identificador nao declarado  ["+s.getLexema()+"]");
            System.exit(0);
         }
         if(!s.getTipo().equals("inteiro")){
            System.out.println(lexico.linha+": Tipos incompativeis.1");
            System.exit(0);
         }
         casaToken(tabela.ID);
      
         casaToken(tabela.IGUAL);
         String Exp_tipo = exp();  
         if(!Exp_tipo.equals("inteiro")){
            System.out.println(lexico.linha+": Tipos incompativeis.2");
            System.exit(0);
         }
         casaToken(tabela.TO);
      
         Exp_tipo = exp();  
         if(!Exp_tipo.equals("inteiro")){
            System.out.println(lexico.linha+": Tipos incompativeis.3");
            System.exit(0);
         }
         
         if (s.getToken() == tabela.STEP) {
         
            casaToken(tabela.STEP);
         
            if(s.getClasse().equals("classe-const")) {
               if(!s.getTipo().equals("inteiro")) {
                  System.out.println(lexico.linha+": Tipos incompativeis.");
                  System.exit(0);
               }
               casaToken(tabela.ID);
            } else {
               if(s.getToken() != tabela.CONSTANTES){
                  if(s.getClasse().equals(""))  {
                     System.out.println(lexico.linha+": Identificador nao declarado  ["+s.getLexema()+"]");
                     System.exit(0);
                  }
                  else if(!s.getClasse().equals("classe-const"))  {
                     System.out.println(lexico.linha+": Classe de identificador incompativel  ["+s.getLexema()+"]");
                     System.exit(0);
                  }
                  else if(!s.getTipo().equals("inteiro")) {
                     System.out.println(lexico.linha+": Tipos incompativeis.c");
                     System.exit(0);
                  }
               }
               casaToken(tabela.CONSTANTES);
            }
            
         
         }
         casaToken(tabela.DO);
         if (s.getToken() == tabela.ABCHAV) {
            casaToken(tabela.ABCHAV);
            C();
            casaToken(tabela.FCCHAV);
         }
      
      } else if (s.getToken() == tabela.IF) {
         casaToken(tabela.IF);
         String Exp_tipo = exp();
         if(!Exp_tipo.equals("logico") && (!s.getLexema().equals("0") && !s.getLexema().equals("1"))){
            System.out.println(lexico.linha+": Tipos incompativeis.b");
            System.exit(0);
         }
         casaToken(tabela.THEN);
      
         if (s.getToken() == tabela.ABCHAV) {
            casaToken(tabela.ABCHAV);
            while (s.getToken() != tabela.FCCHAV) {
               C();
            }
            casaToken(tabela.FCCHAV);
         }
         
         else if (s.getToken() == tabela.ID || s.getToken() == tabela.IF || s.getToken() == tabela.READLN
         		|| s.getToken() == tabela.FOR || s.getToken() == tabela.WRITE || s.getToken() == tabela.WRITELN) {
            C();
         }
      
         if (s.getToken() == tabela.ELSE) {
            casaToken(tabela.ELSE);
            if (s.getToken() == tabela.ABCHAV) {
               casaToken(tabela.ABCHAV);
               while (s.getToken() != tabela.FCCHAV) {
                  C();
               }
               casaToken(tabela.FCCHAV);
            } else if (s.getToken() == tabela.ID || s.getToken() == tabela.IF || s.getToken() == tabela.READLN
            		|| s.getToken() == tabela.WRITE || s.getToken() == tabela.FOR
            		|| s.getToken() == tabela.WRITELN) {
               C();
            }
         }
      } else if (s.getToken() == tabela.READLN) {
         casaToken(tabela.READLN);
         casaToken(tabela.ABPAR);
         if(s.getClasse().equals(""))  {
            System.out.println(lexico.linha+": Identificador nao declarado  ["+s.getLexema()+"]");
            System.exit(0);
         }
         if(!s.getClasse().equals("classe-var"))  {
            System.out.println(lexico.linha+": Identificador nao declarado  ["+s.getLexema()+"]");
            System.exit(0);
         }
         casaToken(tabela.ID);
         casaToken(tabela.FCPAR);
         casaToken(tabela.PONTOVIRGULA);
      
      } else if (s.getToken() == tabela.WRITE || s.getToken() == tabela.WRITELN) {
         if (s.getToken() == tabela.WRITE) {
            casaToken(tabela.WRITE);
         } else if (s.getToken() == tabela.WRITELN) {
            casaToken(tabela.WRITELN);
         }
         casaToken(tabela.ABPAR);
         exp();
         if (s.getToken() == tabela.VIRGULA) {
            casaToken(tabela.VIRGULA);
            exp();
         }
      
         while (s.getToken() == tabela.VIRGULA) {
            casaToken(tabela.VIRGULA);
            exp();
         
         }
         casaToken(tabela.FCPAR);
      
         casaToken(tabela.PONTOVIRGULA);
      } else if (s.getToken() == tabela.PONTOVIRGULA) {
         casaToken(tabela.PONTOVIRGULA);
      }
   }

   String exp() throws Exception {
      String Exp_Tipo = "";
      String Exp_Tipo2 = "";
      Simbolo aux= s;
      Exp_Tipo = expS();
      int operacao=0;
      if (s.getToken() == tabela.MAIOR || s.getToken() == tabela.MENOR || s.getToken() == tabela.MAIORIGUAL
      		|| s.getToken() == tabela.MENORIGUAL || s.getToken() == tabela.IGUAL
      		|| s.getToken() == tabela.DIFERENTE) {
      
         if (s.getToken() == tabela.IGUAL) {
            casaToken(tabela.IGUAL);
            operacao=1;
         } else {
            if (s.getToken() == tabela.MAIOR) {
               if(ehString(aux) || aux.getTamanho() > 0) {
                  System.out.println(lexico.linha+": Tipos incompativeis.1");
                  System.exit(0);
               }
               operacao=2;
               casaToken(tabela.MAIOR);
            } else if (s.getToken() == tabela.MENOR) {
               if(ehString(aux) || aux.getTamanho() > 0) {
                  System.out.println(lexico.linha+": Tipos incompativeis.2");
                  System.exit(0);
               }
               operacao=3;
               casaToken(tabela.MENOR);
            } else if (s.getToken() == tabela.MAIORIGUAL) {
               if(ehString(aux) || aux.getTamanho() > 0) {
                  System.out.println(lexico.linha+": Tipos incompativeis.3");
                  System.exit(0);
               }
               operacao=4;
               casaToken(tabela.MAIORIGUAL);
            } else if (s.getToken() == tabela.MENORIGUAL) {
               if(ehString(aux) || aux.getTamanho() > 0) {
                  System.out.println(lexico.linha+": Tipos incompativeis.4");
                  System.exit(0);
               }
               operacao=5;
               casaToken(tabela.MENORIGUAL);
            } else if (s.getToken() == tabela.DIFERENTE) {
               if(ehString(aux) || aux.getTamanho() > 0) {
                  System.out.println(lexico.linha+": Tipos incompativeis.5");
                  System.exit(0);
               }
               operacao=6;
               casaToken(tabela.DIFERENTE);
            }
         }
         Simbolo aux2 = s;
         Exp_Tipo2 = expS();
         if(operacao > 1 && ehString(aux2) || aux2.getTamanho() > 0 && !ehString(aux2))  {
            System.out.println(lexico.linha+": Tipos incompativeis.a"+aux2.getTipo()+" yh"+aux2.getTamanho());
            System.exit(0);
         }
         if(!Exp_Tipo2.equals(Exp_Tipo)&&
            !aux.getLexema().equals("0") &&
            !aux.getLexema().equals("1") &&
            !aux2.getLexema().equals("0") &&
            !aux2.getLexema().equals("1"))
         {
            System.out.println(lexico.linha+": Tipos incompativeis.6");
            System.exit(0);
         } else {
            Exp_Tipo = "logico";
         }
         
      }
      return Exp_Tipo;
   
   }

   String expS() throws Exception {
      String ExpS_tipo = "";
      String ExpS_tipo2 = "";
      if (s.getToken() == tabela.MENOS || s.getToken() == tabela.MAIS) {
         if (s.getToken() == tabela.MENOS) {
            casaToken(tabela.MENOS);
         } else if (s.getToken() == tabela.MAIS) {
            casaToken(tabela.MAIS);
         }
      }
      Simbolo aux = s;
      ExpS_tipo= T();
      int operacao = 0;
      while (s.getToken() == tabela.MENOS || s.getToken() == tabela.MAIS || s.getToken() == tabela.OR) {
         if (s.getToken() == tabela.MENOS) {
            if (!ExpS_tipo.equals("inteiro")) {
               System.out.println(lexico.linha+": Tipos incompativeis.7");
               System.exit(0);
            }
            operacao =2;
            casaToken(tabela.MENOS);
         } else if (s.getToken() == tabela.MAIS) {
            if (!ExpS_tipo.equals("inteiro")) {
               System.out.println(lexico.linha+": Tipos incompativeis.8");
               System.exit(0);
            }
            operacao =3;
            casaToken(tabela.MAIS);
         } else if (s.getToken() == tabela.OR) {
            if (!ExpS_tipo.equals("logico") && !aux.getLexema().equals("0") && !aux.getLexema().equals("1")) {
               System.out.println(lexico.linha+": Tipos incompativeis.9");
               System.exit(0);
            }
            operacao =1;
            casaToken(tabela.OR);
            ExpS_tipo = "logico";
         }
         aux = s;
         ExpS_tipo2 = T();
         if (operacao == 1 && !ExpS_tipo2.equals("logico") && !aux.getLexema().equals("0") && !aux.getLexema().equals("1")) {
            System.out.println(lexico.linha+": Tipos incompativeis.11"+ExpS_tipo2);
            System.exit(0);
         } else if(operacao > 1 && !ExpS_tipo2.equals("inteiro")){
            System.out.println(lexico.linha+": Tipos incompativeis.12");
            System.exit(0);
         }
      }
      return ExpS_tipo;
   }

   String T() throws Exception {
      String T_Tipo ="";
      String T2_Tipo = "";
      Simbolo aux = s;
      T_Tipo = F();
      int operacao = 0;
      while (s.getToken() == tabela.MULT || s.getToken() == tabela.DIV || s.getToken() == tabela.AND) {
      
         if (s.getToken() == tabela.MULT) {
            if (!T_Tipo.equals("inteiro")) {
               System.out.println(lexico.linha+": Tipos incompativeis.13");
               System.exit(0);
            }
            casaToken(tabela.MULT);
            operacao=2;
         } else if (s.getToken() == tabela.DIV) {
            if (!T_Tipo.equals("inteiro")) {
               System.out.println(lexico.linha+": Tipos incompativeis.14");
               System.exit(0);
            }
            casaToken(tabela.DIV);
            operacao=3;
         } else if (s.getToken() == tabela.AND) {
            if (!T_Tipo.equals("logico") && !aux.getLexema().equals("0") && !aux.getLexema().equals("1")) {
               System.out.println(lexico.linha+": Tipos incompativeis15."+T_Tipo+"teste"+aux.getTipo());
               System.exit(0);
            }
            casaToken(tabela.AND);
            operacao=1;
            T_Tipo = "logico";
         }
         Simbolo aux2 = s;
         T2_Tipo = F();
         if (operacao == 1 && !T2_Tipo.equals("logico") && !aux2.getLexema().equals("0") && !aux2.getLexema().equals("1")) {
            System.out.println(lexico.linha+": Tipos incompativeis.16");
            System.exit(0);
         } else if(operacao > 1 && !T2_Tipo.equals("inteiro")){
            System.out.println(lexico.linha+": Tipos incompativeis.17");
            System.exit(0);
         }
            
      }
      
      return T_Tipo;
   
   }

   String F() throws Exception {
      String F_Tipo ="";
      if (s.getToken() == tabela.ABPAR) {
         casaToken(tabela.ABPAR);
         F_Tipo = exp();
         
         casaToken(tabela.FCPAR);
      } else if (s.getToken() == tabela.NOT) {
         casaToken(tabela.NOT);
         Boolean ehLogico = false;
         if(s.getLexema().equals("0") || s.getLexema().equals("1")) {
            ehLogico = true;
         }
         F_Tipo = F();
         
         if(!F_Tipo.equals("logico") && !ehLogico) {
            System.out.println(lexico.linha+": Tipos incompativeis.18");
            System.exit(0);
         } else {
            F_Tipo = "logico";
         }
         
      } else if (s.getToken() == tabela.ID) {
      
         if(s.getClasse().equals("")){
            System.err.println(lexico.linha + ": Identificador nao declarado["+s.getLexema()+"]");
            System.exit(0);
         }else{
            F_Tipo = s.getTipo();
         }
         
         casaToken(tabela.ID);
         if (s.getToken() == tabela.ABCOLC) {
            casaToken(tabela.ABCOLC);
            String Exp_t = exp();  
            if(!Exp_t.equals("inteiro")){
               System.out.println(lexico.linha+": Tipos incompativeis.19");
               System.exit(0);
            }
            casaToken(tabela.FCCOLC);
         }
         
      } else {
         F_Tipo = s.getTipo();
         casaToken(tabela.CONSTANTES);
      }
      return F_Tipo;
   
   }
   
   
   public boolean ehString(Simbolo s) {
      boolean ehString = false;
      if (s.getTipo().equals("caractere") && s.getTamanho() > 0) {
         ehString=true;
      
      }
      return ehString;
   }
}
