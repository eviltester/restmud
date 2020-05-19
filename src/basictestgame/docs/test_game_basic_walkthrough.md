
# Walkthrough and Hints for test_game_basic

## Warning

Using this game walkthrough will prevent you from investigating the game fully and learning what you can. Also note that I am not a professional hint and walkthrough writer so there is no guarantee that you an use this document without spoilage. I suggest you: read the hints first, dip into and out of the walkthrough, map your won map, only use the map here as a last resort.
This walkthrough is automatically generated from a 'test'. It is created as the game is played. So the output you see here is what the game provides, if you are doing something different then you are playing a different version of the game, a different game, or doing something wrong

* for more information on the game visit [The Official RestMud HomePage](http://compendiumdev.co.uk/page/restmud)


## Hints

* examine all signs
* look in description html for 'spans' that are examinable
* if you take something that isn't yours then confess quickly
* use things with ids
* map the maze
* if the game says to do something then do it e.g. 'wobble' or 'confess'
* secret ways often mean points
* some secret ways never run out of points
* polish before hoarding

## Walkthrough


 _we start in room 1_ 


`look `

> `You look.`

> `1` : `The Central Test Room`

> `This is the room in the center of the test game` 

> I can see some things here:

* `A sign on the wall` (`ahint`)

> ... messages ...

* The Wizard Says: Welcome to the Basic Test Game

 _I always examine signs on walls_ 


`examine ahint`

> `You Examine: A sign on the wall ... I think you can go south near the strong room.`

## Room 2 is dark


 _north leads into room 2_ 


`go n`

> `You go n`

> `2` : `The Dark room in the north`

> `It is too dark to see` 

 _oh oh, it is dark here_ 


`look `

> `You look.`

> `2` : `The Dark room in the north`

 _amend the url to go back south /go/s_ 


`go s`

> `You go s`

> `1` : `The Central Test Room`

> I can see some things here:

* `A sign on the wall` (`ahint`)

 _to get back to room 1_ 


`look `

> `You look.`

> `1` : `The Central Test Room`

> I can see some things here:

* `A sign on the wall` (`ahint`)

## Room 3 is more than it seems - so come back later


 _east leads into room 3 - east room_ 


`go e`

> `You go e`

> `3` : `The East Room`

> `This is the room in the east` 

> I can see some things here:

* `A lever on the wall` (`mazelever`)
* `A Sign` (`mazesign`)

 _not much to do here at the moment_ 


`look `

> `You look.`

> `3` : `The East Room`

> I can see some things here:

* `A lever on the wall` (`mazelever`)
* `A Sign` (`mazesign`)

 _I can't go anywhere yet._ 


`go e`

> `i can't go that way. what makes you think i can go that way!`

> `3` : `The East Room`

> I can see some things here:

* `A lever on the wall` (`mazelever`)
* `A Sign` (`mazesign`)

 _I'll examine that sign though_ 


`examine mazesign`

> `You Examine: A Sign ... This way to the maze. Are you ready to map your way out of danger? Use the lever if you dare`
I'm not ready to use the mazesign yet, I'll do that later

 _I will come back here later_ 


`go w`

> `You go w`

> `1` : `The Central Test Room`

> I can see some things here:

* `A sign on the wall` (`ahint`)

`look `

> `You look.`

> `1` : `The Central Test Room`

> I can see some things here:

* `A sign on the wall` (`ahint`)

## Room 4 has a door


`go s`

> `You go s`

> `4` : `The South Room`

> `This is the room in the South` 

> I can see some things here:

* `A sign on the wall` (`adirsign`)

 _I need to open the door before I go east_ 


`go e`

> `You can't go e because the door is closed`

> `4` : `The South Room`

> I can see some things here:

* `A sign on the wall` (`adirsign`)

`open e`

> `OK, you open the door`

> `4` : `The South Room`

> I can see some things here:

* `A sign on the wall` (`adirsign`)

`go e`

> `You go e through the door`

> `6` : `The West Corridor`

> `This is the western corridor` 

`look `

> `You look.`

> `6` : `The West Corridor`

 _Hmm, can't go east no more_ 


`go w`

> `You go w through the door`

> `4` : `The South Room`

> I can see some things here:

* `A sign on the wall` (`adirsign`)

`look `

> `You look.`

> `4` : `The South Room`

> I can see some things here:

* `A sign on the wall` (`adirsign`)

 _I wonder what the sign says_ 


`examine adirsign`

> `You Examine: A sign on the wall ... Since this is room four, I'll tell you some more, you can go east when you think you can't go east no more, and then you can read this sign some more.`

 _I'll come back later then_ 


`go n`

> `You go n`

> `1` : `The Central Test Room`

> I can see some things here:

* `A sign on the wall` (`ahint`)

## Room 5 is a hoard room


`look `

> `You look.`

> `1` : `The Central Test Room`

> I can see some things here:

* `A sign on the wall` (`ahint`)

 _west leads into room 5 - hoard room_ 


`go w`

> `You go w`

> `5` : `The West Hoard Room`

> `This is the room in the West where everyone stores all their stuff, there is a strong room door to the west` 

> I can see some things here:

* `A sign on the wall` (`hintsigne`)

 _I do not have anything to hoard though._ 


`look `

> `You look.`

> `5` : `The West Hoard Room`

> I can see some things here:

* `A sign on the wall` (`hintsigne`)

 _I'll examine that sign though_ 


`examine hintsigne`

> `You Examine: A sign on the wall ... Shh, GO Easily, I think I see a secret way in Room '3'. Shhh.`

 _Hmm, let me visit room 3 again then_ 


`go e`

> `You go e`

> `1` : `The Central Test Room`

> I can see some things here:

* `A sign on the wall` (`ahint`)

`go e`

> `You go e`

> `3` : `The East Room`

> I can see some things here:

* `A lever on the wall` (`mazelever`)
* `A Sign` (`mazesign`)

## Room 3 is so much more than it seemed now


 _but I can't see anything_ 


`look `

> `You look.`

> `3` : `The East Room`

> I can see some things here:

* `A lever on the wall` (`mazelever`)
* `A Sign` (`mazesign`)

 _I will trust the sign_ 


`go e`

> `You go e`

> `7` : `The Secret East Room`

> `This is the secret room in the east, there is a lever on the wall` 

> I can see some things here:

* `A Lever on the wall` (`alever`)

> ... messages ...

* hey, that was strange, i didn't see the exit, it must have been an optical illusion or something.
* your exploration earned you some points on your score

## Oooh, a secret room


`look `

> `You look.`

> `7` : `The Secret East Room`

> I can see some things here:

* `A Lever on the wall` (`alever`)

 _I can't wobble levers_ 


`wobble alever`

> `I don't know how to "wobble alever" here`

 _Examine everything_ 


`examine alever`

> `You Examine: A Lever on the wall ... The lever has a <span id='leverbutton' class='locationobject'>button</span> with a fingerprint worn on it from much use.`

 _Examine everything_ 


`examine leverbutton`

> `You Examine: A button on a lever ... The button looks like it has been worn down over centuries of use. Or perhaps it was just a really cheap button.`

 _Examine everything_ 


`use leverbutton`

> `you press the button on the lever. you hear a squeal of wood and metal as something opens somewhere.`

 _Not sure what opened where, but I'll take the torch just in case_ 


`take torch_1`

> `You took: torch_1. You now have the A Torch of Illumination  , oooh, and you now have the ability to 'illuminate' and 'darken'  (power=10).`

 _I think I'm done here_ 


`go w`

> `You go w`

> `3` : `The East Room`

> I can see some things here:

* `A lever on the wall` (`mazelever`)
* `A Sign` (`mazesign`)

 _I think I'm done here_ 


`go w`

> `You go w`

> `1` : `The Central Test Room`

> I can see some things here:

* `A sign on the wall` (`ahint`)

 _I think I'm done here_ 


`go s`

> `You go s`

> `4` : `The South Room`

> I can see some things here:

* `A sign on the wall` (`adirsign`)

 _I already opened this door_ 


`go e`

> `You go e through the door`

> `6` : `The West Corridor`

`look `

> `You look.`

> `6` : `The West Corridor`

## The lever button opened a secret door


 _I wonder how my score is doing_ 


`score `

> `Your Score is: 10`

`go e`

> `You go e through Secret Panel. And Secret Panel closes behind you.`

> `8` : `The Room behind the gate in the East`

> `This is the room behind the secret gate, you see no reason for the fuss` 

> I can see some things here:

* `A wobbly switch on the wall` (`wobblyswitch`)

`look `

> `You look.`

> `8` : `The Room behind the gate in the East`

> I can see some things here:

* `A wobbly switch on the wall` (`wobblyswitch`)

`examine wobblyswitch`

> `You Examine: A wobbly switch on the wall ... Someone has written something beside the switch, it looks like it says 'wobble me'`

 _It says wobble it, I'll 'wobble' it_ 


`wobble wobblyswitch`

> `you wobble the switch - and your score increased. but i don't think you should try that again, the wiz might not like that.`

 _My score should be more_ 


`score `

> `Your Score is: 30`

 _I'll be on my way then_ 


`go e`

> `You go e`

> `9` : `An Eastern Corridor`

> `This corridor goes east` 

> ... messages ...

* you can see <span id='atinypipe' class='collectable'>a tiny pipe on the floor</span> on the floor

## Elves don't like people taking their stuff


`look `

> `You look.`

> `9` : `An Eastern Corridor`

> ... messages ...

* you can see <span id='atinypipe' class='collectable'>a tiny pipe on the floor</span> on the floor

`examine atinypipe`

> `you look at the tiny pipe. it appears to have 'this is mine. do not touch. signed tiny elf' written on it in tiny writing, to be honest, it is so tiny that you could almost claim to have not seen the writing.`

> ... messages ...

* you can see <span id='atinypipe' class='collectable'>a tiny pipe on the floor</span> on the floor

 _Oooh, I'll feel guilty but I'll take it_ 


`take atinypipe`

> `you bend over and pick up the tiny pipe, it vanishes before you can put it in your pocket. oh well, easy come, easy go.`

> ... messages ...

* you feel a bit guilty about taking the elf's pipe, but since it disappeared you feel less worried. but still a bit worried.

 _I'll wander about for a while_ 


`go e`

> `You go e`

> `10` : `Another Eastern Corridor`

> `This corridor goes east and west` 

> I can see some things here:

* `A sign on the wall` (`asinsign`)

> ... messages ...

* you feel a bit guilty about taking the elf's pipe, but since it disappeared you feel less worried. but still a bit worried.

## I wish I could be forgiven 


`look `

> `You look.`

> `10` : `Another Eastern Corridor`

> I can see some things here:

* `A sign on the wall` (`asinsign`)

> ... messages ...

* you feel a bit guilty about taking the elf's pipe, but since it disappeared you feel less worried. but still a bit worried.

 _This looks promising_ 


`examine asinsign`

> `You Examine: A sign on the wall ... confess <insert your sinning item here> and all will be forgiven. Or don't and it won't. Up to you. You smell like poo!`

> ... messages ...

* you feel a bit guilty about taking the elf's pipe, but since it disappeared you feel less worried. but still a bit worried.

 _This looks promising_ 


`confess atinypipe`

> `you confess your sin of taking the tiny pipe to the sign with an angry face. a tiny elf appears and says 'i forgive you, but do not do it again' and the elf hits you on the nose with a tiny feather. ow, that really hurt. what was the feather made out of? steel? the elf says 'ha, do it again and i hit you with something other than my magic lead feather of much pain'. the elf disappears, leaving you and your pain to wallow in your guilt. happy days.`

> ... messages ...

* you feel as though your score has increased.

 _Who says crime doesn't pay - bwahahahah!_ 


`score `

> `Your Score is: 40`

## I can go no further east - or can I ?


`go e`

> `You go e`

> `11` : `Another Eastern Corridor but this one is really dark`

> `It is too dark to see` 

 _Too dark too see though_ 


`look `

> `You look.`

> `11` : `Another Eastern Corridor but this one is really dark`

 _I should light my torch_ 


`illuminate `

> `Good work. You illuminated the 'A Torch of Illumination'. Your 'A Torch of Illumination' has 10 power left.`

> `11` : `Another Eastern Corridor but this one is really dark`

> ... messages ...

* Your 'A Torch of Illumination' is working and now 'torch_1' has 9 power

 _I can see now_ 


`look `

> `You look.`

> `11` : `Another Eastern Corridor but this one is really dark`

> ... messages ...

* Your 'A Torch of Illumination' is working and now 'torch_1' has 8 power

 _The sign said I could go east_ 


`go e`

> `You go e`

> `4` : `The South Room`

> I can see some things here:

* `A sign on the wall` (`adirsign`)

> ... messages ...

* Your 'A Torch of Illumination' is working and now 'torch_1' has 7 power
* ooer, that felt funny

 _Not sure what happened there_ 


`score `

> `Your Score is: 50`

 _I will save my torch until I need it again_ 


`darken `

> `Good work. You extinguished the 'A Torch of Illumination'. Your 'A Torch of Illumination' has 7 power left.`

> `4` : `The South Room`

> I can see some things here:

* `A sign on the wall` (`adirsign`)

 _That must have been a teleporter_ 


`look `

> `You look.`

> `4` : `The South Room`

> I can see some things here:

* `A sign on the wall` (`adirsign`)

## I am ready for the maze


`go n`

> `You go n`

> `1` : `The Central Test Room`

> I can see some things here:

* `A sign on the wall` (`ahint`)

`go e`

> `You go e`

> `3` : `The East Room`

> I can see some things here:

* `A lever on the wall` (`mazelever`)
* `A Sign` (`mazesign`)

`use mazelever`

> `you use the lever and zap yourself into a maze off the beaten path. oops. i hope you know how to get out of here.`

`look `

> `You look.`

> `14` : `The Start of the Maze`

> `You are in a maze of twisty tiny passages constructed by a loony who is experimenting with his game engine, this can't be good.` 

> I can see some things here:

* `A helpful sign` (`startmazesign`)

`go w`

> `You go w`

> `17` : `In the dark part of the maze`

> `It is too dark to see` 

 _too dark_ 


`look `

> `You look.`

> `17` : `In the dark part of the maze`

 _I should light my torch_ 


`illuminate `

> `Good work. You illuminated the 'A Torch of Illumination'. Your 'A Torch of Illumination' has 7 power left.`

> `17` : `In the dark part of the maze`

> I can see some things here:

* `A dark lever` (`amazelever17`)

> ... messages ...

* Your 'A Torch of Illumination' is working and now 'torch_1' has 6 power

 _And see what I can see_ 


`look `

> `You look.`

> `17` : `In the dark part of the maze`

> I can see some things here:

* `A dark lever` (`amazelever17`)

> ... messages ...

* Your 'A Torch of Illumination' is working and now 'torch_1' has 5 power

`use amazelever17`

> `you use the lever and find yourself in new set of twisty tiny passages`

> ... messages ...

* Your 'A Torch of Illumination' is working and now 'torch_1' has 4 power
* ah, this looks more promising.

`look `

> `You look.`

> `18` : `Near the end of the maze`

> `You are near the end of the maze.` 

> I can see some things here:

* `A sign` (`endmazebuttonsign`)
* `A button` (`endmazebutton`)

> ... messages ...

* Your 'A Torch of Illumination' is working and now 'torch_1' has 3 power

 _Oooh a sign_ 


`examine endmazebuttonsign`

> `You Examine: A sign ... The end maze button does what you think it would do if you use it. Bit of a let down that. But not if you found the secret of the maze, though. You did find it RIGHT?`

> ... messages ...

* Your 'A Torch of Illumination' is working and now 'torch_1' has 2 power

 _Oooh a button_ 


`examine endmazebutton`

> `You Examine: A button ... The shiny red button in front of you screams 'do not push me'. Literally. It was quite loud.`

> ... messages ...

* Your 'A Torch of Illumination' is working and now 'torch_1' has 1 power

`go e`

> `You go e`

> `20` : `The secret of the maze!`

> `You are in an empty room.` 

> ... messages ...

* Your 'A Torch of Illumination' is working and now 'torch_1' has 0 power
* Your 'A Torch of Illumination' has 0 power left and has disappeared
* you found the secret. if you are first to find the secret then there is a prize. if not, then just think of all the work you did to increase your score by 300 points. yes 300!!!! points. how much is the prize worth you wonder?

 _And see what I can see_ 


`take the_secret_of_the_maze_prize`

> `You took: the_secret_of_the_maze_prize. You now have the A very prize like prize `

`go w`

> `You go w`

> `18` : `Near the end of the maze`

> I can see some things here:

* `A sign` (`endmazebuttonsign`)
* `A button` (`endmazebutton`)

`use endmazebutton`

> `you escaped from the maze like a... ehm... an... umm... maze escaper person`

> ... messages ...

* and you finish the maze.

`look `

> `You look.`

> `3` : `The East Room`

> I can see some things here:

* `A lever on the wall` (`mazelever`)
* `A Sign` (`mazesign`)

## I should hoard my treasure


`go w`

> `You go w`

> `1` : `The Central Test Room`

> I can see some things here:

* `A sign on the wall` (`ahint`)

`go w`

> `You go w`

> `5` : `The West Hoard Room`

> I can see some things here:

* `A sign on the wall` (`hintsigne`)

`go s`

> `You go s`

> `12` : `A secret area`

> `A secret area must be around here somewhere. Oh wait a minute, this is it.` 

> I can see some things here:

* `A Torch Dispenser` (`torchdispenser`)

 _I can get a new torch_ 


`use torchdispenser`

> `OK, you use 'A Torch Dispenser' and you see it dispense '<span id='torch_2'>A Dispensed Torch</span>'`

 _But torch isn't treasure_ 


`take torch_2`

> `You took: torch_2. You now have the A Dispensed Torch  , oooh, and you now have the ability to 'illuminate' and 'darken'  (power=18).`

`go n`

> `You go n`

> `5` : `The West Hoard Room`

> I can see some things here:

* `A sign on the wall` (`hintsigne`)

`go e`

> `You go e`

> `1` : `The Central Test Room`

> I can see some things here:

* `A sign on the wall` (`ahint`)

`go n`

> `You go n`

> `2` : `The Dark room in the north`

 _I should light my torch and see_ 


`illuminate `

> `Good work. You illuminated the 'A Dispensed Torch'. Your 'A Dispensed Torch' has 18 power left.`

> `2` : `The Dark room in the north`

`look `

> `You look.`

> `2` : `The Dark room in the north`

`take cloth_1`

> `You took: cloth_1. You now have the A Cloth of Shining  , oooh, and you now have the ability to 'polish' things (power=200).`

`go s`

> `You go s`

> `1` : `The Central Test Room`

> I can see some things here:

* `A sign on the wall` (`ahint`)

`go w`

> `You go w`

> `5` : `The West Hoard Room`

> I can see some things here:

* `A sign on the wall` (`hintsigne`)

`polish the_secret_of_the_maze_prize`

> `Good work. You polished 'A very prize like prize' by 35 and now it is worth 435 hoard points. Your 'A Cloth of Shining' has 165 polish power left. `

`polish the_secret_of_the_maze_prize`

> `Good work. You polished 'A very prize like prize' by 20 and now it is worth 455 hoard points. Your 'A Cloth of Shining' has 145 polish power left. `

`polish the_secret_of_the_maze_prize`

> `Good work. You polished 'A very prize like prize' by 11 and now it is worth 466 hoard points. Your 'A Cloth of Shining' has 134 polish power left. `

`polish the_secret_of_the_maze_prize`

> `Good work. You polished 'A very prize like prize' by 34 and now it is worth 500 hoard points. Your 'A Cloth of Shining' has 100 polish power left. `

`polish the_secret_of_the_maze_prize`

> `Good work. You polished 'A very prize like prize' by 20 and now it is worth 520 hoard points. Your 'A Cloth of Shining' has 80 polish power left. `

`polish the_secret_of_the_maze_prize`

> `Good work. You polished 'A very prize like prize' by 18 and now it is worth 538 hoard points. Your 'A Cloth of Shining' has 62 polish power left. `

`polish the_secret_of_the_maze_prize`

> `Good work. You polished 'A very prize like prize' by 20 and now it is worth 558 hoard points. Your 'A Cloth of Shining' has 42 polish power left. `

`polish the_secret_of_the_maze_prize`

> `Good work. You polished 'A very prize like prize' by 5 and now it is worth 563 hoard points. Your 'A Cloth of Shining' has 37 polish power left. `

`polish the_secret_of_the_maze_prize`

> `Good work. You polished 'A very prize like prize' by 7 and now it is worth 570 hoard points. Your 'A Cloth of Shining' has 30 polish power left. `

`polish the_secret_of_the_maze_prize`

> `Good work. You polished 'A very prize like prize' by 11 and now it is worth 581 hoard points. Your 'A Cloth of Shining' has 19 polish power left. `

`polish the_secret_of_the_maze_prize`

> `Good work. You polished 'A very prize like prize' by 4 and now it is worth 585 hoard points. Your 'A Cloth of Shining' has 15 polish power left. `

`polish the_secret_of_the_maze_prize`

> `Good work. You polished 'A very prize like prize' by 2 and now it is worth 587 hoard points. Your 'A Cloth of Shining' has 13 polish power left. `

`polish the_secret_of_the_maze_prize`

> `Good work. You polished 'A very prize like prize' by 6 and now it is worth 593 hoard points. Your 'A Cloth of Shining' has 7 polish power left. `

`polish the_secret_of_the_maze_prize`

> `Good work. You polished 'A very prize like prize' by 7 and now it is worth 600 hoard points. Your 'A Cloth of Shining' has 0 polish power left.  Your 'A Cloth of Shining' vanishes, it must have been magic.`

`polish the_secret_of_the_maze_prize`

> `You try to polish it with your hand but it doesn't work. You need to carry something that let's you polish stuff!`

`hoard the_secret_of_the_maze_prize`

> `You hoarded: the_secret_of_the_maze_prize [scored 600]`

 _High Score!_ 


`score `

> `Your Score is: 980`

## There must be more treasure


`go s`

> `You go s`

> `12` : `A secret area`

> I can see some things here:

* `A Torch Dispenser` (`torchdispenser`)

 _This is more like it_ 


`go e`

> `You go e`

> `13` : `A secret treasure stash`

> `Oh, this is the secret treasure stash.` 

`take shiny_gold_ring`

> `You took: shiny_gold_ring. You now have the A shiny gold ring `

`go w`

> `You go w`

> `12` : `A secret area`

> I can see some things here:

* `A Torch Dispenser` (`torchdispenser`)

`go n`

> `You go n`

> `5` : `The West Hoard Room`

> I can see some things here:

* `A sign on the wall` (`hintsigne`)

`hoard shiny_gold_ring`

> `You hoarded: shiny_gold_ring [scored 200]`

 _Higher Score!_ 


`score `

> `Your Score is: 1180`
That is about all I can do. So I'll stop now. Or is it!

## Map


![](basicTestGame.png)

---
