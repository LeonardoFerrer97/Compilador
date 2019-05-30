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

   void tipo() throws Exception {
      if (s.getToken() == tabela.INTEGER) {
         casaToken(tabela.INTEGER);
      } else if (s.getToken() == tabela.STRING) {
         casaToken(tabela.STRING);
      } else if(s.getToken() == tabela.CHAR) {
         casaToken(tabela.CHAR);
      } else if (s.getToken() == tabela.CONST) {
         casaToken(tabela.CONST);
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
         
            while (s.getToken() == tabela.STRING || s.getToken() == tabela.INTEGER || s.getToken() == tabela.CHAR || s.getToken() == tabela.CONST) {
               D();
            }
            while ((s.getToken() == tabela.ID || s.getToken() == tabela.IF || s.getToken() == tabela.FOR
            		|| s.getToken() == tabela.READLN || s.getToken() == tabela.WRITE || s.getToken() == tabela.WRITELN
            		|| s.getToken() == tabela.PONTOVIRGULA) && !lexico.EOF) {
               C();
            }
         }
      } catch (Exception e) {
         System.err.println(e.toString());
      }
   }

   void D() throws Exception {
      if (s.getToken() == tabela.STRING || s.getToken() == tabela.INTEGER || s.getToken() == tabela.CHAR || s.getToken() == tabela.CONST) {
         tipo();
         
         casaToken(tabela.ID);
         if (s.getToken() == tabela.ABCOLC) {
            casaToken(tabela.ABCOLC);
            if (s.getToken() == tabela.ID) {
               casaToken(tabela.ID);
            } else
               casaToken(tabela.CONST);
         
            casaToken(tabela.FCCOLC);
         } else if (s.getToken() == tabela.IGUAL) {
         
            casaToken(tabela.IGUAL);
            if (s.getToken() == tabela.MENOS) {
               casaToken(tabela.MENOS);
            } else if(s.getToken() == tabela.MAIS) {
               casaToken(tabela.MAIS);
            }
         
            casaToken(tabela.CONST);
         }
         while (s.getToken() == tabela.VIRGULA) {
            casaToken(tabela.VIRGULA);
            casaToken(tabela.ID);
            if (s.getToken() == tabela.ABCOLC) {
               casaToken(tabela.ABCOLC);
               if (s.getToken() == tabela.ID) {
                  casaToken(tabela.ID);
               } else	casaToken(tabela.CONST);
               casaToken(tabela.FCCOLC);
            } else if (s.getToken() == tabela.IGUAL) {
               casaToken(tabela.IGUAL);
               if (s.getToken() == tabela.MENOS) {
                  casaToken(tabela.MENOS);
               } else if(s.getToken() == tabela.MAIS) {
                  casaToken(tabela.MAIS);
               }
               casaToken(tabela.CONST);
            }
         }
      }
      casaToken(tabela.PONTOVIRGULA);
   }

   void C() throws Exception {
      if (s.getToken() == tabela.ID) {
         casaToken(tabela.ID);
         if (s.getToken() == tabela.ABCOLC) {
            casaToken(tabela.ABCOLC);
            if (s.getToken() == tabela.ID) {
               casaToken(tabela.ID);
            } else
               casaToken(tabela.CONST);
            casaToken(tabela.FCCOLC);
         }
         casaToken(tabela.IGUAL); 
         exp();
         casaToken(tabela.PONTOVIRGULA);
      
      } else if (s.getToken() == tabela.FOR) { 
         casaToken(tabela.FOR);
         casaToken(tabela.ID);
         casaToken(tabela.IGUAL);
         exp();
         casaToken(tabela.TO);
      
         if (s.getToken() == tabela.ID) {
            casaToken(tabela.ID);
            if (s.getToken() == tabela.ABCOLC) {
               casaToken(tabela.ABCOLC);
               if (s.getToken() == tabela.ID) {
                  casaToken(tabela.ID);
               } else
                  casaToken(tabela.CONST);
               casaToken(tabela.FCCOLC);
            }
         } else if (s.getToken() == tabela.CONST) {
            casaToken(tabela.CONST);
         
         }
         if (s.getToken() == tabela.STEP) {
         
            casaToken(tabela.STEP);
            casaToken(tabela.CONST);
         
         }
         casaToken(tabela.DO);
         if (s.getToken() == tabela.ABCHAV) {
            casaToken(tabela.ABCHAV);
            C();
            casaToken(tabela.FCCHAV);
         }
      
      } else if (s.getToken() == tabela.IF) {
         casaToken(tabela.IF);
         exp();
      
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

   void exp() throws Exception {
      expS();
   
      if (s.getToken() == tabela.MAIOR || s.getToken() == tabela.MENOR || s.getToken() == tabela.MAIORIGUAL
      		|| s.getToken() == tabela.MENORIGUAL || s.getToken() == tabela.IGUAL
      		|| s.getToken() == tabela.DIFERENTE) {
      
         if (s.getToken() == tabela.IGUAL) {
            casaToken(tabela.IGUAL);
         } else {
            if (s.getToken() == tabela.MAIOR) {
               casaToken(tabela.MAIOR);
            } else if (s.getToken() == tabela.MENOR) {
               casaToken(tabela.MENOR);
            } else if (s.getToken() == tabela.MAIORIGUAL) {
               casaToken(tabela.MAIORIGUAL);
            } else if (s.getToken() == tabela.MENORIGUAL) {
               casaToken(tabela.MENORIGUAL);
            } else if (s.getToken() == tabela.DIFERENTE) {
               casaToken(tabela.DIFERENTE);
            }
         }
      
         expS();
      
      }
   
   }

   void expS() throws Exception {
      if (s.getToken() == tabela.MENOS || s.getToken() == tabela.MAIS) {
         if (s.getToken() == tabela.MENOS) {
            casaToken(tabela.MENOS);
         } else if (s.getToken() == tabela.MAIS) {
            casaToken(tabela.MAIS);
         }
      }
      T();
   
      while (s.getToken() == tabela.MENOS || s.getToken() == tabela.MAIS || s.getToken() == tabela.OR) {
         if (s.getToken() == tabela.MENOS) {
            casaToken(tabela.MENOS);
         } else if (s.getToken() == tabela.MAIS) {
            casaToken(tabela.MAIS);
         } else if (s.getToken() == tabela.OR) {
            casaToken(tabela.OR);
         }
      
         T();
      
      }
   }

   void T() throws Exception {
      F();
   
      while (s.getToken() == tabela.MULT || s.getToken() == tabela.DIV || s.getToken() == tabela.AND) {
         if (s.getToken() == tabela.MULT) {
            casaToken(tabela.MULT);
         } else if (s.getToken() == tabela.DIV) {
            casaToken(tabela.DIV);
         } else if (s.getToken() == tabela.AND) {
            casaToken(tabela.AND);
         }
         F();
      
      }
   
   }

   void F() throws Exception {
      if (s.getToken() == tabela.ABPAR) {
         casaToken(tabela.ABPAR);
         exp();
         casaToken(tabela.FCPAR);
      } else if (s.getToken() == tabela.NOT) {
         casaToken(tabela.NOT);
         F();
      } else if (s.getToken() == tabela.CONST) {
         casaToken(tabela.CONST);
      } else {
         casaToken(tabela.ID);
         if (s.getToken() == tabela.ABCOLC) {
            casaToken(tabela.ABCOLC);
            if (s.getToken() == tabela.ID) {
               casaToken(tabela.ID);
            } else
               casaToken(tabela.CONST);
            casaToken(tabela.FCCOLC);
         }
      }
   
   }
}
