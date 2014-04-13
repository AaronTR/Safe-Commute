Safe-Commute
============

I just merged the OpenCV stuff into the main repo. Follow these instructions so you can actually use this shit.

1). Download OpenCV here: http://sourceforge.net/projects/opencvlibrary/files/opencv-android/2.4.8/OpenCV-2.4.8-android-sdk.zip/download
2). Create a new project from existing android code using the file you just downloaded.
3). Make sure "is library" is selected. 
      a). Right click the project you just created -> properties -> android -> check the "is library" box under "library" if not already checked
4). Add library as dependency for safecommute
      b). Right click safecommute project -> properties -> android -> add -> select OpenCV Library and make sure you get a green check
      a). If there is a library listed with a red X next to it just delete it
