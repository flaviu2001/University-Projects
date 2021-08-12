#include "GUI.h"
#include <QtWidgets/QListView>
#include <QtWidgets/QVBoxLayout>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QLabel>
#include <QtWidgets/QPushButton>


GUI::GUI(Service& _serv, Teacher _teacher, ModelWrapper& _m, QWidget* parent) : service{ _serv }, m{ _m }, teacher{ _teacher }, QWidget{ parent }
{
	this->setWindowTitle(teacher.name.c_str());
	if (m.m.find(teacher.name) == m.m.end())
		m.m[teacher.name] = new StudentsModel(this->service, teacher);
	if (m.m2.find(teacher.name) == m.m2.end())
		m.m2[teacher.name] = new SearchModel(this->service, teacher.name, "");
	auto students = new QListView;
	students->setModel(m.m[teacher.name]);
	auto layout = new QVBoxLayout{ this };
	layout->addWidget(students);
	auto le = new QLineEdit;
	layout->addWidget(le);
	auto searched = new QListView;
	searched->setModel(m.m2[teacher.name]);
	layout->addWidget(searched);
	connect(le, &QLineEdit::textChanged, [this, le]() {
		this->m.m2[this->teacher.name]->change_search(le->text().toStdString());
		});
	connect(searched, &QListView::doubleClicked, [this](const QModelIndex& index) {
		if (this->m.m2[this->teacher.name]->guys[index.row()].coordinator.empty()) {
			this->service.add_coordinator(this->m.m2[this->teacher.name]->guys[index.row()].name, this->teacher.name);
			this->m.propagate();
		}
		});
	connect(students, &QListView::doubleClicked, [this](const QModelIndex& index) {
		int row = index.row();
		auto students = this->service.get_students();
		int cnt = 0;
		for (auto x : students)
			if (x.coordinator == teacher.name) {
				if (cnt == row) {
					auto widget = new QWidget;
					auto layout2 = new QVBoxLayout{ widget };
					auto l1 = new QLabel{ x.id.c_str() };
					auto l2 = new QLabel{ x.name.c_str() };
					auto l3 = new QLineEdit{ x.email.c_str() };
					auto l4 = new QLabel{ std::to_string(x.year).c_str() };
					auto l5 = new QLineEdit{ x.title.c_str() };
					auto l6 = new QLabel{ x.coordinator.c_str() };
					layout2->addWidget(l1);
					layout2->addWidget(l2);
					layout2->addWidget(l3);
					layout2->addWidget(l4);
					layout2->addWidget(l5);
					layout2->addWidget(l6);
					auto btnsave = new QPushButton{ "Save" };
					auto btncancel = new QPushButton{ "Cancel" };
					layout2->addWidget(btnsave);
					layout2->addWidget(btncancel);
					connect(btnsave, &QPushButton::clicked, [this, l3, l5, x, widget]() {
						this->service.update_email(x.name, l3->text().toStdString());
						this->service.update_thesis(x.name, l5->text().toStdString());
						this->m.propagate();
						widget->close();
						});
					connect(btncancel, &QPushButton::clicked, [this, widget]() {
						widget->close();
						});
					widget->show();
					++cnt;
				}
				else ++cnt;
			}
		});
}
