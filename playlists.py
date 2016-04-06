from google.appengine.ext import ndb

class Playlist(ndb.Model):
    songs = ndb.StructuredProperty(Song, repeated=True)

    # ownedby should be parent ID???
    #ownedby = ndb.

    def add_song(cls):
        # Add a song

    def delete_song(cls):
        # Delete a song

class Song(ndb.Model):
    name = ndb.StringProperty(required=True)
    vote_count = ndb.IntegerProperty()

    def upvote(cls):
       # Increment vote_count 

    def downvote(cls):
        # Decrement vote count
