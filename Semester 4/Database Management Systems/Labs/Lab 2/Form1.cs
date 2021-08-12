using System;
using System.Data;
using System.Windows.Forms;
using System.Data.SqlClient;
using System.Configuration;

namespace WindowsForm
{
    public partial class Form1 : Form
    {
        private DataSet dataSet = new DataSet();
        private SqlConnection dbConnection;

        private SqlDataAdapter dataAdapterChessboardCompany, dataAdapterChessboard;
        private readonly BindingSource bindingChessboardCompany = new BindingSource();
        private readonly BindingSource bindingChessboard = new BindingSource();

        private void InitializeDatabase()
        {
            String connectionString = ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            String database = ConfigurationManager.AppSettings["Database"];
            dbConnection = new SqlConnection(String.Format(connectionString, database));
            dataAdapterChessboardCompany = new SqlDataAdapter(ConfigurationManager.AppSettings["SelectParent"], dbConnection);
            dataAdapterChessboard = new SqlDataAdapter(ConfigurationManager.AppSettings["SelectChild"], dbConnection);

            new SqlCommandBuilder(dataAdapterChessboard);
            new SqlCommandBuilder(dataAdapterChessboardCompany).GetInsertCommand();

            dataAdapterChessboardCompany.Fill(dataSet, ConfigurationManager.AppSettings["ParentTableName"]);
            dataAdapterChessboard.Fill(dataSet, ConfigurationManager.AppSettings["ChildTableName"]);

            var dataRelation = new DataRelation(
                ConfigurationManager.AppSettings["ForeignKey"],
                dataSet.Tables[ConfigurationManager.AppSettings["ParentTableName"]].Columns[ConfigurationManager.AppSettings["ParentReferencedKey"]],
                dataSet.Tables[ConfigurationManager.AppSettings["ChildTableName"]].Columns[ConfigurationManager.AppSettings["ChildForeignKey"]]);
            dataSet.Relations.Add(dataRelation);
        }

        private void InitializeUI()
        {
            bindingChessboardCompany.DataSource = dataSet;
            bindingChessboardCompany.DataMember = ConfigurationManager.AppSettings["ParentTableName"];

            bindingChessboard.DataSource = bindingChessboardCompany;
            bindingChessboard.DataMember = ConfigurationManager.AppSettings["ForeignKey"];

            dataGridChessboardCompany.DataSource = bindingChessboardCompany;
            dataGridChessboars.DataSource = bindingChessboard;
        }

        private void buttonUpdateDB_Click(object sender, EventArgs e)
        {
            dataAdapterChessboard.Update(dataSet, ConfigurationManager.AppSettings["ChildTableName"]);
            dataAdapterChessboardCompany.Update(dataSet, ConfigurationManager.AppSettings["ParentTableName"]);
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            InitializeDatabase();
            InitializeUI();
        }

        private void buttonRefreshDB_Click(object sender, EventArgs e)
        {
            dataSet.Tables[ConfigurationManager.AppSettings["ChildTableName"]].Clear();
            dataSet.Tables[ConfigurationManager.AppSettings["ParentTableName"]].Clear();
            dataAdapterChessboardCompany.Fill(dataSet, ConfigurationManager.AppSettings["ParentTableName"]);
            dataAdapterChessboard.Fill(dataSet, ConfigurationManager.AppSettings["ChildTableName"]);
        }


        public Form1()
        {
            InitializeComponent();
        }
    }
}
