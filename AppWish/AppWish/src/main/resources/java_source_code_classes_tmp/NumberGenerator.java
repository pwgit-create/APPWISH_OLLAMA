
import java.util.Random; // Importing Random class for number generation

public class NumberGenerator {

    public static void main(String[] args) {
        Random random = new Random(); // Creating instance of Random class
        int number = random.nextInt(601) + 300; // Generating number between 300 and 901
        System.out.println("Generated number is: " + number);
    }
} 