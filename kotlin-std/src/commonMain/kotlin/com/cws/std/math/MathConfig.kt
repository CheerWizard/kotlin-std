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
package com.cws.std.math

enum class CoordinateSystem {
    LEFT_HANDED,
    RIGHT_HANDED,
}

enum class ClipSpace {
    ZERO_TO_ONE,
    MINUS_ONE_TO_ONE,
}

object MathConfig {

    private var initialized = false

    lateinit var coordinateSystem: CoordinateSystem
        private set

    lateinit var clipSpace: ClipSpace
        private set

    fun init(
        coordinateSystem: CoordinateSystem,
        clipSpace: ClipSpace,
    ) {
        if (initialized) return
        initialized = true

        this.coordinateSystem = coordinateSystem
        this.clipSpace = clipSpace
    }

}