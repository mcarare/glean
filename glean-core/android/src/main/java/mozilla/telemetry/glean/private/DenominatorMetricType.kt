/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.telemetry.glean.private

/**
 * This implements the developer facing API for recording a denominator of a rate metric,
 * where the denominator is external.
 * It is essentially a wrapper around `CounterMetricType`.
 *
 * Instances of this class type are automatically generated by the parsers at build time,
 * allowing developers to record values that were previously registered in the metrics.yaml file.
 *
 * The denominator API exposes the [add] method,
 * which takes care of validating the input data and making sure that limits are enforced.
 */
typealias DenominatorMetricType = mozilla.telemetry.glean.internal.DenominatorMetric
