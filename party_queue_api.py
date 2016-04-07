

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

# TODO: Add authorized clients
WEB_CLIENT_ID = 'replace this with your web client application ID'
ANDROID_CLIENT_ID = 'replace this with your Android client ID'
ANDROID_AUDIENCE = WEB_CLIENT_ID

package = 'party-queue'

""" TODO: Add auth'd client list to api decorator for android
               allowed_client_ids=[ANDROID_CLIENT_ID,
                                   endpoints.API_EXPLORER_CLIENT_ID],
               scopes=[endpoints.EMAIL_SCOPE]) 
"""
"""
               allowed_client_ids=[WEB_CLIENT_ID,
                    endpoints.API_EXPLORER_CLIENT_ID],
               scopes=[endpoints.EMAIL_SCOPE])
"""
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
        new_user = Account(username=request.username,
                           email=request.email)
        new_user.put()
        # Return user's ID
        return AccountResponse(id=new_user.key.id())

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
        response = MultiplePlaylistResponse(pids=[])
        for pl in pls:
            response.pids.append(pl.key.id())
        return response

    @endpoints.method(AddSongRequest, PlaylistResponse,
            http_method='GET',
            name='party-queue.add_song')
    def add_song_to_playlist(self, request):
        pl = Playlist.find_by_id(request.pid)
        pl.songs.append(request.song)
        pl.put()
        return PlaylistResponse(pid=pl.key.id(),
                                name=pl.name,
                                songs=pl.songs)
        


APPLICATION = endpoints.api_server([PartyQueueApi])
