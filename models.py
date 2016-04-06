"""
Use this module to update/query from database 
Assisting Source: https://github.com/GoogleCloudPlatform/appengine-endpoints-tictactoe-python
"""

from google.appengine.ext import endpoints
from google.appengine.ext import ndb
import Playlist

def get_current_user(raise_unauthorized=True):
    """ Returns current user. Raises unauthorized exception unless 
        raise_unauthorized is set to false
    """
    current_user = endpoints.get_current_user()
    if raise_unauthorized and current_user is None:
        raise endpoints.UnauthorizedException('Invalid token.')
    return current_user

class Account(ndb.Model):
    """ Class for a user's account
    """
    username = ndb.StringProperty(required=True)
    userid = ndb.IntegerProperty(required=True)
    email = ndb.StringProperty(required=True)
    playlists = ndb.StructuredProperty(Playlist, repeated=True)

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
        
        new_pl = Playlist(parent=current_user.user_id(), name=name)


    def delete_playlist(self, name):

    def add_song(self):

    def remove_song(cls):
        
    @classmethod
    def query_current_user(cls)
        # Gets a ndb.Query object bound to the current user
        current_user = get_current_user()
        user_query = cls.query(cls.userid == current_user.user_id())
        if user_query is None:
            print "User not found in database"
        return user_query
