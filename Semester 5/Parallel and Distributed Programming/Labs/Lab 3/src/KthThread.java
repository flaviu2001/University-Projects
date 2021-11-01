public class KthThread implements Runnable{
    private final Integer[][] ans;
    private final Integer k;
    private final Integer stepSize;

    KthThread(Integer[][] ans, int k, int stepSize) {
        this.ans = ans;
        this.k = k;
        this.stepSize = stepSize;
    }

    @Override
    public void run() {
        int n = Main.a;
        int m = Main.c;
        int i = 0;
        int j = k;
        while (true) {
            int overshoot = j/m;
            i += overshoot;
            j -= overshoot * m;
            if (i >= n)
                break;
            ans[i][j] = Matrix.getCell(Main.m1, Main.m2, i, j);
            j += stepSize;
        }
    }
}
