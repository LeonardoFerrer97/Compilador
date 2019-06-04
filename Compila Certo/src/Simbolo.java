public class Simbolo {
   private byte token;
   private String lexema = "";
   private String classe = "";
   private String tipo = "";
   private int tamanho = 0;
   private int endereco = 0;
	
   public Simbolo(){
   	
   }
	
   public Simbolo(byte token, String lexema, int tamanho){
      this.lexema = lexema;
      this.token = token;
      this.tamanho = tamanho;
   }
   public Simbolo(byte token, String lexema, int tamanho, int endereco){
      this.lexema = lexema;
      this.token = token;
      this.tipo = tipo;
      this.tamanho = tamanho;
      this.endereco = endereco;
   }
	
   public Simbolo(byte token, String lexema, String tipo, int tamanho){
      this.lexema = lexema;
      this.token = token;
      this.tipo = tipo;
      this.tamanho = tamanho;
   }
	
   public Simbolo(String lexema, byte token, String classe, String tipo, int tamanho) {
      super();
      this.lexema = lexema;
      this.token = token;
      this.classe = classe;
      this.tipo = tipo;
      this.tamanho = tamanho;
   }
	
   public byte getToken(){
      return token;
   }
	
   public int getTamanho(){
      return tamanho;
   }
   public void setTamanho(int tamanho){
      this.tamanho = tamanho;
   }
	
   public String getLexema(){
      return lexema;
   }

   public String getTipo() {
      return tipo;
   }

   public void setTipo(String tipo) {
      this.tipo = tipo;
   }

   public String getClasse() {
      return classe;
   }

   public void setClasse(String classe) {
      this.classe = classe;
   }
      public int getEndereco(){
      return this.endereco;
   }
      public void setEndereco(int endereco){
      this.endereco = endereco;
   }

	
}