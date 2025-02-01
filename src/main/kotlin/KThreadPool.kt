package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class KThreadPool(
    private val countAvailableProcessors: Int = 2
//    Runtime.getRuntime().availableProcessors()
) {
    private var workers: MutableList<Job> = mutableListOf()
    private var tasks: MutableList<suspend () -> Unit> = mutableListOf()
        set(value) {
            println("Изменение списка задач")
            field = value
        }

    init {
        for (i in 1..countAvailableProcessors) {
            workers.add(GlobalScope.launch {
                println("Запуск $i процесса")
                run()
            })
        }
    }


    fun addTask(job: suspend () -> Unit) {
        tasks = tasks.plus(job).toMutableList()
    }

    private suspend fun run() {
        while (true) {
            var task: (suspend () -> Unit)? = null
            // доступ к общему ресурсу только для одного "пользователя ресурсом" в одно время
            synchronized(tasks) {
                task = tasks.removeFirstOrNull()
            }
            task?.invoke()
            delay(1000)
        }
    }
}