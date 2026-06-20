package com.cws.std.memory

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.sizeOf
import platform.Foundation.NSProcessInfo
import platform.darwin.KERN_SUCCESS
import platform.darwin.TASK_VM_INFO
import platform.darwin.integer_tVar
import platform.darwin.mach_msg_type_number_tVar
import platform.darwin.mach_task_self_
import platform.darwin.task_info
import platform.darwin.task_vm_info_data_t

@OptIn(ExperimentalForeignApi::class)
actual fun fetchMemoryInfo(): MemoryInfo {
    return MemoryInfo(0, 0, 0, 0)
//    memScoped {
//        val totalPhysicalSize = NSProcessInfo.processInfo.physicalMemory.toLong()
//        val info = alloc<task_vm_info_data_t>()
//        val count = alloc<mach_msg_type_number_tVar>()
//        count.value = (sizeOf<task_vm_info_data_t>() / sizeOf<integer_tVar>()).toUInt()
//        val result = task_info(
//            mach_task_self_,
//            TASK_VM_INFO,
//            info.reinterpret(),
//            count.ptr
//        )
//        val usedPhysicalSize = if (result == KERN_SUCCESS) info.phys_footprint.toLong() else -1
//
//        return@memScoped MemoryInfo(
//            totalPhysicalSize = totalPhysicalSize,
//            freePhysicalSize = totalPhysicalSize - usedPhysicalSize,
//            // on iOS there is no VM heap memory
//            totalHeapSize = totalPhysicalSize,
//            freeHeapSize = totalPhysicalSize - usedPhysicalSize
//        )
//    }
}