#include "drawing.h"
#include <QtWidgets/QApplication>

int main(int argc, char *argv[])
{
	QApplication a(argc, argv);
	Drawing w;
	w.show();
	return a.exec();
}
