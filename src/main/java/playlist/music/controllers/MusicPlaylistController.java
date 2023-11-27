package playlist.music.controllers;



import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import playlist.music.models.Music;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import playlist.music.models.collections.MusicCollection;
import playlist.music.services.Allmusic;
import playlist.music.services.DataSource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public class MusicPlaylistController {

    private boolean status;

    @FXML
    private VBox allmusicVbox;

    @FXML
    private ImageView soundButton;

    @FXML
    private Label artistLabel;

    @FXML
    private VBox musicContainer;

    @FXML
    private ImageView musicImageView;

    @FXML
    private VBox myPlaylistContrainer;

    @FXML
    private VBox myplaylistVbox;

    @FXML
    private ImageView nextButton;

    @FXML
    private ImageView playButton;

    @FXML
    private ImageView preButton;

    @FXML
    private ImageView resetButton;

    @FXML
    private Label songLabel;

    @FXML
    private ProgressBar songProgressBar;

    @FXML
    private Slider volumeSlider;
    private File directory;
    private File[] files;

    private Music currentMusic;

    private MusicCollection myPlaylist;
    private ArrayList<File> songs;
    private int songNumber;

    private Timer timer;
    private TimerTask task;

    private boolean running;

    private boolean page;
    private Image pauseimage,playimage;
    private MusicCollection musics;
    private Media media;
    private MediaPlayer mediaPlayer;

    @FXML
    public void initialize() {
        Allmusic dataSource = new  Allmusic();
        musics = dataSource.readData();

        playimage = new Image(getClass().getResourceAsStream("/images/play.png"));
        pauseimage = new Image(getClass().getResourceAsStream("/images/pause.png"));
        soundButton.setOnMouseClicked((e) ->{
            if(volumeSlider.getValue()>0){
                volumeSlider.setValue(0);
            }else{
                volumeSlider.setValue(100);
            }
        });

        loadAllmusic(musics,musicContainer);
        myPlaylist = new MusicCollection();
        loadMyPlaylist(myPlaylist,myPlaylistContrainer);
        volumeSlider.setValue(100);
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

         @Override
           public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {

                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });

        songProgressBar.setStyle("-fx-accent: #333333;");
        playButton.setOnMouseClicked((e) -> {
            status = !status;
            if (!status){
                playMedia();
                playButton.setImage(pauseimage);
            }
            else {
                pauseMedia();
                playButton.setImage(playimage);
            }

        });
          resetButton.setOnMouseClicked((e) -> {resetMedia();});
          preButton.setOnMouseClicked((e) -> {previousMedia();});
          nextButton.setOnMouseClicked((e) -> {nextMedia();});
//
    }

    public void loadMyPlaylist(MusicCollection musics ,VBox container){
        for(Music music: musics.getmusics()) {
            loadMyMusic(music,container);
        }
    }
    public void loadMyMusic(Music music,VBox container){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/playlist/music/views/music.fxml"));
            HBox musicComponent = fxmlLoader.load();
            MusicController musicController = fxmlLoader.getController();
            musicController.setMusicData(music);
            musicComponent.setOnMouseClicked((e) -> {
                Music exist = musics.findMusicById(music.getId());
                if (exist != null){
                    myPlaylist.removeMusic(exist);
                    container.getChildren().remove(musicComponent);
                    if(exist == currentMusic){
                        nextMedia();
                    }
                }
            });
            container.getChildren().add(musicComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadAllmusic(MusicCollection musics ,VBox container){
        for(Music music: musics.getmusics()) {
            loadMusic(music,container);
        }
    }
    public void loadMusic(Music music,VBox container){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/playlist/music/views/music.fxml"));
            HBox musicComponent = fxmlLoader.load();
            MusicController musicController = fxmlLoader.getController();
            musicController.setMusicData(music);
            musicComponent.setOnMouseClicked((e) -> {
                Music exist = musics.findMusicById(music.getId());
                Music playlistExist = myPlaylist.findMusicById(music.getId());
                if (playlistExist == null){
                    myPlaylist.adMusic(exist);
                    if(myPlaylist.getmusics().size()==1){
                        playMusic(exist);
                        playMedia();
                        currentMusic=exist;
                        playButton.setImage(pauseimage);
                    }
                    loadMyMusic(exist,myPlaylistContrainer);
                }
            });
            container.getChildren().add(musicComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void playMusic(Music music){
        String musicFile = "/music/"+music.getName()+".mp3";
        media = new Media(getClass().getResource(musicFile).toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        artistLabel.setText(music.getArtist());
        songLabel.setText(music.getName());
        Image currentMusicImage = new Image(getClass().getResourceAsStream("/images/"+music.getName()+".png"));
        musicImageView.setImage(currentMusicImage);
    }
    @FXML
    private void onClickViewPlayMusic(){
        myplaylistVbox.setVisible(!page);
        allmusicVbox.setVisible(page);
        page = !page;

    }
    public void playMedia() {

        beginTimer();
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        mediaPlayer.play();
    }

    public void pauseMedia() {

        cancelTimer();
        mediaPlayer.pause();
    }

    public void resetMedia() {

        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    public void previousMedia() {
        playButton.setImage(pauseimage);
        if(songNumber > 0) {

            songNumber--;

            mediaPlayer.stop();

            if(running) {

                cancelTimer();
            }

            currentMusic = myPlaylist.getmusics().get(songNumber);



            playMusic(currentMusic);
            playMedia();
        }
        else {

            songNumber = songs.size() - 1;

            mediaPlayer.stop();

            if(running) {

                cancelTimer();
            }

            currentMusic = myPlaylist.getmusics().get(songNumber);



            playMusic(currentMusic);
            playMedia();
        }
    }

    public void nextMedia() {
        playButton.setImage(pauseimage);
        if(songNumber < myPlaylist.getmusics().size()-1) {

            songNumber++;

            mediaPlayer.stop();

            if(running) {

                cancelTimer();
            }
            currentMusic = myPlaylist.getmusics().get(songNumber);



            playMusic(currentMusic);
            playMedia();
        }
        else {

            songNumber = 0;

            mediaPlayer.stop();

            currentMusic = myPlaylist.getmusics().get(songNumber);



            playMusic(currentMusic);
            playMedia();
        }
    }


    public void beginTimer() {

        timer = new Timer();

        task = new TimerTask() {

            public void run() {

                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                songProgressBar.setProgress(current/end);

                if(current/end == 1) {
                    cancelTimer();
                    nextMedia();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);

    }

    public void cancelTimer() {

        running = false;
        timer.cancel();
    }
}

