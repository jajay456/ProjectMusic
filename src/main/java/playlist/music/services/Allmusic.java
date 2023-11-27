package playlist.music.services;

import playlist.music.models.Music;
import playlist.music.models.collections.MusicCollection;

public class Allmusic implements DataSource<MusicCollection>{

    @Override
    public MusicCollection readData() {
        MusicCollection musics = new MusicCollection();
        Music Janji= new Music("01","Janji","Janji","3.21");
        Music Cartoon= new Music("02","Cartoon","Cartoon","3.49");
        musics.adMusic(Janji);
        musics.adMusic(Cartoon);

        return musics;
    }
}
