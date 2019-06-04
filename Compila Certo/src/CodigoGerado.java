import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodigoGerado {
   public static List<String> codigo;
	public static List<String> real = new ArrayList<>();
	public BufferedWriter arquivo;

	public CodigoGerado() throws Exception{
		codigo = new ArrayList<>();
		arquivo = new BufferedWriter(new FileWriter("..\\Testes\\saida.asm"));
	}
   
	public void criarArquivo() throws IOException{
		for(String s : codigo){
			arquivo.write(s);
			arquivo.newLine();
		}
		arquivo.close();
	}
}