package com.csc301.profilemicroservice;

import com.csc301.profilemicroservice.requests.AddSongRequest;
import javax.rmi.CORBA.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.csc301.profilemicroservice.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class ProfileController {

  public static final String KEY_USER_NAME = "userName";
  public static final String KEY_USER_FULLNAME = "fullName";
  public static final String KEY_USER_PASSWORD = "password";

  @Autowired
  private final ProfileDriverImpl profileDriver;

  @Autowired
  private final PlaylistDriverImpl playlistDriver;

  OkHttpClient client = new OkHttpClient();

  public ProfileController(ProfileDriverImpl profileDriver, PlaylistDriverImpl playlistDriver) {
    this.profileDriver = profileDriver;
    this.playlistDriver = playlistDriver;
  }

  /**
   * /profile route has a single endpoint, POST used to add a profile to the database with userName,
   * fullName and password
   */
  @RequestMapping(value = "/profile", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> addSong(@RequestParam Map<String, String> params,
      HttpServletRequest request) {

    DbQueryStatus status = profileDriver
        .createUserProfile(params.get("userName"), params.get("fullName"), params.get("password"));
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("POST %s", Utils.getUrl(request)));
    response.put("message", status.getMessage());
    Utils.setResponseStatus(response, status.getdbQueryExecResult(), status.getData());

    return response;
  }

  /**
   * /followFriend/{userName}/{friendUserName} route has a single endpoint, PUT used to add a
   * follows relationship from profiles with userName to friendUserName
   */
  @RequestMapping(value = "/followFriend/{userName}/{friendUserName}", method = RequestMethod.PUT)
  public @ResponseBody
  Map<String, Object> followFriend(@PathVariable("userName") String userName,
      @PathVariable("friendUserName") String friendUserName, HttpServletRequest request) {

    DbQueryStatus status = profileDriver.followFriend(userName, friendUserName);
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("POST %s", Utils.getUrl(request)));
    response.put("message", status.getMessage());
    Utils.setResponseStatus(response, status.getdbQueryExecResult(), status.getData());

    return response;
  }

  /**
   * /getAllFriendFavouriteSongTitles/{userName} route has a single endpoint, GET used to retrieve
   * all the users the given user follows and all their favorite songs
   */
  @RequestMapping(value = "/getAllFriendFavouriteSongTitles/{userName}", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getAllFriendFavouriteSongTitles(@PathVariable("userName") String userName,
      HttpServletRequest request) {

    DbQueryStatus status = profileDriver.getAllSongFriendsLike(userName);
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("POST %s", Utils.getUrl(request)));
    response.put("message", status.getMessage());
    Utils.setResponseStatus(response, status.getdbQueryExecResult(), status.getData());

    return response;
  }


  /**
   * /unfollowFriend/{userName}/{friendUserName} route has a single endpoint, PUT used to remove a
   * follows relationship from profiles with userName to friendUserName
   */
  @RequestMapping(value = "/unfollowFriend/{userName}/{friendUserName}", method = RequestMethod.PUT)
  public @ResponseBody
  Map<String, Object> unfollowFriend(@PathVariable("userName") String userName,
      @PathVariable("friendUserName") String friendUserName, HttpServletRequest request) {

    DbQueryStatus status = profileDriver
        .unfollowFriend(userName, friendUserName);
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("POST %s", Utils.getUrl(request)));
    response.put("message", status.getMessage());
    Utils.setResponseStatus(response, status.getdbQueryExecResult(), status.getData());

    return response;
  }

  /**
   * /likeSong/{userName}/{songId} route has a single endpoint, PUT used to add a contains
   * relationship from username-favorites playlist to the song node with songID
   */
  @RequestMapping(value = "/likeSong/{userName}/{songId}", method = RequestMethod.PUT)
  public @ResponseBody
  Map<String, Object> likeSong(@PathVariable("userName") String userName,
      @PathVariable("songId") String songId, HttpServletRequest request) {

    DbQueryStatus status = playlistDriver.likeSong(userName, songId);
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("POST %s", Utils.getUrl(request)));
    response.put("message", status.getMessage());
    Utils.setResponseStatus(response, status.getdbQueryExecResult(), status.getData());

    return response;
  }

  /**
   * /unlikeSong/{userName}/{songId} route has a single endpoint, PUT used to remove a contains
   * relationship from username-favorites playlist to the song node with songID
   */
  @RequestMapping(value = "/unlikeSong/{userName}/{songId}", method = RequestMethod.PUT)
  public @ResponseBody
  Map<String, Object> unlikeSong(@PathVariable("userName") String userName,
      @PathVariable("songId") String songId, HttpServletRequest request) {

    DbQueryStatus status = playlistDriver.unlikeSong(userName, songId);
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("POST %s", Utils.getUrl(request)));
    response.put("message", status.getMessage());
    Utils.setResponseStatus(response, status.getdbQueryExecResult(), status.getData());

    return response;
  }

  /**
   * /deleteAllSongsFromDb/{songId} route has a single endpoint, PUT used to delete the song node
   * with the songID
   */
  @RequestMapping(value = "/deleteAllSongsFromDb/{songId}", method = RequestMethod.PUT)
  public @ResponseBody
  Map<String, Object> deleteAllSongsFromDb(@PathVariable("songId") String songId,
      HttpServletRequest request) {

    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("PUT %s", Utils.getUrl(request)));
    DbQueryStatus dbQueryStatus = playlistDriver.deleteSongFromDb(songId);
    response =
        Utils.setResponseStatus(
            response, dbQueryStatus.getdbQueryExecResult(), dbQueryStatus.getData());
    return response;
  }

  /**
   * /song route has a single endpoint, POST used to add a song node to the datbase with properties
   * songID and songName
   */
  @RequestMapping(value = "/song", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> addSongNode(@RequestBody AddSongRequest requestBody,
      HttpServletRequest request) {
    DbQueryStatus status = profileDriver.addSong(requestBody.getId(), requestBody.getSongName());
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("POST %s", Utils.getUrl(request)));
    response.put("message", status.getMessage());
    Utils.setResponseStatus(response, status.getdbQueryExecResult(), status.getData());

    return response;
  }
}