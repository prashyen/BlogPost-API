package com.csc301.songmicroservice;

import com.csc301.songmicroservice.requests.AddSongRequest;
import com.csc301.songmicroservice.response.AddSongResponse;
import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class SongDalImpl implements SongDal {

  private final MongoTemplate db;

  @Autowired
  public SongDalImpl(MongoTemplate mongoTemplate) {
    this.db = mongoTemplate;
  }

  @Override
  public DbQueryStatus addSong(Song songToAdd) {
    DbQueryStatus dbQueryStatus = null;
    Song addedSong = null;
    Query query = new Query();
    query.addCriteria(Criteria.where(Song.KEY_SONG_NAME).is(songToAdd.getSongName()));
    query.addCriteria(Criteria.where(Song.KEY_SONG_ALBUM).is(songToAdd.getSongAlbum()));
    query.addCriteria(
        Criteria.where(Song.KEY_SONG_ARTIST_FULL_NAME).is(songToAdd.getSongArtistFullName()));

    if (!db.exists(query, Song.class)) {
      addedSong = db.insert(songToAdd);
    } else {
      addedSong = db.findOne(query, Song.class);
    }
    if(addedSong == null){

      return new DbQueryStatus("Failed to add song", DbQueryExecResult.QUERY_ERROR_GENERIC);
    }
    final String uri = "http://localhost:3002/song";
    // setting up the request body
    AddSongRequest addSongRequest = new AddSongRequest();
    addSongRequest.setSongName(addedSong.getSongName());
    addSongRequest.setId(addedSong.getId());

    // request entity is created with request body and headers
    HttpEntity<AddSongRequest> requestEntity = new HttpEntity<>(addSongRequest, null);

    RestTemplate addSongRestTemplate = new RestTemplate();
    ResponseEntity<AddSongResponse> responseEntity =
            addSongRestTemplate.exchange(uri, HttpMethod.POST, requestEntity, AddSongResponse.class);

    if (responseEntity.getStatusCode() != HttpStatus.OK) {
      deleteSongById(addedSong.getId());
      return new DbQueryStatus("AddSong API in Profile microservice failed", DbQueryExecResult.QUERY_ERROR_GENERIC);
    }
    dbQueryStatus = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
    dbQueryStatus.setData(addedSong.getJsonRepresentation());
    return dbQueryStatus;
  }

  @Override
  public DbQueryStatus findSongById(String songId) {
    ObjectId songObjectId = new ObjectId(songId);
    Song songById = db.findById(songObjectId, Song.class);
    if (songById == null) {
      return new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
    }
    DbQueryStatus dbQueryStatus = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
    dbQueryStatus.setData(songById.getJsonRepresentation());
    return dbQueryStatus;
  }

  @Override
  public DbQueryStatus getSongTitleById(String songId) {
    ObjectId songObjectId = new ObjectId(songId);
    Song songById = db.findById(songObjectId, Song.class);
    if (songById == null) {
      return new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
    }
    DbQueryStatus dbQueryStatus = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
    dbQueryStatus.setData(songById.getSongName());
    return dbQueryStatus;
  }

  @Override
  public DbQueryStatus deleteSongById(String songId) {
    ObjectId songObjectId = new ObjectId(songId);
    Song songById = db.findById(songObjectId, Song.class);
    if (songById == null) {
      return new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
    }
    DeleteResult deletedSong = db.remove(songById);
    if (!deletedSong.wasAcknowledged()) {
      DbQueryStatus dbQueryStatus = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_GENERIC);
      return dbQueryStatus;
    }

    final String uri = "http://localhost:3002/deleteAllSongsFromDb/{songId}";

    Map<String, String> params = new HashMap<String, String>();
    params.put("songId", songId);

    RestTemplate restTemplate = new RestTemplate();
    restTemplate.put(uri, null, params);

    DbQueryStatus dbQueryStatus = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
    return dbQueryStatus;
  }

  @Override
  public DbQueryStatus updateSongFavouritesCount(String songId, boolean shouldDecrement) {
    ObjectId songObjectId = new ObjectId(songId);
    Song songById = db.findById(songObjectId, Song.class);
    if (songById == null) {
      return new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
    }
    Update update = new Update();
    if (shouldDecrement) {
      if (songById.getSongAmountFavourites() < 1) {
        return new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_GENERIC);
      }
      update.set("songAmountFavourites", songById.getSongAmountFavourites() - 1);
    } else {
      update.set("songAmountFavourites", songById.getSongAmountFavourites() + 1);
    }
    Query query = new Query();
    query.addCriteria(Criteria.where("_id").is(songById.getId()));
    db.updateFirst(query, update, Song.class);
    DbQueryStatus dbQueryStatus = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
    return dbQueryStatus;
  }
}
