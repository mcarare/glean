/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

/// This implements the developer facing API for recording UUID metrics.
///
/// Instances of this class type are automatically generated by the parsers at build time,
/// allowing developers to record values that were previously registered in the metrics.yaml file.
///
/// The UUID API only exposes the `UuidMetricType.generateAndSet()` and `UuidMetricType.set(_:)` methods,
/// which takes care of validating the input data and making sure that limits are enforced.
public class UuidMetricType {
    let inner: UuidMetric

    /// The public constructor used by automatically generated metrics.
    public init(_ meta: CommonMetricData) {
        self.inner = UuidMetric(meta)
    }

    /// Generate a new UUID and set it in the metric store.
    ///
    /// - returns: The `UUID` that was generated or `nil` if disabled.
    public func generateAndSet() -> UUID {
        let uuid = inner.generateAndSet()
        return UUID(uuidString: uuid)!
    }

    /// Explicitly set an existing UUID value.
    ///
    /// - parameters:
    ///     * value: A valid `UUID` to set the metric to.
    public func set(_ value: UUID) {
        inner.set(value.uuidString.lowercased())
    }

    /// Tests whether a value is stored for the metric for testing purposes only. This function will
    /// attempt to await the last task (if any) writing to the the metric's storage engine before
    /// returning a value.
    ///
    /// - parameters:
    ///     * pingName: represents the name of the ping to retrieve the metric for.
    ///                 Defaults to the first value in `sendInPings`.
    /// - returns: true if metric value exists, otherwise false
    public func testHasValue(_ pingName: String? = nil) -> Bool {
        return inner.testGetValue(pingName) != nil
    }

    /// Returns the stored value for testing purposes only. This function will attempt to await the
    /// last task (if any) writing to the the metric's storage engine before returning a value.
    ///
    /// Throws a `Missing value` exception if no value is stored
    ///
    /// - parameters:
    ///     * pingName: represents the name of the ping to retrieve the metric for.
    ///                 Defaults to the first value in `sendInPings`.
    ///
    /// - returns:  value of the stored metric
    public func testGetValue(_ pingName: String? = nil) -> UUID? {
        guard let uuid = inner.testGetValue() else { return nil }
        return UUID(uuidString: uuid)!
    }
}
