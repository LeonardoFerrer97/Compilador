public class Rotulo {
   int contadorRotulos = 0;
   
   public void Rotulo(){
      this.contadorRotulos = 0;
   }
   public String novoRotulo(){
      String rotulo = "Rot"+this.contadorRotulos;
      this.contadorRotulos++;
      return rotulo;
   }

}