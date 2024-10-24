/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package frmFrame;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ADMIN
 */
public class PanelQLChiPhi extends javax.swing.JPanel {

    /**
     * Creates new form PanelQLChiPhi
     */
    public PanelQLChiPhi() {
        initComponents();
        loadTable();
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    private void loadTable() {
    try {
        Connection con = ConDB.ketnoiDB();
        Statement st = con.createStatement();
        
        String sql = "SELECT * FROM ChiPhi";
        ResultSet rs = st.executeQuery(sql);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã Hóa Đơn", "Ngày", "Tiền Mặt Bằng", "Tiền Điện", "Tiền Nước", "Tiền Sửa Chữa "}, 0);

        while (rs.next()) {
            Vector<Object> v = new Vector<>();
            v.add(rs.getString("id"));
            v.add(rs.getDate("ngay").toString());
            v.add(rs.getInt("matbang"));
            v.add(rs.getInt("dien"));
            v.add(rs.getInt("nuoc"));
            v.add(rs.getInt("suachua")); 
            model.addRow(v);
        }

        tablet.setModel(model);
        con.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu!");
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tiendien = new javax.swing.JTextField();
        tiennuoc = new javax.swing.JTextField();
        tienmatbang = new javax.swing.JTextField();
        phisuachua = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        datee = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablet = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        them = new javax.swing.JButton();
        sua = new javax.swing.JButton();
        xoa = new javax.swing.JButton();
        in = new javax.swing.JButton();
        thoat = new javax.swing.JButton();
        nhaplai = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông Tin Chi Phí", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("tiền điên :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("tiền nước: ");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Tiền mặt bằng :");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Phí sửa chữa :");

        tiendien.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        tiennuoc.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        tienmatbang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        phisuachua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Chọn Ngày : ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tiennuoc)
                            .addComponent(tiendien, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tienmatbang)
                            .addComponent(phisuachua, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(datee, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(datee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(tienmatbang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(tiendien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(70, 70, 70)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(phisuachua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(tiennuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(90, 90, 90))
        );

        tablet.setModel(new javax.swing.table.DefaultTableModel(
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
        tablet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabletMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablet);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tác vụ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 18))); // NOI18N

        them.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        them.setText("Thêm");
        them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themActionPerformed(evt);
            }
        });

        sua.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        sua.setText("Sửa");
        sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suaActionPerformed(evt);
            }
        });

        xoa.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        xoa.setText("Xóa");
        xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xoaActionPerformed(evt);
            }
        });

        in.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        in.setText("In");
        in.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inActionPerformed(evt);
            }
        });

        thoat.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        thoat.setText("Thoát");
        thoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thoatActionPerformed(evt);
            }
        });

        nhaplai.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        nhaplai.setText("Nhập lại");
        nhaplai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nhaplaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(thoat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nhaplai, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(in, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xoa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(them, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(them)
                .addGap(18, 18, 18)
                .addComponent(sua)
                .addGap(18, 18, 18)
                .addComponent(xoa)
                .addGap(18, 18, 18)
                .addComponent(in)
                .addGap(18, 18, 18)
                .addComponent(nhaplai)
                .addGap(28, 28, 28)
                .addComponent(thoat)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themActionPerformed
       try {
        Connection con = ConDB.ketnoiDB();
        PreparedStatement ps = con.prepareStatement("INSERT INTO ChiPhi (ngay ,matbang, dien, nuoc,  suachua) VALUES (?, ?, ?, ?, ?)");
        
        java.sql.Date ngay = new java.sql.Date(datee.getDate().getTime());
        ps.setDate(1, ngay);
        ps.setInt(2, Integer.parseInt(tienmatbang.getText()));
        ps.setInt(3, Integer.parseInt(tiendien.getText()));
        ps.setInt(4, Integer.parseInt(tiennuoc.getText()));
        ps.setInt(5, Integer.parseInt(phisuachua.getText()));
        ps.executeUpdate();
        JOptionPane.showMessageDialog(this, "Thêm thành công!");
        loadTable();
       
        con.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
       
    }//GEN-LAST:event_themActionPerformed

    private void suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suaActionPerformed
        int selectedRow = tablet.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                String maHoaDon = tablet.getValueAt(selectedRow, 0).toString(); 
                Connection con = ConDB.ketnoiDB();
                PreparedStatement ps = con.prepareStatement("UPDATE ChiPhi SET ngay=? ,matbang=?, dien=?, nuoc=?, suachua=? WHERE id=?");
                java.sql.Date ngay = new java.sql.Date(datee.getDate().getTime());
                ps.setDate(1, ngay);
                ps.setInt(2, Integer.parseInt(tienmatbang.getText()));
                ps.setInt(3, Integer.parseInt(tiendien.getText()));
                ps.setInt(4, Integer.parseInt(tiennuoc.getText()));
                ps.setInt(5, Integer.parseInt(phisuachua.getText()));
                ps.setString(6, maHoaDon);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Sửa thành công!");
                loadTable(); 
                con.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi sửa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_suaActionPerformed

    private void xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xoaActionPerformed
       int selectedRow = tablet.getSelectedRow();
    if (selectedRow >= 0) {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String maHoaDon = tablet.getValueAt(selectedRow, 0).toString(); 
                Connection con = ConDB.ketnoiDB();
                PreparedStatement ps = con.prepareStatement("DELETE FROM ChiPhi WHERE id=?");
                ps.setString(1, maHoaDon);
                
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadTable(); 
                con.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_xoaActionPerformed

    private void inActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inActionPerformed
        // TODO add your handling code here:
        try {
            // Tạo Workbook và Sheet trong Excel
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet spreadsheet = workbook.createSheet("KhuyenMai");

            XSSFRow row = null;
            Cell cell = null;

            row = spreadsheet.createRow(0);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("DANH SÁCH KHUYẾN MÃI");

            row = spreadsheet.createRow(1);
            row.setHeight((short) 500);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("STT");
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue("Mã Khuyến Mãi");
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue("Tên Khuyến Mãi");
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue("Mã Sản Phẩm");
            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue("Ngày Bắt Đầu");
            cell = row.createCell(5, CellType.STRING);
            cell.setCellValue("Ngày Kết Thúc");
            cell = row.createCell(6, CellType.STRING);
            cell.setCellValue("Giảm Giá");
            cell = row.createCell(7, CellType.STRING);
            cell.setCellValue("Giá Gốc");
            cell = row.createCell(8, CellType.STRING);
            cell.setCellValue("Giá Sau Khuyến Mãi");

            Connection con = ConDB.ketnoiDB();
            String sql = "SELECT * FROM KhuyenMai JOIN sanpham ON KhuyenMai.MaSanPham = sanpham.MaSP";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            int i = 0;
            while (rs.next()) {
                row = spreadsheet.createRow(2 + i);
                row.setHeight((short) 400);

                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(rs.getString("MaKhuyenMai"));
                row.createCell(2).setCellValue(rs.getString("TenKhuyenMai"));
                row.createCell(3).setCellValue(rs.getString("MaSanPham"));

                Date ngayBatDau = new Date(rs.getDate("NgayBatDau").getTime());
                Date ngayKetThuc = new Date(rs.getDate("NgayKetThuc").getTime());

                CreationHelper createHelper = workbook.getCreationHelper();
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

                cell = row.createCell(4);
                cell.setCellValue(ngayBatDau);
                cell.setCellStyle(cellStyle);

                cell = row.createCell(5);
                cell.setCellValue(ngayKetThuc);
                cell.setCellStyle(cellStyle);

                row.createCell(6).setCellValue(rs.getInt("GiamGia"));
                row.createCell(7).setCellValue(rs.getInt("GiaBan"));

                java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
                if (!currentDate.before(ngayBatDau) && !currentDate.after(ngayKetThuc)) {
                    int giaBan = rs.getInt("GiaBan");
                    int giamGia = rs.getInt("GiamGia");
                    int giaSauKhuyenMai = giaBan * (1 - giamGia / 100);
                    row.createCell(8).setCellValue(giaSauKhuyenMai);
                } else {
                    row.createCell(8).setCellValue(rs.getInt("GiaBan"));
                }

                i++;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Lưu file Excel");

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }
                FileOutputStream fileOut = new FileOutputStream(filePath);
                workbook.write(fileOut);
                fileOut.close();
                JOptionPane.showMessageDialog(this, "Xuất dữ liệu ra Excel thành công!");
            }

            rs.close();
            st.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất dữ liệu ra Excel.");
        }
    }//GEN-LAST:event_inActionPerformed

    private void thoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thoatActionPerformed
        // TODO add your handling code here:
        new Dashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_thoatActionPerformed

    private void nhaplaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhaplaiActionPerformed
       
        tiendien.setText("");
        tiennuoc.setText("");
        tienmatbang.setText("");
        datee.setDate(null);
        phisuachua.setText("");
       them.setEnabled(true);
       in.setEnabled(true);
    }//GEN-LAST:event_nhaplaiActionPerformed

    private void tabletMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabletMouseClicked
        // TODO add your handling code here:
    int i = tablet.getSelectedRow(); 
    DefaultTableModel model = (DefaultTableModel) tablet.getModel();
    tiendien.setText(model.getValueAt(i, 3).toString()); 
    tienmatbang.setText(model.getValueAt(i, 2).toString()); 
    tiennuoc.setText(model.getValueAt(i, 4).toString()); 
    phisuachua.setText(model.getValueAt(i, 5).toString());
    try {
        java.util.Date dateee = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(i, 1).toString());
        datee.setDate(dateee); 
    } catch (Exception e) {
        e.printStackTrace();
    }
    them.setEnabled(false);
       in.setEnabled(false);
    }//GEN-LAST:event_tabletMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser datee;
    private javax.swing.JButton in;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton nhaplai;
    private javax.swing.JTextField phisuachua;
    private javax.swing.JButton sua;
    private javax.swing.JTable tablet;
    private javax.swing.JButton them;
    private javax.swing.JButton thoat;
    private javax.swing.JTextField tiendien;
    private javax.swing.JTextField tienmatbang;
    private javax.swing.JTextField tiennuoc;
    private javax.swing.JButton xoa;
    // End of variables declaration//GEN-END:variables

    private void dispose() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
