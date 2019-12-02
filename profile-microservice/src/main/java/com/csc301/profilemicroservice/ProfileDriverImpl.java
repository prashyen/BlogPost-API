package com.csc301.profilemicroservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import org.springframework.stereotype.Repository;
import org.neo4j.driver.v1.Transaction;

@Repository
public class ProfileDriverImpl implements ProfileDriver {

  Driver driver = ProfileMicroserviceApplication.driver;

  public static void InitProfileDb() {
    String queryStr;

    try (Session session = ProfileMicroserviceApplication.driver.session()) {
      try (Transaction trans = session.beginTransaction()) {
        queryStr = "CREATE CONSTRAINT ON (nProfile:profile) ASSERT exists(nProfile.userName)";
        trans.run(queryStr);

        queryStr = "CREATE CONSTRAINT ON (nProfile:profile) ASSERT exists(nProfile.password)";
        trans.run(queryStr);

        queryStr = "CREATE CONSTRAINT ON (nProfile:profile) ASSERT nProfile.userName IS UNIQUE";
        trans.run(queryStr);

        trans.success();
      }
      session.close();
    }
  }

  @Override
  public DbQueryStatus createUserProfile(String userName, String fullName, String password) {
    DbQueryStatus status = null;
    // ensure userName fullName and password are not blank
    if (userName == null || userName.isEmpty()) {
      status = new DbQueryStatus("The userName cannot be empty",
          DbQueryExecResult.QUERY_ERROR_GENERIC);
    } else if (fullName == null || fullName.isEmpty()) {
      status = new DbQueryStatus("The fullName cannot be empty",
          DbQueryExecResult.QUERY_ERROR_GENERIC);
    } else if (password == null || password.isEmpty()) {
      status = new DbQueryStatus("The password cannot be empty",
          DbQueryExecResult.QUERY_ERROR_GENERIC);
    } else {
      try (Session addProfileSession = driver.session()) {
        // check if user with the given userName already exist
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userName", userName);
        String query = "MATCH (p:profile {userName:{userName}}) return p";
        StatementResult statementResult = addProfileSession.run(query, params);
        if (statementResult.hasNext()) {
          // the user with the given username already exists
          status = new DbQueryStatus("Username already exists",
              DbQueryExecResult.QUERY_ERROR_GENERIC);
        } else {
          // if there is no profile with the given username create it and create a favorites
          // profile and link them together

          params.put("fullName", fullName);
          params.put("password", password);
          params.put("plName", userName + "-favorites");
          query = "CREATE (:profile {userName: {userName}, fullName: {fullName}, password:" +
              " {password}}) -[:created]->(:playlist{plName:{plName}})";
          statementResult = addProfileSession.run(query, params);
          status = new DbQueryStatus("OK",
              DbQueryExecResult.QUERY_OK);
        }
      } catch (Exception e) {
        status = new DbQueryStatus("ERROR",
            DbQueryExecResult.QUERY_ERROR_GENERIC);
      }
    }
    return status;
  }

  @Override
  public DbQueryStatus followFriend(String userName, String frndUserName) {
    // try following a friend
    DbQueryStatus status = null;
    try (Session followSession = driver.session()) {
      // check if both users exist
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("user", userName);
      params.put("friend", frndUserName);
      String query = "MATCH (p:profile {userName:{user}}) return p";
      StatementResult result = followSession.run(query, params);
      if (result.hasNext()) {
        // if the user exists then check if the friend exists
        query = "MATCH (p:profile {userName:{friend}}) return p";
        result = followSession.run(query, params);
        if (result.hasNext()) {
          // if both exist then check if the relation already exists
          query = "MATCH (p:profile{userName:{user}})-[f:follows]->(r:profile{userName:{friend}})" +
              " return r";
          result = followSession.run(query, params);
          if (!result.hasNext()) {
            query = "MATCH (p:profile {userName:{user}}), (r:profile {userName:{friend}})"
                + " CREATE (p)-[:follows]->(r)";
            followSession.run(query, params);
          }
          status = new DbQueryStatus("OK",
              DbQueryExecResult.QUERY_OK);

        } else {
          status = new DbQueryStatus("FRIEND: " + frndUserName + " NOT FOUND",
              DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
        }
      } else {
        status = new DbQueryStatus("USER: " + userName + " NOT FOUND",
            DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
      }
    } catch (Exception e) {
      status = new DbQueryStatus("ERROR",
          DbQueryExecResult.QUERY_ERROR_GENERIC);
    }
    return status;
  }

  @Override
  public DbQueryStatus unfollowFriend(String userName, String frndUserName) {

    return null;
  }

  @Override
  public DbQueryStatus getAllSongFriendsLike(String userName) {

    return null;
  }
}
