from google.appengine.ext import ndb

class Playlist(ndb.Model):
    songs = ndb.StructuredProperty(Song, repeated=True)
    name = ndb.StringProperty(required=True)

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

class Song(ndb.Model):
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
        """
