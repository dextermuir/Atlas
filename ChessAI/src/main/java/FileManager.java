import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {
  private Logger logger = Logger.getLogger(Main.class.getName());
  private String biasAndWeightA = "src/biasAndWeightA.txt";
  private String biasAndWeightB = "src/biasAndWeightB.txt";
  private String biasAndWeightC = "src/biasAndWeightC.txt";
  private BufferedWriter bWriter;
  private BufferedReader bReader;

  /**
   * @return the contents of the weights file as a stream of doubles
   */

  public double[] readValues()
  {
    double[] arrayOfWeights = new double[NeuralNetworkManager.LAYER_ARRAY_SIZE +
        NeuralNetworkManager.LAYER_ARRAY_SIZE + NeuralNetworkManager.OUTPUT_ARRAY_SIZE +
        NeuralNetworkManager.LAYER_ARRAY_SIZE * NeuralNetworkManager.INPUT_ARRAY_SIZE +
        NeuralNetworkManager.LAYER_ARRAY_SIZE * NeuralNetworkManager.LAYER_ARRAY_SIZE +
        NeuralNetworkManager.OUTPUT_ARRAY_SIZE * NeuralNetworkManager.LAYER_ARRAY_SIZE];
    try {
      //Load contents of file into file contents string
      int tempInt;
      String fileContents = "";
      bReader = new BufferedReader(new FileReader(biasAndWeightA));
      while ((tempInt = bReader.read()) != -1)
      {
        fileContents += (char) tempInt;
      }
      bReader = new BufferedReader(new FileReader(biasAndWeightB));
      while ((tempInt = bReader.read()) != -1)
      {
        fileContents += (char) tempInt;
      }
      bReader = new BufferedReader(new FileReader(biasAndWeightC));
      while ((tempInt = bReader.read()) != -1)
      {
        fileContents += (char) tempInt;
      }
      bReader.close();
      logger.log(Level.INFO, "file read");

      //String manipulation -> remove fluff -> separate the string into an array -> cast string array to double array
      fileContents = fileContents.replace("biasA", "");
      fileContents = fileContents.replace("biasB", "");
      fileContents = fileContents.replace("biasC", "");
      fileContents = fileContents.replace("matrixA", "");
      fileContents = fileContents.replace("matrixB", "");
      fileContents = fileContents.replace("matrixC", "");
      fileContents = fileContents.replace("\n", "");
      String[] splitString = fileContents.split(" ");

      System.out.println("split sting length: " + splitString.length + "; array of weights length: " + arrayOfWeights.length);
      if(splitString.length != arrayOfWeights.length)
      {
        logger.log(Level.SEVERE, "weights file corrupted.");
      }
      for(int i = 0; i < splitString.length; i++)
      {
        arrayOfWeights[i] = Double.parseDouble(splitString[i]);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return arrayOfWeights;
  }
  public void editValues(double[] biasVectorA, double[] biasVectorB, double[] biasVectorC, double[][] weightMatrixA, double[][] weightMatrixB, double[][] weightMatrixC)
  {
    try {
      //Load in the biases
      bWriter = new BufferedWriter(new FileWriter(biasAndWeightA));
      bWriter.write("biasA\n");
      for(int i = 0; i < NeuralNetworkManager.LAYER_ARRAY_SIZE; i++)
      {
        bWriter.write(biasVectorA[i] + " ");
      }
      bWriter.write("\nmatrixA");
      for(int i = 0; i < NeuralNetworkManager.LAYER_ARRAY_SIZE; i++)
      {
        bWriter.write("\n");
        for(int j = 0; j < NeuralNetworkManager.INPUT_ARRAY_SIZE; j++)
        {
          bWriter.write(weightMatrixA[i][j] + " ");
        }
      }
      bWriter.close();
      bWriter = new BufferedWriter(new FileWriter(biasAndWeightB));
      bWriter.write("biasB\n");
      for(int i = 0; i < NeuralNetworkManager.LAYER_ARRAY_SIZE; i++)
      {
        bWriter.write(biasVectorB[i] + " ");
      }
      bWriter.write("\nmatrixB");
      for(int i = 0; i < NeuralNetworkManager.LAYER_ARRAY_SIZE; i++)
      {
        bWriter.write("\n");
        for(int j = 0; j < NeuralNetworkManager.LAYER_ARRAY_SIZE; j++)
        {
          bWriter.write(weightMatrixB[i][j] + " ");
        }
      }
      bWriter.close();
      bWriter = new BufferedWriter(new FileWriter(biasAndWeightC));
      bWriter.write("biasC\n");
      for(int i = 0; i < NeuralNetworkManager.OUTPUT_ARRAY_SIZE; i++)
      {
        bWriter.write(biasVectorC[i] + " ");
      }
      bWriter.write("\nmatrixC");
      for(int i = 0; i < NeuralNetworkManager.OUTPUT_ARRAY_SIZE; i++)
      {
        bWriter.write("\n");
        for(int j = 0; j < NeuralNetworkManager.LAYER_ARRAY_SIZE; j++)
        {
          bWriter.write(weightMatrixC[i][j] + " ");
        }
      }
      bWriter.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
