# Development Setup/Troubleshooting: 

### Setup Google App engine backend:
* Install google app engine sdk [here](https://cloud.google.com/appengine/downloads#Google_App_Engine_SDK_for_Python) 
* Add the following to your shell configuration file:
```
export PATH=$PATH:/path/to/downloaded/google_appengine
```
but replace `/path/to/downloaded/google_appenine` to the fully qualified path 
that you downloaded the app engine sdk to. Be sure to restart your terminal or 
type `source ~/.bashrc` for these changes to take effect. 

### Running the application:
* In a terminal window, navigate to your Party-Queue directory and run `dev_appserver.py .`
* Open your favorite web browser and navigate to `localhost:8080` 
