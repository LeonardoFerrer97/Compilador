import java.io.BufferedReader;

public class Parse {
   AnalisadorLexico lexico;
   Rotulo r;
   CodigoGerado c;
   
   int memoria_global = 0;
   int endereco_temporario = 0;
   int endereco_Exp =0;
   int endereco_ExpS = 0;
   int endereco_T=0;
   int endereco_F=0;
   
   TabelaSimbolos tabela;

   Simbolo s;

   BufferedReader arquivo;


   Parse(BufferedReader arquivo) { // declarações para executar o Compilador
      try {
         this.arquivo = arquivo; // arquivo com o programa lido
         lexico = new AnalisadorLexico(); // declara o Analisador lexico (automato)
         tabela = new TabelaSimbolos();
         r= new Rotulo();
         c = new CodigoGerado();
         
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
                  System.err.println(lexico.linha + ":Token não esperado: " + s.getLexema()+ token); 
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
            c.codigo.add("sseg SEGMENT STACK ;início seg. pilha ");
            c.codigo.add("byte 4000h DUP(?)  ;dimensiona pilha");
            c.codigo.add("sseg ENDS ;fim seg. pilha");
            c.codigo.add("dseg SEGMENT PUBLIC ;início seg. dados");
            c.codigo.add("byte 4000h DUP(?) ;temporários");
            memoria_global = 16384;
            while (s.getToken() == tabela.INTEGER || s.getToken() == tabela.CHAR || s.getToken() == tabela.CONST) {
               D();
            }
            c.codigo.add("dseg ends  ; fim seg.dados");
            c.codigo.add("cseg SEGMENT PUBLIC");
            c.codigo.add("ASSUME CS:cseg, DS:dseg");
            c.codigo.add("strt:");
            while ((s.getToken() == tabela.ID || s.getToken() == tabela.IF || s.getToken() == tabela.FOR
            		|| s.getToken() == tabela.READLN || s.getToken() == tabela.WRITE || s.getToken() == tabela.WRITELN
            		|| s.getToken() == tabela.PONTOVIRGULA) && !lexico.EOF) {
               C();
            }
            
            c.codigo.add("Mov AH, 4Ch");
            c.codigo.add("int 21h");
            c.codigo.add("cseg ENDS");
            c.codigo.add("END strt");
            c.criarArquivo();
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
      int endereco = 0;
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
            
            
            // Codigo Array
            
            c.codigo.add("byte " + aux.getTamanho()+ " DUP(?) ; vetor " + aux.getLexema() + " em " + memoria_global);
            alocaVetor(aux.getTamanho());
            
            aux.setEndereco(memoria_global);
            
         } else if (s.getToken() == tabela.IGUAL) {
         
            casaToken(tabela.IGUAL);
            if (s.getToken() == tabela.MENOS) {
               casaToken(tabela.MENOS);
            }
            
            if(Tipo_tipo != s.getTipo())  {
               System.out.println(lexico.linha+": Tipos incompativeis2");
               System.exit(0);
               
            }
             
            
            switch (s.getTipo()) {
               case "inteiro":
                  c.codigo.add("sword " + s.getLexema() + " ; inteiro " + s.getLexema());
                  endereco = alocaInteiro();
                  break;
               case "caractere":
                  c.codigo.add("byte " + s.getLexema() + " ; caractere " + s.getLexema());
                  endereco = alocaChar();
                  break;
            }
            casaToken(tabela.CONSTANTES);
            aux.setEndereco(endereco);
            
              
            
            
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
               
               // Geracao codigo array
               
               c.codigo.add("byte " + aux.getTamanho()+ " DUP(?) ; vetor " + aux.getLexema() + " em " + memoria_global);
               endereco = alocaVetor(aux.getTamanho());
            
               aux.setEndereco(endereco);
            
            } else if (s.getToken() == tabela.IGUAL) {
               casaToken(tabela.IGUAL);
               if (s.getToken() == tabela.MENOS) {
                  casaToken(tabela.MENOS);
               }
               if(Tipo_tipo != s.getTipo())  {
                  System.out.println(lexico.linha+": Tipos incompativeis2");
                  System.exit(0);
               
               }
             
            
               switch (s.getTipo()) {
                  case "inteiro":
                     c.codigo.add("sword " + s.getLexema() + " ; inteiro " + s.getLexema());
                     endereco = alocaInteiro();
                     break;
                  case "caractere":
                     c.codigo.add("byte " + s.getLexema() + " ; caractere " + s.getLexema());
                     endereco = alocaChar();
                     break;
               }
               casaToken(tabela.CONSTANTES);
               aux.setEndereco(endereco);
               
               
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
         if(aux.getTamanho() > 0 && s.getTipo().equals("caractere")) {
            c.codigo.add("byte " + s.getLexema() + " ; caractere " + s.getLexema());
            endereco = alocaString(s.getTamanho() -1 );
            c.codigo.add("byte " + s.getLexema().substring(0, s.getTamanho()-1) + "$" + s.getLexema().charAt(s.getTamanho() - 1));
         
         } else {
         
            switch (s.getTipo()) {
               case "inteiro":
                  c.codigo.add("sword " + s.getLexema() + " ; inteiro " + s.getLexema());
                  endereco = alocaInteiro();
                  break;
               case "caractere":
                  c.codigo.add("byte " + s.getLexema() + " ; caractere " + s.getLexema());
                  endereco = alocaChar();
                  break;
            }
         }
      
         
         casaToken(tabela.CONSTANTES);
         aux.setEndereco(endereco);
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
            while(s.getToken() != tabela.FCCHAV){
               C();
            
            }
            
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
            System.out.println(lexico.linha+": Classe de identificador incompativel  ["+s.getLexema()+"]");
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
      endereco_Exp = endereco_ExpS;
      
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
         
         c.codigo.add("mov ax, DS: ["+endereco_Exp+"]");
         c.codigo.add("mov bx, DS: ["+endereco_ExpS+"]");
         c.codigo.add("cmp ax,bx");
         String RotuloTrue = r.novoRotulo();  
         switch(operacao) {
            case 1:
               c.codigo.add("je "+RotuloTrue);
               break;
            case 2:
               c.codigo.add("jg " +RotuloTrue);
               break;
            case 3:
               c.codigo.add("jl "+RotuloTrue);
               break;
            case 4:
               c.codigo.add("jge "+RotuloTrue);
               break;
            case 5:
               c.codigo.add("jle "+RotuloTrue);
               break;
            case 6:
               c.codigo.add("jne "+RotuloTrue);
               break;
         
         }
         
         String RotuloFalso = r.novoRotulo();
         c.codigo.add("mov AL, 0");
         
         c.codigo.add("jmp " + RotuloFalso);
         
         c.codigo.add(RotuloTrue+":");
         c.codigo.add("mov AL, 1");
         c.codigo.add(RotuloFalso+":");
         
         c.codigo.add("mov DS: ["+endereco_Exp+"],AL");
         
      }
      return Exp_Tipo;
   
   }

