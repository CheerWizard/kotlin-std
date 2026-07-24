package com.cws.std.platform

actual fun PlatformInfo.fetchMaxThreadCount(): Int = Runtime.getRuntime().availableProcessors() * 2

actual fun PlatformInfo.fetchCurrentThreadId(): Int = Thread.currentThread().id.toInt()

actual fun PlatformInfo.fetchCurrentProcessId(): Int = ProcessHandle.current().pid().toInt()

actual fun PlatformInfo.fetchCurrentThreadName(): String = Thread.currentThread().name