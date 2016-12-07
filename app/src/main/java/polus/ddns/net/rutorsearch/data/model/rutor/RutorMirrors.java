package polus.ddns.net.rutorsearch.data.model.rutor;

/**
 * Created by Игорь on 05.12.2016.
 */

public enum RutorMirrors {
    rutor2("http://rutor.info"),
    rutor1("http://rus-film.org"),
    rutor3("http://rutor.is");

    private final String name;

    RutorMirrors(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
