import java.io.*;
import java.util.*;

public class Main {

    static final double INF = Double.POSITIVE_INFINITY;
    static final int BASE_CASE = 5;

    static double dist(long[] a, long[] b) {
        double dx = a[0] - b[0], dy = a[1] - b[1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    static double perimeter(long[] a, long[] b, long[] c) {
        return dist(a, b) + dist(b, c) + dist(c, a);
    }

    static double bruteForce(long[][] p) {
        double best = INF;
        for (int i = 0; i < p.length; i++)
            for (int j = i + 1; j < p.length; j++)
                for (int k = j + 1; k < p.length; k++)
                    best = Math.min(best, perimeter(p[i], p[j], p[k]));
        return best;
    }

    static double solve(long[][] px, long[][] py) {
        int n = px.length;
        if (n < 3) return INF;
        if (n <= BASE_CASE) return bruteForce(px);

        int mid = n / 2;
        long[][] leftX = Arrays.copyOfRange(px, 0, mid);
        long[][] rightX = Arrays.copyOfRange(px, mid, n);

        Set<long[]> leftSet = Collections.newSetFromMap(new IdentityHashMap<>());
        leftSet.addAll(Arrays.asList(leftX));

        long[][] leftY = new long[mid][];
        long[][] rightY = new long[n - mid][];
        int li = 0, ri = 0;
        for (long[] p : py) {
            if (leftSet.contains(p)) leftY[li++] = p;
            else rightY[ri++] = p;
        }

        double best = Math.min(solve(leftX, leftY), solve(rightX, rightY));
        if (best == 0) return 0;

        double midX = ((double) px[mid - 1][0] + px[mid][0]) / 2.0;

        long[][] strip = new long[n][];
        int m = 0;
        for (long[] p : py)
            if (Math.abs(p[0] - midX) < best / 2.0) strip[m++] = p;

        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                if (strip[j][1] - strip[i][1] >= best / 2.0) break;
                for (int k = j + 1; k < m; k++) {
                    if (strip[k][1] - strip[i][1] >= best / 2.0) break;
                    best = Math.min(best, perimeter(strip[i], strip[j], strip[k]));
                }
            }
        }
        return best;
    }

    static double minimumPerimeter(long[][] pts) {
        long[][] px = pts.clone();
        long[][] py = pts.clone();
        Arrays.sort(px, (a, b) -> a[0] != b[0] ? Long.compare(a[0], b[0]) : Long.compare(a[1], b[1]));
        Arrays.sort(py, (a, b) -> a[1] != b[1] ? Long.compare(a[1], b[1]) : Long.compare(a[0], b[0]));
        return solve(px, py);
    }

    public static void main(String[] args) throws IOException {
        StreamTokenizer st = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in), 1 << 16));
        StringBuilder out = new StringBuilder();
        while (st.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) st.nval;
            long[][] pts = new long[n][2];
            for (int i = 0; i < n; i++) {
                st.nextToken(); pts[i][0] = (long) st.nval;
                st.nextToken(); pts[i][1] = (long) st.nval;
            }
            out.append(String.format(Locale.US, "%.3f%n", minimumPerimeter(pts)));
        }
        System.out.print(out);
    }
}