/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p_18090051;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lilissonia
 */
public class JavaMySQL extends javax.swing.JFrame {
    int idBaris=0;
    String role;
    DefaultTableModel model;
    
    public JavaMySQL() {
        initComponents();
        
        //KoneksiDB.sambungDB();
        aturModelTabel();
        pangkat();
        jafung();
        showForm(false);
        showData("");
    }
private void aturModelTabel(){
    Object[] kolom = {"NO","NIDN","NAMA","JAFUNG","Golongan/Pangkat","No HP"};
    model = new DefaultTableModel(kolom, 0){
        boolean[] canEdit = new boolean[]{
            false, false, false, false, false, false
        };
        
        //@Override
        public boolean isCellEditabel(int rowIndex, int columnIndex){
            return canEdit[columnIndex];
        }
    };
    tblData.setModel(model);
    tblData.setRowHeight(20);
    tblData.getColumnModel().getColumn(0).setMinWidth(0);
    tblData.getColumnModel().getColumn(0).setMaxWidth(0);
}

private void showForm(boolean b){
    areaSplit.setDividerLocation(0.3);
    areaSplit.getLeftComponent().setVisible(b);   
}

private void resetForm(){
    tblData.clearSelection();
    txtNidn.setText("");
    txtNama.setText("");
    cmbJafung.getSelectedIndex();
    cmbPangkat.getSelectedIndex();
    txtNohp.setText("");
    txtNidn.requestFocus();
}

private void pangkat(){
    cmbPangkat.removeAllItems();
    cmbPangkat.addItem("Pilih Golongan/Pangkat");
    cmbPangkat.addItem("Belum ada");
    cmbPangkat.addItem("II/c (Pengatur)");
    cmbPangkat.addItem("II/d (Pengatur Tk. I");
    cmbPangkat.addItem("III/a (Penata Muda)");
    cmbPangkat.addItem("III/b (Penata Muda Tk. I)");
    cmbPangkat.addItem("III/c (Penata)");
    cmbPangkat.addItem("III/d (Penata Tk. I)");
    cmbPangkat.addItem("IV/a (Pembina)");
    cmbPangkat.addItem("IV/b (Pembina Tk. I)");
    cmbPangkat.addItem("IV/c (Pembina Utama Muda)");
    cmbPangkat.addItem("IV/d (Pembina Utama Madya)");
    cmbPangkat.addItem("IV/e (Pembina Utama)");
}

private void jafung(){
    cmbJafung.removeAllItems();
    cmbJafung.addItem("Pilih Golongan/Pangkat");
    cmbJafung.addItem("Belum ada");
    cmbJafung.addItem("Asisten Ahli (100)");
    cmbJafung.addItem("Asisten Ahli (150)");
    cmbJafung.addItem("Lektor (200)");
    cmbJafung.addItem("Lektor (3000)");
    cmbJafung.addItem("Lektor Kepala (400)");
    cmbJafung.addItem("Lektor Kepala (550)");
    cmbJafung.addItem("Lektor Kepala (700)");
    cmbJafung.addItem("Profesor (850)");
    cmbJafung.addItem("Profesor (1050)");
}
   
private void showData(String key){
    model.getDataVector().removeAllElements();
    String where = "";
    if(!key.isEmpty()){
        where += "WHERE nidn LIKE '%"+key+"% "
                + "OR nama LIKE '%"+key+"%' "
                + "OR jabatan_fungsional LIKE '%"+key+"%' "
                + "OR pangkat_golongan LIKE '%"+key+"%' "
                + "OR no_hp LIKE '%"+key+"%' ";
    }
    String sql = "SELECT * FROM data_dosen "+where;
    Connection con;
    Statement st;
    ResultSet rs;
    int baris = 0;
    try {
        con = KoneksiDB.sambungDB();
        st = con.createStatement();
        rs = st.executeQuery(sql);
        while (rs.next()){
            Object id = rs.getInt(1);
            Object nidn = rs.getString(2);
            Object nama = rs.getString(3);
            Object jafung = rs.getString(4);
            Object pangkat = rs.getString(5);
            Object no_hp = rs.getString(5);
            Object[] data = {id,nidn,nama,jafung,pangkat,no_hp};
            model.insertRow(baris, data);
            baris++;
        }
        st.close();
        con.close();
        tblData.revalidate();
        tblData.repaint();
    } catch (SQLException e) {
        System.err.println("showData(): "+e.getMessage());
    } 
}

private void resetView(){
    resetForm();
    showForm(false);
    showData("");
    btnHapus.setEnabled(false);
    idBaris = 0;
}

private void pilihData(String n){
    btnHapus.setEnabled(true);
    
    String sql = "SELECT * FROM data_dosen WHERE id='"+n+"'";
    Connection con;
    Statement st;
    ResultSet rs;
    try {
        con = KoneksiDB.sambungDB();
        st = con.createStatement();
        rs = st.executeQuery(sql);
        while (rs.next()){
            int id = rs.getInt(1);
            String nidn = rs.getString(2);
            String nama = rs.getString(3);
            Object jafung = rs.getString(4);
            Object pangkat = rs.getString(5);
            String no_hp = rs.getString(5);
            idBaris = id;
            txtNidn.setText(nidn);
            txtNama.setText(nama);
            cmbJafung.setSelectedItem(jafung);
            cmbPangkat.setSelectedItem(pangkat);
            txtNohp.setText(no_hp);
        }
        st.close();
        con.close();
        showForm(true);
    } catch (SQLException e) {
        System.err.println("showData(): "+e.getMessage());
    }
}

private void simpanData(){
    String nidn = txtNidn.getText();
    String nama = txtNama.getText();
    int jafung = cmbJafung.getSelectedIndex();
    int pangkat = cmbPangkat.getSelectedIndex();
    String nohp = txtNohp.getText();
    if(nidn.isEmpty() || nama.isEmpty() || jafung==0 || pangkat==0 || nohp.isEmpty()){
        JOptionPane.showMessageDialog(this, "Mohon Lengkapi Data!");
    }else{
        String jafung_isi = cmbJafung.getSelectedItem().toString();
        String pangkat_isi = cmbPangkat.getSelectedItem().toString();
        
        String sql = "INSERT INTO data_dosen (nidn,nama,jabatan_fungsional,"
                + "pangkat_golongan,no_hp)"
                + "VALUES (\""+nidn+"\",\""+nama+"\","
                + "\""+jafung_isi+"\",\""+pangkat_isi+"\",\""+nohp+
                "\")";
        
        Connection con;
        Statement st;
        try {
            con = KoneksiDB.sambungDB();
            st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
            con.close();
            
            resetView();
            JOptionPane.showMessageDialog(this, "Data Telah disimpan!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}

private void ubaData(){
    String nidn = txtNidn.getText();
    String nama = txtNama.getText();
    int jafung = cmbJafung.getSelectedIndex();
    int pangkat = cmbPangkat.getSelectedIndex();
    String nohp = txtNohp.getText();
    
    if(nidn.isEmpty() || nama.isEmpty() || jafung==0 || pangkat==0 || nohp.isEmpty()){
        JOptionPane.showMessageDialog(this, "Mohon Lengkapi Data!");
    }else{
        String jafung_isi = cmbJafung.getSelectedItem().toString();
        String pangkat_isi = cmbPangkat.getSelectedItem().toString();
        
        String sql = "UPDATE data_dosen "
                + "SET nidn=\""+nidn+"\","
                + "nama=\""+nama+"\","
                + "jabatan_fungsional=\""+jafung_isi+"\","
                + "pangkat_golongan=\""+pangkat_isi+"\","
                + "no_hp=\""+nohp+"\" WHERE id=\""+idBaris+"\"";
        Connection con;
        Statement st;
        try {
            con = KoneksiDB.sambungDB();
            st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
            con.close();
            
            resetView();
            JOptionPane.showMessageDialog(this, "Data telah diubah!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}

private void hapusData(int baris){
    Connection con;
    Statement st;
    try {
        con = KoneksiDB.sambungDB();
        st = con.createStatement();
        st.executeUpdate("DELETE FROM data_dosen WHERE id="+baris);
        st.close();
        con.close();
        
        resetView();
        JOptionPane.showMessageDialog(this, "Data Telah dihapus!");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnTambah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        panelKiri = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnTutup = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        txtNidn = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        txtNohp = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbJafung = new javax.swing.JComboBox<>();
        cmbPangkat = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        btnCari = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        areaSplit = new javax.swing.JSplitPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        btnTambah.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnHapus.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        jLabel1.setText("NIDN");

        jLabel2.setText("NAMA");

        jLabel3.setText("JABATAN FUNGSIONAL");

        jLabel4.setText("PANGKAT/GOLONGAN");

        jLabel5.setText(":");

        jLabel6.setText(":");

        jLabel7.setText(":");

        jLabel8.setText(":");

        btnTutup.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnTutup.setText("Tutup");
        btnTutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTutupActionPerformed(evt);
            }
        });

        btnSimpan.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jLabel9.setText("NO HP");

        jLabel10.setText(":");

        cmbJafung.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbPangkat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout panelKiriLayout = new javax.swing.GroupLayout(panelKiri);
        panelKiri.setLayout(panelKiriLayout);
        panelKiriLayout.setHorizontalGroup(
            panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKiriLayout.createSequentialGroup()
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKiriLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelKiriLayout.createSequentialGroup()
                                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelKiriLayout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtNidn))
                                    .addGroup(panelKiriLayout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelKiriLayout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cmbJafung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panelKiriLayout.createSequentialGroup()
                                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelKiriLayout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtNohp, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelKiriLayout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cmbPangkat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(panelKiriLayout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(btnTutup)
                        .addGap(29, 29, 29)
                        .addComponent(btnSimpan)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelKiriLayout.setVerticalGroup(
            panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKiriLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel8))
                    .addComponent(txtNidn, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel7))
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(cmbJafung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(cmbPangkat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(jLabel10))
                    .addComponent(txtNohp, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTutup)
                    .addComponent(btnSimpan))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblData);

        btnCari.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnCari.setText("Cari");

        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelKiri, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(areaSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah)
                        .addComponent(btnHapus))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCari)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addComponent(panelKiri, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(areaSplit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        role = "Tambah";
        btnSimpan.setText("SIMPAN");
        idBaris = 0;
        resetForm();
        showForm(true);
        btnHapus.setEnabled(false);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(role.equals("Tambah")){
            simpanData();
        }else if(role.equals("Ubah")){
            ubaData();
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnTutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutupActionPerformed
        resetForm();
        showForm(false);
        btnHapus.setEnabled(false);
        idBaris = 0;
    }//GEN-LAST:event_btnTutupActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if(idBaris == 0){
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
        }else{
            hapusData(idBaris);
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
        String key = txtCari.getText();
        showData(key);
    }//GEN-LAST:event_txtCariKeyReleased

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        areaSplit.setDividerLocation(0.3);
    }//GEN-LAST:event_formComponentResized

    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        role = "Ubah";
        int row = tblData.getRowCount();
        if(row > 0){
            int sel = tblData.getSelectedRow();
            if(sel != -1){
                pilihData(tblData.getValueAt(sel, 0).toString());
                btnSimpan.setText("UBAH DATA");
            }
        }
    }//GEN-LAST:event_tblDataMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JavaMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JavaMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JavaMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JavaMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JavaMySQL().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane areaSplit;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTutup;
    private javax.swing.JComboBox<String> cmbJafung;
    private javax.swing.JComboBox<String> cmbPangkat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel panelKiri;
    private javax.swing.JTable tblData;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNidn;
    private javax.swing.JTextField txtNohp;
    // End of variables declaration//GEN-END:variables
}
