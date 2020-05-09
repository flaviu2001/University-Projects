#ifndef DRAWING_H
#define DRAWING_H

#include <QtWidgets/QMainWindow>
#include <QPen>

class Drawing : public QWidget
{
	Q_OBJECT

private:
	enum Paint
	{
		ManyThings,
		Ellipse,
		EllipseUp,
		EllipseDown
	};
	Paint whatToPaint;

public:
	Drawing(QWidget *parent = 0);
	~Drawing();
	QSize sizeHint() const Q_DECL_OVERRIDE; // this must be redefined (the default implementation returns an invalid size, if there is no layout)

	void paintManyThings();
	void paintEllipse();
	void paintEllipseUp();
	void paintEllipseDown();

protected:
	void paintEvent(QPaintEvent *event) override;		// will draw shapes on the widget
	void keyPressEvent(QKeyEvent *event) Q_DECL_OVERRIDE;		// is invoked when a key is pressed, IF the widget has the focus
	void mousePressEvent(QMouseEvent * event) Q_DECL_OVERRIDE;	// is invoked when a key on the mouse is pressed
};

#endif // DRAWING_H
