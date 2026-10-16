import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.Scanner;

public class ProgramaHash {

    private Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        ProgramaHash p = new ProgramaHash();
        p.principal();
    }

    public void principal() {
        int opt = 0;
        do {
            System.out.println("\n=== PRÀCTICA 02: CONTROL D'INTEGRITAT I HASH ===");
            System.out.println("(1) Part 1: Càlcul de resums (SHA-256)");
            System.out.println("(2) Part 2: Verificació d'integritat amb fitxers");
            System.out.println("(3) Part 3: Registre d'usuaris amb salt");
            System.out.println("(4) Part 4: Inici de sessió");
            System.out.println("(0) Sortir");
            System.out.print("Tria una opció: ");
            
            try {
                opt = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                opt = -1;
            }

            switch (opt) {
                case 1:
                    part1CalculResums();
                    break;
                case 2:
                    part2IntegritatFitxers();
                    break;
                case 3:
                    part3RegistreUsuari();
                    break;
                case 4:
                    part4IniciSessio();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Please enter a valid option.");
                    break;
            }
        } while (opt != 0);
    }

    public String generarHash(String text) {
        String res = "";
        try {
            byte[] bytesIn = text.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha512 = MessageDigest.getInstance("SHA-256");
            byte[] hashResult = sha512.digest(bytesIn);
            res = HexFormat.of().withUpperCase().formatHex(hashResult);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("(!) Error en l'algorisme de hash: " + e.getMessage());
        }
        return res;
    }

    public String generarSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] saltBytes = new byte[16];
        sr.nextBytes(saltBytes);
        String saltHex = HexFormat.of().withUpperCase().formatHex(saltBytes);
        return saltHex;
    }

    public void part1CalculResums() { }
    public void part2IntegritatFitxers() { }
    public void part3RegistreUsuari() { }
    public void part4IniciSessio() { }
}