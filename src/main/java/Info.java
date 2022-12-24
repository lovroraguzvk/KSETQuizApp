public class Info implements Comparable<Info> {
    private final String name;
    private final double score;
    private final String mail;


    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public String getMail() {
        return mail;
    }

    public Info(String name, String mail, double score) {
        this.name = name;
        this.mail = mail;
        this.score = score;
    }

    @Override
    public int compareTo(Info o) {
        return Double.compare(this.score, o.score);
    }
}
