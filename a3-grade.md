# assignment 3

**total grade**: 48.0/54.0

---

## automarker test results

Running auto-marker
{
    "Add Song with additional unexpected param, unexpected param: unexepectedParam: 12345": "Test Passed!",
    "Add Song with all invalid param keys, param keys: inValidSongName:songName1 invalidSongArtistFullName:songArtistFullName1 invalidSongAlbum:songAlbum": "Test Failed! Response status did not match expected response status",
    "Add Song with all valid paramaters, params: songName:songName1 songArtistFullName:songArtistFullName1 songAlbum:songAlbum": "Test Failed! Song data recieved differs from the song data added to the DB",
    "Add Song with one mandatory missing paramater, missing param: songArtistFullName": "Test Failed! Response status did not match expected response status",
    "Calling /followFriend as non-existing userName 'user1' to follow user with userName 'tahmid'": "Test Passed!",
    "Calling /followFriend as userName 'user1' to follow a non-existing user with userName 'non-existing-user'": "Test Passed!",
    "Calling /followFriend as userName 'user1' to follow friend with userName 'ilir'": "Test Passed!",
    "Calling /followFriend as userName 'user1' to follow friend with userName 'shabaz'": "Test Passed!",
    "Calling /followFriend as userName 'user1' to follow friend with userName 'tahmid'": "Test Passed!",
    "Calling /followFriend with missing param 'friendUserName'": "Test Passed!",
    "Calling /getAllFriendFavouriteSongTitles to get songs of user with userName 'user1' friends likes": "Test Failed! Expected response OK",
    "Calling /unfollowFriend as userName 'user1' to unfollow friend with userName 'ilir'": "Test Passed!",
    "Calling rount /profile with missing params 'fullName'": "Test Passed!",
    "Calling route /profile to add user with userName 'user1'": "Test Passed!",
    "Checking DB to check if all nodes remains as expected after creating user": "Test Passed!",
    "Checking DB to check if all nodes remains as expected after follow": "Test Passed!",
    "Checking DB to check if all nodes remains as expected after unfollow": "Test Passed!",
    "Checking DB to see if user 'user1' was correctly followed users with userNames 'ilir', 'tahmid' and 'shabaz'": "Test Passed!",
    "Checking DB to see if user 'user1' was correctly unfollowed users with userNames 'shabaz'": "Test Passed!",
    "Checking DB to see if user1 actually liked songId 5d620f54d78b833e34e65b46 and 5d620f54d78b833e34e65b47": "Test Failed! DB data does not match expected data",
    "Checking DB to see if user1 actually unliked songId 5d620f54d78b833e34e65b46 and 5d620f54d78b833e34e65b47": "Test Passed!",
    "Checking if the follow was one directional. Only 'user1' followed 'ilir' and not the other way around": "Test Passed!",
    "Checking if the unfollow was one directional. Only 'user1' unfollowed 'shabaz' and not the other way around": "Test Passed!",
    "Checking returned data after calling /getAllFriendFavouriteSongTitles": "Could not run test. Something went horribly wrong!",
    "Decrementing favourites count below 0 for a valid songId, id=5dfb1a1a1cccaee29c0b68e4": "Test Passed!",
    "Decrementing favourites count for a songId that does not exist, id=000000000000000000000000": "Test Passed!",
    "Decrementing favourites count for a valid songId, id=5d61728193528481fe5a3122": "Test Passed!",
    "Deleting song by id that does not exist in the DB, id=000000000000000000000000": "Test Passed!",
    "Deleting song by id that exists in the DB, id=5d61728193528481fe5a3122": "Test Passed!",
    "Getting song by id that does not exist in the DB, id=000000000000000000000000": "Test Passed!",
    "Getting song by id that exists in the DB, id=5dfb1a1a1cccaee29c0b68e3": "Test Passed!",
    "Getting song title by id that does not exist in the DB, id=000000000000000000000000": "Test Passed!",
    "Getting song title by id that exists in the DB, id=5d61728193528481fe5a3122": "Test Passed!",
    "Incrementing favourites count for a songId that does not exist, id=000000000000000000000000": "Test Passed!",
    "Incrementing favourites count for a songId which exists, but providing invalid ?shouldDecrement param, ?shouldDecrement=gibberish!, songId=5d61728193528481fe5a3122": "Test Failed! Response status did not match expected response status",
    "Incrementing favourites count for a valid songId id=5d61728193528481fe5a3122": "Test Passed!",
    "calling /getAllFriendFavouriteSongTitles to get songs user with userName 'ilir' likes'": "Could not run test. Something went horribly wrong!",
    "calling /likesong with user1 to like songId 5d620f54d78b833e34e65b46 to see if correct response type is returned": "Test Failed! Expected response OK",
    "calling /likesong with user1 to like songId 5d620f54d78b833e34e65b47 to see if correct response type is returned": "Test Failed! Expected response OK",
    "calling /unlikeSong with user1 to unlike songId 5d620f54d78b833e34e65b46": "Test Failed! Expected response OK",
    "calling /unlikeSong with user1 to unlike songId 5d620f54d78b833e34e65b47": "Test Failed! Expected response OK",
    "checking song 5d620f54d78b833e34e65b46 in DB to see if the favorite counter is decremented by 1": "Test Passed!",
    "checking song DB to see if the favorite counter is decremented for songId 5d620f54d78b833e34e65b47": "Test Passed!",
    "checking song DB to see if the favorite counter is incremented for song with id: 5d620f54d78b833e34e65b46": "Test Failed! Expected favorite counter to be 45",
    "checking song DB to see if the favorite counter is incremented for songId 5d620f54d78b833e34e65b47": "Test Failed! Expected favorite counter to be 48"
}

**automarker grade**: 42.0/48.0

---

## code style

git usage: 2.0/2.0

- proper git usage

---

code style: 4.0/4.0

- good code style
