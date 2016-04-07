

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

# TODO: Add authorized clients
package = 'party-queue'

@endpoints.api(name='party_queue', version='v1')
class PartyQueueApi(remote.Service):
    """ PARTY QUEUE API """

    @endpoints.method(AccountRequest, AccountResponse,
            path='signup', http_method='POST',
            name='party-queue.signup')
    def signup(self, request):
        """ Adds a new user to the datastore
        """
        # TODO: Add user authentication. Currently, we will create an acct 
        if Account.find_by_username(request.username) is None:
            new_user = Account(username=request.username,
                               email=request.email)
            new_user.put()
            # Return user's ID to the client for future use
            return AccountResponse(id=new_user.key.id())
        else:
            return AccountResponse(errmsg="Username already exists!")

    @endpoints.method(AccountRequest, AccountResponse,
            path='login', http_method='POST',
            name='party-queue.login')
    def login(self, request):
        """ logs in a user based on username """ 
        user_query = Account.find_by_username(request.username)
        if user_query is None:
            print "User not found" 
            return AccountResponse(errmsg="Username not recognized")
        user = user_query.get()
        return AccountResponse(id=user.key.id())

    @endpoints.method(PlaylistRequest, PlaylistResponse,
            http_method='POST',
            name='party-queue.create_playlist')
    def create_playlist(self, request):
        """ Creates a playlist for the user """
        user = Account.find_by_id(request.userid)
        if user is None:
            print "User not found" 
            return PlaylistResponse(errmsg="User ID not found")
        new_pl = Playlist(owner=user.key, name=request.name)
        new_pl.put()
        return PlaylistResponse(pid=new_pl.key.id())

    @endpoints.method(PlaylistRequest, PlaylistResponse,
            http_method='GET',
            name='party-queue.get_playlist')
    def get_playlist_by_id(self, request):
        pl = Playlist.find_by_id(request.pid)
        return PlaylistResponse(pid=pl.key.id(),
                                name=pl.name,
                                songs=pl.songs)

    @endpoints.method(PlaylistRequest, MultiplePlaylistResponse,
            http_method='GET',
            name='party-queue.get_playlists_for_user')
    def get_playlists_for_user(self, request):
        user = Account.find_by_id(request.userid)
        # Max out playlists at 20
        pls = Playlist.find_by_owner(user.key).fetch(20)
        response = MultiplePlaylistResponse(playlists=[])
        for pl in pls:
            playlist = PlaylistResponse(pid=pl.key.id(),
                                        name=pl.name,
                                        songs=[])
            for song in pl.songs:
                playlist.songs.append(SongMessage(spotify_id=song.spotify_id,
                                                  name=song.name,
                                                  vote_count=song.vote_count))

            response.playlists.append(playlist)
        return response

    @endpoints.method(AddSongRequest, PlaylistResponse,
            http_method='POST',
            name='party-queue.add_song')
    def add_song_to_playlist(self, request):
        pl = Playlist.find_by_id(request.pid)
        pl.songs.append(Song(spotify_id=request.spotify_id,
                             name=request.name,
                             vote_count=0))
        pl.put()
        return PlaylistResponse(pid=pl.key.id(),
                                name=pl.name)
        
APPLICATION = endpoints.api_server([PartyQueueApi])
