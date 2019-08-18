# What is this?

Single Player RestMud Basic Test Game version 1.0

Written by:

* Alan Richardson
* Compendium Developments Ltd [Compendiumdev.co.uk](http://compendiumdev.co.uk)
* [EvilTester.com](http://eviltester.com)

Copyright 2016, Compendium Developments Ltd

This game is used to practice:

*  some of the skills taught in [Technical Web Testing 101](http://compendiumdev.co.uk/page/techweb101course)
    * An online training course that teaches the basics of Technical Web Testing
* skills taught in Hands on Workshops that could help you and your team
    * improve your technical testing - [contact Alan](http://compendiumdev.co.uk/page/contact_us) to arrange a training session for you team 


## This is a Text Adventure

This is a single player Text Adventure Game written to help people improve their Web and REST testing skills.

Feel free to use it to practice:

* Browser Development Tools
* URL amendment
* REST Interactive testing
* Automating GUI
* Automating REST Interfaces

## What is a text adventure?

* [https://en.wikipedia.org/wiki/Interactive_fiction](https://en.wikipedia.org/wiki/Interactive_fiction)

Basically a game where the GUI is 'text' and you 'type in text commands' to play the game.

## How do I play?

In this text adventure the GUI is 'slightly different' and inconsistent (this is by design):

* Some commands are obvious:
    * you click in the browser to do stuff: take collectables, examine things, etc.
* Some commands can not be entered from the GUI
    * You may have to amend the url to `go/s` if an exit is not listed on screen
* Some commands are not listed
    * Read the messages on screen, you may be told about new verbs you can use when you take objects or examine objects etc.
* Some objects are not listed
    * You may have to use the Browser developer tools to learn how to interact with objects in the location

Part of playing a text adventure is exploring the 'game' to learn how to 'play' the game.

There are puzzles, some are obvious, some are puzzling.

There is also a maze.

There is no end to this game. You have to decide when you are finished. A bit like testing really. I'd suggest you aim for a high score. On my last play test I scored 1190 points - but I made a mistake, so I could have scored a little more. If you get up to that amount of points then chances are you've 'won' the game. But you may not have squeezed all the learning out of it yet.

There may well be bugs.

The game is small so there is no way to save the game. You might want to learn to make notes so that you can replay the game.

You can find some more information on the help page:

* [http://localhost:4567/player/user/help](http://localhost:4567/player/user/help)

# How to Download and Install

Download:

* [http://compendiumdev.co.uk/downloads/games/restmud/spbasic_v1.zip](http://compendiumdev.co.uk/downloads/games/restmud/spbasic_v1.zip)

Unzip the download file and you will see:

* `readme.md` - this file
* `readme.pdf` - this file as a pdf
* `restmudSingleUser_BasicTestGame_v_1_0.jar` - the game file

# Requirements

You need to have:

* Java JRE 1.8 or JDK 1.8 installed

How do I know?

Type `java -version` at the command line.

If it says something like:

~~~~~~~~
C:\>java -version
java version "1.8.0_45"
Java(TM) SE Runtime Environment (build 1.8.0_45-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.45-b02, mixed mode)
~~~~~~~~

Then you would be good to go. If your Java version is less than 1.8 then expect to see errors when you try to run it.


# How to start

From the command line, type:

`java -jar restmudSingleUser_BasicTestGame_v_1_0.jar`

We created a specifically catchy filename to prepare you for a funtastic time ahead.

You will see something like:

~~~~~~~~
D:\games\>java -jar restmudSingleUser_BasicTestGame_v_1_0.jar
SECRET GAME CODE for REGISTRATION is upomtxhipg
Adding user You(user)
Added user You(user)
[Thread-0] INFO org.eclipse.jetty.util.log - Logging initialized @671ms
[Thread-0] INFO spark.webserver.JettySparkServer - == Spark has ignited ...
[Thread-0] INFO spark.webserver.JettySparkServer - >> Listening on 0.0.0.0:4567
[Thread-0] INFO org.eclipse.jetty.server.Server - jetty-9.3.z-SNAPSHOT
[Thread-0] INFO org.eclipse.jetty.server.ServerConnector - Started ServerConnector@b41a7da{HTTP/1.1,[http/1.1]}{0.0.0.0:4567}
[Thread-0] INFO org.eclipse.jetty.server.Server - Started @787ms
~~~~~~~~

The HTTP server is now running on port `4567`.

In a browser visit `localhost:4567/player/user/look` and you can start playing the game.

* [http://localhost:4567/player/user/look](http://localhost:4567/player/user/look)


# I need more help than that

OK, visit [compendiumdev.co.uk/page/restmud](http://compendiumdev.co.uk/page/restmud) and we might have more documentation or released some videos or help or something.

* [http://compendiumdev.co.uk/page/restmud](http://compendiumdev.co.uk/page/restmud)

But we might not. Adventure games are renowned for not being player friendly and for making the player do some work to figure out how to play the game. That is one reason why we play them.

# API

Yes, there is also an API.

Ah, bold adventurer, you want to know more?

I use [postman](https://www.getpostman.com/) for most of my interactive REST API usage.

* GET
    * `http://localhost:4567/api/player/user/<verb>`
    * `http://localhost:4567/api/player/user/<verb>/<nounphrase>`
* POST
    * `http://localhost:4567/api/player/user`
        * `{"verb":"<verb>"}`
        * `{"verb":"<verb>","nounphrase":"<nounphrase>"}`

# Comments

If wish to contact us then feel free to [contact us](http://compendiumdev.co.uk/page/contact_us)

* [http://compendiumdev.co.uk/page/contact_us](http://compendiumdev.co.uk/page/contact_us)

Suggested topics of contact:

* Hey, this is a great game. Thanks. I'm stuck, how do I ...?
* Hey, this is great, I can really see how this is a fun way to train people, we have a budget, when can you [come and train us](http://compendiumdev.co.uk/page/contact_us)? 
* Hey. Thanks. I've [bought all your books](http://compendiumdev.co.uk/page/books) and [bought all your online training courses](http://compendiumdev.co.uk/page/online_training) because I played the game.

* Alan, I'm a Test Manager and want to use this game for an in-house lunch time training session, [what should I do](http://compendiumdev.co.uk/page/contact_us)?
* Alan, I train people in stuff, and want to base a course on your material, can we [agree commercial terms](http://compendiumdev.co.uk/page/contact_us)? 

# About the Author

Alan Richardson has more than twenty years of professional IT experience, working as a programmer and at every level of the testing hierarchy from tester through head of testing. He has performed [keynote speaches and tutorials at conferences](http://compendiumdev.co.uk/page/conferences) worldwide. Author of the books "[Dear Evil Tester](http://compendiumdev.co.uk/page/deareviltester)", "[Java For Testers](http://compendiumdev.co.uk/page/javafortestersbook)" and "[Selenium Simplified](http://compendiumdev.co.uk/selenium/)". Alan also has created [online training courses](http://compendiumdev.co.uk/page/online_training) to help people learn [Technical Web Testing](http://compendiumdev.co.uk/page/techweb101course) and [Selenium WebDriver with Java](http://compendiumdev.co.uk/page/seleniumwebdrivercourse). He works as an [independent consultant](http://compendiumdev.co.uk/page/consultancy), helping companies improve their use of automation, agile, and exploratory technical testing. Alan posts his writing and training videos on [SeleniumSimplified.com](http://SeleniumSimplified.com), [EvilTester.com](http://EvilTester.com), [JavaForTesters.com](http://JavaForTesters.com), and [CompendiumDev.co.uk](http://CompendiumDev.co.uk). 

# License

This game is released for free. For your pleasure and education. If you don't enjoy it then you have broken the license and should stop playing immediately. If you don't learn anything then tough, you broke the license, not my fault.

This game is released as a single player game. That means you play it on your own. If you play it in some strange multi-player mode, then you have broken the license and should stop playing immediately, or don't, I can't really stop you.

If you decompile this game then you do so at your own risk. I can not be held responsible if you see code you don't like or if it allows you to finish the game.

This game should not be used in any commercial training courses, so if you are playing this game during a training course that you have paid for then you should point this out to the instructor because the only people allowed to do that with impunity are the author (Alan Richardson, Compendium Developments Ltd. http://compendiumdev.co.uk) or instructors who have contacted the author and gained permission to use this in their training courses. Please ask for permission. Again, I can't stop you, but the Karma gods will know, and you will know that they know, and they will know that you know that they know, and you don't know what they know they will do because you know that they know that you know; safer just to ask for permission really.

We take no responsibility for anything that happens when you run this code. Nothing. If you experience nightmares or if your printer starts printing strange sigils at midnight, none of that. If you're concerned then decompile the code. It's just an adventure game running as an HTTP server.

For more information about the license, read this licence again.

# Tech Notes

- BasicTestGameGenerator
- v 124
- 20160609
- using SinglePlayerRestMud and SinglePlayerAuthenticator
- Spark 2.3, Gson 2.5, JDK 1.8, JUnit 4.12