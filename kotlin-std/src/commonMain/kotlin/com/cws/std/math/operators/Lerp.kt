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
package com.cws.std.math.operators

fun lerp(
    a: UInt,
    b: UInt,
    x: UInt,
): UInt = x * (b - a) + a

fun lerp(
    a: Int,
    b: Int,
    x: Int,
): Int = x * (b - a) + a

fun lerp(
    a: Long,
    b: Long,
    x: Long,
): Long = x * (b - a) + a

fun lerp(
    a: Float,
    b: Float,
    x: Float,
): Float = x * (b - a) + a

fun lerp(
    a: Double,
    b: Double,
    x: Double,
): Double = x * (b - a) + a
