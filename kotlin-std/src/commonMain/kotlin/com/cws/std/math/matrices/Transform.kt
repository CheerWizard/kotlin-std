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
package com.cws.std.math.matrices

import com.cws.std.math.vectors.Float3
import com.cws.std.memory.NativeData

@NativeData
data class Transform(
    var position: Float3 = Float3(),
    var rotation: Float3 = Float3(),
    var scale: Float3 = Float3(),
) {
    fun toMat4(): Mat4 = ModelMatrix(position, rotation.x, rotation.y, rotation.z, scale)
}
