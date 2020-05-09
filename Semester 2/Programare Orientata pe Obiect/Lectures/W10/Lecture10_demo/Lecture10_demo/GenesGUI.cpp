#include "GenesGUI.h"
#include <QDebug>

GenesGUI::GenesGUI(std::vector<Gene> _genes, QWidget * parent): QWidget{parent}, genes{_genes}
{
	this->initGUI();
	this->connectSignalsAndSlots();
	this->populateGenesList();
}

GenesGUI::~GenesGUI()
{
}

void GenesGUI::initGUI()
{
	//General layout of the window
	QHBoxLayout* layout = new QHBoxLayout{ this };

	// left side - just the list
	this->genesList = new QListWidget{};
	// set the selection model
	this->genesList->setSelectionMode(QAbstractItemView::SingleSelection);
	layout->addWidget(this->genesList);

	// right side - gene information + buttons
	QWidget* rightSide = new QWidget{};
	QVBoxLayout* vLayout = new QVBoxLayout{ rightSide };

	// gene information
	QWidget* geneDataWidget = new QWidget{};
	QFormLayout* formLayout = new QFormLayout{ geneDataWidget };
	this->organismEdit = new QLineEdit{};
	this->geneNameEdit = new QLineEdit{};
	this->sequenceEdit = new QTextEdit{};
	QFont f{ "Verdana", 15 };
	QLabel* organismLabel = new QLabel{ "&Organism name:" };
	organismLabel->setBuddy(this->organismEdit);
	QLabel* geneLabel = new QLabel{ "&Gene name : " };
	geneLabel->setBuddy(this->geneNameEdit);
	QLabel* sequenceLabel = new QLabel{ "&Sequence:" };
	sequenceLabel->setBuddy(this->sequenceEdit);
	organismLabel->setFont(f);
	geneLabel->setFont(f);
	sequenceLabel->setFont(f);
	this->organismEdit->setFont(f);
	this->geneNameEdit->setFont(f);
	this->sequenceEdit->setFont(f);
	formLayout->addRow(organismLabel, this->organismEdit);
	formLayout->addRow(geneLabel, this->geneNameEdit);
	formLayout->addRow(sequenceLabel, this->sequenceEdit);

	vLayout->addWidget(geneDataWidget);

	// buttons
	QWidget* buttonsWidget = new QWidget{};
	QHBoxLayout* hLayout = new QHBoxLayout{ buttonsWidget };
	this->addGeneButton = new QPushButton("Add Gene");
	this->addGeneButton->setFont(f);
	this->deleteGeneButton = new QPushButton("Delete Gene");
	this->deleteGeneButton->setFont(f);
	hLayout->addWidget(this->addGeneButton);
	hLayout->addWidget(this->deleteGeneButton);

	vLayout->addWidget(buttonsWidget);

	// add everything to the big layout
	layout->addWidget(this->genesList);
	layout->addWidget(rightSide);
}

void GenesGUI::connectSignalsAndSlots()
{
	// when the vector of genes is updated - re-populate the list
	//QObject::connect(this, SIGNAL(genesUpdatedSignal()), this, SLOT(populateGenesList()));
	QObject::connect(this, &GenesGUI::genesUpdatedSignal, this, &GenesGUI::populateGenesList);

	// add a connection: function listItemChanged() will be called when an item in the list is selected
	QObject::connect(this->genesList, &QListWidget::itemSelectionChanged, this, [this]() {this->listItemChanged(); });

	// add button connections
	QObject::connect(this->addGeneButton, &QPushButton::clicked, this, &GenesGUI::addGeneButtonHandler);
	QObject::connect(this->deleteGeneButton, &QPushButton::clicked, this, &GenesGUI::deleteGeneButtonHandler);

	// connect the addGene signal to the addGene slot, which adds a gene to vector
	QObject::connect(this, SIGNAL(addGeneSignal(const std::string&, const std::string&, const std::string&)), 
						this, SLOT(addGene(const std::string&, const std::string&, const std::string&)));
}

void GenesGUI::addGene(const std::string& geneName, const std::string& organismName, const std::string& sequence)
{
	this->genes.push_back(Gene{ geneName, organismName, sequence });

	// emit the signal: the genes were updated
	emit genesUpdatedSignal();
}

void GenesGUI::addGeneButtonHandler()
{
	// read data from the textboxes and add the new gene
	QString organismName = this->organismEdit->text();
	QString geneName = this->geneNameEdit->text();
	QString sequence = this->sequenceEdit->toPlainText();

	// emit the addGene signal
	emit addGeneSignal(geneName.toStdString(), organismName.toStdString(), sequence.toStdString());
}

void GenesGUI::deleteGeneButtonHandler()
{
	// get the selected index and delete the gene
	int idx = this->getSelectedIndex();

	if (idx < 0 || idx >= this->genes.size())
		return;

	this->genes.erase(this->genes.begin() + idx);

	// emit the signal: the genes were updated
	emit genesUpdatedSignal();
}

void GenesGUI::populateGenesList()
{
	// clear the list, if there are elements in it
	if (this->genesList->count() > 0)
		this->genesList->clear();

	for (auto g : this->genes)
	{
		QString itemInList = QString::fromStdString(g.getOrganismName() + " - " + g.getName());
		QFont f{ "Verdana", 15 };
		QListWidgetItem* item = new QListWidgetItem{ itemInList };
		item->setFont(f);
		this->genesList->addItem(item);
	}

	// set the selection on the first item in the list
	if (this->genes.size() > 0)
		this->genesList->setCurrentRow(0);
}

int GenesGUI::getSelectedIndex()
{
	if (this->genesList->count() == 0)
		return -1;

	// get selected index
	QModelIndexList index = this->genesList->selectionModel()->selectedIndexes();
	if (index.size() == 0)
	{
		this->organismEdit->clear();
		this->geneNameEdit->clear();
		this->sequenceEdit->clear();
		return -1;
	}

	int idx = index.at(0).row();
	return idx;
}

void GenesGUI::listItemChanged()
{
	int idx = this->getSelectedIndex();
	if (idx == -1)
		return;

	// get the song at the selected index
	if (idx >= this->genes.size())
		return;
	Gene g = this->genes[idx];

	this->organismEdit->setText(QString::fromStdString(g.getOrganismName()));
	this->geneNameEdit->setText(QString::fromStdString(g.getName()));
	this->sequenceEdit->setText(QString::fromStdString(g.getSequence()));
}
