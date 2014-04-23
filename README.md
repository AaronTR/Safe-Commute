Safe-Commute
============

I just merged the OpenCV stuff into the main repo. Follow these instructions so you can actually use this shit.

1). Download OpenCV here: http://sourceforge.net/projects/opencvlibrary/files/opencv-android/2.4.8/OpenCV-2.4.8-android-sdk.zip/download

2). Create a new project from existing android code using the file you just downloaded.

3). Make sure "is library" is selected. 
      
      a). Right click the project you just created -> properties -> android -> check the "is library" box under "library" if not already checked

4). Add library as dependency for safecommute
      
      a). Right click safecommute project -> properties -> android -> add -> select OpenCV Library and make sure you get a green check
      
      b). If there is a library listed with a red X next to it just delete it

==============
To Use Maps:

1. Go to Android sdk manager -> Scroll down to extras -> Choose google play services and install, if not already

2. Copy the google-play services_lib library project to your workspace (Click File > Import, select Android > Existing Android Code into Workspace, and browse the workspace import the library project). The library project can be found under the following path:

<android-sdk-folder> / extras/ google/ google_play_services/ libproject/ google-play-services_lib library project 

3. Next refer the library project in your android map project.

Right click on your android map project goto properties. choose android. click add browse the library project and click add and apply.

https://developers.google.com/maps/documentation/android/start#getting_the_google_maps_android_api_v2
