# robocup2019
Preperations for the Robocup 2019

Start med at installere VirtualBox: https://www.virtualbox.org/
Lav en virtuel maskine i VirtualBox (1-3 kerner, 1024-2048MB ram, de to ting her er nemme at ændre senere), 20GB harddisk).
installer Ubuntu 16.04 LTS: http://releases.ubuntu.com/16.04/ubuntu-16.04.5-desktop-amd64.iso.torrent?_ga=2.215073687.1263508812.1544486165-1815367349.1544218738 (kræver bittorrent)

Efter at ubuntu er installeret, så giv den 5 minutter så opdatere den (Opgrader IKKE Ubuntu, vi skal bruge en lidt ældre version..)

Efter at Ubuntu er opdateret og genstart, er det tid til at installere jGrasp: https://spider.eng.auburn.edu/user-cgi/grasp/grasp.pl?;dl=download_jgrasp.html


Derefter installer ROS gå til https://wiki.ros.org/kinetic/Installation/Ubuntu og følg guiden.

Efter du har installeret ROS og opsat Environment setup etc. er det tid til at installere ROSJava: https://wiki.ros.org/rosjava/Tutorials/kinetic/Source%20Installation

Efter det skal du refere til den setup.bash der er lavet. Kør source ~/rosjava/devel/setup.bash (ref: https://answers.ros.org/question/277437/solvedrosjavacatkin_create_rosjava_pkg-command-not-found/ )

Nu er det tid til at kode .. :)



