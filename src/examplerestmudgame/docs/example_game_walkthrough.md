
# Walkthrough and Hints for example_game

## Warning

Using this game walkthrough will prevent you from investigating the game fully and learning what you can. Also note that I am not a professional hint and walkthrough writer so there is no guarantee that you an use this document without spoilage. I suggest you: read the hints first, dip into and out of the walkthrough, map your won map, only use the map here as a last resort.
This walkthrough is automatically generated from a 'test'. It is created as the game is played. So the output you see here is what the game provides, if you are doing something different then you are playing a different version of the game, a different game, or doing something wrong

## Hints

* use the corridorbutton
* hoard treasure not junk
* the zapherebutton zaps the same treasure to the location
* this is a static map so is easy to automate
* this is a good map to switch off the GUI and just use the api

## Map


![](exampleDocumentedGame.png)


## Walkthrough


 _we start in room 1_ 


`look `

> `You Look.`

> `1` : `The Start Test Room`

> `This is the room where a user starts` 

> I can see some things here:

* `A Teleporter Button To go to the corridor of test rooms` (`corridorbutton`)

> ... messages ...

* The Wizard Says: Welcome to the Example Test Game

 _north leads into room 2_ 


`go n`

> `You go n`

> `2` : `The North Room`

> `This is the room north of the start room` 

`look `

> `You Look.`

> `2` : `The North Room`

 _nothing here, we can go back to room 1_ 


`go s`

> `You go s`

> `1` : `The Start Test Room`

> I can see some things here:

* `A Teleporter Button To go to the corridor of test rooms` (`corridorbutton`)

`look `

> `You Look.`

> `1` : `The Start Test Room`

> I can see some things here:

* `A Teleporter Button To go to the corridor of test rooms` (`corridorbutton`)

 _we can go east into room 3_ 


`go e`

> `You go e`

> `3` : `The East Room`

> `This is the room east of the start room` 

`look `

> `You Look.`

> `3` : `The East Room`

 _game says we can go south, but we can't really_ 


`go s`

> `I can't go that way at the moment`

> `3` : `The East Room`

 _we have to go west back to the start room_ 


`go w`

> `You go w`

> `1` : `The Start Test Room`

> I can see some things here:

* `A Teleporter Button To go to the corridor of test rooms` (`corridorbutton`)

`look `

> `You Look.`

> `1` : `The Start Test Room`

> I can see some things here:

* `A Teleporter Button To go to the corridor of test rooms` (`corridorbutton`)

 _we can go west into room 5_ 


`go w`

> `You go w`

> `5` : `The West Room`

> `This is the room West of the start room` 

> I can see some things here:

* `A Teleporter Button` (`teleporterbutton`)

 _oooh a button, use the button to start the teleporter in the south_ 


`look `

> `You Look.`

> `5` : `The West Room`

> I can see some things here:

* `A Teleporter Button` (`teleporterbutton`)

`examine teleporterbutton`

> `You Examine: A Teleporter Button ... Use the button to switch on the teleporter`

 _use the button to start the teleporter in the south_ 


`use teleporterbutton`

> `the teleporter is active now`

> ... messages ...

* you push the button and the teleporter flickers to life scarily. are you sure you want to go through it?

 _go south through the teleporter to go back to the start_ 


`go s`

> `You go s . The teleporter makes an awful screeching noise and you feel as if you are being torn limb from limb. Luckily you arrive safely and clamber out the teleporter. And the teleporter closes behind you.`

> `1` : `The Start Test Room`

> I can see some things here:

* `A Teleporter Button To go to the corridor of test rooms` (`corridorbutton`)

`look `

> `You Look.`

> `1` : `The Start Test Room`

> I can see some things here:

* `A Teleporter Button To go to the corridor of test rooms` (`corridorbutton`)

 _go west to get back to the telerporter room_ 


`go w`

> `You go w`

> `5` : `The West Room`

> I can see some things here:

* `A Teleporter Button` (`teleporterbutton`)

`look `

> `You Look.`

> `5` : `The West Room`

> I can see some things here:

* `A Teleporter Button` (`teleporterbutton`)

 _but the teleporter to the south is not on, we would have to push the button again if we want to go south_ 


`go s`

> `You can't go s because the teleporter is not active`

> `5` : `The West Room`

> I can see some things here:

* `A Teleporter Button` (`teleporterbutton`)

 _I can just walk back to the start room by going east_ 


`go e`

> `You go e`

> `1` : `The Start Test Room`

> I can see some things here:

* `A Teleporter Button To go to the corridor of test rooms` (`corridorbutton`)

`look `

> `You Look.`

