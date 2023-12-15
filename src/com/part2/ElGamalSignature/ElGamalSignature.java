package com.part2.ElGamalSignature;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.function.BiFunction;

public class ElGamalSignature {
    BigInteger p;
    BigInteger g;
    BigInteger sK;
    BigInteger y;
    MessageDigest messageDigest;

    public ElGamalSignature(BigInteger p, BigInteger g) throws NoSuchAlgorithmException {
        messageDigest =  MessageDigest.getInstance("SHA-256");
        this.g = g;
        this.p = p;
        //Generate keys in the class constructor
        this.keyGeneration();
    }
    public void keyGeneration(){
        this.sK = generateRandomBigInteger(BigInteger.TWO,this.p.subtract(BigInteger.ONE));
        this.y = this.g.modPow(this.sK,this.p);
        System.out.println("ElGamal signing key x: "+ sK.toString());
        System.out.println("ElGamal verification key vk_y: "+ this.y.toString());
        System.out.println("ElGamal verification key vk_g: "+ this.g.toString());
        System.out.println("ElGamal verification key vk_p: "+ this.p.toString());


    }
    public BigInteger generateRandomBigInteger(BigInteger min, BigInteger max){
        BigInteger difference = max.subtract(min);
        BigInteger result = new BigInteger(max.bitLength(), new Random());
        if (result.compareTo(min) < 0) {result = result.add(min);}
        if (result.compareTo(difference) >= 0) {result = result.mod(difference).add(min);}
        return  result;
    }
    public Signature signMessage(String Message) throws NoSuchAlgorithmException {

        BigInteger k = this.generateRandomBigInteger(BigInteger.TWO,this.p.subtract(BigInteger.TWO));
        while(k.gcd(this.p.subtract(BigInteger.ONE)).compareTo(BigInteger.ONE) != 0){
            k = this.generateRandomBigInteger(BigInteger.TWO,this.p.subtract(BigInteger.TWO));
        }

        BigInteger r = this.g.modPow(k,this.p);
        BigInteger k_Prime = k.modInverse(this.p.subtract(BigInteger.ONE));


        byte[] data = messageDigest.digest(Message.getBytes());
        String hexValue = "";
        for (byte B : data) {hexValue+=String.format("%02x", B);}
        BigInteger hashMessage = new BigInteger(hexValue,16);

        BigInteger s = (k_Prime.multiply(hashMessage.subtract((this.sK.multiply(r))))).mod(this.p.subtract(BigInteger.ONE));
        System.out.println("\n\nSigning:");
        System.out.println("Message to be signed m= "+ Message);
        System.out.println("Signature σ_r "+ r.toString());
        System.out.println("Signature σ_s "+ s.toString());

        return new Signature(r,s);
    }

    public String signatureVerification(String Message,Signature signature) throws Exception {



        if (signature.r.compareTo(BigInteger.ONE)!=1) throw new Exception("Invalid Signature");
        if(this.p.subtract(BigInteger.ONE).compareTo(signature.s)<0 ) throw new Exception("Invalid Signature");
        BigInteger u =( (y.modPow(signature.r,p)).multiply(signature.r.modPow(signature.s,p)) ).mod(p);



        byte[] data = messageDigest.digest(Message.getBytes());
        String hexValue = "";
        for (byte B : data) {hexValue+=String.format("%02x", B);}
        BigInteger hashMessage = new BigInteger(hexValue,16);
        BigInteger v = g.modPow(hashMessage,p);



        String status="";
        if(u.compareTo(v)==0){status = "Accepted";}
        else{status = "Rejected";}

        System.out.println("\n\nVerification");
        System.out.println("u= "+u.toString());
        System.out.println("h= "+ hashMessage.toString());
        System.out.println("v= "+v.toString());
        System.out.println("Verification result: "+status);

        return  status;

    }


}
class Signature{
    BigInteger r;
    BigInteger s;
    public Signature(BigInteger r, BigInteger s){
        this.r=r;
        this.s=s;
    }
}


