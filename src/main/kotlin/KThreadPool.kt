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
    private var isTaskBlocked = false
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
        var index = 0
        while (true) {
            var task: (suspend () -> Unit)? = null
            if (!isTaskBlocked) {
                isTaskBlocked = true
                if (tasks.isNotEmpty()) {
                    task = tasks.removeFirstOrNull()
                }
                isTaskBlocked = false
            }
            task?.invoke()
            delay(1000)
            index += 1
        }
    }
}