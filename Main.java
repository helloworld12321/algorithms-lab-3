import java.util.*;
import java.util.function.*;

class Main {
  /**
   * An ordered pair consisting of a sorting algorithm and a human-readable
   * name for that sorting algorithm.
   */
  private static class SortingAlgorithmWithName {
    Consumer<TestInteger[]> algorithm;
    String name;

    SortingAlgorithmWithName(
        Consumer<TestInteger[]> algorithm,
        String name) {
      this.algorithm = algorithm;
      this.name = name;
    }
  }

  public static void main(String[] args) {
    SortingAlgorithmWithName[] algorithms = {
      new SortingAlgorithmWithName(
        array -> { Quicksorts.simpleQuicksort(array, 0, array.length - 1); },
        "simple quicksort"),
      new SortingAlgorithmWithName(
        Arrays::sort,
        "timsort"),
    };

    System.out.println("=== Testing sorting an entirely random array:");
    for (int trial = 1; trial <= 5; trial++) {
      System.out.println("Trial " + trial + ":");
      testSorting(randomArray(10_000), algorithms);
    }
    System.out.println();

    System.out.println("=== Testing sorting an already-sorted array:");
    for (int trial = 1; trial <= 5; trial++) {
      System.out.println("Trial " + trial + ":");
      testSorting(orderedArray(10_000, 1), algorithms);
    }
    System.out.println();

    System.out.println("=== Testing sorting a multiple-sorted array (10 sorted sections):");
    for (int trial = 1; trial <= 5; trial++) {
      System.out.println("Trial " + trial + ":");
      TestInteger[] multipleSortedArray10 = new TestInteger[10_000];
      for (int i = 0; i < 10; i++) {
        TestInteger[] sortedSubarray =
          orderedArray(1_000, getRandomIntInclusive(1, 1_000_000));
        System.arraycopy(
          // Copy from the subarray (starting at the beginning),
          sortedSubarray,
          0,
          // into the destination array, starting at the ith section,
          multipleSortedArray10,
          i * 1_000,
          // all 1_000 elements of the subarray.
          1_000);
      }
      testSorting(multipleSortedArray10, algorithms);
    }
    System.out.println();

    System.out.println("=== Testing sorting a multiple-sorted array (100 sorted sections):");
    for (int trial = 1; trial <= 5; trial++) {
      System.out.println("Trial " + trial + ":");
      TestInteger[] multipleSortedArray100 = new TestInteger[10_000];
      for (int i = 0; i < 100; i++) {
        TestInteger[] sortedSubarray =
          orderedArray(100, getRandomIntInclusive(1, 1_000_000));
        System.arraycopy(
          // Copy from the subarray (starting at the beginning),
          sortedSubarray,
          0,
          // into the destination array, starting at the ith section,
          multipleSortedArray100,
          i * 100,
          // all 100 elements of the subarray.
          100);
      }
      testSorting(multipleSortedArray100, algorithms);
    }
  }

  /**
   * See which of a set of algorithms is faster at sorting a given array.
   *
   * Also keep track of how many comparisons each algorithm made.
   *
   * This function doesn't return anything, but it does print its results to
   * the standard output.
   */
  public static void testSorting(
      TestInteger[] arrayToSort,
      SortingAlgorithmWithName[] algorithmsAndNames) {

    class Result {
      boolean didSucceed;
      long millis;
      long numberOfComparisons;

      Result(boolean didSucceed, long millis, long numberOfComparisons) {
        this.didSucceed = didSucceed;
        this.millis = millis;
        this.numberOfComparisons = numberOfComparisons;
      }
    }

    // We could always print out the results as we go, but I'd like to put all
    // of the success-and-failure messages in one place and all of the
    // performace metrics in another place. (It's easier to read that way 🙂)
    // In order to do that, we need to keep track of the behavior of each
    // algorithm and then print them all out at the end.
    Result[] results = new Result[algorithmsAndNames.length];

    for (int i = 0; i < algorithmsAndNames.length; i++) {
      Consumer<TestInteger[]> algorithm = algorithmsAndNames[i].algorithm;
      String name = algorithmsAndNames[i].name;

      // Set up the array to be sorted.
      TestInteger[] copyOfArray =
        Arrays.copyOf(arrayToSort, arrayToSort.length);

      // Reset the "number of comparisons" counter.
      TestInteger.counter = 0;

      long startTime = System.currentTimeMillis();
      algorithm.accept(copyOfArray);
      long endTime = System.currentTimeMillis();

      results[i] = new Result(
        isSorted(copyOfArray),
        endTime - startTime,
        TestInteger.counter);
    }

    // Now, print out all of the results.

    for (int i = 0; i < results.length; i++) {
      if (results[i].didSucceed) {
        System.out.printf("\tThe %s worked.\n", algorithmsAndNames[i].name);
      } else {
        System.out.printf("\tThe %s failed :(\n", algorithmsAndNames[i].name);
      }
    }

    System.out.println("\t---");

    for (int i = 0; i < results.length; i++) {
      System.out.printf(
        "\tThe %s ran in %d milliseconds and took %d comparisons\n",
        algorithmsAndNames[i].name,
        results[i].millis,
        results[i].numberOfComparisons);
    }
  }

  /**
   * Takes a TestInteger[] and returns true if sorted and false if not.
   */
  public static boolean isSorted(TestInteger[] array) {
    for (int i = 0; i < array.length - 1; i++) {
      if (array[i].compareTo(array[i + 1]) > 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Generate an array of a specified length whose elements are
   * random TestIntegers between one and a million (inclusive).
   */
  public static TestInteger[] randomArray(int length) {
    TestInteger[] result = new TestInteger[length];
    for (int i = 0; i < length; i++) {
      result[i] = new TestInteger(getRandomIntInclusive(1, 1_000_000));
    }
    return result;
  }

  /**
   * Return a random integer in the range { min, min+1, ..., max }.
   *
   * Source:
   * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
   */
  public static int getRandomIntInclusive(int min, int max) {
    return (int)Math.floor(Math.random() * (max - min + 1) + min);
  }

  /**
   * Generate an array of the length given, starting at startingValue
   * and increasing by one.
   */
   public static TestInteger[] orderedArray(int length, int startingValue) {
     TestInteger[] result = new TestInteger[length];
     for (int i = 0; i < length; i++) {
       result[i] = new TestInteger(i + startingValue);
     }
     return result;
   }
}