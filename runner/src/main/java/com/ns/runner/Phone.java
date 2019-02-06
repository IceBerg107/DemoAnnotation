package com.ns.runner;

import com.ns.annotations.NotEmpty;
import com.ns.annotations.NotEmptyClass;

@NotEmptyClass
class Phone {
    @NotEmpty
    private String ignored = "";
    @NotEmpty
    String os = "";
    @NotEmpty
    String number;

    String manufacturer = "Samsung";

    private Phone(String os, String number) {
        this.os = os;
        this.number = number;
    }
}
