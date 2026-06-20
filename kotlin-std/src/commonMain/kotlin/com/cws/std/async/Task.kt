package com.cws.std.async

data class Task(
    val name: String,
    val action: () -> Unit,
)