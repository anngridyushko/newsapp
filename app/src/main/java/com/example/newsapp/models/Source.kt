package com.example.newsapp.models

data class Source(
    val name: String
) {

    lateinit var id: String

    constructor(id: String?, name: String) : this(name) {
        if (id == null) {
            this.id = name.replace(' ', '-').lowercase()
        }
    }
}