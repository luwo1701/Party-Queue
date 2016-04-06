

import endpoints
from protorpc import messages
from protorpc import message_types
from protorpc import remote
from models import Account
from models import Playlist
from models import Song
from party_queue_api_messages import AccountSetupRequest
from party_queue_api_messages import AccountResponse

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

    @endpoints.method(AccountSetupRequest, AccountResponse,
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

"""
    @endpoints.method(AccountRequest, AccountResponse,
            path='signup', http_method='POST',
            name='party-queue.signup')
            """

APPLICATION = endpoints.api_server([PartyQueueApi])
