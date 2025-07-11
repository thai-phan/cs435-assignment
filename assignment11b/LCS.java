


public class LCS {

  static int count1 = 0;
  static int count2 = 0;

  public static void main(String[] args) {
//    String s1 = "ABCD";
//    String s2 = "ABEECD";

    String s1 = "ABCDEEEEDASDAS";
    String s2 = "ABEECDGUELKALK";


    int num1 = LCSBruteForce(s1, s2, s1.length(), s2.length());
    System.out.println(num1);

    int m = s1.length();
    int n = s2.length();
    int[][] arr = new int[m + 1][n + 1];
    for (int i = 0; i <= m; i++) {
      for (int j = 0; j <= n; j++) {
        arr[i][j] = -1;
      }
    }
    int num3 = LCSMemoized(s1, s2, m, n, arr);
    System.out.println(num3);

    System.out.println(count1);
    System.out.println(count2);
  }


  public static int LCSBruteForce(String s1, String s2, int m, int n) {
    count1++;
    if (n == 0)
      return 0;
    else if (m == 0)
      return 0;
    else if (s1.charAt(m - 1) == s2.charAt(n - 1)) {
      return LCSBruteForce(s1, s2, m - 1, n - 1) + 1;
    } else
      return Math.max(LCSBruteForce(s1, s2, m, n - 1), LCSBruteForce(s1, s2, m - 1, n));
  }

  public static int LCSMemoized(String s1, String s2, int m, int n, int[][] arr) {
    if (arr[m][n] > -1)
      return arr[m][n];
    count2++;
    if (n == 0)
      arr[m][n] = 0;
    else if (m == 0)
      arr[m][n] = 0;
    else if (s1.charAt(m - 1) == s2.charAt(n - 1))
      arr[m][n] = LCSMemoized(s1, s2, m - 1, n - 1, arr) + 1;
    else
      arr[m][n] = Math.max(LCSMemoized(s1, s2, m, n - 1, arr), LCSMemoized(s1, s2, m - 1, n, arr));
    return arr[m][n];
  }


  public static int LCSDP(String X, String Y) {
    int m = X.length();
    int n = Y.length();
    int[] A = new int[n + 1];
    int[] L = new int[n + 1];
    for (int i = 0; i <= m - 1; i++) {
      int[] temp = A; // switch arrays L & A
      A = L;
      L = temp;
      for (int j = 0; j <= n - 1; j++) {
        if (X.charAt(i) == Y.charAt(j)) {
          L[j + 1] = A[j] + 1;
        } else {
          L[j + 1] = intMax(A[j + 1], L[j]);
        }
      }
    }
    return L[n];
  }

  public static int intMax(int x, int y) {
    return x > y ? x : y;
  }
}
