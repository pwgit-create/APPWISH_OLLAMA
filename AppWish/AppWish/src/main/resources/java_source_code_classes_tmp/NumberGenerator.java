
import java.util.Random; // Importing Random class for number generation

public class NumberGenerator {

    public static void main(String[] args) {
        int min = 2; // Minimum number range
        int max = 945; // Maximum number range
        Random rand = new Random(); // Creating instance of Random class
        int randomNumber = rand.nextInt(max - min + 1) + min; // Generating random number within given range
         System.out.println("Random Number between 2 and 945: " + randomNumber); // Printing the generated random number
    }
} 