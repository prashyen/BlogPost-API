package com.csc301.songmicroservice.response;

public class AddSongResponse {
  public String path;
  public String status;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
