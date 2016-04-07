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

class PlaylistResponse(messages.Message):
    pid = messages.IntegerField(1)
    errmsg = messages.StringField(2)
    name = messages.StringField(3) 
    songs = messages.StringField(4, repeated=True)

class MultiplePlaylistResponse(messages.Message):
    pids = messages.IntegerField(1, repeated=True)

class AddSongRequest(messages.Message):
    pid = messages.IntegerField(1, required=True)
    song = messages.StringField(2, required=True)


