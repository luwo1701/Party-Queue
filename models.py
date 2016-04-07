"""
Use this module to update/query from database 
Assisting Source: https://github.com/GoogleCloudPlatform/appengine-endpoints-tictactoe-python
"""

from google.appengine.ext import endpoints
from google.appengine.ext import ndb

def get_current_user(raise_unauthorized=True):
    """ Returns current user. Raises unauthorized exception unless 
        raise_unauthorized is set to false
    """
    current_user = endpoints.get_current_user()
    if raise_unauthorized and current_user is None:
        raise endpoints.UnauthorizedException('Invalid token.')
    return current_user

""" CLASS CONTAINING SONGS"""
class Song(ndb.Model):
    spotify_id = ndb.StringProperty(required=True)
    name = ndb.StringProperty(required=True)
    vote_count = ndb.IntegerProperty()

"""
    @classmethod
    def upvote(cls, name):
        # Increment vote_count 
        song_query = get_song_by_name(name)
        if song_query is None:
            print "Song not found in playlist"
        

    def downvote(cls, name):
        # Decrement vote count

    @classmethod
    def get_song_by_name(cls, name):
        return cls.query(cls.name == name)
        "
"""

""" CLASS CONTAINING PLAYLISTS"""
class Playlist(ndb.Model):
    songs = ndb.StructuredProperty(Song, repeated=True)
    owner = ndb.KeyProperty(required=True)
    name = ndb.StringProperty(required=True)

    @classmethod
    def find_by_id(cls, id):
        user_query = cls.get_by_id(id)
        return user_query

    @classmethod
    def find_by_owner(cls, ownerkey):
        return cls.query(cls.owner == ownerkey).order(cls.name)
        

"""
    @classmethod
    def rename(cls, new):

    @classmethod
    def add_song(cls):
        # Add a song

    @classmethod
    def delete_song(cls):
        # Delete a song
"""

""" CLASS FOR USER ACCOUNTS"""
class Account(ndb.Model):
    """ Class for a user's account
    """
    username = ndb.StringProperty(required=True)
    email = ndb.StringProperty(required=True)

    @classmethod
    def update_email(cls, email):
        """ Updates an email on the account
        """
        user_query = cls.query_current_user()

        current_user = user_query.get()
        current_user.email = email
        current_user.put()
          
    @classmethod
    def create_new_playlist(cls, name):
        # Create's a new playlist. If playlist name already exists, creates
        # playlist with name '{name}(1)'
        user_query = cls.query_current_user()
        current_user = user_query.get()
        # TODO: Find out if name exists in Playlist set, and fix name if so

        
        # TODO: Can we use the user id as the parent, or should it be ndb.Key???
        
        new_pl = Playlist(parent=current_user.key, name=name)
        current_user.Playlists.append(new_pl)
        current_user.put()

    """
    @classmethod
    def delete_playlist(self, name):
        user_query = cls.query_current_user()
        current_user = user_query.get()

    @classmethod
    def add_song(self, playlist):
        user_query = cls.query_current_user()
        current_user = user_query.get()

    @classmethod
    def remove_song(cls, playlist):
        user_query = cls.query_current_user()
        current_user = user_query.get()
    """     
    @classmethod
    def query_current_user(cls, id):
        # Gets a ndb.Query object bound to the current user
        #current_user = get_current_user()
        user_query = cls.query(cls.key.id() == id)
        if user_query is None:
            print "User not found in database"
        return user_query

    @classmethod
    def find_by_username(cls, username):
        user_query = cls.query(cls.username == username)
        return user_query

    @classmethod
    def find_by_id(cls, uid):
        user_query = cls.get_by_id(uid)
        return user_query



