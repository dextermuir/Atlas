public class Main {
  /**
   * always let the program terminate so that the WAB (weights and biases) file doesn't get corrupted, and therefore have to be regenerated
   */
  public static void main(String args[])
  {
    NeuralNetworkManager networkManager = new NeuralNetworkManager();
    networkManager.generateNeuralNetwork();
    FileManager fileManager = new FileManager();
    Game game = new Game(networkManager);
    //game.play();
  }
}
