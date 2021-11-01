public class ColumnThread implements Runnable{
    private final Integer[][] ans;
    private final Integer left;
    private final Integer right;

    ColumnThread(Integer[][] ans, int left, int right) {
        this.ans = ans;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        int n = Main.a;
        int m = Main.c;
        int i = left % n;
        int j = left / n;
        int k = right-left;
        for (int index = 0; index < k; ++index) {
            this.ans[i][j] = Matrix.getCell(Main.m1, Main.m2, i, j);
            ++i;
            if (i == n) {
                i = 0;
                ++j;
            }
        }
    }
}
