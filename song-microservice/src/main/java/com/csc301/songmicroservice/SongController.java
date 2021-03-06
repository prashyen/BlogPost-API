package com.csc301.songmicroservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class SongController {

  @Autowired
  private final SongDal songDal;

  private OkHttpClient client = new OkHttpClient();

  public SongController(SongDal songDal) {
    this.songDal = songDal;
  }

  /**
   * /getSongById/{songId} route has a single endpoint, GET used to retrieve all information about
   * the song using the song id
   */
  @RequestMapping(value = "/getSongById/{songId}", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getSongById(
      @PathVariable("songId") String songId, HttpServletRequest request) {

    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("GET %s", Utils.getUrl(request)));

    DbQueryStatus dbQueryStatus = songDal.findSongById(songId);
    response =
        Utils.setResponseStatus(
            response, dbQueryStatus.getdbQueryExecResult(), dbQueryStatus.getData());

    return response;
  }

  /**
   * /getSongTitleById/{songId} route has a single endpoint, GET used to retrieve a song name using
   * the song id
   */
  @RequestMapping(value = "/getSongTitleById/{songId}", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getSongTitleById(
      @PathVariable("songId") String songId, HttpServletRequest request) {

    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("GET %s", Utils.getUrl(request)));

    DbQueryStatus dbQueryStatus = songDal.getSongTitleById(songId);
    response =
        Utils.setResponseStatus(
            response, dbQueryStatus.getdbQueryExecResult(), dbQueryStatus.getData());

    return response;
  }

  /**
   * /deleteSongById/{songId} route has a single endpoint, Delete used to remove a song given the
   * song id
   */
  @RequestMapping(value = "/deleteSongById/{songId}", method = RequestMethod.DELETE)
  public @ResponseBody
  Map<String, Object> deleteSongById(
      @PathVariable("songId") String songId, HttpServletRequest request) {

    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("DELETE %s", Utils.getUrl(request)));
    DbQueryStatus dbQueryStatus = songDal.deleteSongById(songId);
    response =
        Utils.setResponseStatus(
            response, dbQueryStatus.getdbQueryExecResult(), dbQueryStatus.getData());
    return response;
  }

  /**
   * /addSong route has a single endpoint, Post used to add a song given the song name, artist and
   * album
   */
  @RequestMapping(value = "/addSong", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> addSong(
      @RequestParam Map<String, String> params, HttpServletRequest request) {

    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("POST %s", Utils.getUrl(request)));

    Song songToAdd =
        new Song(params.get("songName"), params.get("songArtistFullName"), params.get("songAlbum"));

    DbQueryStatus dbQueryStatus = songDal.addSong(songToAdd);
    response =
        Utils.setResponseStatus(
            response, dbQueryStatus.getdbQueryExecResult(), dbQueryStatus.getData());

    return response;
  }

  /**
   * /updateSongFavouritesCount/{songId} route has a single endpoint, Put used to update hte
   * favourite count of a song given the song id
   */
  @RequestMapping(value = "/updateSongFavouritesCount/{songId}", method = RequestMethod.PUT)
  public @ResponseBody
  Map<String, Object> updateFavouritesCount(
      @PathVariable("songId") String songId,
      @RequestParam("shouldDecrement") String shouldDecrement,
      HttpServletRequest request) {

    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("PUT %s", Utils.getUrl(request)));
    DbQueryStatus dbQueryStatus =
        songDal.updateSongFavouritesCount(songId, Boolean.parseBoolean(shouldDecrement));
    response =
        Utils.setResponseStatus(
            response, dbQueryStatus.getdbQueryExecResult(), dbQueryStatus.getData());
    return response;
  }
}