> `1` : `The Start Test Room`

> I can see some things here:

* `A Teleporter Button To go to the corridor of test rooms` (`corridorbutton`)

Now I will experiment with the gates.


 _I can't go south because the gate is closed_ 


`go s`

> `You can't go s because the door is closed`

> `1` : `The Start Test Room`

> I can see some things here:

* `A Teleporter Button To go to the corridor of test rooms` (`corridorbutton`)

 _I better open the gate to the south_ 


`open s`

> `OK, you open the door`

> `1` : `The Start Test Room`

> I can see some things here:

* `A Teleporter Button To go to the corridor of test rooms` (`corridorbutton`)

 _Then I can go through_ 


`go s`

> `You go s through the door`

> `4` : `The South Room`

> `This is the room south of the start room` 

`look `

> `You Look.`

> `4` : `The South Room`

 _If I close the gate I can't go back, until I open it again_ 


`close n`

> `OK, you close the door`

> `4` : `The South Room`

`go n`

> `You can't go n because the door is closed`

> `4` : `The South Room`

 _There is a gate to the east_ 


`go e`

> `You can't go e because the door is closed`

> `4` : `The South Room`

 _I have to open it first_ 


`open e`

> `OK, you open the door`

> `4` : `The South Room`

`go e`

> `You go e through the door`

> `6` : `The Room East of the South Room`

> `This is a room south of the start room.` 

`look `

> `You Look.`

> `6` : `The Room East of the South Room`

 _It is a one way gate so I can't close it from this side and it doesn't stop me going west_ 


`close w`

> `There is nothing to close that way`

> `6` : `The Room East of the South Room`

 _I need to go back to the start room to get to the next part of the map_ 


`go w`

> `You go w`

> `4` : `The South Room`

`look `

> `You Look.`

> `4` : `The South Room`

`open n`

> `OK, you open the door`

> `4` : `The South Room`

`go n`

> `You go n through the door`

> `1` : `The Start Test Room`

> I can see some things here:

* `A Teleporter Button To go to the corridor of test rooms` (`corridorbutton`)

`look `

> `You Look.`

> `1` : `The Start Test Room`

> I can see some things here:

* `A Teleporter Button To go to the corridor of test rooms` (`corridorbutton`)

 _I can use the corridorbutton to teleport to the next part of the map_ 


`look `

> `You Look.`

> `1` : `The Start Test Room`

> I can see some things here:

* `A Teleporter Button To go to the corridor of test rooms` (`corridorbutton`)

`examine corridorbutton`

> `You Examine: A Teleporter Button To go to the corridor of test rooms ... Use the button to go to the corridor of test rooms`

`use corridorbutton`

> `you are now in the corridor of test rooms.`

> `corridor` : `The North End of the Corridor of Test Rooms`

> `This is the North end of the corridor, a test room is not off to the east because location 7 does not exist` 

> I can see some things here:

* `A Teleporter Button To go to the start room` (`startroombutton`)

> ... messages ...

* you push the button and are transported to the corridor of test rooms.

`look `

> `You Look.`

> `corridor` : `The North End of the Corridor of Test Rooms`

> I can see some things here:

* `A Teleporter Button To go to the start room` (`startroombutton`)

 _Even though the location says I can go east, that location does not exist_ 


`go e`

> `You can't go e`

> `corridor` : `The North End of the Corridor of Test Rooms`

> I can see some things here:

* `A Teleporter Button To go to the start room` (`startroombutton`)

 _I guess I have to go south_ 


`go s`

> `You go s`

> `8` : `South of The Northern most End of the Corridor of Test Rooms`

> `This is still at the the North end of the corridor, but not the most northern end, a test room is off to the east where we can hoard things` 

`look `

> `You Look.`

> `8` : `South of The Northern most End of the Corridor of Test Rooms`

 _My score is zero so I don't really want to pick up the cursed item_ 


`score `

> `Your Score is: 0`

 _I will go east to a treasure hoard room_ 


`go e`

> `You go e`

> `9` : `The room of hoarding`

> `This is a test room of hoarding` 

`look `

> `You Look.`

> `9` : `The room of hoarding`

 _I will take the shiny treasure that can be hoarded, and hoard it to increase my score_ 


`take treasure1`

> `You took: treasure1. You now have the A shiny treasure `

`hoard treasure1`

> `You hoarded: treasure1 [scored 100]`

 _I will take the shiny treasure that can be hoarded, and hoard it to increase my score_ 


`score `

> `Your Score is: 100`

 _I can take the other treasure, but cannot hoard it, so my score does not increase_ 


`take junk1`

> `You took: junk1. You now have the A shiny thing that can not be hoarded `

