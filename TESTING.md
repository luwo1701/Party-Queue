# Party-Queue: Testing Document

#### Party-QueueTeam Members: 
* Luke Worley 
* Corwin Sheehan 
* Daniel Henderson 
* Bu Sun Kim

#### Our Vision: 
Play music that everyone likes.

#### Automated Tests: 
###### Front end:
For frontend tests for the Android application, JUnit 4 was used to create unit tests. Tests are located in the directory Party-Queue/app/src/test/java/party_queue/myapplication, and can be run in Android Studio as shown in the below screenshot. 

See current unit tests for the frontend on this fork: https://github.com/busunkim96/Party-Queue/tree/master/app/src/test/java/party_queue/myapplication

[Screenshot of Unittests](https://drive.google.com/file/d/1rhqs7-9Svk8_IyKgTpXQfo8AsDK9XcSWiw/view?usp=sharing)

###### Back end:
Our backend is written in python2.x and hosted on google app engine. To run the tests, you will need to do the following:

1. Clone this repository
2. Due to heavy integration of our backend with google app engine, you will need the [google app engine sdk](https://cloud.google.com/appengine/downloads) to run the tests. 
3. After installaction, navigate to where you cloned the Party-Queue directory and change the system path insertions on lines 6-8 in models_test.py to:
```
sys.path.insert(1, '/path/to/google_appengine')
sys.path.insert(1, '/path/to/google_appengine/lib/yaml/lib')
sys.path.insert(1, '/path/to/Party-Queue/lib')
```
Replace `/path/to/google_appengine` with the path to where you installed the gae sdk, and replace `/path/to/Party-Queue/lib` with the path to where you cloned the Party-Queue directory.

Now, run `python models_test.py`. Ignore output lines like `WARNING:root:initial generator _put_tasklet(context.py:348) raised BadValueError(Entity has uninitialized properties: name)`. This is an artifact of unit testing with google app engine's models and is expected behavior. 

If you run into any issues, please contact Corwin Sheahan at corwin.sheahan@colorado.edu


#### User Acceptance Tests:


<p align="center"> <b>Test 1:</b> </p>

|  **Use Case ID:** | PQ-01 |
|------|------|
|**Use Case Name:**|Sign in to Spotify|
| **Description:** |Confirm user is able to sign into their spotify account and their authorization gives them authorization to see album artwork|

|  **Users:** | Friends |
|------|------|
|**Pre-Conditions:**|Make sure user has spotify Premium account. If not provide them with one|
| **Post-Conditions** |After entering their credentials a new screen appears with album artwork|
|**Frequency of Use:**|Single use.|


|Flow of Events|Actor Action| System Response| Comments|
|------|------|------|------|
|                  |1. Sign into party queue with an email address and password between 4 and 10 characters| Spotify Login Page appears|  |
|                 | 2. Enter Spotify login information| New page should load with album artwork and play button | |
|**Test Pass?:**| Pass/Fail| | |
| **Notes and Issues:**| | | |

<p align="center"> <b>Test 2:</b> </p>

|  **Use Case ID:** | PQ-02 |
|------|------|
|**Use Case Name:**|Play Song|
| **Description:** |Confirm when user logs in that audio plays|

|  **Users:** | Friends |
|------|------|
|**Pre-Conditions:**|Make sure user has spotify Premium account. If not provide them with one|
| **Post-Conditions:** |Audio Plays from device|
|**Frequency of Use:**|Single use.|


|Flow of Events|Actor Action| System Response| Comments|
|------|------|------|------|
|                  |1. Sign into party queue with an email address and password between 4 and 10 characters| Spotify login page should appear |  |
|                 | 2. Enter Spotify login information| New page should load with album artwork and a play button | |
|                 | 3. Press Play Button|The audio from the song that is displayed should play| |
|**Test Pass?:**| Pass/Fail| | |
| **Notes and Issues:**| | | |


<p align="center"> <b>Test 3:</b> </p>

|  **Use Case ID:** | PQ-03 |
|------|------|
|**Use Case Name:**|Valid Email Required|
| **Description:** |Confirm when user trys to login in to Part-Queue it requires an @something.net or .edu etc|

|  **Users:** | Friends |
|------|------|
|**Pre-Conditions:**|App is installed on device |
| **Post-Conditions:** |User is alerted if email does not contain @  symbol or .net, .edu, .org, or .com|
|**Frequency of Use:**|Single use|


|Flow of Events|Actor Action| System Response| Comments|
|------|------|------|------|
|                  |1. Attempt to sign into party queue without a .com .edu or .org in email address| Error should appear |  |
|                 | 2. Attempt to sign into party queue without an @ sign| Error should appear | |
|**Test Pass?:**| Pass/Fail| | |
| **Notes and Issues:**| | | |
