# AAFC - Android ADB Fastboot Commander

--I had trouble pushing my existing repo to github, so i pushed the current code for the people waiting for it. As soon as I fix the git issues, I'll push the whole repo. This is a snapshot from build 2833--

This tool lets you use ADB and Fastboot commands on your android phone without the need to install any sdk tools, it works on windows, mac & linux. Just open it and wait until it recognizes your phone. It will automatically go into the right mode (adb or fastboot), so you don't have to type "adb " or "fastboot " in front of every command. For some actions, like flashing a recovery, installing an apk, push/pulling a file or rebooting the phone (to bootloader, recovery or system), you dont even need commands, just use the graphical user interface, select your desired files and start it with one click.

This tool is a early beta. Some things might not work as expected. Running commands from the console window should always work, as it just uses the adb and fastboot binaries and passes your command. adb shell is not possible at the moment.

Builds at [my server](http://aafc.kuenzler.io/).