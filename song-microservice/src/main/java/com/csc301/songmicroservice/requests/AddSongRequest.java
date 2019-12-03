package com.csc301.songmicroservice.requests;

public class AddSongRequest {

  private String songName;

  private String id;

  public String getSongName() {
    return songName;
  }

  public void setSongName(String songName) {
    this.songName = songName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
