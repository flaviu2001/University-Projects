#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_Lecture10_demo_spin_slider.h"

class Lecture10_demo_spin_slider : public QMainWindow
{
	Q_OBJECT

public:
	Lecture10_demo_spin_slider(QWidget *parent = Q_NULLPTR);

private:
	Ui::Lecture10_demo_spin_sliderClass ui;
};
