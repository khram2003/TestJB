package com.example.testtaskjb

class Author{
    var firstName: String?
    var lastName: String?

    //is needed to covert
    constructor(){
        firstName = null
        lastName = null
    }

    constructor(firstName: String, lastName: String){
        this.firstName = firstName
        this.lastName = lastName
    }
}
