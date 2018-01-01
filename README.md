

(https://github.com/CunningLogic/DUMLRacer/blob/master/dumlracer_1024.png?raw=true)

If you don’t agree to the terms in this read me, then stop dont clone, don’t read, don’t use. Don’t email me for support, open an issue if you have one.

I either need to recoup the cost of my Mavic, or sell it. If I can recoup it, I can keep hacking. If I can’t recoup it, I will be selling the drone, and I’ll be done giving the drone community goodies. Sucks, but I can’t really afford to be dropping money on this in the long term.

PayPal Donations - > jcase@cunninglogic.com
Bitcoin Donations - > 1LrunXwPpknbgVYcBJyDk6eanxTBYnyRKN
Bitcoin Cash Donations - > 1LrunXwPpknbgVYcBJyDk6eanxTBYnyRKN
Amazon giftcards, plain thank yous or anything else -> jcase@cunninglogic.com

Any donations in excess of the drone cost, will go to Special Olympics!

Dear DJI, next time someone requests source code they are entitled to under the GPL, you shouldn’t tell them no. You should comply with the license you agreed to. Had you complied, or responded appropriately, this project would never have been public.

You do NOT have permission to re-host these files in any form.
You do NOT have permission to use these files, or the intellectual property contained in them for commercial purposes without written permission from APIs Research LLC.

You do have permission to read, understand and learn from this (horrible) code.


This software comes with NO WARRANTY AT ALL. If it bricks your equipment, it is your fault not anyone else's. You agree to take full responsibility of any harm, damage, injury or loss of life from using your equipment improperly. Do not use this software to illegally modify your equipment. Do not redistribute this software. Do not use it in any commercial venture without first getting written permission from APIs Research LLC.



DUMLRacer is a race condition in the update system for DJI drones and remotes that run Android.

Usage:
    private static void printHelp() {
        System.out.println("java -jar DUMLRacer.jar <mode>");
        System.out.println("Modes:");
        System.out.println("AC - target AC");
        System.out.println("RC - target RC");
    }

ToDo:
	Add root payload, version 1.0 only enabled downgrading
	Play with sleeps and dummy file size, we can speed up this process.
	Finish testing CRC and dynamically generate the packets


Change Log:
1.0:
	Initial release

Copyright 2017/2018 APIs Research LLC 
