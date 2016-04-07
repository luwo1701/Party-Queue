Who: Luke Worley, Corwin Sheehan, Daniel Henderson, Bu Sun Kim

Title: Party Queue  

Vision: Play music that everyone likes.

Automated Tests: 

For frontend tests for the Android application, JUnit 4 was used to create unit tests. Tests are located in the directory Party-Queue/app/src/test/java/party_queue/myapplication/test, and can be run in Android Studio. 

See current unit tests for the frontend on this fork: https://github.com/busunkim96/Party-Queue/tree/master/app/src/test/java/party_queue/myapplication

[Screenshot](https://drive.google.com/file/d/1rhqs7-9Svk8_IyKgTpXQfo8AsDK9XcSWiw/view?usp=sharing)

User Acceptance Tests:



|  **Use Case ID:** | PQ-01 |
|------|------|
|**Use Case Name:**|Sign in to Spotify|
| **Description:** |Confirm user is able to sign into their spotify account|

|  **Users:** | Friends |
|------|------|
|**Pre-Conditions:**|Make sure user has spotify Premium account. If not provide them with one|
| **Description:** |Confirm user is able to sign into their spotify account|
|**Frequency of Use:**|Single use.|


|Flow of Events|Actor Action| System Response| Comments|
|------|------|------|------|
|                  |1. Sign into party queue with an email address and password between 4 and 10 characters| |  |
|                 | 2. Enter Spotify login information| | |
|**Test Pass?:**| Pass/Fail| | |
| **Notes and Issues:**| | | |


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
|                  |1. Sign into party queue with an email address and password between 4 and 10 characters| |  |
|                 | 2. Enter Spotify login information| | |
|                 | 3. Press Play Button| | |
|**Test Pass?:**| Pass/Fail| | |
| **Notes and Issues:**| | | |


|  **Use Case ID:** | PQ-03 |
|------|------|
|**Use Case Name:**|Valid Email Required|
| **Description:** |Confirm when user trys to login in to Part-Queue it require an @something.net or .edu etc|

|  **Users:** | Friends |
|------|------|
|**Pre-Conditions:**|App |
| **Post-Conditions:** |Audio Plays from device|
|**Frequency of Use:**|Single use.|


|Flow of Events|Actor Action| System Response| Comments|
|------|------|------|------|
|                  |1. Sign into party queue with an email address and password between 4 and 10 characters| |  |
|                 | 2. Enter Spotify login information| | |
|                 | 3. Press Play Button| | |
|**Test Pass?:**| Pass/Fail| | |
| **Notes and Issues:**| | | |
