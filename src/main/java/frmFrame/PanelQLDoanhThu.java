/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package frmFrame;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
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
public class PanelQLDoanhThu extends javax.swing.JPanel {

    /**
     * Creates new form PanelQLDoanhThu
     */
    public PanelQLDoanhThu() {
        initComponents();
        loadSanPham();
        loadThuChhi();
        ngayban.addPropertyChangeListener("date", evt -> {tinhDoanhThuNgay();});
        tienban.setEnabled(false);
        tiennhap.setEnabled(false);
        tongtien.setEnabled(false);
        tienthang.setEnabled(false);
        tiennam.setEnabled(false);
        
    }
    private void loadSanPham() {
        try {
            Connection conn = ConDB.ketnoiDB();
            Statement st = conn.createStatement();
            String sql = "SELECT * FROM sanpham";
            ResultSet rs = st.executeQuery(sql);

        
            tablemh.removeAll();

          
            String[] tieuDeBang = {"Mã sản phẩm", "Tên sản phẩm", "Giá nhập", "Giá bán", "Số lượng", "Ngày nhập"};
            DefaultTableModel model = new DefaultTableModel(tieuDeBang, 0);

          
            while (rs.next()) {
                Vector<Object> v = new Vector<>();
                v.add(rs.getString("masp"));
                v.add(rs.getString("tensp"));
                v.add(rs.getInt("gianhap"));
                v.add(rs.getInt("giaban"));
                v.add(rs.getInt("soluong"));
                v.add(rs.getDate("ngaynhap"));
                model.addRow(v);
            }

            tablemh.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void loadThuChhi() {
    try {
        Connection conn = ConDB.ketnoiDB();
        Statement st = conn.createStatement();
        String sql = "SELECT * FROM ThuChiNhapBan";
        ResultSet rs = st.executeQuery(sql);

        tablethuchi.removeAll();

        String[] tieuDeBang = {"Mã phiếu", "Ngày bán", "Tiền bán", "Tiền nhập ", "Tổng"};
        DefaultTableModel model = new DefaultTableModel(tieuDeBang, 0);

        while (rs.next()) {
            Vector<Object> v = new Vector<>();
            v.add(rs.getString("id")); 
            v.add(rs.getDate("ngayban")); 
            v.add(rs.getInt("tienban")); 
            v.add(rs.getInt("tiennhap")); 
            v.add(rs.getInt("tongtien"));
            model.addRow(v);
        }

        tablethuchi.setModel(model);
        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}

    
   private void tinhDoanhThuNgay() {
    if (ngayban.getDate() != null) {
        try {
            Connection conn = ConDB.ketnoiDB();
            PreparedStatement ps = conn.prepareStatement("SELECT SUM(giaban * soluong) AS tongban, SUM(gianhap * soluong) AS tongnhap FROM sanpham WHERE CONVERT(date, ngaynhap) = ?");
            java.sql.Date ngay = new java.sql.Date(ngayban.getDate().getTime());
            ps.setDate(1, ngay);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tienban.setText(String.valueOf(rs.getInt("tongban")));
                tiennhap.setText(String.valueOf(rs.getInt("tongnhap")));
                tongtien.setText(String.valueOf(rs.getInt("tongban") - rs.getInt("tongnhap")));
            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
   
   
private void updateTongTienThang(java.sql.Date ngayban) {
    try {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(ngayban);
        int month = cal.get(java.util.Calendar.MONTH) + 1; // Tháng (1-12)
        int year = cal.get(java.util.Calendar.YEAR); // Năm

        String sqlTongTienThang = "SELECT SUM(tongtien) AS tongtien FROM ThuChiNhapBan WHERE MONTH(ngayban) = ? AND YEAR(ngayban) = ?";
        Connection conn = ConDB.ketnoiDB();
        PreparedStatement ps = conn.prepareStatement(sqlTongTienThang);
        ps.setInt(1, month);
        ps.setInt(2, year);
        
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int tongtienThang = rs.getInt("tongtien");
            // Nếu không có bản ghi nào, mặc định là 0
            tongtienThang = (rs.wasNull()) ? 0 : tongtienThang; 
            tienthang.setText(String.valueOf(tongtienThang)); // Cập nhật ô hiển thị tổng tiền tháng
        }

        rs.close();
        ps.close();
        conn.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi tính tổng tiền tháng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}

private void tinhDoanhThuNam() {
    if (ngayban.getDate() != null) {
        try {
            Connection conn = ConDB.ketnoiDB();
            
            // Lấy năm từ ngày bán
            java.util.Calendar cal = ngayban.getCalendar();
            int year = cal.get(java.util.Calendar.YEAR); // Lấy năm
            
            // Tính tổng doanh thu bán trong năm theo tháng
            String sqlTongDoanhThuNam = "SELECT MONTH(ngayban) AS thang, SUM(tongtien) AS tongtien " +
                                          "FROM ThuChiNhapBan " +
                                          "WHERE YEAR(ngayban) = ? " +
                                          "GROUP BY MONTH(ngayban)";
            PreparedStatement ps = conn.prepareStatement(sqlTongDoanhThuNam);
            ps.setInt(1, year);
            
            ResultSet rs = ps.executeQuery();
            int tongDoanhThuNam = 0;

            while (rs.next()) {
                int tongtienThang = rs.getInt("tongtien");
                tongDoanhThuNam += tongtienThang; // Cộng dồn doanh thu theo tháng
            }

            // Cập nhật ô hiển thị tổng doanh thu năm
            tiennam.setText(String.valueOf(tongDoanhThuNam));

            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tính tổng doanh thu năm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}


private void updateTongTienNam(int year) {
    try {
        // Tính tổng tiền bán cho năm này
        String sqlTongTienNam = "SELECT SUM(tongtien) AS tongtien FROM ThuChiNhapBan WHERE YEAR(ngayban) = ?";
        Connection conn = ConDB.ketnoiDB();
        PreparedStatement ps = conn.prepareStatement(sqlTongTienNam);
        ps.setInt(1, year);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int tongtienNam = rs.getInt("tongtien");
            tiennam.setText(String.valueOf(tongtienNam)); // Cập nhật ô hiển thị tổng tiền năm
        } else {
            tiennam.setText("0"); // Nếu không có bản ghi nào, hiển thị 0
        }

        rs.close();
        ps.close();
        conn.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi tính tổng tiền năm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}

private void addRecord(java.sql.Date ngayban, int tienban, int tiennhap) {
    try {
        // Thêm bản ghi vào bảng
        String sqlInsert = "INSERT INTO ThuChiNhapBan (ngayban, tienban, tiennhap, tongtien) VALUES (?, ?, ?, ?)";
        Connection conn = ConDB.ketnoiDB();
        PreparedStatement ps = conn.prepareStatement(sqlInsert);
        
        // Tính tổng tiền
        int tongtien = tienban - tiennhap; // Điều chỉnh theo cách bạn tính tổng tiền
        ps.setDate(1, ngayban);
        ps.setInt(2, tienban);
        ps.setInt(3, tiennhap);
        ps.setInt(4, tongtien);
        
        ps.executeUpdate();
        ps.close();
        
        // Cập nhật tổng tiền tháng và tổng tiền năm
        updateTongTienThang(ngayban); // Gọi hàm tính tổng tiền tháng
        tinhDoanhThuNam(); // Gọi hàm tính tổng doanh thu năm

        JOptionPane.showMessageDialog(this, "Thêm bản ghi thành công!");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi thêm bản ghi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablemh = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablethuchi = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tiennhap = new javax.swing.JTextField();
        tienban = new javax.swing.JTextField();
        tongtien = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        btadd = new javax.swing.JButton();
        btedit = new javax.swing.JButton();
        btdelete = new javax.swing.JButton();
        btexit = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        in = new javax.swing.JButton();
        ngayban = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tienthang = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tiennam = new javax.swing.JTextField();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách mặt hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

        tablemh.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tablemh);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thu Chi nhập bán", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

        tablethuchi.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablethuchi);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tác vụ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Tiền nhập hàng :");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Tiền bán hàng :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Tổng tiền :");

        tiennhap.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        tienban.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tienban.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tienbanActionPerformed(evt);
            }
        });

        tongtien.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        btadd.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btadd.setText("Thêm");
        btadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btaddActionPerformed(evt);
            }
        });

        btedit.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btedit.setText("Sửa");
        btedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bteditActionPerformed(evt);
            }
        });

        btdelete.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btdelete.setText("Xóa");
        btdelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btdeleteActionPerformed(evt);
            }
        });

        btexit.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btexit.setText("Thoát");
        btexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btexitActionPerformed(evt);
            }
        });

        in.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        in.setText("In bảng Thu Chi bán hàng");
        in.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inActionPerformed(evt);
            }
        });

        ngayban.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ngaybanFocusLost(evt);
            }
        });
        ngayban.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ngaybanMouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Ngày bán hàng :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jSeparator2)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tiennhap)
                            .addComponent(tienban)
                            .addComponent(tongtien)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btadd, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                            .addComponent(btdelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btedit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btexit, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)))
                    .addComponent(in, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ngayban, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ngayban, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tienban, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tiennhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tongtien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(in, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btadd)
                    .addComponent(btedit))
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btdelete)
                    .addComponent(btexit))
                .addGap(45, 45, 45)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Tổng Tiền Tháng : ");

        tienthang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Tổng Tiền Năm : ");

        tiennam.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tienthang, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tiennam, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(tienthang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(tiennam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(165, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ngaybanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ngaybanMouseClicked
        // TODO add your handling code here
        tinhDoanhThuNgay();
       
    }//GEN-LAST:event_ngaybanMouseClicked

    private void ngaybanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ngaybanFocusLost
        // TODO add your handling code here:
        tinhDoanhThuNgay();
    }//GEN-LAST:event_ngaybanFocusLost

    private void btaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btaddActionPerformed
        // TODO add your handling code here:
        if (ngayban.getDate() != null) {
        try {
            
            Connection conn = ConDB.ketnoiDB();
            
            String sqlCheck = "SELECT COUNT(*) AS count FROM ThuChiNhapBan WHERE ngayban = ?";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            java.sql.Date ngay = new java.sql.Date(ngayban.getDate().getTime());
            psCheck.setDate(1, ngay);
            
            ResultSet rsCheck = psCheck.executeQuery();
            if (rsCheck.next() && rsCheck.getInt("count") > 0) {
                
                JOptionPane.showMessageDialog(this, "Ngày bán hàng này đã tồn tại, không thể thêm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            
            String sqlInsert = "INSERT INTO ThuChiNhapBan (ngayban, tienban, tiennhap ,tongtien) VALUES (?, ?, ?,?)";
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            psInsert.setDate(1, ngay);
            psInsert.setInt(2, Integer.parseInt(tienban.getText()));
            psInsert.setInt(3, Integer.parseInt(tiennhap.getText()));
            psInsert.setInt(4, Integer.parseInt(tongtien.getText()));
            int result = psInsert.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Thêm thông tin thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadThuChhi(); 
                updateTongTienThang(ngay);
                updateTongTienNam(ngay.getYear() + 1900);
            }
            
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bán hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
    }
        btedit.setEnabled(false);
    }//GEN-LAST:event_btaddActionPerformed

    private void btdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btdeleteActionPerformed
        // TODO add your handling code here:
        int selectedRow = tablethuchi.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng cần xóa trong bảng Thu Chi nhập bán.");
        return;
    }

    
    String maPhieu = tablethuchi.getValueAt(selectedRow, 0).toString().trim();

    // Kiểm tra xem mã phiếu có tồn tại không
    if (maPhieu.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Không tìm thấy mã phiếu cần xóa.");
        return;
    }

    // Kết nối tới cơ sở dữ liệu
    Connection conn = ConDB.ketnoiDB();
    try {
        // Xóa phiếu thu chi khỏi bảng ThuChiNhapBan
        String sql = "DELETE FROM ThuChiNhapBan WHERE id = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, maPhieu);
        int rowsAffected = pst.executeUpdate();

        if (rowsAffected > 0) {
            // Hiển thị thông báo xóa thành công
            JOptionPane.showMessageDialog(null, "Xóa phiếu Thu Chi nhập bán thành công!");
            loadThuChhi(); // Cập nhật lại bảng Thu Chi nhập bán sau khi xóa
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy phiếu Thu Chi để xóa.");
        }

        // Đóng kết nối
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi xóa phiếu Thu Chi nhập bán!");
    }
    }//GEN-LAST:event_btdeleteActionPerformed

    private void inActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inActionPerformed
        // TODO add your handling code here:
        try {
    // Tạo Workbook và Sheet trong Excel
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet spreadsheet = workbook.createSheet("ThuChiNhapBan");

    XSSFRow row = null;
    Cell cell = null;

    // Tiêu đề bảng
    row = spreadsheet.createRow(0);
    cell = row.createCell(0, CellType.STRING);
    cell.setCellValue("Quản Lý Doanh Thu");

    // Tiêu đề các cột
    row = spreadsheet.createRow(1);
    row.setHeight((short) 500);
    cell = row.createCell(0, CellType.STRING);
    cell.setCellValue("STT");
    cell = row.createCell(1, CellType.STRING);
    cell.setCellValue("Ngày Bán");
    cell = row.createCell(2, CellType.STRING);
    cell.setCellValue("Tổng Tiền Bán");
    cell = row.createCell(3, CellType.STRING);
    cell.setCellValue("Tổng Tiền Nhập Hàng");
    cell = row.createCell(4, CellType.STRING);
    cell.setCellValue("Doanh Thu Tháng");
    cell = row.createCell(5, CellType.STRING);
    cell.setCellValue("Doanh Thu Năm");

    // Kết nối đến database
    Connection con = ConDB.ketnoiDB();
    String sql = "SELECT * FROM ThuChiNhapBan";
    PreparedStatement st = con.prepareStatement(sql);
    ResultSet rs = st.executeQuery();

    int i = 0;
    while (rs.next()) {
        row = spreadsheet.createRow(2 + i);
        row.setHeight((short) 400);

       
        row.createCell(0).setCellValue(i + 1); 
        
        java.sql.Date ngayBan = rs.getDate("ngayban");
        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

        cell = row.createCell(1);
        cell.setCellValue(ngayBan);
        cell.setCellStyle(cellStyle);

        // Tổng tiền bán
        row.createCell(2).setCellValue(rs.getInt("tienban"));

        // Tổng tiền nhập hàng
        row.createCell(3).setCellValue(rs.getInt("tiennhap"));

        // Doanh thu tháng
        row.createCell(4).setCellValue(rs.getInt("tongdoanhthuthang"));

        // Doanh thu năm
        row.createCell(5).setCellValue(rs.getInt("tongdoanhthunam"));

        i++;
    }

    // Lưu file Excel
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

    private void btexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btexitActionPerformed
        // TODO add your handling code here:
        new Dashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_btexitActionPerformed

    private void tienbanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tienbanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tienbanActionPerformed

    private void bteditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bteditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bteditActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btadd;
    private javax.swing.JButton btdelete;
    private javax.swing.JButton btedit;
    private javax.swing.JButton btexit;
    private javax.swing.JButton in;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private com.toedter.calendar.JDateChooser ngayban;
    private javax.swing.JTable tablemh;
    private javax.swing.JTable tablethuchi;
    private javax.swing.JTextField tienban;
    private javax.swing.JTextField tiennam;
    private javax.swing.JTextField tiennhap;
    private javax.swing.JTextField tienthang;
    private javax.swing.JTextField tongtien;
    // End of variables declaration//GEN-END:variables

    private void dispose() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
