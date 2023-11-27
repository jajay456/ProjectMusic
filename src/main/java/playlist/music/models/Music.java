package playlist.music.models;

public class Music {
    private String id;
    private String name;
    private String artist;
    private String time;

    public Music(String id, String name, String artist,String time) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
