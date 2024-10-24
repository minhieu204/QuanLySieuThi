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
import java.text.SimpleDateFormat;
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
public class Panelthongke extends javax.swing.JPanel {

    /**
     * Creates new form frmthongke
     */
    public Panelthongke() {
        initComponents();
        loadtable();
        tongbanhang.setEnabled(false);
        tongchiphi.setEnabled(false);
        tongluong.setEnabled(false);
        tongnhaphang.setEnabled(false);
        tongquangcao.setEnabled(false);

        xoa1.setEnabled(false);
    }
    
    
    private void loadtable(){
        try {
        Connection con = ConDB.ketnoiDB();
        Statement st = con.createStatement();
        
        String sql = "SELECT * FROM thongke";
        ResultSet rs = st.executeQuery(sql);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã số","Ngày bắt đầu", "Ngày kết Thúc", "Lương nhân viên", "Tiền quảng cáo", "Chi phí ", "Tiền nhập hàng ", "Tiền bán hàng"}, 0);

        while (rs.next()) {
            Vector<Object> v = new Vector<>();
            v.add(rs.getInt("id"));
            v.add(rs.getDate("NgayBatDau").toString());
            v.add(rs.getDate("NgayKetThuc").toString());
            v.add(rs.getFloat("LuongNhanVien"));
            v.add(rs.getInt("PhiQuangCao"));
            v.add(rs.getInt("ChiPhi"));
            v.add(rs.getInt("TienNhapHang")); 
            v.add(rs.getInt("TienBanHang"));
            model.addRow(v);
        }

        table.setModel(model);
        con.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu!");
    }
    }
    