`hoard junk1`

> `You hoarded: nothing (junk1 can not be hoarded)`

`score `

> `Your Score is: 100`

 _I am carrying junk now_ 


`inventory `

> `You Are Carrying: A shiny thing that can not be hoarded`

 _I may as well just drop that junk_ 


`drop junk1`

> `You dropped: junk1. You are no longer carrying A shiny thing that can not be hoarded.`

`inventory `

> `You Are Carrying: Nothing.`

 _I will go west and continue to explore to the south_ 


`go w`

> `You go w`

> `8` : `South of The Northern most End of the Corridor of Test Rooms`

`look `

> `You Look.`

> `8` : `South of The Northern most End of the Corridor of Test Rooms`

`go s`

> `You go s`

> `10` : `A corridor`

> `The corridor of collectable zapping` 

> I can see some things here:

* `A Button` (`zapherebutton`)

`look `

> `You Look.`

> `10` : `A corridor`

> I can see some things here:

* `A Button` (`zapherebutton`)

 _That button looks important_ 


`examine zapherebutton`

> `You Examine: A Button ... Use the button to zap the thing`

 _I can use the button to bring a treasure item here_ 


`use zapherebutton`

> `you instantiated greed.`

> `10` : `A corridor`

> I can see some things here:

* `A Button` (`zapherebutton`)

> ... messages ...

* you push the button and the zappable valuable treasure appears.

 _I will now take the treasure_ 


`take treasure2`

> `You took: treasure2. You now have the A valuable treasure `

 _There must be a hoard nearby - try to the east_ 


`go e`

> `You go e`

> `11` : `A room`

> `The room of collectable zapping` 

`look `

> `You Look.`

> `11` : `A room`

`inventory `

> `You Are Carrying: A valuable treasure`

`hoard treasure2`

> `You hoarded: treasure2 [scored 100]`

 _If I keep this up I'll get a super high score_ 


`score `

> `Your Score is: 200`

 _I could to that all day hoarding the same treasure_ 


`go w`

> `You go w`

> `10` : `A corridor`

> I can see some things here:

* `A Button` (`zapherebutton`)

`look `

> `You Look.`

> `10` : `A corridor`

> I can see some things here:

* `A Button` (`zapherebutton`)

`go w`

> `You go w`

> `21` : `A room with a southward view`

> `The room with southernly direction` 

`look `

> `You Look.`

> `21` : `A room with a southward view`

 _I can smell a secret exit to the south_ 


`go s`

> `you walk south where there is no exit and end up in a different room.`

> `10` : `A corridor`

> I can see some things here:

* `A Button` (`zapherebutton`)

 _Let us explore more_ 


`go s`

> `You go s`

> `12` : `A inspect corridor`

> `The corridor of inspection` 

`look `

> `You Look.`

> `12` : `A inspect corridor`

`go e`

> `You go e`

> `13` : `A room`

> `The room of inspection` 

> I can see some things here:

* `A Thing I cannot inspect` (`notinspect`)

`look `

> `You Look.`

> `13` : `A room`

> I can see some things here:

* `A Thing I cannot inspect` (`notinspect`)

 _I need a score to inspect things to learn more about them_ 


`score `

> `Your Score is: 200`

 _I can inspect things I am not carrying_ 


`inspect inspectability1`

> `You inspect inspectability1 at a cost of 19 points`

`take inspecthoardable1`

> `You took: inspecthoardable1. You now have the A valuable treasure `

`inspect inspecthoardable1`

> `You inspect inspecthoardable1 at a cost of 13 points`

 _I will find things I can examine_ 


`go w`

> `You go w`

> `12` : `A inspect corridor`

`go s`

> `You go s`

> `14` : `An examine corridor`

> `The corridor of examining` 

`look `

> `You Look.`

> `14` : `An examine corridor`

`go e`

> `You go e`

> `15` : `A room of examining things`

> `The room of examining` 

> I can see some things here:

* `A thing to examine` (`examineme`)

`look `

> `You Look.`

> `15` : `A room of examining things`

> I can see some things here:

* `A thing to examine` (`examineme`)

`examine examineme`

> `You Examine: A thing to examine ... OK, you examined it alright`

 _There is a dark room around here somewhere_ 


`go w`

> `You go w`

> `14` : `An examine corridor`

`go s`

> `You go s`

> `16` : `An corridor of light and dark`

> `Leads to a room of darkness` 

`look `

> `You Look.`

> `16` : `An corridor of light and dark`

`go e`

> `You go e`

> `17` : `A room of darkness`

> `It is too dark to see` 

 _I think I found it_ 


`look `

