# ws-proxy-server
The program I wrote because I had nothing else to do, it allows you to connect to a remote websoket, this program can be used in different ways, I just gave you an example of my code. 

# Example of use:
This is not a call to action! Do everything at your own risk! In this example I have a MySQL database on (mysql's mypy) and port 3306, the problem is that I can't connect to it because my ISP has blocked connections to (mysql's mypy), since it's too easy to use vpn I went the other way and wrote a code that will pass connections and data through another mypy address, the code is quite simple and easy to read. I compiled Main.java into proxy.jar and sent it to a friend to run with java -jar proxy.jar, the friend did it and now I can connect to (mysql IP) from my computer where (mysql IP) is blocked.

My configuration that I gave to my friend:
<br>
**(config.properties):**
<br>
`proxyPort = 40000`
<br>
`targetAddress = (mysql IP)`
<br>
`targetPort = 3306`

If you don't have such a friend, you can find any java hosting service and run the proxy there, as long as it doesn't violate the hosting rules!

# Dependencies
- java 8+
- open port
- white ip-adress

# Сompiling
`javac -source 8 -target 8 Main.java`
<br>
`jar cvfm proxy.jar MANIFEST.MF *.class`

# Starting
`java -jar server.jar`
