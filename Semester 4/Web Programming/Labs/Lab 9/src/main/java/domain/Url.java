package domain;

public class Url {
    private final int id;
    private final int userId;
    private final String url;

    public Url(int id, int userId, String url) {
        this.id = id;
        this.userId = userId;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Url{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
