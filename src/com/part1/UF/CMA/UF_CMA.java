package com.part1.UF.CMA;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Random;


public class UF_CMA {
    BigInteger K;
    String Original_Message;
    BigInteger ID_Alice;
    BigInteger ID_Bob;
    String originalMessageTag;
    MessageDigest messageDigest;
    public UF_CMA(BigInteger K, String Original_Message, BigInteger ID_Alice, BigInteger ID_Bob) throws NoSuchAlgorithmException {
        this.K = K;
        this.ID_Alice = ID_Alice;
        this.ID_Bob = ID_Bob;
        this.Original_Message = Original_Message;
        this.messageDigest = MessageDigest.getInstance("SHA-256");
        this.originalMessageTag = this.computeMAC(this.Original_Message);


        System.out.println("\n\nOriginal input and output:");
        System.out.println("M: "+this.Original_Message);
        System.out.println("ID A: "+this.ID_Alice.toString());
        System.out.println("ID B: "+this.ID_Bob.toString());
        System.out.println("MAC Tag: "+this.originalMessageTag);

    }

    public String computeMAC(String Message)  {
        String payLoad = "";
        payLoad += this.K.toString();
        payLoad += Message;
        payLoad += this.ID_Bob.toString();
        payLoad += this.ID_Alice.toString();

        byte[] data = messageDigest.digest(payLoad.getBytes());
        // convert the hash to a hexadecimal string then return first 32 bit
        // which equal to first 32 bit = first 8 byte
        String hexValue = convertBytesToHex(data);
        return hexValue.substring(0,8);
    }


    private  String convertBytesToHex(byte[] data) {
        StringBuilder Result = new StringBuilder();
        for (byte B : data) {
            String hexData = Integer.toHexString(0xff & B);
            if(hexData.length() == 1) {Result.append('0');}
            Result.append(hexData);
        }
        return Result.toString();
    }

    public String forgeTag(){
        String RandomMessage="";
        String tagforNewMessage;
        while (true){
            RandomMessage = this.generateRandomString();
            tagforNewMessage = this.computeMAC(RandomMessage);

            if(tagforNewMessage.equals(originalMessageTag)){
                System.out.println("\n\nForged input and output:");
                System.out.println("M: " + RandomMessage);
                System.out.println("MAC tag1: " +tagforNewMessage);
                System.out.println("\n\nVerification result:" + "Accepted");
                break;
            }

           }
        return RandomMessage;
    }
    private String generateRandomString() {
        Random random = new Random();
         //int length = random.nextInt(91) + 10; // Random length between 2 and 500
        int length = 50;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
         StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

}
