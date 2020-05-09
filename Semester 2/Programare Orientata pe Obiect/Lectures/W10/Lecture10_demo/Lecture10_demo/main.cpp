#include <QtWidgets/QApplication>
#include <QSpinBox>
#include <QSlider>
#include <QHBoxLayout>
#include "GenesGUI.h"

int main(int argc, char *argv[])
{
	QApplication a(argc, argv);
	std::vector<Gene> genes{ Gene{ "yqgE", "E_Coli_K12", "ATGAATTTACAGCAT" }, 
							Gene{ "ppiA", "M_tuberculosis", "TTTTCATCACCGTCGG" }, 
							Gene{ "Col2a1", "Mouse", "TTAAAGCATGGCTCTGTG" } };
	GenesGUI gui{genes};
	gui.show();
	return a.exec();
}
