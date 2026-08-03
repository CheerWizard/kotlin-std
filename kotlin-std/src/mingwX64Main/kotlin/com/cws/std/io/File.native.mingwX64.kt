/*
 * Copyright 2026 CheerWizard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cws.std.io

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.invoke
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toCPointer
import kotlinx.cinterop.toLong
import platform.posix._get_osfhandle
import platform.windows.CloseHandle
import platform.windows.CreateFileMappingA
import platform.windows.FILE_MAP_READ
import platform.windows.FILE_MAP_WRITE
import platform.windows.MapViewOfFile
import platform.windows.PAGE_READWRITE
import platform.windows.UnmapViewOfFile

actual enum class FileAccess(val flags: Int) {
    READ_ONLY(FILE_MAP_READ),
    WRITE_ONLY(FILE_MAP_WRITE),
    READ_WRITE(FILE_MAP_READ or FILE_MAP_WRITE),
}

// as a workaround to clean up mapping ptr
@OptIn(ExperimentalForeignApi::class)
private val mappings = mutableMapOf<Long, CPointer<ByteVar>>()

@OptIn(ExperimentalForeignApi::class)
internal actual fun File.mmap(
    size: Int,
    access: FileAccess,
    fileDescriptor: Int
): CPointer<ByteVar>? {
    val handle = _get_osfhandle(fileDescriptor)

    val mapping = CreateFileMappingA(
        handle.toCPointer(),
        null,
        PAGE_READWRITE.toUInt(),
        0.toUInt(),
        0.toUInt(),
        null
    ) ?: return null

    val view = MapViewOfFile(
        mapping,
        access.flags.toUInt(),
        0.toUInt(),
        0.toUInt(),
        0.toULong()
    )

    if (view == null) {
        CloseHandle(mapping)
        return null
    }

    mappings[view.toLong()] = mapping.reinterpret()

    return view.reinterpret()
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun File.munmap(
    ptr: CPointer<ByteVar>,
    size: Int
) {
    UnmapViewOfFile(ptr)
    mappings.remove(ptr.toLong())?.let { mapping ->
        CloseHandle(mapping)
    }
}