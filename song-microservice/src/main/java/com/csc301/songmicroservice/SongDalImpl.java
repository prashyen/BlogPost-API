package com.csc301.songmicroservice;

import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Update;
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
    Query query = new Query();
    query.addCriteria(Criteria.where(Song.KEY_SONG_NAME).is(songToAdd.getSongName()));
    query.addCriteria(Criteria.where(Song.KEY_SONG_ALBUM).is(songToAdd.getSongAlbum()));
    query.addCriteria(
        Criteria.where(Song.KEY_SONG_ARTIST_FULL_NAME).is(songToAdd.getSongArtistFullName()));
    if (!db.exists(query, Song.class)) {
      Song addedSong = db.insert(songToAdd);
      dbQueryStatus = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
      dbQueryStatus.setData(addedSong.getJsonRepresentation());
    }

    dbQueryStatus = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
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
    // TODO Auto-generated method stub
    return null;
  }
}
