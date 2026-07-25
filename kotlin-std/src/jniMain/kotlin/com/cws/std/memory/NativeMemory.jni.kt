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
package com.cws.std.memory

import com.cws.print.JniLibrary
import java.nio.ByteBuffer

object CMemory {

    init {
        JniLibrary.load("cmemory")
    }

    external fun malloc(size: Int): ByteBuffer?
    external fun free(buffer: ByteBuffer)
    external fun realloc(buffer: ByteBuffer, size: Int): ByteBuffer?
    external fun addressOf(buffer: ByteBuffer): Long
    external fun toByteBuffer(ptr: Long, capacity: Int): ByteBuffer?
    external fun toByteBufferString(ptr: Long): ByteBuffer?

}