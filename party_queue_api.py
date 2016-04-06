

import endpoints
from protorpc import messages
from protorpc import message_types
from protorpc import remote
from models import Account
from models import Playlist
from models import Song

# TODO: Add authorized clients
#WEB_CLIENT_ID = 'replace this with your web client application ID'
ANDROID_CLIENT_ID = 'replace this with your Android client ID'
#ANDROID_AUDIENCE = WEB_CLIENT_ID

package 'party-queue'

class 


@endpoints.api(name='party_queue', version='v1')
        """ TODO: Add auth'd client list to api decorator
               allowed_client_ids=[ANDROID_CLIENT_ID,
                                   endpoints.API_EXPLORER_CLIENT_ID],
               scopes=[endpoints.EMAIL_SCOPE])
        """
class PartyQueueApi(remote.Service):
    """ PARTY QUEUE API """

    @endpoints.method(, 

    def add_current_user(self, request):
        """ Adds current user to the datastore
        """
        
