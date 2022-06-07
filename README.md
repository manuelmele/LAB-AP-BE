# LAB-AP-BE
This is the we-fix BE of the WeFix project

---------------------------
PREREQUISITES

-install docker

---------------------------
INSTUCTIONS

-Download the "we-fix" folder from git

-Start Docker Engine

-Move to the we-fix folder:
$ cd ~/we-fix/

-Create a custom docker image:
$ docker build -t we-fix .

-Check if there is a new image named "we-fix":
$ docker images

-Start a docker container(port is 8080):
$ docker run -p 8080:8080 we-fix




