from protorpc import messages

class AccountRequest(messages.Message):
    """ProtoRPC message definition to represent a new user query""" 
    #TODO: Add user authentication
    username = messages.StringField(1)
    email = messages.StringField(2)

class AccountResponse(messages.Message):
    id = messages.IntegerField(1)
    username = messages.StringField(2)
    email = messages.StringField(3)
    errmsg = messages.StringField(4)

class PlaylistRequest(messages.Message):
    """ ProtoRPC message def. for creating/getting a playlist"""
    userid = messages.IntegerField(1)
    pid = messages.IntegerField(2)
    name = messages.StringField(3)
    firstsong = messages.StringField(4)

class SongMessage(messages.Message):
    id = messages.IntegerField(1)
    spotify_id = messages.StringField(2)
    name = messages.StringField(3)
    vote_count = messages.IntegerField(4)

class PlaylistResponse(messages.Message):
    pid = messages.IntegerField(1)
    song_id = messages.IntegerField(2)
    errmsg = messages.StringField(3)
    name = messages.StringField(4) 
    songs = messages.MessageField(SongMessage, 5, repeated=True)

class MultiplePlaylistResponse(messages.Message):
    playlists = messages.MessageField(PlaylistResponse, 1, repeated=True)

class AddSongRequest(messages.Message):
    pid = messages.IntegerField(1, required=True)
    spotify_id = messages.StringField(2,required=True)
    name = messages.StringField(3, required=True)

class VoteSongRequest(messages.Message):
    id = messages.IntegerField(1, required=True)

class VoteSongResponse(messages.Message):
    id = messages.IntegerField(1, required=True)

