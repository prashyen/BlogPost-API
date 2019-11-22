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

    return null;
  }

  @Override
  public DbQueryStatus unlikeSong(String userName, String songId) {

    return null;
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
