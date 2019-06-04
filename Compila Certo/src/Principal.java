import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
public class Principal {
	static Parse p;
	static String nomeArquivo = "";
	static BufferedReader arquivo;
	//C:\\Users\\Marco\\Desktop\\Compilador\\src\\teste.txt
	

	static void lerCaminho(){

		//BufferedReader in = new BufferedReader(new InputStreamReader(System.in));  	

		//String file = "";
		
      Scanner sc = new Scanner(System.in);  
		try{
			
			System.out.println("Digite o nome do arquivo de teste! Digite 0 para sair do programa!");
			nomeArquivo = sc.nextLine();
			if (!nomeArquivo.equals("0"))arquivo = new BufferedReader(new FileReader("..\\Testes\\"+nomeArquivo));

		}catch (Exception e) {

			System.err.println("Arquivo não encontrado"); 

			lerCaminho();

		}

	}

	

	public static void main(String[] args) throws Exception{

		while (!nomeArquivo.equals("0")){
			try{
				lerCaminho();
				if (!nomeArquivo.equals("0")) {
            
					p = new Parse(arquivo); // faz a leitura do Arquivo 
					
					p.S(); // Chama o Primeiro caminho de acordo com a Gramatica da linguagem
					
					System.out.println("Finalizado - sem erros.");	
				}
	
	
			}catch (Exception e) {
	
				System.err.println("Erro: " + e.getMessage());
	
			}
		}

	}
}