   String expS() throws Exception {
      String ExpS_tipo = "";
      String ExpS_tipo2 = "";
      boolean menos = false;
      if (s.getToken() == tabela.MENOS || s.getToken() == tabela.MAIS) {
         
         if (s.getToken() == tabela.MENOS) {
            casaToken(tabela.MENOS);
            menos= true;
         }
      }
      
   
      Simbolo aux = s;
      ExpS_tipo= T();
      
      if(menos) {
         endereco_ExpS = endereco_temporario;
         c.codigo.add("mov ax, DS: ["+endereco_T+"]" );
         c.codigo.add("not ax");
         c.codigo.add("mov DS["+endereco_ExpS+"], ax");
      }else {
         endereco_ExpS = endereco_T;
      }
      int operacao = 0;
      while (s.getToken() == tabela.MENOS || s.getToken() == tabela.MAIS || s.getToken() == tabela.OR) {
         if (s.getToken() == tabela.MENOS) {
            if (ehString(aux)) {
               System.out.println(lexico.linha+": Tipos incompativeis.7");
               System.exit(0);
            }
            operacao =2;
            casaToken(tabela.MENOS);
         } else if (s.getToken() == tabela.MAIS) {
            if (ehString(aux)) {
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
         } else if(operacao > 1 && ehString(aux)){
            System.out.println(lexico.linha+": Tipos incompativeis.12");
            System.exit(0);
         }
         c.codigo.add("mov ax, DS: ["+endereco_ExpS+"] ");
         c.codigo.add("mov bx, DS: ["+endereco_T+"] ");
         switch (operacao) {
            case 1:
               c.codigo.add("or ax, bx");
               break;
            case 2: 
               c.codigo.add("sub ax,bx");
               break;
            case 3: 
               c.codigo.add("add ax,bx");
               break;
         }
         c.codigo.add("mov DS: ["+endereco_ExpS+"],ax");
         
      
      }
      return ExpS_tipo;
   }

   String T() throws Exception {
      String T_Tipo ="";
      String T2_Tipo = "";
      Simbolo aux = s;
      T_Tipo = F();
      
      endereco_T = endereco_F;
      
      
      int operacao = 0;
      while (s.getToken() == tabela.MULT || s.getToken() == tabela.DIV || s.getToken() == tabela.MOD || s.getToken() == tabela.AND) {
      
         if (s.getToken() == tabela.MULT) {
            if (ehString(aux)) {
               System.out.println(lexico.linha+": Tipos incompativeis.13");
               System.exit(0);
            }
            casaToken(tabela.MULT);
            operacao=2;
         } else if (s.getToken() == tabela.DIV) {
            if (ehString(aux)) {
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
         }else if (s.getToken() == tabela.MOD) {
            if(ehString(aux) || aux.getTamanho() > 0) {
               System.out.println(lexico.linha+": Tipos incompativeis.5");
               System.exit(0);
            }
            operacao=4;
            
            casaToken(tabela.MOD);
         }
         Simbolo aux2 = s;
         
         T2_Tipo = F();
         
         if (operacao == 1 && !T2_Tipo.equals("logico") && !aux2.getLexema().equals("0") && !aux2.getLexema().equals("1")) {
            System.out.println(lexico.linha+": Tipos incompativeis.16");
            System.exit(0);
         } else if(operacao > 1 && ehString(aux2)){
            System.out.println(lexico.linha+": Tipos incompativeis.17");
            System.exit(0);
         }
         
         c.codigo.add("mov ax, DS: ["+ endereco_T+"]");
         
         c.codigo.add("mov bx, DS: ["+ endereco_F+"]");
         
         switch (operacao) {
            case 1:
               c.codigo.add("and ax , bx ; Operacao and");
               alocaLogicoTemp();
               break;
            case 2:
               c.codigo.add("imul bx");
               alocaInteiroTemp();
               break;
            
            case 3:
               c.codigo.add("idiv bx ; divisao");
               alocaInteiroTemp();
               break;
            
            case 4:
               c.codigo.add("idiv bx ; mod");
               c.codigo.add("mov ax,dx ; mod");
               alocaInteiroTemp();
               break;
         }
         c.codigo.add("mov DS: ["+endereco_T+"] , ax");
         
         
            
      }
      
      return T_Tipo;
   
   }

   String F() throws Exception {
      String F_Tipo ="";
      if (s.getToken() == tabela.ABPAR) {
         casaToken(tabela.ABPAR);
         F_Tipo = exp();
         // GERACAO DE CODIGO
         
         endereco_F = endereco_Exp;
         
         
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
            
            endereco_F = endereco_temporario;
            c.codigo.add("mov al,"+ "DS: ["+endereco_F+"]");
            c.codigo.add("not al");
            c.codigo.add("mov DS: ["+endereco_F+"] , al");
            alocaLogicoTemp();
         }
         
      } else if (s.getToken() == tabela.ID) {
         endereco_F = s.getEndereco();
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
         if(ehString(s)){
         
            endereco_F = endereco_temporario;
            c.codigo.add("mov al,"+ s.getLexema().substring(1, s.getTamanho()-1) + "$");
            c.codigo.add("mov DS: ["+endereco_F+"] , al");
            alocaStringTemp(s.getTamanho());
            
         } else {
            endereco_F = endereco_temporario;
            c.codigo.add("mov al, "+s.getLexema());
            c.codigo.add("mov DS: ["+endereco_F+"] , al");
            if(s.getTipo().equals("caractere")) {
               alocaCharTemp();
            
            }else {
               alocaInteiroTemp();
            }
         }
         // GERACAO DE CODIGO
         
         
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
   
   
   
   /*  METODOS GERAÇÂO DE CODIGO    */ 

   public int alocaInteiro() {
      int aux = memoria_global;
      memoria_global +=2;
      return aux;
   }
   public int alocaChar() {
   
      int aux = memoria_global;
      memoria_global++;
      return aux;
   
   }
   public int alocaLogico() {
   
      int aux = memoria_global;
      memoria_global++;
      return aux;
   
   }
   public int alocaVetor(int tam) {
   
      int aux = memoria_global;
      memoria_global+=tam; 
      return aux;
   }
   public int alocaString(int tam) {
   
      int aux = memoria_global;
      memoria_global +=tam;
      return aux;
   }
   public int alocaInteiroTemp() {
      int aux = endereco_temporario;
      endereco_temporario +=2;
      return aux;
   }
   public int alocaCharTemp() {
   
      int aux = endereco_temporario;
      endereco_temporario++;
      return aux;
   
   }
   public int alocaLogicoTemp() {
   
      int aux = endereco_temporario;
      endereco_temporario++;
      return aux;
   
   }
   public int alocaVetorTemp(int tam) {
   
      int aux = endereco_temporario;
      endereco_temporario+=tam; 
      return aux;
   }
   public int alocaStringTemp(int tam) {
   
      int aux = endereco_temporario;
      endereco_temporario +=tam;
      return aux;
   }

   
}
