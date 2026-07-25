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

fun step(
    edge: UInt,
    x: UInt,
): UInt = if (x < edge) 0u else 1u

fun step(
    edge: Int,
    x: Int,
): Int = if (x < edge) 0 else 1

fun step(
    edge: Long,
    x: Long,
): Long = if (x < edge) 0L else 1L

fun step(
    edge: Float,
    x: Float,
): Float = if (x < edge) 0f else 1f

fun step(
    edge: Double,
    x: Double,
): Double = if (x < edge) 0.0 else 1.0
