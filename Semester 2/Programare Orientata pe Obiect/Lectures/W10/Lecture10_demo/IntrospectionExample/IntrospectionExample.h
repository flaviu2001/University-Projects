#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_IntrospectionExample.h"

class IntrospectionExample : public QMainWindow
{
	Q_OBJECT

public:
	IntrospectionExample(QWidget *parent = Q_NULLPTR);

private:
	Ui::IntrospectionExampleClass ui;
};
