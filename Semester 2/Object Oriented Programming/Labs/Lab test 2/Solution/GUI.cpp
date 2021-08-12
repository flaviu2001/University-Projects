#include "GUI.h"
#include <sstream>

GUI::GUI(Service _service, QWidget* Parent)
	: service(_service), QWidget(Parent)
{
	this->setWindowTitle("App");
	layout = new QVBoxLayout(this);
	display = new QListWidget;
	fields = new QVBoxLayout;
	conjurerlayout = new QHBoxLayout;
	conjurerlabel = new QLabel("conjurer");
	conjurerlineedit = new QLineEdit;
	showStatues = new QPushButton{"Show Statues"};
	numberConjurers = new QLabel;
	fields->addLayout(conjurerlayout);
	conjurerlayout->addWidget(conjurerlabel);
	conjurerlayout->addWidget(conjurerlineedit);
	fields->addWidget(showStatues);
	fields->addWidget(numberConjurers);
	layout->addWidget(display);
	layout->addLayout(fields);
	this->loadServiceObjects();
	this->connectSignalsAndSlots();
}

void GUI::loadServiceObjects()
{
	std::vector<Statue> container = service.list();
	sort(container.begin(), container.end(), [](Statue a, Statue b) {
		return a.getconjurer() < b.getconjurer();
	});
	int cnt = 0;
	for (Statue currentObject : container)
	{
		this->display->addItem(QString::fromStdString(to_string(currentObject)));
		this->display->item(cnt)->setTextColor(currentObject.getcolor().c_str());
		++cnt;
	}
}

void GUI::connectSignalsAndSlots()
{
	QObject::connect(showStatues, &QPushButton::clicked, [this]() {
		std::stringstream ss;
		ss << service.nrStatues(conjurerlineedit->text().toStdString().c_str());
		numberConjurers->setText(ss.str().c_str());
	});
}
