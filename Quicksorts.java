import java.util.concurrent.ThreadLocalRandom;

/**
 * This class's static methods are various implementations of the quicksort
 * algorithm.
 */
public class Quicksorts {
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
}