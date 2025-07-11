


/**
 * Output
 * Recursive: 1346268
 * DP 1: 29
 * DP 2: 29
 * <p>
 * Recursive recalculate previous function many times that make count increase
 **/

public class Fibonacci {
  static int count1 = 0;
  static int count2 = 0;
  static int count3 = 0;


  public static void main(String[] args) {
    int result1 = recursiveFibonacci(30);
    System.out.println(result1);

    int result2 = memoizedFibonacci(30);
    System.out.println(result2);

    int result3 = memoizedFibonacciTwo(30);
    System.out.println(result3);


    System.out.println("Recursive: " + count1);
    System.out.println("DP 1: " + count2);
    System.out.println("DP 2: " + count3);
  }


  public static int recursiveFibonacci(int n) {
    if (n == 0) {
      return 0;
    }
    if (n == 1) {
      return 1;
    }
    count1++;
    return recursiveFibonacci(n - 1) + recursiveFibonacci(n - 2);
  }

  public static int memoizedFibonacci(int n) {

    int[] arr = new int[n + 1];

    arr[0] = 0;
    arr[1] = 1;

    if (n < 2) {
      return arr[n];
    }


    for (int i = 2; i <= n; i++) {
      count2++;
      arr[i] = arr[i - 1] + arr[i - 2];
    }

    return arr[n];
  }

  public static int memoizedFibonacciTwo(int n) {

    int[] arr = new int[2];

    arr[0] = 0;
    arr[1] = 1;

    if (n < 2) {
      return arr[n];
    }

    for (int i = 2; i <= n; i++) {
      count3++;
      int sum = arr[0] + arr[1];
      arr[0] = arr[1];
      arr[1] = sum;
    }

    return arr[1];
  }
}
