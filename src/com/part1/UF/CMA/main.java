package com.part1.UF.CMA;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        BigInteger SharedKey = new BigInteger("00112233445566778899AABBCCDDEEFF",16);
        BigInteger ID_Bob = new BigInteger("0070070");
        BigInteger ID_Alice = new BigInteger("3730756");
        String originalMessage= "You know my methods, Bob.";
        UF_CMA ufCma = new UF_CMA(SharedKey,originalMessage,ID_Alice,ID_Bob);
        String Mprime = ufCma.forgeTag();
        System.out.println(Mprime);


    }
}
