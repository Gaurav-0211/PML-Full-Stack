package com.crud.crud_lombok_dto.test;

public class OTP {
    public static void main(String[] args) {
        StringBuilder otp = new StringBuilder();
        for(int i =0; i< 6; i++){
            otp.append((int) (Math.random() * 10));
        }
        System.out.println("Generated otp is "+otp);
    }
}
