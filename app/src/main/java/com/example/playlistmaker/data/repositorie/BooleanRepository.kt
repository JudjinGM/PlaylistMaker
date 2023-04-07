package com.example.playlistmaker.data.repositorie

interface BooleanRepository: Repository {
    fun saveBoolean(key: String, value: Boolean)
    fun loadBoolean(key: String, default: Boolean): Boolean
    fun remove(key: String)
}