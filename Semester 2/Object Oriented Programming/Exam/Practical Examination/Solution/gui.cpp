#include "gui.h"

#include <QtWidgets/QLabel>
#include <QtWidgets/QVBoxLayout>
#include <QtWidgets/QTableView>
#include <QtWidgets/QCheckBox>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QMessageBox>
#include <QtWidgets/QListView>
#include <QPainter>
#include <QTimer>
#include <string>
#include "qdebug.h"

class Drawing : public QWidget {
private:
	void setFloatBased(bool floatBased) {
		this->floatBased = floatBased;
		update();
	}
	void setAntialiased(bool antialiased) {
		this->antialiased = antialiased;
		update();
	}

	QSize minimumSizeHint() const override {
		return QSize(50, 50);
	}
	QSize sizeHint() const override {
		return QSize(180, 180);
	}

	bool floatBased;
	bool antialiased;
	int frameNo;
	std::vector<Star> stars;
	Star theStar;
public:
	Drawing(QWidget* parent = Q_NULLPTR) {
		floatBased = false;
		antialiased = false;
		frameNo = 0;
		setBackgroundRole(QPalette::Base);
		setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
	}
	void addStars(std::vector<Star> s, Star star) {
		stars = std::move(s);
		theStar = star;
	}
protected:
	void paintEvent(QPaintEvent* event) override {
		QPainter painter(this);
		painter.setRenderHint(QPainter::Antialiasing, antialiased);
		painter.translate(width() / 2, height() / 2);
		for (auto el : stars) {
			if (el.name == theStar.name) {
				QPen pen;
				pen.setColor(QColor(255, 0, 0, 255));
				painter.setPen(pen);
				painter.drawEllipse(QPoint(el.ra, el.dec), el.diameter,
					el.diameter);
			}
			else {
				QPen pen;
				pen.setColor(QColor(0, 0, 0, 255));
				painter.setPen(pen);
				painter.drawEllipse(QPoint(el.ra, el.dec), el.diameter,
					el.diameter);
			}
		}
	}

};


GUI::GUI(Service& _serv, std::string _name, ModelWrapper* _m, QWidget* parent) : serv{ _serv }, QWidget(parent), name{_name}, m{_m}
{
	this->setWindowTitle(this->name.c_str());
	Astronomer *a = new Astronomer;
	for (auto x : serv.get_astronomers())
		if (x.name == name)
			*a = x;
	if (m->mp.find(name) == m->mp.end())
		m->mp[name] = new TableModel{ this->serv, *a };
	auto layout = new QVBoxLayout{ this };
	auto tv = new QTableView;
	tv->setModel(m->mp[name]);
	auto checkbox = new QCheckBox("Switch", this);
	layout->addWidget(checkbox);
	layout->addWidget(tv);
	connect(checkbox, &QCheckBox::stateChanged, [this](int new_state) {
		m->mp[name]->change();
		m->propagate();
		});
	auto le_name = new QLineEdit{ "Name: " };
	auto le_ra = new QLineEdit{ "ra: " };
	auto le_dec = new QLineEdit{ "dec: " };
	auto le_diameter = new QLineEdit{ "diameter: " };
	auto btn = new QPushButton{ "Add" };
	layout->addWidget(le_name);
	layout->addWidget(le_ra);
	layout->addWidget(le_dec);
	layout->addWidget(le_diameter);
	layout->addWidget(btn);
	connect(btn, &QPushButton::clicked, [this, le_name, le_ra, le_dec, le_diameter, a]() {
		try {
			this->serv.add_star(le_name->text().toStdString(), (*a).constellation, 
				atoi(le_ra->text().toStdString().c_str()), 
				atoi(le_dec->text().toStdString().c_str()),
				atoi(le_diameter->text().toStdString().c_str()));
			m->propagate();
		}
		catch (std::exception& e) {
			QMessageBox::critical(this, "Error", e.what());
		}
	});
	auto le = new QLineEdit;
	layout->addWidget(le);
	if (m->mp2.find(name) == m->mp2.end())
		m->mp2[name] = new ListModel{ this->serv, "" };
	auto lv = new QListView;
	lv->setModel(m->mp2[name]);
	layout->addWidget(lv);
	connect(le, &QLineEdit::textChanged, [this, le]() {
		m->mp2[name]->change_search(le->text().toStdString());
		m->propagate();
	});
	auto btn_view = new QPushButton{ "View" };
	layout->addWidget(btn_view);
	connect(btn_view, &QPushButton::clicked, [this, tv]() {
		auto window = new QWidget;
		window->show();
		auto list = tv->selectionModel()->selectedIndexes();
		int pos = 0;
		if (list.empty()) {
			pos = -1;
		}
		else {
			pos = list.at(0).row();
		}
		Star star;
		if (pos >= 0)
			star = this->serv.get_stars()[pos];
		auto d = new Drawing(window);
		d->addStars(this->serv.get_stars(), star);
	});
}