    private void timKiem() {
        Date ngayBatDau = ngaybd.getDate();
        Date ngayKetThuc = ngaykt.getDate();

        if (ngayBatDau == null || ngayKetThuc == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bắt đầu và ngày kết thúc.");
            return;
        }

        try {
            Connection conn = ConDB.ketnoiDB();

            //tổng lương nhân viên
            String truyVanLuong = "SELECT SUM(tongluong) FROM luong WHERE ngaynhan BETWEEN ? AND ?";
            PreparedStatement stmtLuong = conn.prepareStatement(truyVanLuong);
            stmtLuong.setDate(1, new java.sql.Date(ngayBatDau.getTime()));
            stmtLuong.setDate(2, new java.sql.Date(ngayKetThuc.getTime()));
            ResultSet rsLuong = stmtLuong.executeQuery();
            if (rsLuong.next()) {
                tongluong.setText(rsLuong.getString(1) != null ? rsLuong.getString(1) : "0");
            }

            //tổng phí quảng cáo
            String truyVanQuangCao = "SELECT SUM(Chiphi) FROM DoiTac WHERE Ngaybatdau BETWEEN ? AND ?";
            PreparedStatement stmtQuangCao = conn.prepareStatement(truyVanQuangCao);
            stmtQuangCao.setDate(1, new java.sql.Date(ngayBatDau.getTime()));
            stmtQuangCao.setDate(2, new java.sql.Date(ngayKetThuc.getTime()));
            ResultSet rsQuangCao = stmtQuangCao.executeQuery();
            if (rsQuangCao.next()) {
                tongquangcao.setText(rsQuangCao.getString(1) != null ? rsQuangCao.getString(1) : "0");
            }
            
            //tổng tiền nhập hàng
            String truyVanNhapHang = "SELECT SUM(gianhap * soluong) FROM sanpham WHERE ngaynhap BETWEEN ? AND ?";
            PreparedStatement stmtNhaphang = conn.prepareStatement(truyVanNhapHang);
            stmtNhaphang.setDate(1, new java.sql.Date(ngayBatDau.getTime()));
            stmtNhaphang.setDate(2, new java.sql.Date(ngayKetThuc.getTime()));
            ResultSet rsNhaphang = stmtNhaphang.executeQuery();
            if (rsNhaphang.next()) {
                tongnhaphang.setText(rsNhaphang.getString(1) != null ? rsNhaphang.getString(1) : "0");
            }
            
            //tổng tiền bán hàng
            String truyVanBanhang = "SELECT SUM(tongtien) FROM donhang WHERE ngayban BETWEEN ? AND ?";
            PreparedStatement stmtBanhang = conn.prepareStatement(truyVanBanhang);
            stmtBanhang.setDate(1, new java.sql.Date(ngayBatDau.getTime()));
            stmtBanhang.setDate(2, new java.sql.Date(ngayKetThuc.getTime()));
            ResultSet rsBanhang = stmtBanhang.executeQuery();
            if (rsBanhang.next()) {
                tongbanhang.setText(rsNhaphang.getString(1) != null ? rsBanhang.getString(1) : "0");
            }
            
            
            //tổng chi phí
            String truyVanCHiPhi = "SELECT SUM(matbang + dien + nuoc + suachua) FROM ChiPhi WHERE ngay BETWEEN ? AND ?";
            PreparedStatement stmtchiphi = conn.prepareStatement(truyVanCHiPhi);
            stmtchiphi.setDate(1, new java.sql.Date(ngayBatDau.getTime()));
            stmtchiphi.setDate(2, new java.sql.Date(ngayKetThuc.getTime()));
            ResultSet rschiphi = stmtchiphi.executeQuery();
            if (rschiphi.next()) {
                tongchiphi.setText(rsNhaphang.getString(1) != null ? rschiphi.getString(1) : "0");
            }

       
            rsLuong.close();
            stmtLuong.close();
            rsQuangCao.close();
            stmtQuangCao.close();
            rsNhaphang.close();
            stmtNhaphang.close();
            rsBanhang.close();
            stmtBanhang.close();
            rschiphi.close();
            stmtchiphi.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi kết nối cơ sở dữ liệu.");
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
        them = new javax.swing.JButton();
        sua = new javax.swing.JButton();
        xoa = new javax.swing.JButton();
        in = new javax.swing.JButton();
        thoat = new javax.swing.JButton();
        nhaplai = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ngaybd = new com.toedter.calendar.JDateChooser();
        ngaykt = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tongluong = new javax.swing.JTextField();
        tongquangcao = new javax.swing.JTextField();
        tongchiphi = new javax.swing.JTextField();
        tongnhaphang = new javax.swing.JTextField();
        tongbanhang = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        them1 = new javax.swing.JButton();
        xoa1 = new javax.swing.JButton();
        in1 = new javax.swing.JButton();
        thoat1 = new javax.swing.JButton();
        nhaplai1 = new javax.swing.JButton();
        bttimkiem = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

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

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setText("Nhập file");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(them, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                    .addComponent(sua, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                    .addComponent(xoa, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                    .addComponent(in, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                    .addComponent(thoat, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                    .addComponent(nhaplai, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
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
                .addGap(23, 23, 23)
                .addComponent(thoat)
                .addGap(23, 23, 23)
                .addComponent(jButton1)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        setMaximumSize(new java.awt.Dimension(1056, 607));
        setPreferredSize(new java.awt.Dimension(1024, 729));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Chọn ngày :");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("-");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Lương nhân viên :");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Phí quảng cáo :");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Chi phí :");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Tiền nhập hàng :");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Tiền bán hàng :");

        tongluong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        tongquangcao.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tongquangcao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tongquangcaoActionPerformed(evt);
            }
        });

        tongchiphi.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        tongnhaphang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        tongbanhang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Ngày bắt đầu");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Ngày kết thúc");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(tongquangcao)
                    .addComponent(tongluong)
                    .addComponent(tongchiphi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tongbanhang)
                    .addComponent(tongnhaphang, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ngaybd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel8)))
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(36, 36, 36))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(45, 45, 45)
                        .addComponent(ngaykt, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(51, 51, 51))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ngaybd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ngaykt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tongluong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(tongnhaphang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tongquangcao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(tongbanhang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(tongchiphi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(92, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tác vụ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 18))); // NOI18N

        them1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        them1.setText("Thêm");
        them1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                them1ActionPerformed(evt);
            }
        });

        xoa1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        xoa1.setText("Xóa");
        xoa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xoa1ActionPerformed(evt);
            }
        });

        in1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        in1.setText("In");
        in1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                in1ActionPerformed(evt);
            }
        });

        thoat1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        thoat1.setText("Thoát");
        thoat1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thoat1ActionPerformed(evt);
            }
        });

        nhaplai1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        nhaplai1.setText("Nhập lại");
        nhaplai1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nhaplai1ActionPerformed(evt);
            }
        });

        bttimkiem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        bttimkiem.setText("Tìm Kiếm");
        bttimkiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttimkiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(them1, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                        .addComponent(xoa1, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                        .addComponent(in1, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                        .addComponent(thoat1, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                        .addComponent(nhaplai1, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE))
                    .addComponent(bttimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(bttimkiem)
                .addGap(27, 27, 27)
                .addComponent(them1)
                .addGap(30, 30, 30)
                .addComponent(xoa1)
                .addGap(28, 28, 28)
                .addComponent(in1)
                .addGap(29, 29, 29)
                .addComponent(nhaplai1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(thoat1)
                .addGap(23, 23, 23))
        );

        table.setModel(new javax.swing.table.DefaultTableModel(
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
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(200, 200, 200))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 953, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tongquangcaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tongquangcaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tongquangcaoActionPerformed

    private void themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themActionPerformed

    }//GEN-LAST:event_themActionPerformed

    private void suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suaActionPerformed
        
    }//GEN-LAST:event_suaActionPerformed

    private void xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xoaActionPerformed
        
    }//GEN-LAST:event_xoaActionPerformed

    private void inActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_inActionPerformed

    private void thoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thoatActionPerformed
        // TODO add your handling code here:
        new Dashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_thoatActionPerformed

    private void nhaplaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhaplaiActionPerformed
        
    }//GEN-LAST:event_nhaplaiActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void them1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_them1ActionPerformed
    Date ngayBatDau = ngaybd.getDate();
    Date ngayKetThuc = ngaykt.getDate();

    if (ngayBatDau == null || ngayKetThuc == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bắt đầu và ngày kết thúc.");
        return;
    }

    try {
        Connection conn = ConDB.ketnoiDB();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO thongke (NgayBatDau ,NgayKetThuc, LuongNhanVien, PhiQuangCao,  ChiPhi, TienNhapHang, TienBanHang) VALUES (?, ?, ?, ?, ?,?,?)");
        stmt.setDate(1, new java.sql.Date(ngayBatDau.getTime()));
        stmt.setDate(2, new java.sql.Date(ngayKetThuc.getTime()));
        stmt.setFloat(3, Float.parseFloat(tongluong.getText()));
        stmt.setInt(4, Integer.parseInt(tongquangcao.getText()));
        stmt.setInt(5, Integer.parseInt(tongchiphi.getText()));
        stmt.setInt(6, Integer.parseInt(tongnhaphang.getText()));
        stmt.setInt(7, Integer.parseInt(tongbanhang.getText()));
        stmt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Thêm thành công!");
        loadtable();
        stmt.close();
        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    }//GEN-LAST:event_them1ActionPerformed

    private void xoa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xoa1ActionPerformed
        int selectedRow = table.getSelectedRow();
    if (selectedRow >= 0) {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String maHoaDon = table.getValueAt(selectedRow, 0).toString(); 
                Connection con = ConDB.ketnoiDB();
                PreparedStatement ps = con.prepareStatement("DELETE FROM thongke WHERE id=?");
                ps.setString(1, maHoaDon);
                
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadtable(); 
                con.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_xoa1ActionPerformed

    private void in1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_in1ActionPerformed
        // TODO add your handling code here:
        try {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("thongke");

        XSSFRow row = null;
        Cell cell = null;
        row = spreadsheet.createRow(0);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("BẢNG THỐNG KÊ");
        row = spreadsheet.createRow(1);
        row.setHeight((short) 500);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("STT");
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Ngày Bắt Đầu");
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Ngày Kết Thúc");
        cell = row.createCell(3, CellType.NUMERIC);
        cell.setCellValue("Lương Nhân Viên");
        cell = row.createCell(4, CellType.NUMERIC);
        cell.setCellValue("Phí Quảng Cáo");
        cell = row.createCell(5, CellType.NUMERIC);
        cell.setCellValue("Chi Phí");
        cell = row.createCell(6, CellType.NUMERIC);
        cell.setCellValue("Tiền Nhập Hàng");
        cell = row.createCell(7, CellType.NUMERIC);
        cell.setCellValue("Tiền Bán Hàng");
        Connection con = ConDB.ketnoiDB();
        String sql = "SELECT * FROM thongke";
        PreparedStatement st = con.prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        int i = 0;
        while (rs.next()) {
            row = spreadsheet.createRow(2 + i);
            row.setHeight((short) 400);
            row.createCell(0).setCellValue(i + 1);
            java.sql.Date ngayBatDau = new java.sql.Date(rs.getDate("NgayBatDau").getTime());
            java.sql.Date ngayKetThuc = new java.sql.Date(rs.getDate("NgayKetThuc").getTime());

            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

            cell = row.createCell(1);
            cell.setCellValue(ngayBatDau);
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            cell.setCellValue(ngayKetThuc);
            cell.setCellStyle(cellStyle);
            row.createCell(3).setCellValue(rs.getInt("LuongNhanVien"));
            row.createCell(4).setCellValue(rs.getInt("PhiQuangCao"));
            row.createCell(5).setCellValue(rs.getInt("ChiPhi"));
            row.createCell(6).setCellValue(rs.getInt("TienNhapHang"));
            row.createCell(7).setCellValue(rs.getInt("TienBanHang"));

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
    }//GEN-LAST:event_in1ActionPerformed

    private void nhaplai1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhaplai1ActionPerformed
       tongbanhang.setText("");
       tongchiphi.setText("");
       tongluong.setText("");
       tongnhaphang.setText("");
       tongquangcao.setText("");
       ngaybd.setDate(null);
       ngaykt.setDate(null);
    xoa1.setEnabled(false);
    them1.setEnabled(true);
    in1.setEnabled(true);
    }//GEN-LAST:event_nhaplai1ActionPerformed

    private void bttimkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttimkiemActionPerformed
        // TODO add your handling code here:
        timKiem();
    }//GEN-LAST:event_bttimkiemActionPerformed

    private void thoat1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thoat1ActionPerformed
        // TODO add your handling code here:
        new Dashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_thoat1ActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        // TODO add your handling code here:
        int i = table.getSelectedRow(); 
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    tongbanhang.setText(model.getValueAt(i, 7).toString()); 
    tongluong.setText(model.getValueAt(i, 3).toString()); 
    tongquangcao.setText(model.getValueAt(i, 4).toString()); 
    tongchiphi.setText(model.getValueAt(i, 5).toString());
    tongnhaphang.setText(model.getValueAt(i, 6).toString());
    try {
        java.util.Date dateee = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(i, 1).toString());
        ngaybd.setDate(dateee); 
        java.util.Date datee = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(i, 2).toString());
        ngaykt.setDate(datee);
    } catch (Exception e) {
        e.printStackTrace();
    }
    xoa1.setEnabled(true);
    them1.setEnabled(false);
    in1.setEnabled(false);
    }//GEN-LAST:event_tableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bttimkiem;
    private javax.swing.JButton in;
    private javax.swing.JButton in1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private com.toedter.calendar.JDateChooser ngaybd;
    private com.toedter.calendar.JDateChooser ngaykt;
    private javax.swing.JButton nhaplai;
    private javax.swing.JButton nhaplai1;
    private javax.swing.JButton sua;
    private javax.swing.JTable table;
    private javax.swing.JButton them;
    private javax.swing.JButton them1;
    private javax.swing.JButton thoat;
    private javax.swing.JButton thoat1;
    private javax.swing.JTextField tongbanhang;
    private javax.swing.JTextField tongchiphi;
    private javax.swing.JTextField tongluong;
    private javax.swing.JTextField tongnhaphang;
    private javax.swing.JTextField tongquangcao;
    private javax.swing.JButton xoa;
    private javax.swing.JButton xoa1;
    // End of variables declaration//GEN-END:variables

    private void dispose() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
