export default function assertDataIsPresent<D>(
  d: D
): asserts d is NonNullable<D> {
  if (!d) {
    throw new Error("data should be present.");
  }
}
