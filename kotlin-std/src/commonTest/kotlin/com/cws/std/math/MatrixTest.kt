package com.cws.std.math;

import com.cws.std.math.matrices.*
import com.cws.std.math.vectors.*
import com.cws.std.math.operators.*
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

class MatrixTest {

    companion object {
        private const val EPSILON = 0.0001f
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private fun assertClose(
        expected: Float,
        actual: Float,
        epsilon: Float = EPSILON,
        message: String = ""
    ) {
        assertTrue(
            abs(expected - actual) <= epsilon,
            "$message expected=$expected actual=$actual"
        )
    }

    private fun assertVec4Close(
        expected: Float4,
        actual: Float4,
        epsilon: Float = EPSILON,
    ) {
        assertClose(expected.x, actual.x, epsilon, "x")
        assertClose(expected.y, actual.y, epsilon, "y")
        assertClose(expected.z, actual.z, epsilon, "z")
        assertClose(expected.w, actual.w, epsilon, "w")
    }

    private fun assertMat4Close(
        expected: Mat4,
        actual: Mat4,
        epsilon: Float = EPSILON,
    ) {
        assertVec4Close(
            expected * Float4(1f, 0f, 0f, 0f),
            actual * Float4(1f, 0f, 0f, 0f),
            epsilon
        )

        assertVec4Close(
            expected * Float4(0f, 1f, 0f, 0f),
            actual * Float4(0f, 1f, 0f, 0f),
            epsilon
        )

        assertVec4Close(
            expected * Float4(0f, 0f, 1f, 0f),
            actual * Float4(0f, 0f, 1f, 0f),
            epsilon
        )

        assertVec4Close(
            expected * Float4(0f, 0f, 0f, 1f),
            actual * Float4(0f, 0f, 0f, 1f),
            epsilon
        )
    }

    private fun transformPoint(
        matrix: Mat4,
        x: Float,
        y: Float,
        z: Float,
    ): Float3 {
        val result = matrix * Float4(x, y, z, 1f)
        return Float3(
            result.x / result.w,
            result.y / result.w,
            result.z / result.w,
        )
    }

    private fun transformVector(
        matrix: Mat4,
        x: Float,
        y: Float,
        z: Float,
    ): Float3 {
        val result = matrix * Float4(x, y, z, 0f)

        return Float3(
            result.x,
            result.y,
            result.z,
        )
    }

    private fun assertVec3Close(
        expected: Float3,
        actual: Float3,
        epsilon: Float = EPSILON,
    ) {
        assertClose(expected.x, actual.x, epsilon, "x")
        assertClose(expected.y, actual.y, epsilon, "y")
        assertClose(expected.z, actual.z, epsilon, "z")
    }

    // -------------------------------------------------------------------------
    // ModelMatrix
    // -------------------------------------------------------------------------

    @Test
    fun modelMatrix_identity() {
        val matrix = ModelMatrix(
            translation = Float3(0f, 0f, 0f),
            rx = 0f,
            ry = 0f,
            rz = 0f,
            scalar = Float3(1f, 1f, 1f),
        )

        assertMat4Close(Mat4(), matrix)
    }

    @Test
    fun modelMatrix_translation() {
        val matrix = ModelMatrix(
            translation = Float3(10f, 20f, 30f),
            rx = 0f,
            ry = 0f,
            rz = 0f,
            scalar = Float3(1f, 1f, 1f),
        )

        assertVec3Close(
            Float3(10f, 20f, 30f),
            transformPoint(matrix, 0f, 0f, 0f)
        )

        assertVec3Close(
            Float3(11f, 20f, 30f),
            transformPoint(matrix, 1f, 0f, 0f)
        )
    }

    @Test
    fun modelMatrix_scale() {
        val matrix = ModelMatrix(
            translation = Float3(0f, 0f, 0f),
            rx = 0f,
            ry = 0f,
            rz = 0f,
            scalar = Float3(2f, 3f, 4f),
        )

        assertVec3Close(
            Float3(2f, 0f, 0f),
            transformPoint(matrix, 1f, 0f, 0f)
        )

        assertVec3Close(
            Float3(0f, 3f, 0f),
            transformPoint(matrix, 0f, 1f, 0f)
        )

        assertVec3Close(
            Float3(0f, 0f, 4f),
            transformPoint(matrix, 0f, 0f, 1f)
        )
    }

    @Test
    fun modelMatrix_translationAndScale() {
        val matrix = ModelMatrix(
            translation = Float3(10f, 20f, 30f),
            rx = 0f,
            ry = 0f,
            rz = 0f,
            scalar = Float3(2f, 3f, 4f),
        )

        assertVec3Close(
            Float3(10f, 20f, 30f),
            transformPoint(matrix, 0f, 0f, 0f)
        )

        assertVec3Close(
            Float3(12f, 20f, 30f),
            transformPoint(matrix, 1f, 0f, 0f)
        )

        assertVec3Close(
            Float3(10f, 23f, 30f),
            transformPoint(matrix, 0f, 1f, 0f)
        )

        assertVec3Close(
            Float3(10f, 20f, 34f),
            transformPoint(matrix, 0f, 0f, 1f)
        )
    }

    // -------------------------------------------------------------------------
    // RigidMatrix
    // -------------------------------------------------------------------------

    @Test
    fun rigidMatrix_identity() {
        val matrix = RigidMatrix(
            translation = Float3(0f, 0f, 0f),
            rx = 0f,
            ry = 0f,
            rz = 0f,
        )

        assertMat4Close(Mat4(), matrix)
    }

    @Test
    fun rigidMatrix_translation() {
        val matrix = RigidMatrix(
            translation = Float3(5f, 6f, 7f),
            rx = 0f,
            ry = 0f,
            rz = 0f,
        )

        assertVec3Close(
            Float3(5f, 6f, 7f),
            transformPoint(matrix, 0f, 0f, 0f)
        )
    }

    // -------------------------------------------------------------------------
    // ViewMatrix
    // -------------------------------------------------------------------------

    @Test
    fun viewMatrix_leftHanded_cameraAtOrigin() {
        val view = ViewMatrix(
            position = Float3(0f, 0f, 0f),
            front = Float3(0f, 0f, 1f),
            up = Float3(0f, 1f, 0f),
            coordinateSystem = CoordinateSystem.LEFT_HANDED,
        )

        assertVec3Close(
            Float3(0f, 0f, 1f),
            transformPoint(view, 0f, 0f, 1f)
        )

        assertVec3Close(
            Float3(0f, 0f, 2f),
            transformPoint(view, 0f, 0f, 2f)
        )
    }

    @Test
    fun viewMatrix_rightHanded_cameraAtOrigin() {
        val view = ViewMatrix(
            position = Float3(0f, 0f, 0f),
            front = Float3(0f, 0f, -1f),
            up = Float3(0f, 1f, 0f),
            coordinateSystem = CoordinateSystem.RIGHT_HANDED,
        )

        assertVec3Close(
            Float3(0f, 0f, -1f),
            transformPoint(view, 0f, 0f, -1f)
        )

        assertVec3Close(
            Float3(0f, 0f, -2f),
            transformPoint(view, 0f, 0f, -2f)
        )
    }

    @Test
    fun viewMatrix_movesCameraToOrigin() {
        val position = Float3(10f, 20f, 30f)

        val left = ViewMatrix(
            position = position,
            front = Float3(0f, 0f, 1f),
            up = Float3(0f, 1f, 0f),
            coordinateSystem = CoordinateSystem.LEFT_HANDED,
        )

        val right = ViewMatrix(
            position = position,
            front = Float3(0f, 0f, -1f),
            up = Float3(0f, 1f, 0f),
            coordinateSystem = CoordinateSystem.RIGHT_HANDED,
        )

        assertVec3Close(
            Float3(0f, 0f, 0f),
            transformPoint(left, position.x, position.y, position.z)
        )

        assertVec3Close(
            Float3(0f, 0f, 0f),
            transformPoint(right, position.x, position.y, position.z)
        )
    }

    @Test
    fun viewMatrix_leftHanded_frontMapsToPositiveZ() {
        val position = Float3(10f, 20f, 30f)
        val front = normalize(Float3(1f, 0f, 1f))

        val view = ViewMatrix(
            position = position,
            front = front,
            up = Float3(0f, 1f, 0f),
            coordinateSystem = CoordinateSystem.LEFT_HANDED,
        )

        val point = position + front * 5f

        val result = transformPoint(view, point.x, point.y, point.z)

        assertClose(5f, result.z)
    }

    @Test
    fun viewMatrix_rightHanded_frontMapsToNegativeZ() {
        val position = Float3(10f, 20f, 30f)
        val front = normalize(Float3(1f, 0f, -1f))

        val view = ViewMatrix(
            position = position,
            front = front,
            up = Float3(0f, 1f, 0f),
            coordinateSystem = CoordinateSystem.RIGHT_HANDED,
        )

        val point = position + front * 5f

        val result = transformPoint(view, point.x, point.y, point.z)

        assertClose(-5f, result.z)
    }

    // -------------------------------------------------------------------------
    // Perspective
    // -------------------------------------------------------------------------

    @Test
    fun perspective_leftHanded_zeroToOne_nearAndFar() {
        assertPerspectiveDepthMapping(
            CoordinateSystem.LEFT_HANDED,
            ClipSpace.ZERO_TO_ONE,
            nearZ = 1f,
            farZ = 100f,
        )
    }

    @Test
    fun perspective_leftHanded_minusOneToOne_nearAndFar() {
        assertPerspectiveDepthMapping(
            CoordinateSystem.LEFT_HANDED,
            ClipSpace.MINUS_ONE_TO_ONE,
            nearZ = 1f,
            farZ = 100f,
        )
    }

    @Test
    fun perspective_rightHanded_zeroToOne_nearAndFar() {
        assertPerspectiveDepthMapping(
            CoordinateSystem.RIGHT_HANDED,
            ClipSpace.ZERO_TO_ONE,
            nearZ = -1f,
            farZ = -100f,
        )
    }

    @Test
    fun perspective_rightHanded_minusOneToOne_nearAndFar() {
        assertPerspectiveDepthMapping(
            CoordinateSystem.RIGHT_HANDED,
            ClipSpace.MINUS_ONE_TO_ONE,
            nearZ = -1f,
            farZ = -100f,
        )
    }

    private fun assertPerspectiveDepthMapping(
        coordinateSystem: CoordinateSystem,
        clipSpace: ClipSpace,
        nearZ: Float,
        farZ: Float,
    ) {
        val projection = PerspectiveMatrix(
            aspectRatio = 1f,
            fov = Degree(90f),
            zNear = 1f,
            zFar = 100f,
            coordinateSystem = coordinateSystem,
            clipSpace = clipSpace,
        )

        val near = transformPoint(projection, 0f, 0f, nearZ)
        val far = transformPoint(projection, 0f, 0f, farZ)

        when (clipSpace) {
            ClipSpace.ZERO_TO_ONE -> {
                assertClose(0f, near.z)
                assertClose(1f, far.z)
            }

            ClipSpace.MINUS_ONE_TO_ONE -> {
                assertClose(-1f, near.z)
                assertClose(1f, far.z)
            }
        }
    }

    @Test
    fun perspective_preservesCenterXAndY() {
        for (coordinateSystem in CoordinateSystem.entries) {
            for (clipSpace in ClipSpace.entries) {
                val projection = PerspectiveMatrix(
                    aspectRatio = 16f / 9f,
                    fov = Degree(90f),
                    zNear = 1f,
                    zFar = 100f,
                    coordinateSystem = coordinateSystem,
                    clipSpace = clipSpace,
                )

                val z = when (coordinateSystem) {
                    CoordinateSystem.LEFT_HANDED -> 10f
                    CoordinateSystem.RIGHT_HANDED -> -10f
                }

                val result = transformPoint(
                    projection,
                    0f,
                    0f,
                    z
                )

                assertClose(0f, result.x)
                assertClose(0f, result.y)
            }
        }
    }

    // -------------------------------------------------------------------------
    // Orthographic
    // -------------------------------------------------------------------------

    @Test
    fun ortho_leftHanded_zeroToOne() {
        assertOrthoDepthMapping(
            CoordinateSystem.LEFT_HANDED,
            ClipSpace.ZERO_TO_ONE,
            nearZ = 1f,
            farZ = 100f,
        )
    }

    @Test
    fun ortho_leftHanded_minusOneToOne() {
        assertOrthoDepthMapping(
            CoordinateSystem.LEFT_HANDED,
            ClipSpace.MINUS_ONE_TO_ONE,
            nearZ = 1f,
            farZ = 100f,
        )
    }

    @Test
    fun ortho_rightHanded_zeroToOne() {
        assertOrthoDepthMapping(
            CoordinateSystem.RIGHT_HANDED,
            ClipSpace.ZERO_TO_ONE,
            nearZ = -1f,
            farZ = -100f,
        )
    }

    @Test
    fun ortho_rightHanded_minusOneToOne() {
        assertOrthoDepthMapping(
            CoordinateSystem.RIGHT_HANDED,
            ClipSpace.MINUS_ONE_TO_ONE,
            nearZ = -1f,
            farZ = -100f,
        )
    }

    private fun assertOrthoDepthMapping(
        coordinateSystem: CoordinateSystem,
        clipSpace: ClipSpace,
        nearZ: Float,
        farZ: Float,
    ) {
        val projection = OrthoMatrix(
            left = -10f,
            right = 10f,
            bottom = -10f,
            top = 10f,
            zNear = 1f,
            zFar = 100f,
            coordinateSystem = coordinateSystem,
            clipSpace = clipSpace,
        )

        val near = transformPoint(
            projection,
            0f,
            0f,
            nearZ
        )

        val far = transformPoint(
            projection,
            0f,
            0f,
            farZ
        )

        when (clipSpace) {
            ClipSpace.ZERO_TO_ONE -> {
                assertClose(0f, near.z)
                assertClose(1f, far.z)
            }

            ClipSpace.MINUS_ONE_TO_ONE -> {
                assertClose(-1f, near.z)
                assertClose(1f, far.z)
            }
        }
    }

    @Test
    fun ortho_mapsXY_boundaries() {
        val projection = OrthoMatrix(
            left = -10f,
            right = 10f,
            bottom = -5f,
            top = 5f,
            zNear = 1f,
            zFar = 100f,
            coordinateSystem = CoordinateSystem.LEFT_HANDED,
            clipSpace = ClipSpace.ZERO_TO_ONE,
        )

        // Your projection intentionally flips Y:
        // left   -> -1
        // right  -> +1
        // bottom -> +1
        // top    -> -1

        val left = transformPoint(projection, -10f, 0f, 10f)
        val right = transformPoint(projection, 10f, 0f, 10f)
        val bottom = transformPoint(projection, 0f, -5f, 10f)
        val top = transformPoint(projection, 0f, 5f, 10f)

        assertClose(-1f, left.x)
        assertClose(1f, right.x)

        assertClose(1f, bottom.y)
        assertClose(-1f, top.y)
    }

    @Test
    fun ortho_centerMapsToOrigin() {
        for (coordinateSystem in CoordinateSystem.entries) {
            for (clipSpace in ClipSpace.entries) {
                val projection = OrthoMatrix(
                    left = -10f,
                    right = 10f,
                    bottom = -10f,
                    top = 10f,
                    zNear = 1f,
                    zFar = 100f,
                    coordinateSystem = coordinateSystem,
                    clipSpace = clipSpace,
                )

                val z = when (coordinateSystem) {
                    CoordinateSystem.LEFT_HANDED -> 10f
                    CoordinateSystem.RIGHT_HANDED -> -10f
                }

                val result = transformPoint(
                    projection,
                    0f,
                    0f,
                    z
                )

                assertClose(0f, result.x)
                assertClose(0f, result.y)
            }
        }
    }

    // -------------------------------------------------------------------------
    // Normal matrices
    // -------------------------------------------------------------------------

    @Test
    fun normalMatrix3_isInverseTransposeOfUpper3x3() {
        val model = ModelMatrix(
            translation = Float3(10f, 20f, 30f),
            rx = 0.3f,
            ry = 0.7f,
            rz = 1.1f,
            scalar = Float3(2f, 3f, 4f),
        )

        val expected = Mat3(model).inverse().transpose()
        val actual = NormalMatrix3(model)

        assertMat3Close(expected, actual)
    }

    // -------------------------------------------------------------------------
    // Basic normal correctness
    // -------------------------------------------------------------------------

    @Test
    fun normalMatrix3_preservesNormalOrthogonality() {
        val model = ModelMatrix(
            translation = Float3(0f, 0f, 0f),
            rx = 0.4f,
            ry = 0.8f,
            rz = 0.2f,
            scalar = Float3(2f, 3f, 5f),
        )

        val normalMatrix = NormalMatrix3(model)

        val tangent = Float3(1f, 0f, 0f)
        val bitangent = Float3(0f, 1f, 0f)

        val normal = normalize(cross(tangent, bitangent))

        val transformedTangent =
            transformVector(model, tangent.x, tangent.y, tangent.z)

        val transformedNormal =
            normalMatrix * normal

        assertClose(
            0f,
            dot(transformedTangent, transformedNormal),
            0.001f
        )
    }

    // -------------------------------------------------------------------------
    // Mat3 helper
    // -------------------------------------------------------------------------

    private fun assertMat3Close(
        expected: Mat3,
        actual: Mat3,
        epsilon: Float = EPSILON,
    ) {
        assertVec3Close(
            expected * Float3(1f, 0f, 0f),
            actual * Float3(1f, 0f, 0f),
            epsilon
        )

        assertVec3Close(
            expected * Float3(0f, 1f, 0f),
            actual * Float3(0f, 1f, 0f),
            epsilon
        )

        assertVec3Close(
            expected * Float3(0f, 0f, 1f),
            actual * Float3(0f, 0f, 1f),
            epsilon
        )
    }
}

