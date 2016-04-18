

import endpoints
from protorpc import messages
from protorpc import message_types
from protorpc import remote
from models import Account
from models import Playlist
from models import Song
from party_queue_api_messages import AccountRequest
from party_queue_api_messages import AccountResponse
from party_queue_api_messages import PlaylistRequest
from party_queue_api_messages import PlaylistResponse
from party_queue_api_messages import MultiplePlaylistResponse
from party_queue_api_messages import AddSongRequest
from party_queue_api_messages import SongMessage
from party_queue_api_messages import VoteSongRequest
from party_queue_api_messages import VoteSongResponse

# TODO: Add authorized clients
package = 'party-queue'

@endpoints.api(name='party_queue', version='v1')
class PartyQueueApi(remote.Service):
    """ PARTY QUEUE API """
    @staticmethod
    def build_playlist_response(playlists):
        """ Builds a playlist response for the given playlists """
        response = MultiplePlaylistResponse(playlists=[])
        for pl in playlists:
            playlist = PlaylistResponse(pid=pl.key.id(),
                                        name=pl.name,
                                        songs=[])
            for song in pl.songs:
                playlist.songs.append(SongMessage(id=song.key.id(),
                                                  spotify_id=song.spotify_id,
                                                  name=song.name,
                                                  vote_count=song.vote_count))

            response.playlists.append(playlist)
        return response

    @endpoints.method(AccountRequest, AccountResponse,
            path='signup', http_method='POST',
            name='party-queue.signup')
    def signup(self, request):
        """ Adds a new user to the datastore
        """
        # TODO: Add user authentication. Currently, we will create an acct 
        new_user = Account.add_new_user(request)
        if new_user is None:
            return AccountResponse(errmsg="Username already exists!")
        return AccountResponse(id=new_user.key.id())

    @endpoints.method(AccountRequest, AccountResponse,
            path='login', http_method='POST',
            name='party-queue.login')
    def login(self, request):
        """ logs in a user based on username """ 
        user = Account.find_by_username(request.username)
        if user is None:
            print "User not found" 
            return AccountResponse(errmsg="Username not recognized")
        return AccountResponse(id=user.key.id())

    @endpoints.method(PlaylistRequest, PlaylistResponse,
            http_method='POST',
            name='party-queue.create_playlist')
    def create_playlist(self, request):
        """ Creates a playlist for the user """
        # TODO: Max amount of playlists at 20 for a user
        user = Account.find_by_id(request.userid)
        if user is None:
            print "User not found" 
            return PlaylistResponse(errmsg="User ID not found")
        new_pl = Playlist.add_new_playlist(user.key, request.name)
        return PlaylistResponse(pid=new_pl.key.id())

    @endpoints.method(PlaylistRequest, PlaylistResponse,
            http_method='GET',
            name='party-queue.get_playlist')
    def get_playlist_by_id(self, request):
        """ Returns a playlist based on the plalist id """
        pl = Playlist.find_by_id(request.pid)
        return PlaylistResponse(pid=pl.key.id(),
                                name=pl.name,
                                songs=pl.songs)

    @endpoints.method(PlaylistRequest, MultiplePlaylistResponse,
            http_method='GET',
            name='party-queue.get_playlists_for_user')
    def get_playlists_for_user(self, request):
        """ Gets all playlists and songs in each playlist for a user's id """ 
        user = Account.find_by_id(request.userid)
        playlists = Playlist.find_by_owner(user.key).fetch(20)
        return self.build_playlist_response(playlists)

    @endpoints.method(AddSongRequest, PlaylistResponse,
            http_method='POST',
            name='party-queue.add_song')
    def add_song_to_playlist(self, request):
        song = Playlist.add_song(request)
        return PlaylistResponse(song_id=song.key.id())

    @endpoints.method(VoteSongRequest, VoteSongResponse,
            http_method='POST',
            name='party-queue.upvote')
    def upvote_song(self, request):
        Song.upvote(request.id)
        return UpvoteSongResponse(id=request.id)
 
    @endpoints.method(VoteSongRequest, VoteSongResponse,
            http_method='POST',
            name='party-queue.downvote')
    def downvote_song(self, request):
        Song.downvote(request.id)
        return UpvoteSongResponse(id=request.id)
        
       
APPLICATION = endpoints.api_server([PartyQueueApi])
