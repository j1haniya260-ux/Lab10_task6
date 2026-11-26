import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Scanner;

public class BankLookup {

    private static final String NBP_URL = "https://ewib.nbp.pl/plewibnra?dokNazwa=plewibnra.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the first three digits of bank account: ");
        String prefix = scanner.nextLine().trim();

        if (!prefix.matches("\\d{3}")) {
            System.out.println("Please enter exactly three digits (e.g., 105).");
            return;
        }

        Charset charset = Charset.forName("windows-1250");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(URI.create(NBP_URL).toURL().openStream(), charset))) {

            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\t+");
                if (parts.length >= 2) {
                    String shortBankCode = parts[0].trim();
                    String bankName = parts[1].trim();

                    if (shortBankCode.equals(prefix)) {
                        System.out.println("Bank number: " + shortBankCode + " | Bank name: " + bankName);
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                System.out.println("No bank found for prefix: " + prefix);
            }

        } catch (IOException e) {
            System.out.println("Error loading bank data from NBP: " + e.getMessage());
        }
    }
}
