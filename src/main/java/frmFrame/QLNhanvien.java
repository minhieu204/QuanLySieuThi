/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frmFrame;

import com.toedter.calendar.JDateChooser;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Oulyne
 */
public class QLNhanvien extends javax.swing.JFrame {

    /**
     * Creates new form QLNhanvien
     */
    public QLNhanvien() {
        initComponents();
        load_taikhoan();
        jDialog1.setLocationRelativeTo(null);
        jDialog2.setLocationRelativeTo(null);
         String[] tdb={"Mã", "Mã nhân viên", "Tổng TG", "Lương/h", "Tổng lương","Ngày nhận","Thưởng","Được nhận"};
            DefaultTableModel model= new DefaultTableModel(tdb, 0);
            tbll.removeAll();
            tbll.setModel(model);
    }
    String id="";
    String name="";
    Connection con;
     private static CellStyle DinhdangHeader(XSSFSheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.WHITE.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        return cellStyle;
    }
     Map<String, String> ncc1= new HashMap<>();
    private void load_taikhoan(){
        try {
            con=ConDB.ketnoiDB();
            Statement st2= con.createStatement();
            String[] tdb={"Mã tài khoản", "Họ và Tên", "Giới tính", "Điện thoại", "Email"};
            DefaultTableModel model= new DefaultTableModel(tdb, 0);
            tblnhanvien.removeAll();
            String sql2="Select * from nhanvien";
            ResultSet rs2= st2.executeQuery(sql2);
            while(rs2.next()){
                Vector v2= new Vector();
                v2.add(rs2.getString("manhanvien"));
                v2.add(rs2.getString("hoten"));
                v2.add(rs2.getString("gioitinh"));
                v2.add(rs2.getString("sdt"));
                v2.add(rs2.getString("email"));
                model.addRow(v2);
                ncc1.put(rs2.getString("manhanvien"), rs2.getString("hoten"));
            }
            tblnhanvien.setModel(model);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadls(String idnv){
        try {
            con=ConDB.ketnoiDB();
            Statement st2= con.createStatement();
            String[] tdb={"Mã", "Mã nhân viên", "TGBĐ", "TGKT", "Tổng giờ","Ngày","Trạng thái"};
            DefaultTableModel model= new DefaultTableModel(tdb, 0);
            tblls.removeAll();
            String sql2 = "SELECT mals, manhanvien, FORMAT(logintime, 'HH:mm') AS logintime, FORMAT(logouttime, 'HH:mm') AS logouttime, giolamviec, ngay, workstatus FROM lslamviec WHERE manhanvien='" + idnv + "'";
            ResultSet rs2= st2.executeQuery(sql2);
            while(rs2.next()){
                Vector v2= new Vector();
                v2.add(rs2.getString("mals"));
                v2.add(rs2.getString("manhanvien"));
                v2.add(rs2.getString("logintime"));
                v2.add(rs2.getString("logouttime"));
                v2.add(rs2.getString("giolamviec"));
                v2.add(rs2.getString("ngay"));
                v2.add(rs2.getString("workstatus"));
                model.addRow(v2);
            }
            tblls.setModel(model);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadl(String idnv){
        try {
            con=ConDB.ketnoiDB();
            Statement st2= con.createStatement();
            String[] tdb={"Mã", "Mã nhân viên", "Tổng TG", "Lương/h", "Tổng lương","Ngày nhận","Thưởng","Được nhận"};
            DefaultTableModel model= new DefaultTableModel(tdb, 0);
            tbll.removeAll();
            String sql2 = "SELECT * FROM luong WHERE manhanvien='" + idnv + "'";
            ResultSet rs2= st2.executeQuery(sql2);
            while(rs2.next()){
                Vector v2= new Vector();
                v2.add(rs2.getString("maluong"));
                v2.add(rs2.getString("manhanvien"));
                v2.add(rs2.getString("tonggio"));
                v2.add(rs2.getString("luongtheogio"));
                v2.add(rs2.getString("tongluong"));
                v2.add(rs2.getString("ngaynhan"));
                v2.add(rs2.getString("thuong"));
                v2.add(rs2.getString("tienduocnhan"));
                model.addRow(v2);
            }
            tbll.setModel(model);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
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

        jDialog1 = new javax.swing.JDialog();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblls = new javax.swing.JTable();
        jButton7 = new javax.swing.JButton();
        jDialog2 = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        luongth2 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        thuong2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblnhanvien = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbll = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        dc1 = new com.toedter.calendar.JDateChooser();
        dc2 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        btnxn = new javax.swing.JButton();
        txttonggio = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tennhanvien = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        luongh = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnls = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();

        jDialog1.setResizable(false);
        jDialog1.setSize(new java.awt.Dimension(712, 431));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 153, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("LỊCH SỬ PHIÊN LÀM VIỆC");

        tblls.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblls);

        jButton7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton7.setText("Thoát");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton7)))
                .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addGap(67, 67, 67))
        );

