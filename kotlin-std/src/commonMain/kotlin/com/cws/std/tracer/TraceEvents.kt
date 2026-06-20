package com.cws.std.tracer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TraceScope(val value: String) {
    GLOBAL("g"),
    PROCESS("p"),
    THREAD("t"),
}

@Serializable
enum class TracePhase(val value: String) {
    BEGIN_DURATION("B"),
    END_DURATION("END"),
    COMPLETE("X"),
    INSTANT("I"),
    COUNTER("C"),
    ASYNC_BEGIN("b"),
    ASYNC_END("e"),
    ASYNC_INSTANT("n"),
    FLOW_START("s"),
    FLOW_STEP("t"),
    FLOW_END("f"),
}

@Serializable
enum class TraceColor(val value: String) {
    GOOD("good"),
    BAD("bad"),
    TERRIBLE("terrible"),
    WARNING("warning"),
    HIGHLIGHT("highlight"),
    THREAD_RUNNING("thread_state_running"),
    THREAD_RUNNABLE("thread_state_runnable"),
    THREAD_SLEEP("thread_state_sleeping"),
    THREAD_WAIT("thread_state_iowait"),
}

@Serializable
data class TraceEvent(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("cat")
    val category: String,
    @SerialName("ph")
    val phase: String,
    @SerialName("ts")
    val timestamp: Long,
    @SerialName("pid")
    val processId: Int,
    @SerialName("tid")
    val threadId: Int,
    @SerialName("scope")
    val scope: String,
    @SerialName("dur")
    val durationNanos: Long,
    @SerialName("cname")
    val color: String,
    @SerialName("args")
    val arguments: Map<String, String> = emptyMap(),
)

@Serializable
data class TraceEvents(
    val events: MutableList<TraceEvent>
)