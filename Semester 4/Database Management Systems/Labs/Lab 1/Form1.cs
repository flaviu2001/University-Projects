using System;
using System.Data;
using System.Data.SqlClient;
using System.Windows.Forms;

namespace WindowsForm
{
    public partial class Form1 : Form
    {
        private readonly string CHESSBOARDCOMPANY_TABLE = "ChessboardCompany";
        private readonly string CHESSBOARD_TABLE = "Chessboard";
        private readonly string FK_CHESSBOARD_CHESSBOARDCOMPANY = "FK__Chessboar__comp__29572725";

        private DataSet dataSet = new DataSet();
        private SqlConnection dbConnection;

        private SqlDataAdapter dataAdapterChessboardCompany, dataAdapterChessboard;
        private BindingSource bindingChessboardCompany = new BindingSource();
        private BindingSource bindingChessboard = new BindingSource();

        private void InitializeDatabase()
        {
            dbConnection = new SqlConnection("Data Source = (LocalDb)\\MSSQLLocalDb; " +
                "Initial Catalog = chess; Integrated Security = SSPI;");

            dataAdapterChessboardCompany = new SqlDataAdapter($"SELECT * FROM {CHESSBOARDCOMPANY_TABLE}", dbConnection);
            dataAdapterChessboard = new SqlDataAdapter($"SELECT * FROM {CHESSBOARD_TABLE}", dbConnection);

            new SqlCommandBuilder(dataAdapterChessboard);

            dataAdapterChessboardCompany.Fill(dataSet, CHESSBOARDCOMPANY_TABLE);
            dataAdapterChessboard.Fill(dataSet, CHESSBOARD_TABLE);

            var dataRelation = new DataRelation(
                FK_CHESSBOARD_CHESSBOARDCOMPANY,
                dataSet.Tables[CHESSBOARDCOMPANY_TABLE].Columns["comp_id"],
                dataSet.Tables[CHESSBOARD_TABLE].Columns["comp_id"]);
            dataSet.Relations.Add(dataRelation);
        }

        private void InitializeUI()
        {
            bindingChessboardCompany.DataSource = dataSet;
            bindingChessboardCompany.DataMember = CHESSBOARDCOMPANY_TABLE;

            bindingChessboard.DataSource = bindingChessboardCompany;
            bindingChessboard.DataMember = FK_CHESSBOARD_CHESSBOARDCOMPANY;

            dataGridChessboardCompany.DataSource = bindingChessboardCompany;
            dataGridChessboars.DataSource = bindingChessboard;
        }

        private void buttonUpdateDB_Click(object sender, EventArgs e)
        {
            SqlCommandBuilder builder = new SqlCommandBuilder(dataAdapterChessboardCompany);
            builder.GetInsertCommand();
            dataAdapterChessboard.Update(dataSet, CHESSBOARD_TABLE);
            dataAdapterChessboardCompany.Update(dataSet, CHESSBOARDCOMPANY_TABLE);
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            InitializeDatabase();
            InitializeUI();
        }

        public Form1()
        {
            InitializeComponent();
        }

    }
}
