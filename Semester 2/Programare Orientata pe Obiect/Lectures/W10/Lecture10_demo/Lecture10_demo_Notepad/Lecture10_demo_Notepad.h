#ifndef LECTURE10_DEMO_NOTEPAD_H
#define LECTURE10_DEMO_NOTEPAD_H

#include <QtWidgets/QMainWindow>
#include "ui_lecture10_demo_notepad.h"
#include <QTextEdit>

class Lecture10_demo_Notepad : public QMainWindow
{
	Q_OBJECT

public:
	Lecture10_demo_Notepad(QWidget *parent = 0);
	~Lecture10_demo_Notepad();

	void buildWindow();

private:
	Ui::Lecture10_demo_NotepadClass ui;

	QMenu* fileMenu;
	QAction *openAction;
	QAction *saveAction;
	QAction *exitAction;
	QTextEdit* textEdit;

	std::string filename;

	void connectSignalsAndSlots();

	void open();
	void save();
};

#endif // LECTURE10_DEMO_NOTEPAD_H
