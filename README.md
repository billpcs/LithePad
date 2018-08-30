lithepad
======

<p align="center">
<img src="http://i.imgur.com/fhkgMF7.png">
</p>

A simple notepad in Scala
=

<p align="center">
<img src="http://i.imgur.com/YtpECPA.png">
</p>

This project is a place to try out new things I learn in Scala. So do not expect consistency and optimized code.

In order to make changes to lithepad and then run it you will need to have sbt installed on your system.
You can get it [here](http://www.scala-sbt.org/download.html).

Here is a quick start guide:

`$ git clone https://github.com/billpcs/lithepad`

`$ cd lithepad/`

`$ sbt run`


You can read the [README.txt](https://raw.githubusercontent.com/billpcs/lithepad/master/src/main/resources/README.txt) for more information.

### If you just want to run it as an app, grab the jar from the [releases](https://github.com/billpcs/lithepad/releases) page.

# Use At Your Own Risk ¯\\_(ツ)_/¯

**Use only if you know what you are doing. There may be bugs that can cause data loss.**


Known Issues
=======

 1. <s>Even if you choose the same tab-size as before the whole text gets reloaded</s> **(FIXED v0.0.0.15)**
 2. If you choose multiple times the font-size from the menu, new windows keep opening
 3. The line-counter is relatively unreliable (wrapping messes it up)
 4. <s>There are no *file-not-saved* indicators so various strange things can happen</s> **(FIXED v0.0.1.1)**
 5. <s>Although it be started from command line, it can not load files provided as arguments</s> **(FIXED v0.0.1.1)**
 6. You can not edit the `settings.properties` file without using a compression software

LithePad is licensed under the MIT license. See the file
[LICENSE](https://github.com/billpcs/LithePad/blob/master/LICENSE) for more information.