> `You Look.`

> `17` : `A room of darkness`

`go w`

> `You go w`

> `16` : `An corridor of light and dark`

 _I can illuminate when I have a torch, but it runs out_ 


`take atorchonthefloor`

> `You took: atorchonthefloor. You now have the A torch on the floor  , oooh, and you now have the ability to 'illuminate' and 'darken'  (power=10).`

`illuminate `

> `Good work. You illuminated the 'A torch on the floor'. Your 'A torch on the floor' has 10 power left.`

> ... messages ...

* Your 'A torch on the floor' is working and now 'atorchonthefloor' has 9 power

`go e`

> `You go e`

> `17` : `A room of darkness`

> ... messages ...

* Your 'A torch on the floor' is working and now 'atorchonthefloor' has 8 power

`look `

> `You Look.`

> `17` : `A room of darkness`

> ... messages ...

* Your 'A torch on the floor' is working and now 'atorchonthefloor' has 7 power

 _I can only take things when I can see _ 


`take aThingInTheDark`

> `You took: athinginthedark. You now have the A thing on the floor `

> ... messages ...

* Your 'A torch on the floor' is working and now 'atorchonthefloor' has 6 power

 _It is possible to polish things that are hoardable and make them even more valuable_ 


`go w`

> `You go w`

> `16` : `An corridor of light and dark`

> ... messages ...

* Your 'A torch on the floor' is working and now 'atorchonthefloor' has 5 power

`look `

> `You Look.`

> `16` : `An corridor of light and dark`

> ... messages ...

* Your 'A torch on the floor' is working and now 'atorchonthefloor' has 4 power

`go s`

> `You go s`

> `18` : `A corridor of polishing`

> `Leads to a room of polishing` 

> ... messages ...

* Your 'A torch on the floor' is working and now 'atorchonthefloor' has 3 power

`look `

> `You Look.`

> `18` : `A corridor of polishing`

> ... messages ...

* Your 'A torch on the floor' is working and now 'atorchonthefloor' has 2 power

`take athingtopolish`

> `You took: athingtopolish. You now have the A thing to polish `

> ... messages ...

* Your 'A torch on the floor' is working and now 'atorchonthefloor' has 1 power

`go e`

> `You go e`

> `19` : `A room of polishing`

> `A Dark Room` 

> ... messages ...

* Your 'A torch on the floor' is working and now 'atorchonthefloor' has 0 power
* Your 'A torch on the floor' has 0 power left and has disappeared

`look `

> `You Look.`

> `19` : `A room of polishing`

`take athingtopolishwith`

> `You took: athingtopolishwith. You now have the A cloth of mighty polishing  , oooh, and you now have the ability to 'polish' things (power=100).`

`polish athingtopolish`

> `Good work. You polished 'A thing to polish' by 35 and now it is worth 185 hoard points. Your 'A cloth of mighty polishing' has 65 polish power left. `

 _I remember reading about some dispensers of cool stuff_ 


`go w`

> `You go w`

> `18` : `A corridor of polishing`

`look `

> `You Look.`

> `18` : `A corridor of polishing`

`go n`

> `You go n`

> `16` : `An corridor of light and dark`

`look `

> `You Look.`

> `16` : `An corridor of light and dark`

`go n`

> `You go n`

> `14` : `An examine corridor`

`look `

> `You Look.`

> `14` : `An examine corridor`

`go w`

> `You go w`

> `23` : `The dispensery`

> `The room with a dispenser` 

> I can see some things here:

* `A Gold Dispenser` (`golddispenser`)
* `A Torch Dispenser` (`torchdispenser`)

 _Ah, here they are_ 


`look `

> `You Look.`

> `23` : `The dispensery`

> I can see some things here:

* `A Gold Dispenser` (`golddispenser`)
* `A Torch Dispenser` (`torchdispenser`)

 _I can use the dispensers for infinite stuff_ 


`use golddispenser`

> `OK, you use 'A Gold Dispenser' and you see it dispense '<span id='gold_1'>A Gold Nugget</span>'`

`use golddispenser`

> `OK, you use 'A Gold Dispenser' and you see it dispense '<span id='gold_2'>A Gold Nugget</span>'`

`use golddispenser`

> `OK, you use 'A Gold Dispenser' and you see it dispense '<span id='gold_3'>A Gold Nugget</span>'`

`use golddispenser`

> `OK, you use 'A Gold Dispenser' and you see it dispense '<span id='gold_4'>A Gold Nugget</span>'`

`use golddispenser`

> `OK, you use 'A Gold Dispenser' and you see it dispense '<span id='gold_5'>A Gold Nugget</span>'`

