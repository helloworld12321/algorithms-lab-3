public class TestInteger implements Comparable<TestInteger> {
  public int value;

  // Here's a static integer that counts the number of times compareTo has
  // been called.
  public static long counter = 0;

  public TestInteger(int value) {
    this.value = value;
  }

  public int compareTo(TestInteger other) {
    counter++;
    return this.value - other.value;
  }

  public String toString() {
    return Integer.toString(value);
  }
}