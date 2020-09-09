import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class's static methods are various implementations of the quicksort
 * algorithm.
 */
public class Quicksorts {
  // We found both of the thresholds below using trial-and-error.

  /**
   * This value is the minimum size of array for which the median-of-three
   * quicksort will try to take a sample of pivot candidates. Below this
   * threshold, we'll just choose one pivot and stick with it.
   */
  public static int MEDIAN_OF_THREE_THRESHOLD = 7;

  /**
   * As MEDIAN_OF_THREE_THRESHOLD, but below this threshold we'll use an
   * insertion sort.
   */
  public static int FALLBACK_TO_INSERTION_SORT_THRESHOLD = 7;

  /**
   * Sort an array of TestIntegers using a standard quicksort algorithm without
   * any optimizations.
   *
   * (This function sorts a sub-range of the array from p up to and including r.)
   *
   * @author Equal contributions from all partners
   */
  public static void simpleQuicksort(TestInteger[] array, int p, int r) {
    if (p < r) {
      int pivotLocation = simplePartition(array, p, r);
      simpleQuicksort(array, p, pivotLocation-1);
      simpleQuicksort(array, pivotLocation+1, r);
    }
  }

  /**
   * Partition a section of an array for the quicksort algorithm, in a standard
   * way, without any optimizations.
   *
   * For the sub-range of the array from indices p to r inclusive:
   *
   * Break the range into three groups: a pivot element, all the elements less
   * than the pivot, and all of the elements greater than the pivot. Reorder the
   * range so that the smaller elements come first, then the pivot, then the
   * bigger elements.
   *
   * Return the new index of the pivot element.
   *
   * @author Contributions from all partners, especially Tyler Rowland
   */
  private static int simplePartition(TestInteger[] array, int p, int r) {
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
   * Like simpleQuicksort, but the pivot is chosen at random.
   *
   * @author Joe Walbran
   */
  public static void randomizedQuicksort(TestInteger[] array, int p, int r) {
    if (p < r) {
      int pivotLocation = randomizedPartition(array, p, r);
      randomizedQuicksort(array, p, pivotLocation-1);
      randomizedQuicksort(array, pivotLocation+1, r);
    }
  }

  /**
   * Like simplePartition, but the pivot is chosen at random.
   *
   * @author Joe Walbran
   */
  private static int randomizedPartition(TestInteger[] array, int p, int r) {
    int pivotIndex = ThreadLocalRandom.current().nextInt(p, r+1);
    TestInteger pivot = array[pivotIndex];
    // Move the pivot to the back of the sub-range.
    array[pivotIndex] = array[r];
    array[r] = pivot;

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
    array[i+1] = array[r];
    array[r] = temp;
    return i+1;
  }

  /**
   * Like simpleQuicksort, but three potential pivots are chosen at random,
   * and then we use the one in the middle.
   *
   * @author Equal contributions from all partners
   */
  public static void medianOfThreeQuicksort(TestInteger[] array, int p, int r) {
    if (r - p + 1 > MEDIAN_OF_THREE_THRESHOLD) {
      int pivotLocation = medianOfThreePartition(array, p, r);
      medianOfThreeQuicksort(array, p, pivotLocation-1);
      medianOfThreeQuicksort(array, pivotLocation+1, r);
    } else if (p < r) {
      int pivotLocation = simplePartition(array, p, r);
      simpleQuicksort(array, p, pivotLocation-1);
      simpleQuicksort(array, pivotLocation+1, r);
    }
  }

  /**
   * @author Equal contributions from all partners
   */
  private static int medianOfThreePartition(TestInteger[] array, int p, int r){
    Integer[] candidatePivotLocations = new Integer[3];
    // Not checking for multiples because of innefficiency
    for (int i = 0; i<3; i++) {
      int randomElement = ThreadLocalRandom.current().nextInt(p, r+1);
      candidatePivotLocations[i] = randomElement;
    }

    Arrays.sort(
      candidatePivotLocations,
      (self, other) -> array[self].compareTo(array[other]));

    int pivotLocation = candidatePivotLocations[1];
    TestInteger pivot = array[pivotLocation];
    // Move the pivot to the back of the sub-range.
    array[pivotLocation] = array[r];
    array[r] = pivot;

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
    array[i+1] = array[r];
    array[r] = temp;
    return i+1;
  }

  /**
   * Like simpleQuicksort, but for small enough arrays we fall back to using
   * an insertion sort instead.
   *
   * @author Equal contributions from all partners
   */
  public static void quicksortWithFallback(TestInteger[] array, int p, int r) {
    if (r - p + 1 > FALLBACK_TO_INSERTION_SORT_THRESHOLD) {
      int pivotLocation = simplePartition(array, p, r);
      quicksortWithFallback(array, p, pivotLocation-1);
      quicksortWithFallback(array, pivotLocation+1, r);
    } else {
      insertionSort(array, p, r);
    }
  }

  /**
   * Insertion Sort
   *
   * @author Equal contributions from all partners
   */
  private static void insertionSort(TestInteger[] array, int p, int r){
    int i;
    int j;
    for(j = p + 1; j <= r; j++){
      TestInteger key = array[j];
      i=j-1;
      while(i >= p && array[i].compareTo(key) > 0){
        array[i+1] = array[i];
        i = i-1;
      }
      array[i+1] = key;
    }
  }
}
