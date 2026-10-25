import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.Scanner;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProgramaHash {

    private java.util.ArrayList<Usuari> usuaris = new java.util.ArrayList<>();
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

    public void part1CalculResums() {
        System.out.println("\n--- PART 1: Càlcul de resums ---");
        System.out.print("Entra un missatge: ");
        String msg1 = sc.nextLine();
        String hash1 = generarHash(msg1);
        System.out.println("Hash SHA-256: " + hash1);

        System.out.print("\nEntra un segon missatge per comparar: ");
        String msg2 = sc.nextLine();
        String hash2 = generarHash(msg2);
        System.out.println("Hash 2 SHA-256: " + hash2);

        if (hash1.equals(hash2)) {
            System.out.println("RESULTAT: Els dos hashes coincideixen exactament.");
        } else {
            System.out.println("RESULTAT: Els hashes són diferents.");
        }
    }
    
    public void part2IntegritatFitxers() {
        System.out.println("\n--- PART 2: Verificació d'integritat ---");
        try {
            // 1. Demanar el missatge a l'emissor
            System.out.print("Entra el contingut del missatge original: ");
            String textOriginal = sc.nextLine();

            // Guardar text a missatge.txt
            FileWriter fwTxt = new FileWriter("missatge.txt");
            fwTxt.write(textOriginal);
            fwTxt.close();

            // 2. Calcular i guardar hash a missatge.hash
            String hashOriginal = generarHash(textOriginal);
            FileWriter fwHash = new FileWriter("missatge.hash");
            fwHash.write(hashOriginal);
            fwHash.close();

            System.out.println("[+] Fitxers 'missatge.txt' i 'missatge.hash' creats correctament.");

            // Crear un fitxer modificat de prova 'missatgeCanviat.txt'
            FileWriter fwCanviat = new FileWriter("missatgeCanviat.txt");
            fwCanviat.write(textOriginal + " (modificat)");
            fwCanviat.close();

            // 3 i 4. Simulació de recepció
            System.out.print("\nEscriu el nom del fitxer a llegir (ex: 'missatge.txt' o 'missatgeCanviat.txt'): ");
            String nomFitxer = sc.nextLine();

            // Llegir contingut del fitxer indicat
            String contingutLlegit = new String(Files.readAllBytes(Paths.get(nomFitxer)), StandardCharsets.UTF_8);
            System.out.println("Contingut llegit del fitxer: " + contingutLlegit);

            // 5 i 6. Calcular hash del contingut llegit i comparar amb l'original
            String hashLlegit = generarHash(contingutLlegit);
            String hashEsperat = new String(Files.readAllBytes(Paths.get("missatge.hash")), StandardCharsets.UTF_8).trim();

            System.out.println("Hash esperat (original): " + hashEsperat);
            System.out.println("Hash calculat del fitxer: " + hashLlegit);

            if (hashLlegit.equals(hashEsperat)) {
                System.out.println("[+] INTEGRITAT OK] Els dos hashes coincideixen! El missatge no s'ha modificat.");
            } else {
                System.out.println("(!) S'ha detectat una modificació en el missatge!");
            }

        } catch (Exception e) {
            System.out.println("(!) Error durant la gestió de fitxers: " + e.getMessage());
        }
    }

    public void part3RegistreUsuari() {
        System.out.println("\n--- PART 3: Registre d'usuari ---");
        System.out.print("Nom d'usuari: ");
        String nom = sc.nextLine();

        System.out.print("Contrasenya: ");
        String pass = sc.nextLine();

        // Generar salt aleatòria
        String salt = generarSalt();

        // Hash de contrasenya + salt
        String hashAmbSalt = generarHash(pass + salt);

        // Emmagatzemar usuari a la llista
        Usuari nouUsuari = new Usuari(nom, salt, hashAmbSalt);
        usuaris.add(nouUsuari);

        System.out.println("\nUsuari registrat amb èxit!");
        System.out.println("Salt generat: " + salt);
        System.out.println("Hash emmagatzemat: " + hashAmbSalt);
    }

    public void part4IniciSessio() {
        System.out.println("\n--- PART 4: Inici de sessió ---");
        System.out.print("Nom d'usuari: ");
        String nom = sc.nextLine();

        System.out.print("Contrasenya: ");
        String pass = sc.nextLine();

        // Cercar usuari registrat
        Usuari usuariTrobat = null;
        int i = 0;
        while (i < usuaris.size() && usuariTrobat == null) {
            if (usuaris.get(i).getNom().equalsIgnoreCase(nom)) {
                usuariTrobat = usuaris.get(i);
            } else {
                i++;
            }
        }

        if (usuariTrobat == null) {
            System.out.println("(!) Error: L'usuari no existeix.");
        } else {
            // Recuperar la salt de l'usuari i recalcular el hash
            String saltGuardat = usuariTrobat.getSalt();
            String hashCalculat = generarHash(pass + saltGuardat);

            if (hashCalculat.equals(usuariTrobat.getHashContrasenya())) {
                System.out.println("\n[+] Accés permès! Benvingut/da, " + nom + ".");
            } else {
                System.out.println("\n(!) Error: Contrasenya incorrecta.");
            }
        }
    }
}