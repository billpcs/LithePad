LithePad
======

<p align="center">
<img src="http://i.imgur.com/h7zi17F.png">
</p>

A simple notepad in Scala
=

<p align="center">
<img src="http://i.imgur.com/LRgHmmN.png">
</p>


***Scala Version 2.11.2 -- Copyright 2002-2013, LAMP/EPFL***

As I am not developing LithePad professionally it will be slow in respect to updates and commits.
I will update the repository as soon as I have made a serious improvement and/or have added new features.
Also note that I am not a professional software engineer, I just like programming, so this repo may seem to you strange in terms of file construction, code style and generally functionality. I will be doing my best.

In order to make changes to LithePad and then run it you will need to have sbt installed on your system.
If you do not have it you can download it from [here](http://www.scala-sbt.org/download.html).

Here is a quick start guide:

`$ git clone https://github.com/billpcs/LithePad`

`$ cd LithePad/`

`$ sbt run`


You can read the [README.txt](https://raw.githubusercontent.com/billpcs/LithePad/master/src/main/resources/README.txt) for more information.

### If you just want to run it as an app [grab the jar](https://www.dropbox.com/s/7tybots2ps1ff19/LithePad-assembly-1.0.jar?dl=0) from my dropbox.

#Use At Your Own Risk 
**Use only if you know what you are doing. There may be bugs that can cause data loss.**


Known Issues
=======

 1. <s>Even if you choose the same tab-size as before the whole text gets reloaded</s> **(FIXED v0.0.0.15)**
 2. If you choose multiple times the font-size from the menu, new windows keep opening
 3. The line-counter is relatively unreliable (wrapping messes it up)
 4. There are no *file-not-saved* indicators so various strange things can happen
 5. Although it be started from command line, it can not load files provided as arguments

LithePad is licensed under the MIT license. See the file
[LICENSE](https://github.com/billpcs/LithePad/blob/master/LICENSE) for more information.