`use golddispenser`

> `OK, you use 'A Gold Dispenser' and you see it dispense '<span id='gold_6'>A Gold Nugget</span>'`

`use golddispenser`

> `OK, you use 'A Gold Dispenser' and you see it dispense '<span id='gold_7'>A Gold Nugget</span>'`

 _I will now take all the gold_ 


`look `

> `You Look.`

> `23` : `The dispensery`

> I can see some things here:

* `A Gold Dispenser` (`golddispenser`)
* `A Torch Dispenser` (`torchdispenser`)

`take gold_2`

> `You took: gold_2. You now have the A Gold Nugget `

`take gold_1`

> `You took: gold_1. You now have the A Gold Nugget `

`take gold_4`

> `You took: gold_4. You now have the A Gold Nugget `

`take gold_3`

> `You took: gold_3. You now have the A Gold Nugget `

`take gold_6`

> `You took: gold_6. You now have the A Gold Nugget `

`take gold_5`

> `You took: gold_5. You now have the A Gold Nugget `

`take gold_7`

> `You took: gold_7. You now have the A Gold Nugget `

 _I heard tell of a room with buttons where I can increase my score_ 


`go e`

> `You go e`

> `14` : `An examine corridor`

`look `

> `You Look.`

> `14` : `An examine corridor`

`go n`

> `You go n`

> `12` : `A inspect corridor`

`look `

> `You Look.`

> `12` : `A inspect corridor`

`go n`

> `You go n`

> `10` : `A corridor`

> I can see some things here:

* `A Button` (`zapherebutton`)

`look `

> `You Look.`

> `10` : `A corridor`

> I can see some things here:

* `A Button` (`zapherebutton`)

`go n`

> `You go n`

> `8` : `South of The Northern most End of the Corridor of Test Rooms`

`look `

> `You Look.`

> `8` : `South of The Northern most End of the Corridor of Test Rooms`

`go w`

> `You go w`

> `20` : `A counter room`

> `The room with a counter` 

> I can see some things here:

* `A counter in the middle of the floor` (`acounter`)
* `A black button on the wall` (`blackbuttonwall`)

 _Ah, here it is_ 


`look `

> `You Look.`

> `20` : `A counter room`

> I can see some things here:

* `A counter in the middle of the floor` (`acounter`)
* `A black button on the wall` (`blackbuttonwall`)

`score `

> `Your Score is: 168`

 _Apparently red is up and blue is down_ 


`examine acounter`

> `you examine the counter and see some buttons on it`

`push redbutton`

> `you push the red button`

`push redbutton`

> `you push the red button`

`push redbutton`

> `you push the red button`

> ... messages ...

* you heard a ding noise

 _When I hear a ding my score has increased_ 


`score `

> `Your Score is: 188`
Could I probably push the blue button and possibly score again?

 _But there is a room where I do stuff and things are revealed_ 


`go e`

> `You go e`

> `8` : `South of The Northern most End of the Corridor of Test Rooms`

`look `

> `You Look.`

> `8` : `South of The Northern most End of the Corridor of Test Rooms`

`go s`

> `You go s`

> `10` : `A corridor`

> I can see some things here:

* `A Button` (`zapherebutton`)

`look `

> `You Look.`

> `10` : `A corridor`

> I can see some things here:

* `A Button` (`zapherebutton`)

`go s`

> `You go s`

> `12` : `A inspect corridor`

`look `

> `You Look.`

> `12` : `A inspect corridor`

`go w`

> `You go w`

> `22` : `A room with shown things`

> `The room with things that are shown` 

> I can see some things here:

* `A Sign on the Wall` (`asignonwall`)

 _Here is the room that shows things, but I am locked in_ 


`look `

> `You Look.`

> `22` : `A room with shown things`

> I can see some things here:

* `A Sign on the Wall` (`asignonwall`)

`examine asignonwall`

> `You Examine: A Sign on the Wall ... the sign says say boo`

`say boo`

> `you say boo and are aware of more stuff.`

> `22` : `A room with shown things`

> I can see some things here:

* `A Sign on the Wall` (`asignonwall`)
* `a sign that was previously hidden` (`hiddensign`)

> ... messages ...

* stuff happened

`open e`

> `OK, you open the door`

> `22` : `A room with shown things`

> I can see some things here:

* `A Sign on the Wall` (`asignonwall`)

 _But there is a room where I do stuff and things are revealed_ 


`go e`

> `You go e through the door`

> `12` : `A inspect corridor`

`look `

> `You Look.`

> `12` : `A inspect corridor`
And with that I have done most of the things. I could keep dispensing and increasing score, but that is really it.

---
