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
import kotlinx.cinterop.reinterpret
import platform.posix.MAP_SHARED
import platform.posix.PROT_READ
import platform.posix.PROT_WRITE
import platform.posix.mmap
import platform.posix.munmap

actual enum class FileAccess(val flags: Int) {
    READ_ONLY(PROT_READ),
    WRITE_ONLY(PROT_WRITE),
    READ_WRITE(PROT_READ or PROT_WRITE),
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun File.mmap(
    size: Int,
    access: FileAccess,
    fileDescriptor: Int
): CPointer<ByteVar>? {
    return mmap(
        null,
        size.toULong(),
        access.flags,
        MAP_SHARED,
        fileDescriptor,
        0
    )?.reinterpret()
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun File.munmap(
    ptr: CPointer<ByteVar>,
    size: Int
) {
    munmap(ptr, size.toULong())
}