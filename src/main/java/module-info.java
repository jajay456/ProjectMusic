module playlist.music.projectmusicplaylist {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens playlist.music.projectmusicplaylist to javafx.fxml;
    exports playlist.music.projectmusicplaylist;
    exports playlist.music.controllers;
    opens playlist.music.controllers to javafx.fxml, javafx.media;
    exports playlist.music.models;
    opens playlist.music.models to javafx.base;
}