#include "drawing.h"
#include <QPainter>
#include <QKeyEvent>
#include <QDebug>

Drawing::Drawing(QWidget *parent) : QWidget{ parent }
{
	this->whatToPaint = Paint::Ellipse;
}

Drawing::~Drawing()
{
}

QSize Drawing::sizeHint() const
{
	return QSize{ 600, 400 };
}

void Drawing::paintManyThings()
{
	QPainter painter{ this };
	painter.eraseRect(0, 0, 600, 400);

	// draw a line
	painter.drawLine(10, 10, 80, 80);

	// draw a rectangle
	// first change the pen
	QPen pen1{ Qt::green, 3, Qt::DashDotDotLine, Qt::RoundCap };
	painter.setPen(pen1);
	painter.setBrush(Qt::NoBrush);
	QRect rectangle(100, 10, 200, 50);
	painter.drawRect(rectangle);

	// draw an ellipse
	QPen pen2{ Qt::red, 1, Qt::SolidLine, Qt::RoundCap };
	painter.setPen(pen2);
	QBrush brush{ Qt::red, Qt::FDiagPattern };
	painter.setBrush(brush);
	painter.drawEllipse(320, 80, 250, 130);

	// draw a path
	QPen pen3{};		// default pen
	QBrush brush3{};	// default brush
	painter.setPen(pen3);
	painter.setBrush(brush3);
	QPainterPath path;
	path.moveTo(20, 200);
	path.lineTo(20, 300);
	path.lineTo(100, 300);
	painter.drawPath(path);

	// another, more complex, path
	QPainterPath path2;
	path2.addRect(100, 300, 60, 60);
	path2.moveTo(100, 300);
	path2.cubicTo(199, 300, 150, 150, 199, 199);
	path2.cubicTo(300, 399, 150, 150, 100, 300);
	painter.setPen(QPen(QColor(79, 106, 25), 1, Qt::SolidLine, Qt::FlatCap, Qt::MiterJoin));
	painter.setBrush(QColor(10, 80, 39));
	painter.drawPath(path2);
}

void Drawing::paintEllipse()
{
	QPainter painter{ this };
	painter.eraseRect(0, 0, 600, 400);

	// draw an ellipse
	QPen pen2{ Qt::red, 1, Qt::SolidLine, Qt::RoundCap };
	painter.setPen(pen2);
	QBrush brush{ Qt::red, Qt::FDiagPattern };
	painter.setBrush(brush);
	painter.drawEllipse(150, 100, 250, 130);
}

void Drawing::paintEllipseUp()
{
	QPainter painter{ this };
	painter.eraseRect(0, 0, 600, 400);

	// draw an ellipse
	QPen pen2{ Qt::red, 1, Qt::SolidLine, Qt::RoundCap };
	painter.setPen(pen2);
	QBrush brush{ Qt::red, Qt::FDiagPattern };
	painter.setBrush(brush);
	painter.drawEllipse(150, 20, 250, 130);
}

void Drawing::paintEllipseDown()
{
	QPainter painter{ this };
	painter.eraseRect(0, 0, 600, 400);

	// draw an ellipse
	QPen pen2{ Qt::red, 1, Qt::SolidLine, Qt::RoundCap };
	painter.setPen(pen2);
	QBrush brush{ Qt::red, Qt::FDiagPattern };
	painter.setBrush(brush);
	painter.drawEllipse(150, 180, 250, 130);
}

void Drawing::paintEvent(QPaintEvent *event)
{
	switch (this->whatToPaint)
	{
	case Paint::ManyThings:
		this->paintManyThings();
		break;
	case Paint::Ellipse:
		this->paintEllipse();
		break;
	case Paint::EllipseUp:
		this->paintEllipseUp();
		break;
	case Paint::EllipseDown:
		this->paintEllipseDown();
		break;
	default:
		break;
	}
}

void Drawing::mousePressEvent(QMouseEvent * event)
{
	qDebug() << "Mouse pressed";
	this->whatToPaint = Paint::ManyThings;
	this->update();
	this->setFocus();
}

void Drawing::keyPressEvent(QKeyEvent *event)
{
	QWidget::keyPressEvent(event);
	int k = event->key();
	switch (k)
	{
	case Qt::Key_Up:
		qDebug() << "up";
		this->whatToPaint = Paint::EllipseUp;
		this->update();
		break;
	case Qt::Key_Down:
		qDebug() << "down";
		this->whatToPaint = Paint::EllipseDown;
		this->update();
		break;
	}
}