        jDialog2.setResizable(false);
        jDialog2.setSize(new java.awt.Dimension(712, 431));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 153, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("SỬA BẢNG LƯƠNG");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Lương theo giờ :");

        luongth2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Thưởng :");

        thuong2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton1.setText("Thoát");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton6.setText("Sửa");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton6)
                    .addGroup(jDialog2Layout.createSequentialGroup()
                        .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jDialog2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(67, 67, 67)))
                        .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(thuong2, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                            .addComponent(luongth2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(18, 18, 18))
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(73, 73, 73)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(luongth2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(113, 113, 113)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(thuong2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(1024, 768));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUẢN LÝ NHÂN VIÊN");

        jPanel1.setLayout(new java.awt.GridLayout(2, 0));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách nhân viên", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 18))); // NOI18N

        tblnhanvien.setModel(new javax.swing.table.DefaultTableModel(
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
        tblnhanvien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblnhanvienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblnhanvien);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel2);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bảng lương", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 18))); // NOI18N

        tbll.setModel(new javax.swing.table.DefaultTableModel(
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
        tbll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbllMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbll);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel8);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tác vụ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 18))); // NOI18N

        dc1.setToolTipText("");
        dc1.setDateFormatString("dd/MM/yyyy");
        dc1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        dc2.setToolTipText("");
        dc2.setDateFormatString("dd/MM/yyyy");
        dc2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Chọn khoảng thời gian");

        btnxn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnxn.setText("Xác nhận");
        btnxn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxnActionPerformed(evt);
            }
        });

        txttonggio.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txttonggio.setText("Tổng thời gian làm việc :");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Tổng tiền được nhận :");

        tennhanvien.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tennhanvien.setText("Tên nhân viên :");

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton2.setText("Thêm");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton3.setText("Sửa");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton4.setText("Xóa");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton5.setText("Thoát");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Lương theo giờ :");

        luongh.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        luongh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                luonghKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Thưởng :");

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Lương tạm tính :");

        btnls.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnls.setText("Kiểm tra lịch sử làm việc");
        btnls.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlsActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton8.setText("In bảng lương");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(dc2, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(dc1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(129, 129, 129))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jButton2)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton3)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton4)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
                                .addComponent(jLabel5)
                                .addComponent(txttonggio)
                                .addComponent(tennhanvien)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnxn)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel8))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(luongh, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                                            .addComponent(jTextField2)))))
                            .addComponent(jLabel3)
                            .addComponent(jLabel6))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnls, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(20, 20, 20)
                .addComponent(dc1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnxn)
                .addGap(10, 10, 10)
                .addComponent(dc2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(tennhanvien)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txttonggio)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(luongh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(33, 33, 33)
                .addComponent(btnls)
                .addGap(18, 18, 18)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 20, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1040, 782));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tblnhanvienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblnhanvienMouseClicked
         int i=tblnhanvien.getSelectedRow();
        DefaultTableModel tb= (DefaultTableModel) tblnhanvien.getModel();
        id=tb.getValueAt(i, 0).toString();
        name=tb.getValueAt(i, 1).toString();
        tennhanvien.setText("Tên nhân viên: "+name);
        loadls(id);
        loadl(id);
        
    }//GEN-LAST:event_tblnhanvienMouseClicked

    private void btnlsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlsActionPerformed
        jDialog1.setVisible(true);
    }//GEN-LAST:event_btnlsActionPerformed
    Date bd;
    Date kt;
    Float tonggio;
    private void btnxnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxnActionPerformed
        java.util.Date test1= dc1.getDate();
        java.util.Date test2= dc2.getDate();
        
                if(id.equals("")){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên!");
                return;
            }
            if(test1 == null || test2==null){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày!");
                return;
            }
            bd = new Date(dc1.getDate().getTime());
            kt = new Date(dc2.getDate().getTime()); 
        try {
            con=ConDB.ketnoiDB();
            Statement st2= con.createStatement();
           
            
            String sql="select sum(giolamviec) as tonggio from lslamviec where manhanvien='"+id+"' and ngay between '"+bd+"' and '"+kt+"'";
            ResultSet rs=st2.executeQuery(sql);
            while(rs.next()){
            tonggio=rs.getFloat("tonggio");
            }
            txttonggio.setText("Tổng thời gian làm việc : "+String.format("%.2f", tonggio));
            System.out.println(tonggio);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_btnxnActionPerformed
    Float luongtt;
    float luongth=0;
    private void luonghKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_luonghKeyReleased
        String luong= luongh.getText().trim();
        if(luong.equals("")){
           luongth=0;
        }else{
            luongth=Float.parseFloat(luongh.getText().trim());
        }
        luongtt=luongth*tonggio;
        jLabel6.setText("Lương tạm tính : "+String.format("%.2f",luongtt));
    }//GEN-LAST:event_luonghKeyReleased
        float tiennhan;
        float thuongg=0;
    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        String thuong=jTextField2.getText().trim();
        if(thuong.equals("")){
           thuongg=0;
        }else{
            thuongg=Float.parseFloat(jTextField2.getText().trim());
        }
        tiennhan=luongtt+thuongg;
        jLabel5.setText("Lương tạm tính : "+String.format("%.2f",tiennhan));
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
        con = ConDB.ketnoiDB();
        // Sử dụng String.format để xây dựng câu truy vấn an toàn hơn.
        String sql = String.format("INSERT INTO luong (manhanvien, tonggio, luongtheogio, thuong) VALUES ('%s', %.2f, %.2f, %.2f)", 
                                    id, tonggio, luongth, thuongg);
        
        Statement st = con.createStatement();
        st.executeUpdate(sql);
        con.close();
        
        JOptionPane.showMessageDialog(this, "Thêm thành công!");
        luongh.setText("");
        jTextField2.setText("");
        dc1.setDate(new java.util.Date());
        dc2.setDate(new java.util.Date());
            loadl(id);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Có lỗi xảy ra: " + e.getMessage());
    }
    }//GEN-LAST:event_jButton2ActionPerformed
    String id2="";
    private void tbllMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbllMouseClicked
          int i=tbll.getSelectedRow();
        DefaultTableModel tb= (DefaultTableModel) tbll.getModel();
        id2=tb.getValueAt(i, 0).toString();
    }//GEN-LAST:event_tbllMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(id2.equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng chọn trong bảng lương!");
            return;
        }
        jDialog2.setVisible(true);
                
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
       jDialog1.setVisible(false);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       jDialog2.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String sluongth="";
        String sthuong="";
        sthuong=thuong2.getText().trim();
        sluongth=luongth2.getText().trim();
        float luongth=0;
        if(sluongth.equals("")){
            luongth=0;
        }else{
            luongth=Float.parseFloat(sluongth);
        }
        float thuong=0;
        if(sthuong.equals("")){
            thuong=0;
        }else{
            thuong=Float.parseFloat(sthuong);
        }
         try {
        con = ConDB.ketnoiDB();
        // Sử dụng String.format để xây dựng câu truy vấn an toàn hơn.
        String sql = String.format("update luong set luongtheogio=%.2f , thuong=%.2f where maluong='%s' ", 
                                     luongth, thuong, id2);
        Statement st = con.createStatement();
        st.executeUpdate(sql);
        con.close();
        
        JOptionPane.showMessageDialog(jDialog2, "sửa thành công!");
            loadl(id);
            thuong2.setText("");
            luongth2.setText("");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(jDialog2, "Có lỗi xảy ra: " + e.getMessage());
    }
        
    
       
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if(id2.equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng chọn trong bảng lương!");
            return;
        }
         int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa ?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
         if (confirm == JOptionPane.YES_OPTION) {
         try {
            con= ConDB.ketnoiDB();
                 String sql="delete luong where maluong='"+id2+"'";
            Statement st=con.createStatement();
            int s=st.executeUpdate(sql);
            if (s > 0) {
        JOptionPane.showMessageDialog(this, "Xóa thành công");
    } else {
        JOptionPane.showMessageDialog(this, "Không tìm thấy bảng lương với id: " + id2);
    }
             con.close();
             loadl(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
         }else {
            JOptionPane.showMessageDialog(this, "Hủy thao tác xóa");
         }                                      
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        new Dashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        java.util.Date test1= dc1.getDate();
        java.util.Date test2= dc2.getDate();
        if(test1 == null || test2==null){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày!");
                return;
            }
            bd = new Date(dc1.getDate().getTime());
            kt = new Date(dc2.getDate().getTime()); 
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file");
        int chon = fileChooser.showSaveDialog(null);
        if (chon == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }
            System.out.println(filePath);
            try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet spreadsheet = workbook.createSheet("bangluong");
            // register the columns you wish to track and compute the column width
            CreationHelper createHelper = workbook.getCreationHelper();
            XSSFRow row = null;
            Cell cell = null;
            row = spreadsheet.createRow((short) 2);
            row.setHeight((short) 500);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("DANH SÁCH  LƯƠNG NHÂN VIÊN TỪ NGÀY "+bd+" ĐẾN "+ kt);
            //Tạo dòng tiêu đều của bảng
            // create CellStyle
            CellStyle cellStyle_Head = DinhdangHeader(spreadsheet);
            row = spreadsheet.createRow((short) 3);
            row.setHeight((short) 1000);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellStyle(cellStyle_Head);
            cell.setCellValue("STT");
            cell = row.createCell(1, CellType.STRING);
            cell.setCellStyle(cellStyle_Head);
            cell.setCellValue("Mã");
            cell = row.createCell(2, CellType.STRING);
            cell.setCellStyle(cellStyle_Head);
            cell.setCellValue("Tên Nhân viên");
            cell = row.createCell(3, CellType.STRING);
            cell.setCellStyle(cellStyle_Head);
            cell.setCellValue("Tổng giờ");
            cell = row.createCell(4, CellType.STRING);
            cell.setCellStyle(cellStyle_Head);
            cell.setCellValue("Lương/h");
            cell = row.createCell(5, CellType.STRING);
            cell.setCellStyle(cellStyle_Head);
            cell.setCellValue("Tổng lương");
            cell = row.createCell(6, CellType.STRING);
            cell.setCellStyle(cellStyle_Head);
            cell.setCellValue("Thưởng");
            cell = row.createCell(7, CellType.STRING);
            cell.setCellStyle(cellStyle_Head);
            cell.setCellValue("Được nhận");
            cell = row.createCell(8, CellType.STRING);
            cell.setCellStyle(cellStyle_Head);
            cell.setCellValue("Ngày nhận");
            con = ConDB.ketnoiDB();
            String sql = "Select * From luong where ngaynhan between '"+bd+"' and '"+kt+"'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            //Đổ dữ liệu từ rs vào các ô trong excel
            ResultSetMetaData rsmd = rs.getMetaData();
            int tongsocot = rsmd.getColumnCount();
            //Đinh dạng Tạo đường kẻ cho ô chứa dữ liệu
            CellStyle cellStyle_data = spreadsheet.getWorkbook().createCellStyle();
            cellStyle_data.setBorderLeft(BorderStyle.THIN);
            cellStyle_data.setBorderRight(BorderStyle.THIN);
            cellStyle_data.setBorderBottom(BorderStyle.THIN);
            int i = 0;
            while (rs.next()) {
                row = spreadsheet.createRow((short) 4 + i);
                row.setHeight((short) 400);
                cell = row.createCell(0);
                cell.setCellStyle(cellStyle_data);
                cell.setCellValue(i + 1);

                cell = row.createCell(1);
                cell.setCellStyle(cellStyle_data);
                cell.setCellValue(rs.getString("maluong"));
                System.out.println(rs.getString("maluong"));
                cell = row.createCell(2);
                cell.setCellStyle(cellStyle_data);
                cell.setCellValue(ncc1.get(rs.getString("manhanvien")));

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle_data);
                cell.setCellValue(rs.getString("tonggio"));

                cell = row.createCell(4);
                cell.setCellStyle(cellStyle_data);
                cell.setCellValue(rs.getString("luongtheogio"));

                cell = row.createCell(5);
                cell.setCellStyle(cellStyle_data);
                cell.setCellValue(rs.getString("tongluong"));

                cell = row.createCell(6);
                cell.setCellStyle(cellStyle_data);
                cell.setCellValue(rs.getString("thuong"));
                
                //Định dạng ngày tháng trong excel
                java.util.Date ngay = new java.util.Date(rs.getDate("ngaynhan").getTime());
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                cellStyle.setBorderLeft(BorderStyle.THIN);
                cellStyle.setBorderRight(BorderStyle.THIN);
                cellStyle.setBorderBottom(BorderStyle.THIN);
                cell = row.createCell(8);
                cell.setCellValue(ngay);
                cell.setCellStyle(cellStyle);
                
                cell = row.createCell(7);
                cell.setCellStyle(cellStyle_data);
                cell.setCellValue(rs.getString("tienduocnhan"));
                i++;
            }
            //Hiệu chỉnh độ rộng của cột
            for (int col = 3; col <= tongsocot; col++) {
                spreadsheet.autoSizeColumn(col);
            }
            spreadsheet.setColumnWidth(0, 2000);
            spreadsheet.setColumnWidth(1, 2000);
            spreadsheet.setColumnWidth(2, 6000);
            spreadsheet.setColumnWidth(6, 3000); 
            File f = new File(filePath);
            FileOutputStream out = new FileOutputStream(f);
            workbook.write(out);
            out.close();
            JOptionPane.showMessageDialog(this, "In file Excel thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
        
    }//GEN-LAST:event_jButton8ActionPerformed
    
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
            java.util.logging.Logger.getLogger(QLNhanvien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLNhanvien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLNhanvien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLNhanvien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QLNhanvien().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnls;
    private javax.swing.JButton btnxn;
    private com.toedter.calendar.JDateChooser dc1;
    private com.toedter.calendar.JDateChooser dc2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField luongh;
    private javax.swing.JTextField luongth2;
    private javax.swing.JTable tbll;
    private javax.swing.JTable tblls;
    private javax.swing.JTable tblnhanvien;
    private javax.swing.JLabel tennhanvien;
    private javax.swing.JTextField thuong2;
    private javax.swing.JLabel txttonggio;
    // End of variables declaration//GEN-END:variables
}
