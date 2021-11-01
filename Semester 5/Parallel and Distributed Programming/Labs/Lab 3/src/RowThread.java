public class RowThread implements Runnable{
    private final Integer[][] ans;
    private final Integer left;
    private final Integer right;

    RowThread(Integer[][] ans, int left, int right) {
        this.ans = ans;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        int n = Main.a;
        int m = Main.c;
        int i = left / m;
        int j = left % m;
        int k = right-left;
        for (int index = 0; index < k; ++index) {
            this.ans[i][j] = Matrix.getCell(Main.m1, Main.m2, i, j);
            ++j;
            if (j == m) {
                j = 0;
                ++i;
            }
        }
    }
}
