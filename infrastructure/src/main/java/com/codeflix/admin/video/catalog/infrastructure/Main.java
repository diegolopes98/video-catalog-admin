package com.codeflix.admin.video.catalog.infrastructure;

import com.codeflix.admin.video.catalog.application.UseCase;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        System.out.println(new UseCase().execute());
    }
}