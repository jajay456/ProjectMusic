package playlist.music.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import playlist.music.models.Music;

public class MusicController {
    @FXML
    private Label artistLabel;

    @FXML
    private Label songLabel;

    @FXML
    private ImageView songimageview;

    @FXML
    private Label timeLabel;
    public void setMusicData(Music music){
        artistLabel.setText(music.getArtist());
        songLabel.setText(music.getName());
        timeLabel.setText(music.getTime());
        Image musicImage = new Image(getClass().getResourceAsStream(("/images/"+music.getName()+".png")));
        songimageview.setImage(musicImage);
    }
}
