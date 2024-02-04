
import java.util.Random;

public class NameGenerator {

    public static void main(String[] args) {
        String[] firstNames = {"Alice", "Andrew", "Ava", "Aaron", "Abigail"};
        Random random = new Random();
        int index = random.nextInt(firstNames.length);
        String name = firstNames[index];
         String lastName = "Westin"; // Added code to store last name as "Westin"
         System.out.println("Hi " + name + " " + lastName + "!");
         String newName = generateNewName(name, lastName); // Added method call for generating a new name
         System.out.println("Name generated: " + newName);
    }

    public static String generateNewName(String firstName, String lastName) {
        String[] prefixes = {"Mr.", "Mrs.", "Ms.", "Dr."}; // Array of title prefixes
        Random random = new Random();
        int index = random.nextInt(prefixes.length);
         String prefix = prefixes[index]; // Select a random prefix
         return prefix + ", " + firstName + " " + lastName; // Return the generated name with a random prefix
    }
} 