package com.csc301.profilemicroservice;

import org.neo4j.driver.v1.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PlaylistDriverImpl implements PlaylistDriver {

  Driver driver = ProfileMicroserviceApplication.driver;

  public static void InitPlaylistDb() {
    String queryStr;

    try (Session session = ProfileMicroserviceApplication.driver.session()) {
      try (Transaction trans = session.beginTransaction()) {
        queryStr = "CREATE CONSTRAINT ON (nPlaylist:playlist) ASSERT exists(nPlaylist.plName)";
        trans.run(queryStr);
        trans.success();
      }
      session.close();
    }
  }

  @Override
  public DbQueryStatus likeSong(String userName, String songId) {
    DbQueryStatus status = null;
    // try liking a song
    try (Session likeSongSession = driver.session()) {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("userName", userName);
      params.put("songId", songId);
      // check if the user and the song exist
      String query = "MATCH (p:profile {userName:{userName}}) return p";
      StatementResult result = likeSongSession.run(query, params);
      if (result.hasNext()) {
        // if the user exists check if the song exists
        query = "MATCH (s:song {songId:{songId}}) return s";
        result = likeSongSession.run(query, params);
        if (result.hasNext()) {
          // if the song exists create a relation between the song and the users favorites
          params.put("favorites", userName + "-favorites");
          // check if the relation already exists
          query = "MATCH (p:playlist{plName:{favorites}})-[:includes]->(s:song{songId:{songId}})"
              + "return p,s";
          result = likeSongSession.run(query, params);
          if (!result.hasNext()) {
            // if the realtion doesn't exist create it
            query = "MATCH(p:playlist{plName:{favorites}}), (s:song{songId:{songId}}) CREATE "
                + "(p)-[:includes]->(s)";
            likeSongSession.run(query, params);
          }
          status = new DbQueryStatus("OK",
              DbQueryExecResult.QUERY_OK);
        } else {
          status = new DbQueryStatus("Song not found",
              DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
        }
      } else {
        status = new DbQueryStatus("User not found",
            DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
      }
    } catch (Exception e) {
      status = new DbQueryStatus("ERROR",
          DbQueryExecResult.QUERY_ERROR_GENERIC);
    }
    return status;
  }

  @Override
  public DbQueryStatus unlikeSong(String userName, String songId) {
    DbQueryStatus status = null;
    // try unliking a song
    try (Session unlikeSession = driver.session()) {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("favorite", userName + "-favorites");
      params.put("songId", songId);
      String query = "MATCH (p:playlist{plName: {favorite}})-[:includes]->(:song{songId:{songId}}) return p";
      StatementResult result = unlikeSession.run(query, params);
      if (result.hasNext()) {
        // if the relationship exists delete it
        query = "MATCH (:playlist{plName: {favorite}})-[i:includes]->(:song{songId:{songId}}) DELETE i";
        unlikeSession.run(query, params);
        status = new DbQueryStatus("OK",
            DbQueryExecResult.QUERY_OK);
      } else {
        // if the relationship wasn't found output and error
        status = new DbQueryStatus(
            "User: " + userName + " does not like the song with the id: " + songId,
            DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
      }
    } catch (Exception e) {

      status = new DbQueryStatus("ERROR",
          DbQueryExecResult.QUERY_ERROR_GENERIC);
    }
    return status;
  }

  @Override
  public DbQueryStatus deleteSongFromDb(String songId) {
    DbQueryStatus dbQueryStatus = null;
    try (Session deleteSongSession = driver.session()) {
      Record result;
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("songId", songId);
      String query = "MATCH (n:song { songId: {songId} }) DETACH DELETE n";
      StatementResult statementResult = deleteSongSession.run(query, params);
      dbQueryStatus = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
    } catch (Exception e) {
      dbQueryStatus = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_GENERIC);
    }
    return dbQueryStatus;
  }
}
