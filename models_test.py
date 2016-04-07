""" 
    Unit-Testing file for models.py
"""
import sys
sys.path.insert(1, '/home/user/Downloads/google_appengine')
sys.path.insert(1, '/home/user/Downloads/google_appengine/lib/yaml/lib')
sys.path.insert(1, '/home/user/workspaces/Party-Queue/lib')
import unittest
from models import Song, Playlist, Account
from google.appengine.ext import testbed
from google.appengine.ext import ndb
from google.appengine.ext import testbed
from google.appengine.api.datastore_errors import BadValueError


class AccountRequest:
    """ Helper Class to simulate an Account request """
    def __init__(self, username, email):
        self.username = username
        self.email = email

class TestModelsTestCase(unittest.TestCase):
    def setUp(self):
        # Setting up testbed with the method stubs uses an in memory datastore
        self.testbed = testbed.Testbed()
        self.testbed.activate()
        self.testbed.init_datastore_v3_stub()
        self.testbed.init_memcache_stub()
        # Clear ndb's in-context cache between tests.
        # This prevents data from leaking between tests.
        # Alternatively, you could disable caching by
        # using ndb.get_context().set_cache_policy(False)
        ndb.get_context().clear_cache()

    def tearDown(self):
        self.testbed.deactivate()

    def test_songs_initiliazation(self):
        # Test that it fails if we don't put in req'd values
        try:
            Song().put()
            # this is hacky, but for some reason self.assertRaises wasn't 
            # properly catching the exception
            self.assertEqual(1,2)
        except BadValueError:
            pass

        try:
            Song(spotify_id='sldkjfldkjflkd').put()
            self.assertEqual(1,2)
        except BadValueError:
            pass

        try:
            Song(name='song_name').put()
            self.assertEqual(1,2)
        except BadValueError:
            pass

        Song(spotify_id='sldkjfldkjflkd', name='songname').put()
        self.assertEqual(1, len(Song.query().fetch(2)))

    def test_Playlist_initialization(self):
        owner=Account(username='Mr. Magoo', email='mm123@gmail.com').put()
        try:
            Playlist().put()
            self.assertEqual(1,2)
        except BadValueError:
            pass
        try:
            Playlist(owner=owner).put()
            self.assertEqual(1,2)
        except BadValueError:
            pass
        try:
            Playlist(name='atlejljd').put()
            self.assertEqual(1,2)
        except BadValueError:
            pass

        Playlist(owner=owner, name='MM\'s awesome playlist').put()
        self.assertEqual(1, len(Playlist.query().fetch(2)))

    def test_Account_initialization(self):
        try:
            Account().put()
            self.assertEqual(1,2)
        except BadValueError:
            pass
        try:
            Account(username='Captain Blackbeard').put()
            self.assertEqual(1,2)
        except BadValueError:
            pass
        try:
            Account(email='cbb123@gmail.com').put()
            self.assertEqual(1,2)
        except BadValueError:
            pass

        # test contructor
        Account(username='Captain BlackBeard', email='cbb123@gmail.com').put()
        self.assertEqual(1, len(Account.query().fetch(2)))

        # test add new user
        request = AccountRequest('First Mate', 'fm122@gmail.com')
        Account.add_new_user(request)
        self.assertEqual(2, len(Account.query().fetch(3)))

    def test_account(self):
        user = Account(username='Captain BlackBeard', email='cbb123@gmail.com')
        user.put()
        self.assertEqual(1, len(Account.query().fetch(2)))

        # test find by username
        found_user = Account.find_by_username('Captain BlackBeard')
        self.assertEqual(user, found_user, 'Failed to find user based on username')

        # test update email 
        Account.update_email(user.key.id(), 'blackbeardisawesome@gmail.com') 
        self.assertEqual('blackbeardisawesome@gmail.com', 
                         Account.find_by_username('Captain BlackBeard').email,
                         'Failed to update email')

        # test find by id
        found_user = Account.find_by_id(user.key.id())
        self.assertEqual(user, found_user, 'Failed to find user based on id')

    def test_playlist(self):
        owner=Account(username='Mr. Magoo', email='mm123@gmail.com').put()
        pl = Playlist(owner=owner, name='MM\'s awesome playlist')
        pl.put()
        self.assertEqual(1, len(Playlist.query().fetch(2)))
        
        # test find by id
        found_pl = Playlist.find_by_id(pl.key.id())
        self.assertEqual(pl, found_pl, 'Failed to find playlist based on id')

        # test find by owner
        found_pl = Playlist.find_by_owner(owner).get()
        self.assertEqual(pl, found_pl, 'Failed to find playlist based on owner')

        new_pl = Playlist(owner=owner, name='MM\'s other awesome playlist')
        new_pl.put()
        self.assertEqual(2, len(Playlist.query().fetch(2)))


        
# Main: Run Test Cases
if __name__ == '__main__':
    unittest.main()

