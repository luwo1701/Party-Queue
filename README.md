## CSCI 3308 Project: Party-Queue
A social party playlist. Users can vote on songs in a queue, and the songs are
played in the order of highest votes.

#### Description: 
Party Queue is a smartphone app that allows a group of people to create a queue of songs democratically. A main user will create a queue in which people can request to join. Once the master has approved them to join the queue they can push songs they would like to listen to onto the queue. Everyone who is in the group can see the songs that have been pushed and vote for which they would like to listen to next. The highest rated song will play next unless the master vetos the choice. The master will be able to see who pushed the song incase they favor a particular persons choices over others.    

#### Vision Statement: 
Play music that everyone likes.

#### Motivation: 
Choosing music with a large group of people can be nerve racking and you can receive quite a bit of grief from people. Not only this but people at parties will often bother whoever is playing music to play the song they want.  The app would allow people hosting to appease everyones musical desires democratically. It would also let whoever is hosting the party actively choose music at times but not be responsible at all times. 

## Repo Organization:
There are a fair amount of generated code/organizational files that android studio uses. Here we will call out the portions of the app that we wrote. 
* \app holds the most of the code for the android appication
* \playerview holds the code for the music player portion of the app
* \documentation holds our auto-documenter generate documentation
* The python files in the root of the repo all correspond to the backend api and communicating with the google datastore database
* Other files/folders are generated, a dependency, or an artifact of either google app engine or android studio. 

## Running the app
For information on how to build/run/deploy the application, see Party-Queue_Part_6.pdf in the repo root.

#### Risks:
* Little experience with android studio or swift
* Using API from music apps could be hard
* Pushing broken code to master

#### Mitigation:
* Intro to Android studio book has been purchased

#### Requirements:

|  ID| Functional Requirements  | Agile Sizing  |   |   |
|---|---|---|---| --- |
| 1 |Become a collaborator | 3   |   |   |
| 2  | As a programmer I would like users to be able to search for available songs in the spotify library.    | 5  |   |   |
| 3   |       As a programmer i would like to design an intuitive interface for the Android app   | 13  |  |  |



| ID  |User Requirements |  Agile Sizing |   |   |
|---|---|---|---|--- |
|1   | As a party guest, I would like to be able to add tracks to the play queue|  5 |   |   |
|  2 | As a party master, I would like to be able to invite party guests| 5  |   |   |
|3  |As a party guest, I would like to discover my hosts’ current Party Queue via the browser or the Party Queue app| 8  | | |
| 4| As a party master I would like to be able to veto next song incase I really want to hear something else| 3 | | |
| 5 | As a party guest I would like to see a notification of when songs have been added| 5 | | |

| ID  |Non-Functional Requirements |  Agile Sizing |   |   |
|---|---|---|---|--- |
| 1|  Have a database for each party that contains the current queue of tracks  |  8 |    |    |
| 2|Assign each track in the queue a vote count based on number of upvotes received, and reorder the queue accordingly|  5 |    |    |


#### Methodology:
Agile process

#### Project Tracking Software: 
Git Hub Issues and Milestones

#### Project Plan:

#### Collaborators:
* Luke Whorley, Student in Mechanical Engineering, CU Boulder
* Bu Sun Kim, Student in Computer Science and Linguistics, CU Boulder
* Daniel Henderson, Student in Electrical and Computer Engineering, CU Boulder
* Corwin Sheahan, Student in Aerospace Engineering and Computer Science, CU Boulder
