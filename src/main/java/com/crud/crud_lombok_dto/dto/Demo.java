package com.crud.crud_lombok_dto.dto;


import java.util.TreeSet;


public class Demo {

    public static void main(String[] args) {
        TreeSet<Integer> t = new TreeSet<>(Integer::compareTo);

        t.add(1);
        t.add(2);
        t.add(5);
        t.add(4);
        t.add(3);

        System.out.println(t);

    }
}
