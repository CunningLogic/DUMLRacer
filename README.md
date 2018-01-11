
Yes it works on V01.040.0200

![wrong](https://github.com/CunningLogic/DUMLRacer/blob/master/wrong.png?raw=true)
No, you errogant ass, you are wrong. Go ahead brag about your years of drone experience, I only have two weeks with drones, and I seem to have no problem downgrading.


# DUMLRacer
![gogospeedracer](https://github.com/CunningLogic/DUMLRacer/blob/master/dumlracer_1024.png?raw=true)

Downloads: https://github.com/CunningLogic/DUMLRacer/releases

I'm on twitter ! http://twitter.com/jcase

If you don’t agree to the terms in this read me, then stop dont clone, don’t read, don’t use. Don’t email me for support, open an issue if you have one.

I either need to recoup the cost of my Mavic, or sell it. If I can recoup it, I can keep hacking. If I can’t recoup it, I will be selling the drone, and I’ll be done giving the drone community goodies. Sucks, but I can’t really afford to be dropping money on this in the long term.

```
PayPal Donations - > jcase@cunninglogic.com
Bitcoin Donations - > 1LrunXwPpknbgVYcBJyDk6eanxTBYnyRKN
Bitcoin Cash Donations - > 1LrunXwPpknbgVYcBJyDk6eanxTBYnyRKN
Amazon giftcards, plain thank yous or anything else -> jcase@cunninglogic.com
```
Any donations in excess of the drone cost, will go to Special Olympics!

Donations: $857 out of $899+tax



Dear DJI, next time someone requests source code they are entitled to under the GPL, you shouldn’t tell them no. You should comply with the license you agreed to. Had you complied, or responded appropriately, this project would never have been public.

You do NOT have permission to re-host these files in any form.
You do NOT have permission to use these files, or the intellectual property contained in them for commercial purposes without written permission from APIs Research LLC.

You do have permission to read, understand and learn from this (horrible) code.


This software comes with NO WARRANTY AT ALL. If it bricks your equipment, it is your fault not anyone else's. You agree to take full responsibility of any harm, damage, injury or loss of life from using your equipment improperly. Do not use this software to illegally modify your equipment. Do not redistribute this software. Do not use it in any commercial venture without first getting written permission from APIs Research LLC.



DUMLRacer is a race condition in the update system for DJI drones and remotes that run Android.
```
Usage:
    private static void printHelp() {
        System.out.println("java -jar DUMLRacer.jar <mode>");
        System.out.println("Modes:");
        System.out.println("AC - target AC");
        System.out.println("RC - target RC");
        System.out.println("GL - target GL");
    }
```
```
Change Log:
1.1.1:
	Google Support
1.1:
	Persistent root via adb
	Massive re-write
	More Reliable
1.0:
	Initial release

Copyright 2017/2018 APIs Research LLC 
```

Greetz/shouts/thank yous


@rotlogix - tied with a few others for best person in infosec

@hostile - wubba wubba thanks for encouraging me

@mywife - for not saying a damn word when you know I spent wayyyy to much money on a drone

@diff & @beaups for always hacking with me

@tylkologin
@opcode
@kilrah
@kdover
@hdnes
@diff
@jezzab
@jan2642
@coldflake
@hdnes
@b1n4ry

@bunch of ppl i left off because i need a break from pc


### #DeejayeyeHackingClub information repos aka "The OG's" (Original Gangsters)

http://dji.retroroms.info/ - "Wiki"

https://github.com/fvantienen/dji_rev - This repository contains tools for reverse engineering DJI product firmware images.

https://github.com/Bin4ry/deejayeye-modder - APK "tweaks" for settings & "mods" for additional / altered functionality

https://github.com/hdnes/pyduml - Assistant-less firmware pushes and DUMLHacks referred to as DUMBHerring when used with "fireworks.tar" from RedHerring. DJI silently changes Assistant? great... we will just stop using it.

https://github.com/MAVProxyUser/P0VsRedHerring - RedHerring, aka "July 4th Independence Day exploit", "FTPD directory transversal 0day", etc. (Requires Assistant). We all needed a public root exploit... why not burn some 0day?

https://github.com/MAVProxyUser/dji_system.bin - Current Archive of dji_system.bin files that compose firmware updates referenced by MD5 sum. These can be used to upgrade and downgrade, and root your I2, P4, Mavic, Spark, Goggles, and Mavic RC to your hearts content. (Use with pyduml or DUMLDore)

https://github.com/MAVProxyUser/firm_cache - Extracted contents of dji_system.bin, in the future will be used to mix and match pieces of firmware for custom upgrade files. This repo was previously private... it is now open.

https://github.com/MAVProxyUser/DUMLrub - Ruby port of PyDUML, and firmware cherry picking tool. Allows rolling of custom firmware images.

https://github.com/jezzab/DUMLdore - Even windows users need some love, so DUMLDore was created to help archive, and flash dji_system.bin files on windows platforms.

https://github.com/MAVProxyUser/DJI_ftpd_aes_unscramble - DJI has modified the GPL Busybox ftpd on Mavic, Spark, & Inspire 2 to include AES scrambling of downloaded files... this tool will reverse the scrambling
