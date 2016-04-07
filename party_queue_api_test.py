"""
    Unit Testing file for the endpoints api of Party-Queue app
    Much of the basis for this code comes from:
    https://stackoverflow.com/questions/20384743/how-to-unit-test-google-cloud-endpoints 
"""
import sys
sys.path.insert(1, '/home/user/Downloads/google_appengine')
sys.path.insert(1, '/home/user/Downloads/google_appengine/lib/yaml/lib')
sys.path.insert(1, '/home/user/workspaces/Party-Queue/lib')
from google.appengine.ext import testbed
from google.appengine.ext import endpoints 
import webtest
import unittest
import endpoints

class TestPartyQueueApiTestCase(unittest.TestCase):
    def setUp(self):
        tb = testbed.Testbed()
        tb.setup_env(current_version_id='testbed.version') #needed because endpoints expects a . in this value
        tb.activate()
        tb.init_all_stubs()
        self.testbed = tb
        app = endpoints.api_server([PartyQueueApi], restricted=False)
        self.testapp=webtest.TestApp(app)

    def tearDown(self):
        self.testbed.deactivate()

    def test_signup(self):
        self.assertEqual('test', 'not equal', "This test shoud fail")
        """ 
        reqmsg = {
                'username': 'Captain Blackbeard',
                'email':    'captBlackbeard@pirates.org',
        }
        resp = self.testapp.post_json('/_ah/spi/PartyQueueApi.signup')
        self.assertEqual(resp.json, {'id
        """

# Main: Run Test Cases
if __name__ == '__main__':
    unittest.main()
