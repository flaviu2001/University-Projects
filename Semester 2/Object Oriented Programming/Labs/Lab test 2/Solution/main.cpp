#include "Repository.h"
#include "Service.h"
#include "GUI.h"

/**
	Still bitter about these tests though I finished it in 30 minutes. 
	Not at all accurate in testing people, get a bug you're doomed to fail.
*/

#include <QtWidgets/QApplication>
int main(int argc, char* argv[])
{
	Service::test_nrStatues();
	QApplication a(argc, argv);
	Repository Repository;
	Service Service(Repository);
	Service.readFromFile("File.txt");
	GUI AppGUI(Service);
	AppGUI.show();
	return a.exec();

}