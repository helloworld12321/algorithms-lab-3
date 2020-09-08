import java.util.*;

class Main {
  public static void main(String[] args) {
    System.out.println("=== Testing sorting an entirely random array:");
    for (int trial = 1; trial <= 5; trial++) {
      System.out.println("Trial " + trial + ":");
      testSorting(randomArray(10_000));
    }

    System.out.println("=== Testing sorting an already-sorted array:");
    for (int trial = 1; trial <= 5; trial++) {
      System.out.println("Trial " + trial + ":");
      testSorting(orderedArray(10_000, 1));
    }

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
      testSorting(multipleSortedArray10);
    }

    System.out.println("=== Testing sorting a mutliple-sorted array (100 sorted sections):");
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
      testSorting(multipleSortedArray100);
    }
  }

  /**
   * See whether quicksort or timsort is faster at sorting a given array.
   */
  public static void testSorting(TestInteger[] arrayToSort) {
    // Set up
    TestInteger[] quicksortArray = Arrays.copyOf(arrayToSort, arrayToSort.length);
    TestInteger[] timsortArray =  Arrays.copyOf(quicksortArray, quicksortArray.length);

    // Reset the "number of comparisons" counter.
    TestInteger.counter = 0;
    long quicksortStartTime = System.currentTimeMillis();
    quicksort(quicksortArray, 0, quicksortArray.length - 1);
    long quicksortEndTime = System.currentTimeMillis();

    long quicksortTimeMillis = quicksortEndTime - quicksortStartTime;
    long quicksortComparisons = TestInteger.counter;

    TestInteger.counter = 0;
    long timsortStartTime = System.currentTimeMillis();
    Arrays.sort(timsortArray);
    long timsortEndTime = System.currentTimeMillis();

    long timsortTimeMillis = timsortEndTime - timsortStartTime;
    long timsortComparisons = TestInteger.counter;

    if (isSorted(quicksortArray)) {
      System.out.println("The quicksort worked!");
    } else {
      System.out.println("The quicksort failed :(");
    }

    System.out.printf(
      "The quicksort ran in %d milliseconds and took %d comparisons\n",
      quicksortTimeMillis,
      quicksortComparisons);

    System.out.printf(
      "The timsort ran in %d milliseconds and took %d comparisons\n",
      timsortTimeMillis,
      timsortComparisons);
  }

  /**
   * Sort an array of TestIntegers using the quicksort algorithm.
   *
   * (This function sorts a sub-range of the array from p up to and including r.)
   */
  public static void quicksort(TestInteger[] array, int p, int r) {
    if (p < r) {
      int pivotLocation = partition(array, p, r);
      quicksort(array, p, pivotLocation-1);
      quicksort(array, pivotLocation+1, r);
    }
  }

  /**
   * For the sub-range of the arary from indices p to r inclusive:
   *
   * Break the range into three groups: a pivot element, all the elements less
   * than the pivot, and all of the elements greater than the pivot. Reorder the
   * range so that the smaller elements come first, then the pivot, then the
   * bigger elements.
   *
   * Return the new index of the pivot element.
   */
  public static int partition(TestInteger[] array, int p, int r) {
    TestInteger pivot = array[r];
    int i = p-1;
    int j;
    for(j = p; j <= r-1; j++){
      if(array[j].compareTo(pivot) <= 0){
        i = i+1;
        TestInteger temp = array[i];
        array[i] = array[j];
        array[j] = temp;
      }
    }
    TestInteger temp = array[i+1];
    array[i+1]=array[r];
    array[r]=temp;
    return i+1;
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