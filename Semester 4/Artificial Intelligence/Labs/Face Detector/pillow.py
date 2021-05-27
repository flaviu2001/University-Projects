from matplotlib import image
from matplotlib import pyplot
from PIL import Image


def a():
    man = Image.open('dataset1/man1.jpg')
    print(man.format)
    print(man.mode)
    print(man.size)
    man.show()


def b():
    # load and display an image with Matplotlib
    # load image as pixel array
    data = image.imread('dataset1/man1.jpg')
    # summarize shape of the pixel array
    print(data.dtype)
    print(data.shape)
    # display the array of pixels as an image
    pyplot.imshow(data)
    pyplot.show()


def c():
    man = Image.open('dataset1/man1.jpg')
    print(man.size)
    man.thumbnail((150, 150))
    print(man.size)
    man.show()


if __name__ == "__main__":
    a()
