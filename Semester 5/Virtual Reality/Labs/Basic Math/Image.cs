using System.Drawing;
using System.Drawing.Imaging;

namespace ray_tracer
{
    public class Image
    {
        private Bitmap bitmap;

        public Image(int width, int height) {
            bitmap = new Bitmap(width, height);
        }

        public void SetPixel(int x, int y, Color c) {
            bitmap.SetPixel(x, y, c.ToSystemColor());
        }

        public void Store(string filename) {
            bitmap.Save(filename, ImageFormat.Png);
        }
    }
}