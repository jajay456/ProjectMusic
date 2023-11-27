package playlist.music.models.collections;

import playlist.music.models.Music;

import java.util.ArrayList;

public class MusicCollection {
    private ArrayList<Music> musics;

    public MusicCollection() {
        musics = new ArrayList<>();
    }

    public void adMusic(String id, String name, String artist,String time){
        musics.add(new Music(id, name, artist,time));

    }

    public void adMusic(Music music){
        musics.add( music);

    }
    public void removeMusic(Music music){
        musics.remove( music);

    }
    public Music findMusicById(String musicId){
        for(Music exist: musics){
            if(musicId.equals(exist.getId())){
                return exist;
            }
        }
        return null;
    }
    public ArrayList<Music> getmusics() {
        return musics;
    }

}
