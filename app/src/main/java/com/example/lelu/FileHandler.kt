package com.example.lelu

import java.io.File

class FileHandler(fileName: String) {
    var fileName: String? = fileName

    fun WriteToFile(text: String){
        if(fileExists()){
            val fileObject = File(fileName!!)
            fileObject.writeText(text)
        }
        else if(CreateFile()){
            val fileObject = File(fileName!!)
            fileObject.writeText(text)
        }
    }
    fun ReadFromFile():String{
        if(fileExists()){
            val fileObject = File(fileName!!)
            return fileObject.readText()
        }
        throw java.lang.Exception("File not found!")
    }
    private fun fileExists():Boolean{
        val fileObject = File(fileName!!)
        return fileObject.exists()
    }
    private fun CreateFile(): Boolean{
        val fileObject = File(fileName!!)
        return fileObject.createNewFile()
    }

}