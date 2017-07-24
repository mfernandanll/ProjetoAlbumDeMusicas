package com.example.fernanda.albumdemusicas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Fernanda on 23/07/2017.
 */

public class AlbumModels {
    @SerializedName("title")
    private String collectionTitle;
    @SerializedName("author")
    private String author;
    @SerializedName("coverUrl")
    private String cover;
    @SerializedName("followers")
    private String followers;
    @SerializedName("songs")
    private List<Songs> songsList;

    public String getCollectionTitle() {
        return collectionTitle;
    }

    public void setCollectionTitle(String collectionTitle) {
        this.collectionTitle = collectionTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public List<Songs> getSongsList() {
        return songsList;
    }

    public void setSongsList(List<Songs> songsList) {
        this.songsList = songsList;
    }

    public static class Songs {
        @SerializedName("title")
        private String musicTitle;
        @SerializedName("artist")
        private String artist;
        @SerializedName("album")
        private String album;
        @SerializedName("duration")
        private String duration;
        @SerializedName("songs")
        private String rating;

        public String getMusicTitle() {
            return musicTitle;
        }

        public void setMusicTitle(String musicTitle) {
            this.musicTitle = musicTitle;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }
    }
}
