/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.telemetry.glean.private

import androidx.annotation.VisibleForTesting
import mozilla.telemetry.glean.internal.UuidMetric
import java.util.UUID

/**
 * This implements the developer facing API for recording UUID metrics.
 *
 * Instances of this class type are automatically generated by the parsers at build time,
 * allowing developers to record values that were previously registered in the metrics.yaml file.
 *
 * The UUID API exposes the [generateAndSet] and [set] methods.
 *
 * The internal constructor is only used by [LabeledMetricType] directly.
 */
class UuidMetricType(meta: CommonMetricData) {
    val inner = UuidMetric(meta)

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    @JvmOverloads
    fun testGetValue(pingName: String? = null): UUID? {
        return inner.testGetValue(pingName)?.let { UUID.fromString(it) }
    }

    fun set(value: UUID) = inner.set(value.toString())

    fun generateAndSet(): UUID {
        val uuid = inner.generateAndSet()
        return UUID.fromString(uuid)
    }
}
