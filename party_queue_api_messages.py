from protorpc import messages

class AccountSetupRequest(messages.Message):
    """ProtoRPC message definition to represent a new user query""" 
    #TODO: Add user authentication
    username = messages.StringField(1)
    email = messages.StringField(2)

class AccountResponse(messages.Message):
    """ Blank message response"""
    id = messages.IntegerField(1)
    username = messages.StringField(2)
    email = messages.StringField(2)



