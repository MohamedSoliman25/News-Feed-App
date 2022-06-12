package com.example.newsfeedapp.db

import androidx.room.TypeConverter
import com.example.newsfeedapp.model.Source

class Converters {
    @TypeConverter
    fun fromSource(source:Source):String?{
        return source.name
    }

    @TypeConverter
    fun toSource(name:String):Source{
        // i will pass name instead of id because id is always equal null
        return Source(name,name)
    }
}