public class Usuari {
    private String nom;
    private String salt;
    private String hashContrasenya;

    public Usuari(String nom, String salt, String hashContrasenya) {
        this.nom = nom;
        this.salt = salt;
        this.hashContrasenya = hashContrasenya;
    }

    public String getNom() {
        return this.nom;
    }

    public String getSalt() {
        return this.salt;
    }

    public String getHashContrasenya() {
        return this.hashContrasenya;
    }
}