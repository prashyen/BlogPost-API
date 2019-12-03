package com.csc301.profilemicroservice.requests;

import javax.validation.constraints.NotEmpty;

public class AddSongRequest {

  @NotEmpty
  private String songName;

  @NotEmpty
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
