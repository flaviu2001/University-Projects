#pragma once
#include "QtWidgets/qwidget.h"
#include "Service.h"
#include "qlayout.h"
#include "QListWidget.h"
#include "QLabel.h"
#include "QLineEdit.h"
#include "QPushButton.h"
#include "QString.h"
#include "QMessageBox.h"



class GUI : public QWidget
{

	Q_OBJECT
private:
	Service service;
	QVBoxLayout* layout;
	QListWidget* display;
	QVBoxLayout* fields;
	QHBoxLayout* conjurerlayout;
	QLabel* conjurerlabel;
	QLineEdit* conjurerlineedit;
	QPushButton* showStatues;
	QLabel* numberConjurers;

public:
	GUI(Service _service, QWidget* Parent = Q_NULLPTR);

	void loadServiceObjects();
	void connectSignalsAndSlots();
};
