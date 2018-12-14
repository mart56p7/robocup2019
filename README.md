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

For at gøre så man ikke skal køre /opt/ros/kinect/setup.bash og /rosjava/devel/setup.bash så åben din ~/.bashrc og tilføj i bunden af filen:

source /opt/ros/kinect/setup.bash
source /rosjava/devel/setup.bash

Derefter når din terminal åbner, så køre den automatisk de 2 scripts.

--
Ok lad os prøve det første eksempel

Følg de skridt der er i https://us.battle.net/forums/en/wow/topic/20765767302 udfør alt hvad de beder dig om indtil du når til 2.1 hvor du skal slette en mappe.

Husk for for at du kan se filerne så skal vi så source den setup.bat der ligger i det her workspace (den ligger i devel/setup.bash - Du kan evt. tilføje den til dit .bashrc og derefter åbne en ny terminal)

Ok lad os se hvad der er lavet og hvordan det virker.

Du har nu i mappen 

~/rosjava_srv_ws/src/tutorial_custom_src/client_server/src/main/java/com/github/tutorial_custom_src/client_server

2 filer

Talker.java
Listener.java

De her filer kan du compile ved at køre ~/rosjava_src_ws/catkin_make (den computer alle de filer der er i dit workspace "rosjava_srv_ws")

Efter de er compiled, kan du køre filerne via kommandoen

rosrun <pakke> <source mappe> <fil>
  
Note: Husk at starte roscore før du prøver at køre et ros program.

Her er det så

rosrun tutorial_custom_srv client_server com.github.rosjava.tutorial_custom_src.client_server.Talker

rosrun tutorial_custom_srv client_server com.github.rosjava.tutorial_custom_src.client_server.Listener

Vælg 1 hvis du får nogle valgmuligheder.

Åben en terminal og kør: roscore

Åben en terminal og kør: rosrun tutorial_custom_srv client_server com.github.rosjava.tutorial_custom_src.client_server.Talker

Åben en terminal og kør: rosrun tutorial_custom_srv client_server com.github.rosjava.tutorial_custom_src.client_server.Listener

Ok du burde nu få en masse Helle Word! XX at se.

Prøv at gå ind i filen ~/rosjava_srv_ws/src/tutorial_custom_src/client_server/src/main/java/com/github/tutorial_custom_src/client_server/Listener.java og ret så der under log.info nu står en anden tekst. Efter det er gjort compile koden ved at bruge catkin_make i mappen ~/rosjava_srv_ws/

Ok lad os prøve at rette vores Talker og Listener så den kanal de bruger nu hedder robo2019 og den sender en Int32 (se https://wiki.ros.org/std_msgs) i kanalen istedet for en string.

I Listener.java ret så 
Subscriber<std_msgs.String> subscriber = connectedNode.newSubscriber("chatter", st_msgs.String,_TYPE); bliver til 

Subscriber<std_msgs.Int32> subscriber = connectedNote.newSubscriber("Robo2019", st_msgs.Int32,_TYPE); 

Ret så der andre steder står std_msgs.Int32 istedet for std_msgs.String

Og konverter message.getData() til en int via Integer.toString(message.getData()) så vi kan udskrive denne.

I Talker.java ret så
final Publisher<std_msgs.String> publisher = connectedNote.newSubscriber("chatter", st_msgs.String,_TYPE); bliver til 

final Publisher<std_msgs.Int32> publisher = connectedNote.newSubscriber("Robo2019", st_msgs.Int32,_TYPE);

std_msgs.String str = publisher.newMessage();
str.setData("Hello world! " + sequenceNumber); 
publisher.publish(str);

bliver til

std_msgs.Int32 i = publisher.newMessage();
i.setData(sequenceNumber); 
publisher.pusblish(i);

Compile koden ved at kør catkin_make

Prøv at køre koden nu ved at åbne 3 terminaler

terminal1: roscore

terminal2: rosrun tutorial_custom_srv client_server com.github.rosjava.tutorial_custom_src.client_server.Talker

terminal3: rosrun tutorial_custom_srv client_server com.github.rosjava.tutorial_custom_src.client_server.Listener



