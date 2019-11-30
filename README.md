# projet-sgit-scala-IG5

This is a school project that aims to create a git-like named "Sgit".

My project is composed of several files and directories. In order to be able to run it, you need to have :
 - scala (version  2.13.1)
 - sbt (version 1.3.2)
 
Then, this is the way to use the project :
 - clone the project
 - create a working directory : mkdir myproject
 - go into this directory : cd myproject
 - use sgit commands via the script in the cloned project (you can use it bu getting its full path or by adding this fullpath to your   paths by calling it "sgit"
 - if you call it by script, no need to add sgit before
 
Example of commands :
 - C:/...fullpath.../script.sh init
 - C:/...fullpath.../script.sh add .
 - C:/...fullpath.../script.sh commit -m "1st"

Available commands : init, add, commit, status, diff, log, log -p
