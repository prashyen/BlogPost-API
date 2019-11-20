package com.csc301.songmicroservice;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class SongDalImpl implements SongDal {

  private final MongoTemplate db;

  @Autowired
  public SongDalImpl(MongoTemplate mongoTemplate) {
    this.db = mongoTemplate;
  }

  @Override
  public DbQueryStatus addSong(Song songToAdd) {
    Song addedSong = db.insert(songToAdd);
    DbQueryStatus dbQueryStatus = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
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
    dbQueryStatus.setData(songById.getSongName());
    return dbQueryStatus;
  }

  @Override
  public DbQueryStatus getSongTitleById(String songId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DbQueryStatus deleteSongById(String songId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DbQueryStatus updateSongFavouritesCount(String songId, boolean shouldDecrement) {
    // TODO Auto-generated method stub
    return null;
  }
}
