package party_queue.myapplication;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests logic in SignupActivity.validate() method in SignupActivity.
 * Partial mocking is used to stub methods used by the funtion vaidate.
 */

@RunWith(MockitoJUnitRunner.class)
public class SignupActivityTest {

    @Mock
    SignupActivity signupActivity = mock(SignupActivity.class);


    @Test
    //this test shows the mocking of methods in the class
    public void testMock() throws Exception {
        String valid_email = "foo34@colorado.edu";
        when(signupActivity.getemailText()).thenReturn(valid_email);
        String return_email = signupActivity.getemailText();
        assertEquals("mocked object should return", valid_email, return_email);
    }

    @Test
    public void testValidateWithValidParams() throws Exception {
        when(signupActivity.getemailText()).thenReturn("foo34@gmail.com");
        when(signupActivity.getnameText()).thenReturn("foo34");
        when(signupActivity.getpasswordTest()).thenReturn("abc123");
        when(signupActivity.validate()).thenCallRealMethod();

        Boolean validated = signupActivity.validate();
        assertTrue("validation with correct parameters should succeed", validated);
    }

    @Test
    public void testValidateWithEmptyUsername() throws Exception {
        when(signupActivity.getemailText()).thenReturn("foo34@gmail.com");
        when(signupActivity.getnameText()).thenReturn("");
        when(signupActivity.getpasswordTest()).thenReturn("abc123");

        when(signupActivity.validate()).thenCallRealMethod();

        Boolean validated = signupActivity.validate();
        assertFalse("validation with empty username should fail.", validated);
    }

    @Test
    public void testValidateWithShortPassword() throws Exception {
        when(signupActivity.getemailText()).thenReturn("foo34@gmail.com");
        when(signupActivity.getnameText()).thenReturn("foo34");
        when(signupActivity.getpasswordTest()).thenReturn("Ab1");

        when(signupActivity.validate()).thenCallRealMethod();

        Boolean validated = signupActivity.validate();
        assertFalse("validation with password.length() < 4 should fail.", validated);
    }

    @Test
    public void testValidateWithLongPassword() throws Exception {
        when(signupActivity.getemailText()).thenReturn("foo34@gmail.com");
        when(signupActivity.getnameText()).thenReturn("FOObar123456789");
        when(signupActivity.getpasswordTest()).thenReturn("Ab1");

        when(signupActivity.validate()).thenCallRealMethod();

        Boolean validated = signupActivity.validate();
        assertFalse("validation with password.length() > 10 should fail.", validated);
    }


    @Test
    public void testValidateWithShortUsername() throws Exception {
        when(signupActivity.getemailText()).thenReturn("foo34@gmail.com");
        when(signupActivity.getnameText()).thenReturn("fo");
        when(signupActivity.getpasswordTest()).thenReturn("Ab1");

        when(signupActivity.validate()).thenCallRealMethod();

        Boolean validated = signupActivity.validate();
        assertFalse("validation with username.length() < 3 should fail.", validated);
    }
}