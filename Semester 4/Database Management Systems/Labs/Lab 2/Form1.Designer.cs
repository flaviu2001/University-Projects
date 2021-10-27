
namespace WindowsForm
{
    partial class Form1
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.dataGridChessboardCompany = new System.Windows.Forms.DataGridView();
            this.dataGridChessboars = new System.Windows.Forms.DataGridView();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.buttonUpdateDB = new System.Windows.Forms.Button();
            this.buttonRefreshDB = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridChessboardCompany)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridChessboars)).BeginInit();
            this.SuspendLayout();
            // 
            // dataGridChessboardCompany
            // 
            this.dataGridChessboardCompany.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.dataGridChessboardCompany.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridChessboardCompany.Location = new System.Drawing.Point(35, 35);
            this.dataGridChessboardCompany.Name = "dataGridChessboardCompany";
            this.dataGridChessboardCompany.RowTemplate.Height = 25;
            this.dataGridChessboardCompany.Size = new System.Drawing.Size(465, 174);
            this.dataGridChessboardCompany.TabIndex = 0;
            // 
            // dataGridChessboars
            // 
            this.dataGridChessboars.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.dataGridChessboars.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridChessboars.Location = new System.Drawing.Point(35, 249);
            this.dataGridChessboars.Name = "dataGridChessboars";
            this.dataGridChessboars.RowTemplate.Height = 25;
            this.dataGridChessboars.Size = new System.Drawing.Size(465, 167);
            this.dataGridChessboars.TabIndex = 1;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(32, 224);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(68, 13);
            this.label1.TabIndex = 2;
            this.label1.Text = "Chessboards";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(35, 10);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(118, 13);
            this.label2.TabIndex = 3;
            this.label2.Text = "Chessboard Companies";
            // 
            // buttonUpdateDB
            // 
            this.buttonUpdateDB.Location = new System.Drawing.Point(579, 153);
            this.buttonUpdateDB.Name = "buttonUpdateDB";
            this.buttonUpdateDB.Size = new System.Drawing.Size(87, 29);
            this.buttonUpdateDB.TabIndex = 4;
            this.buttonUpdateDB.Text = "Update DB";
            this.buttonUpdateDB.UseVisualStyleBackColor = true;
            this.buttonUpdateDB.Click += new System.EventHandler(this.buttonUpdateDB_Click);
            // 
            // buttonRefreshDB
            // 
            this.buttonRefreshDB.Location = new System.Drawing.Point(579, 271);
            this.buttonRefreshDB.Name = "buttonRefreshDB";
            this.buttonRefreshDB.Size = new System.Drawing.Size(87, 29);
            this.buttonRefreshDB.TabIndex = 5;
            this.buttonRefreshDB.Text = "Refresh DB";
            this.buttonRefreshDB.UseVisualStyleBackColor = true;
            this.buttonRefreshDB.Click += new System.EventHandler(this.buttonRefreshDB_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(880, 505);
            this.Controls.Add(this.buttonRefreshDB);
            this.Controls.Add(this.buttonUpdateDB);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.dataGridChessboars);
            this.Controls.Add(this.dataGridChessboardCompany);
            this.Name = "Form1";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.Form1_Load);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridChessboardCompany)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridChessboars)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.DataGridView dataGridChessboardCompany;
        private System.Windows.Forms.DataGridView dataGridChessboars;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button buttonUpdateDB;
        private System.Windows.Forms.Button buttonRefreshDB;
    }
}

