Copyright 2017/2018 APIs Research LLC

DUMLRacer is a race condition in the update system for DJI drones and remotes that run Android.

Payload is in two parts

payload1.tar.gz:
dummy - 500mb dummy file used to make race condition window longer
jcase - symlink to /data/.bin/cat

payload2.tar.gz
dummy - 500mb dummy file used to make race condition window longer
jcase - script we want ran as root, set to permission 775
dummy - again same file to give us time to remove again jcase

Push first payload via duml, and "upgrade"

While dji_sys is deleting 'dummy', we move it using ftp cmd rename jcase ../jcase

This prevents it from being deleted

Push second payload once first is done, and "upgrade"

while second payload is writing dummy, we move jcase back to signimags with rename ../jcase jcase

once our target is overwriting, and dummy is being writte nagain we delete the jcase symlink
