package com.ns.runner;

import com.ns.annotations.NotEmpty;
import com.ns.annotations.NotEmptyClass;

@NotEmptyClass
class Person {
    @NotEmpty
    String firstname = "";
    @NotEmpty
    String surname = "notempty";
}

