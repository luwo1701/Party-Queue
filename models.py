"""
Use this module to update/query from database 
Assisting Source: https://github.com/GoogleCloudPlatform/appengine-endpoints-tictactoe-python
"""

from google.appengine.ext import ndb

""" CLASS CONTAINING SONGS"""
class Song(ndb.Model):
    spotify_id = ndb.StringProperty(required=True)
    name = ndb.StringProperty(required=True)
    vote_count = ndb.IntegerProperty()
    owner = ndb.KeyProperty(required=True)

    @classmethod
    def find_by_playlist(cls, playlistKey):
        return cls.query(cls.owner == playlistKey).order(-cls.vote_count)

    @classmethod
    def upvote(cls, id):
        song = cls.get_by_id(id)
        song.vote_count = song.vote_count + 1
        song.put()

    @classmethod
    def downvote(cls, id):
        song = cls.get_by_id(id)
        song.vote_count = song.vote_count - 1
        song.put()

""" CLASS CONTAINING PLAYLISTS"""
class Playlist(ndb.Model):
    songs = ndb.StructuredProperty(Song, repeated=True)
    owner = ndb.KeyProperty(required=True)
    name = ndb.StringProperty(required=True)

    @classmethod
    def find_by_id(cls, id):
        """ Returns playlist entity mathching id """
        user = cls.get_by_id(id)
        return user

    @classmethod
    def find_by_owner(cls, ownerkey):
        """ Returns list of playlist queries by owner key """
        return cls.query(cls.owner == ownerkey).order(cls.name)

    @classmethod
    def add_new_playlist(cls, ownerKey, name):
        """ Returns new playlist entity """
        new_pl = cls(owner=ownerKey, name=name)
        new_pl.put()
        return new_pl
        
    @classmethod
    def add_song(cls, request):
        pl = cls.find_by_id(request.pid)
        new_song = Song(spotify_id=request.spotify_id,
                             name=request.name,
                             owner = pl.key,
                             vote_count=0)
        new_song.put()
        pl.songs.append(new_song)        
        pl.put()  
        return new_song

"""
    @classmethod
    def rename(cls, new):


    @classmethod
    def delete_song(cls):
        # Delete a song
"""

""" CLASS FOR USER ACCOUNTS"""
class Account(ndb.Model):
    """ Class for a user's account """
    username = ndb.StringProperty(required=True)
    email = ndb.StringProperty(required=True)

    @classmethod
    def update_email(cls, id, email):
        """ Updates an email on the account """
        user = cls.find_by_id(id)
        user.email = email
        user.put()

    @classmethod
    def find_by_username(cls, username):
        """ Returns a user entity by username """
        user_query = cls.query(cls.username == username)
        return user_query.get()

    @classmethod
    def find_by_id(cls, id):
        """ Returns a user entity by id """
        user = cls.get_by_id(id)
        return user

    @classmethod
    def add_new_user(cls, request):
        if cls.find_by_username(request.username) is None:
            new_user = cls(username=request.username,
                           email=request.email)
            new_user.put()
            return new_user
        return None
