/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.telemetry.glean.private

import androidx.annotation.VisibleForTesting
import mozilla.telemetry.glean.internal.EventMetric
import mozilla.telemetry.glean.testing.ErrorType

/**
 * The ability to convert an enum key back to its string representation.
 * This is necessary because Glean-generated enum values are camelCased,
 * so an enum's `name` property doesn't match the allowed key.
 *
 * This is automatically implemented for generated enums.
 */
interface EventExtraKey {
    fun keyName(): String = throw IllegalStateException("can't serialize this key")
}

/**
 * An enum with no values for convenient use as the default set of extra keys
 * that an [EventMetricType] can accept.
 */
@Suppress("EmptyClassBlock")
enum class NoExtraKeys(
    /**
     * @suppress
     */
    val value: Int
) : EventExtraKey {
    // deliberately empty
}

/**
 * A class that can be converted into key-value pairs of event extras.
 * This will be automatically implemented for event properties of an [EventMetricType].
 */
interface EventExtras {
    /**
     * Convert the event extras into 2 lists:
     *
     * 1. The list of extra key indices.
     *    Unset keys will be skipped.
     * 2. The list of extra values.
     */
    fun toExtraRecord(): Map<String, String>
}

/**
 * An object with no values for convenient use as the default set of extra keys
 * that an [EventMetricType] can accept.
 */
class NoExtras : EventExtras {
    override fun toExtraRecord(): Map<String, String> {
        return emptyMap()
    }
}

/**
 * This implements the developer facing API for recording events.
 *
 * Instances of this class type are automatically generated by the parsers at built time,
 * allowing developers to record events that were previously registered in the metrics.yaml file.
 *
 * The Events API only exposes the [record] method, which takes care of validating the input
 * data and making sure that limits are enforced.
 */
class EventMetricType<ExtraKeysEnum, ExtraObject> internal constructor(
    private var inner: EventMetric
) where ExtraKeysEnum : Enum<ExtraKeysEnum>, ExtraKeysEnum : EventExtraKey, ExtraObject : EventExtras {
    /**
     * The public constructor used by automatically generated metrics.
     */
    constructor(meta: CommonMetricData, allowedExtraKeys: List<String>) :
        this(inner = EventMetric(meta, allowedExtraKeys))

    /**
     * Record an event by using the information provided by the instance of this class.
     *
     * **THIS METHOD IS DEPRECATED.**  Specify types for your event extras.
     * See the reference for details: https://mozilla.github.io/glean/book/reference/metrics/event.html#recording-api
     *
     * @param extra optional. This is a map, both keys and values need to be strings, keys are
     *              identifiers. This is used for events where additional richer context is needed.
     *              The maximum length for values is 100 bytes.
     */
    @Deprecated("Specify types for your event extras. See the reference for details.")
    @JvmOverloads
    fun record(extra: Map<ExtraKeysEnum, String>? = null) {
        val extraRecord = extra?.mapKeys { it.key.keyName() } ?: emptyMap()
        inner.record(extraRecord)
    }

    /**
     * Record an event by using the information provided by the instance of this class.
     *
     * @param extra The event extra properties.
     *              Values are converted to strings automatically
     *              This is used for events where additional richer context is needed.
     *              The maximum length for values is 100 bytes.
     *
     * Note: `extra` is not optional here to avoid overlapping with the above definition of `record`.
     *       If no `extra` data is passed the above function will be invoked correctly.
     */
    fun record(extra: ExtraObject) {
        inner.record(extra.toExtraRecord())
    }

    /**
     * Returns the stored value for testing purposes only. This function will attempt to await the
     * last task (if any) writing to the the metric's storage engine before returning a value.
     *
     * @param pingName represents the name of the ping to retrieve the metric for.
     *                 Defaults to the first value in `sendInPings`.
     * @return value of the stored events
     * @throws [NullPointerException] if no value is stored
     */
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    @JvmOverloads
    fun testGetValue(pingName: String? = null): List<RecordedEvent>? {
        return inner.testGetValue(pingName)
    }

    /**
     * Returns the number of errors recorded for the given metric.
     *
     * @param errorType The type of the error recorded.
     * @param pingName represents the name of the ping to retrieve the metric for.
     *                 Defaults to the first value in `sendInPings`.
     * @return the number of errors recorded for the metric.
     */
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    @JvmOverloads
    fun testGetNumRecordedErrors(errorType: ErrorType, pingName: String? = null): Int {
        return inner.testGetNumRecordedErrors(errorType, pingName)
    }
}
