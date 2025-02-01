package com.example

import java.io.File

fun fibonacci(n: Long): Long {
//    println("Фибоначи $n")
    return if (n <= 2) n
    else fibonacci(n - 1) + fibonacci(n - 2)
}

fun writeToFile(fileName: String, content: String) {
    val file = File(fileName)
    file.createNewFile()
    file.writeText(content)
    println("Тест записан в файл")
}

enum class Choice(val value: Int) {
    FIBONACCI_CHOICE(1),
    FILE_WRITING_CHOICE(2),
    EXIT_CHOICE(3);
}

fun main() {
    val kThreadPool = KThreadPool()
    var choice: Int?
    while (true) {
        println("Выберите команду (1: Фибоначчи, 2: Запись в файл, 3: Выход): ")
        choice = readlnOrNull()?.let {
            try {
                it.toInt()
            } catch (e: Exception) {
                null
            }
        }
        when (choice) {
            Choice.FIBONACCI_CHOICE.value -> {
                println("Введите число для расчета Фибоначчи: ")
                val n = readln().toLong()
                kThreadPool.addTask {
                    val result = fibonacci(n)
                    println("Число Фибоначчи для $n  : $result")
                }
            }

            Choice.FILE_WRITING_CHOICE.value -> {
                print("Введите имя файла: ")
                val fileName = readlnOrNull().toString()
                print("Введите текст для записи в файл: ")
                val text = readlnOrNull().toString()
                kThreadPool.addTask {
                    println("Задача начала выполнение")
                    writeToFile(fileName = fileName, content = text)
                }
            }

            Choice.EXIT_CHOICE.value -> {
                break
            }
        }
    }
}