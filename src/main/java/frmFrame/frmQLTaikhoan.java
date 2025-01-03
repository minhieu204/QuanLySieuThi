/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frmFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Oulyne
 */
public class frmQLTaikhoan extends javax.swing.JFrame {

    /**
     * Creates new form frmQLTaikhoan
     */
    public frmQLTaikhoan() {
        initComponents();
        load_taikhoan();
        loadcbncc();
    }
     private void themnhanvien(String ma, String username, String pass, String mpq, String ten, String gt, String sdt, String email){
        try {
            con=ConDB.ketnoiDB();
            
            String sql="insert into nhanvien values(?,?,?,?,?,?,?,?)";
            PreparedStatement st=con.prepareStatement(sql);
            st.setString(1, ma);
            st.setString(2, username);
            st.setString(3, pass);
            st.setString(4, mpq);
            st.setString(5, ten);
            st.setString(6, gt);
            st.setString(7, sdt);
            st.setString(8, email);
            st.executeUpdate();
            con.close();
            load_taikhoan();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
      private void themquanly(String ma, String username, String pass, String mpq, String ten, String gt, String sdt, String email){
        try {
            con=ConDB.ketnoiDB();
            String sql="insert into quanly values(?,?,?,?,?,?,?,?)";
            PreparedStatement st=con.prepareStatement(sql);
           st.setString(1, ma);
            st.setString(2, username);
            st.setString(3, pass);
            st.setString(4, mpq);
            st.setString(5, ten);
            st.setString(6, gt);
            st.setString(7, sdt);
            st.setString(8, email);
            st.executeUpdate();
            con.close();
            load_taikhoan();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
       private void ReadExcel(String tenfilepath) {
    FileInputStream fis = null;
    try {
        fis = new FileInputStream(tenfilepath);
        // Tạo đối tượng Excel
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0); // Lấy sheet đầu tiên của file

        // Bước 1: Lưu toàn bộ các dòng vào danh sách để xử lý sau
        List<Row> rows = new ArrayList<>();
        Iterator<Row> itr = sheet.iterator();

        while (itr.hasNext()) { // Lặp qua các dòng trong Excel
            Row row = itr.next(); 
            rows.add(row); // Lưu từng dòng vào danh sách
        }

        // Bước 2: Kiểm tra mã trùng lặp
        ArrayList<String> a = new ArrayList<>();
        ArrayList<String> b = new ArrayList<>();
        for (Row row : rows) {
            String ma = row.getCell(0).getStringCellValue();
            String mpq = row.getCell(3).getStringCellValue();
            a.add(ma);
            b.add(mpq);
        }

        for (String ma : a) {
            if (!ktratrung(ma)) { // Kiểm tra mã trùng
                int confirm = JOptionPane.showConfirmDialog(this, "Phát hiện mã tài khoản trùng trong CSDL, bỏ qua tài khoản này?", "Lỗi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(this, "Hủy thao tác nhập file");
                    return; // Dừng nếu chọn NO
                }
            }
        }
        for(String mpq : b){
            if(!mpq.equals("nv") && !mpq.equals("ql")){
                JOptionPane.showMessageDialog(this, "Định dạng dữ liệu không đúng với CSDL!!!");
                return;
            }     
        }
        

        for (Row row : rows) {
            String ma = row.getCell(0).getStringCellValue();
            String username = row.getCell(1).getStringCellValue();
            String pass = row.getCell(2).getStringCellValue();
            String mpq = row.getCell(3).getStringCellValue();
            String ten = row.getCell(4).getStringCellValue();
            String gt = row.getCell(5).getStringCellValue();
            String sdt = row.getCell(6).getStringCellValue();
            String email = row.getCell(7).getStringCellValue();

            // Thêm dữ liệu vào hệ thống
            if (mpq.equals("ql")) {
                themquanly(ma, username, pass, mpq, ten, gt, sdt, email);
            } else if (mpq.equals("nv")) {
                themnhanvien(ma, username, pass, mpq, ten, gt, sdt, email);
            } 
        }

        // Hiển thị thông báo thành công
        JOptionPane.showMessageDialog(this, "Import thành công file Excel.");

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        // Đóng tài nguyên
        try {
            if (fis != null) fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    Connection con;
     private boolean ktratrung(String ma){
        boolean kq=false;
        try {
            con=ConDB.ketnoiDB();
            String sql="select * from quanly where maquanly='"+ma+"'";
            String sql2="select * from nhanvien where manhanvien='"+ma+"'";
            Statement st= con.createStatement();
            Statement st2= con.createStatement();
            ResultSet rs= st.executeQuery(sql);
            ResultSet rs2= st2.executeQuery(sql2);
            if(!rs.next() && !rs2.next()){
                kq=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kq;
    }
     private void load_taikhoan(){
        try {
            con=ConDB.ketnoiDB();
            Statement st=con.createStatement();
            Statement st2= con.createStatement();
            
            
            String sql="Select * from quanly";
            ResultSet rs= st.executeQuery(sql);
            tablestaikhoan.removeAll();
            String[] tdb={"Mã tài khoản", "Họ và Tên", "Giới tính", "Tên đăng nhập", "Passwords", "Mã Phân quyền", "Điện thoại", "Email"};
            DefaultTableModel model= new DefaultTableModel(tdb, 0);
            while(rs.next()){
                Vector v= new Vector();
                v.add(rs.getString("maquanly"));
                v.add(rs.getString("hoten"));
                v.add(rs.getString("gioitinh"));
                v.add(rs.getString("username"));
                v.add(rs.getString("pass"));
                v.add(rs.getString("maphanquyen"));
                v.add(rs.getString("sdt"));
                v.add(rs.getString("email"));
                model.addRow(v);
            }
            String sql2="Select * from nhanvien";
            ResultSet rs2= st2.executeQuery(sql2);
            while(rs2.next()){
                Vector v2= new Vector();
                v2.add(rs2.getString("manhanvien"));
                v2.add(rs2.getString("hoten"));
                v2.add(rs2.getString("gioitinh"));
                v2.add(rs2.getString("username"));
                v2.add(rs2.getString("pass"));
                v2.add(rs2.getString("maphanquyen"));
                v2.add(rs2.getString("sdt"));
                v2.add(rs2.getString("email"));
                model.addRow(v2);
            }
            tablestaikhoan.setModel(model);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     Map<String, String> ncc1= new HashMap<>();
    Map<String, String> ncc2= new HashMap<>();
    private void loadcbncc(){
        try {
            con=ConDB.ketnoiDB();
            String sql="Select * from phanquyen";
            Statement st= con.createStatement();
            ResultSet rs= st.executeQuery(sql);
            while(rs.next()){
                phanquyen.addItem(rs.getString("tenphanquyen"));
                ncc1.put(rs.getString("tenphanquyen"), rs.getString("maphanquyen"));
                ncc2.put(rs.getString("maphanquyen"), rs.getString("tenphanquyen"));
            }
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

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        mataikhoan = new javax.swing.JTextField();
        phone = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        ten = new javax.swing.JTextField();
        username = new javax.swing.JTextField();
        pass = new javax.swing.JTextField();
        phanquyen = new javax.swing.JComboBox<>();
        gioitinh = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        them = new javax.swing.JButton();
        sua = new javax.swing.JButton();
        xoa = new javax.swing.JButton();
        in = new javax.swing.JButton();
        thoat = new javax.swing.JButton();
        nhaplai = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablestaikhoan = new javax.swing.JTable();
        txttiemkiem = new javax.swing.JTextField();
        timkiem = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximizedBounds(new java.awt.Rectangle(1024, 768, 768, 768));
        setMaximumSize(new java.awt.Dimension(1024, 768));
        setMinimumSize(new java.awt.Dimension(1024, 768));
        setPreferredSize(new java.awt.Dimension(1024, 768));
        setResizable(false);
        setSize(new java.awt.Dimension(1024, 768));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 255));
        jLabel1.setText("QUẢN LÝ TÀI KHOẢN");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin tài khoản", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 18))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Mã tài khoản:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Họ và Tên");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Mã phân quyền");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Tên đăng nhập");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Passwords");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Giới tính");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Điện thoại:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("Email:");

        mataikhoan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        phone.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        phone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                phoneFocusLost(evt);
            }
        });
        phone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                phoneKeyTyped(evt);
            }
        });

        email.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        email.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                emailFocusLost(evt);
            }
        });

        ten.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        username.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        pass.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        phanquyen.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        phanquyen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Phân quyền" }));

        gioitinh.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        gioitinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn giới tính", "Nam", "Nữ" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addComponent(pass, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(username, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(phanquyen, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(mataikhoan)
                            .addComponent(ten, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(email)
                    .addComponent(phone)
                    .addComponent(gioitinh, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(mataikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(gioitinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(phanquyen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(pass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

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
        in.setText("Nhập từ file");
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
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(them, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sua, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(in, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(thoat, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nhaplai, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
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
                .addContainerGap(39, Short.MAX_VALUE))
        );

        tablestaikhoan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tablestaikhoan.setModel(new javax.swing.table.DefaultTableModel(
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
        tablestaikhoan.setRowHeight(30);
        tablestaikhoan.setUpdateSelectionOnSort(false);
        tablestaikhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablestaikhoanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablestaikhoan);

        txttiemkiem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txttiemkiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttiemkiemActionPerformed(evt);
            }
        });
        txttiemkiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txttiemkiemKeyReleased(evt);
            }
        });

        timkiem.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        timkiem.setText("Tìm kiếm");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Tìm kiếm tài khoản:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txttiemkiem)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(9, 9, 9))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(391, 391, 391)
                        .addComponent(jLabel1)
                        .addGap(0, 382, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txttiemkiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timkiem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );

        setSize(new java.awt.Dimension(1024, 768));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themActionPerformed
       String maac=mataikhoan.getText().trim();
        String ten=this.ten.getText().trim();
        String phanquyen=this.phanquyen.getSelectedItem().toString();
        String phanquyen2=ncc1.get(phanquyen);
        String username=this.username.getText().trim();
        String pass=this.pass.getText().trim();
        String gioitinh=this.gioitinh.getSelectedItem().toString();
        String sdt=phone.getText().trim();
        String email=this.email.getText().trim();
        if(maac.equals("") || ten.equals("") || phanquyen.equals("Phân quyền") || username.equals("") || pass.equals("") || gioitinh.equals("Chọn giới tính") || sdt.equals("") || email.equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin!");
            return;
        }
        if(!ktratrung(maac)){
            JOptionPane.showMessageDialog(this, "Mã tài khoản này đã tồn tại");
            return;
        }
        if(phanquyen.equals("Quản lý")){
        try {
            con=ConDB.ketnoiDB();
            String sql= "insert into quanly values('"+ maac +"', N'"+ username +"', '"+ pass +"', '"+ phanquyen2 +"', N'"+ ten +"', N'"+ gioitinh +"', '"+ sdt +"', '"+ email +"')";
            Statement st= con.createStatement();
            st.executeUpdate(sql);
            con.close();
            JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
            load_taikhoan();
            mataikhoan.setText("");
            this.ten.setText("");
            this.phanquyen.setSelectedItem("Phân quyền");
            this.username.setText("");
            this.pass.setText("");
            this.gioitinh.setSelectedItem("Chọn giới tính");
            phone.setText("");
            this.email.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
        else if(phanquyen.equals("Nhân viên")){
              try {
            con=ConDB.ketnoiDB();
            String sql= "insert into nhanvien values('"+ maac +"', N'"+ username +"', '"+ pass +"', '"+ phanquyen2 +"', N'"+ ten +"', N'"+ gioitinh +"', '"+ sdt +"', '"+ email +"')";
            Statement st= con.createStatement();
            st.executeUpdate(sql);
            con.close();
            JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
            load_taikhoan();
            mataikhoan.setText("");
            this.ten.setText("");
            this.phanquyen.setSelectedItem("Phân quyền");
            this.username.setText("");
            this.pass.setText("");
            this.gioitinh.setSelectedItem("Chọn giới tính");
            phone.setText("");
            this.email.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    }//GEN-LAST:event_themActionPerformed

    private void suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suaActionPerformed
      String maac=mataikhoan.getText().trim();
        String ten=this.ten.getText().trim();
        String phanquyen=this.phanquyen.getSelectedItem().toString();
        String phanquyen2=ncc1.get(phanquyen);
        String username=this.username.getText().trim();
        String pass=this.pass.getText().trim();
        String gioitinh=this.gioitinh.getSelectedItem().toString();
        String sdt=phone.getText().trim();
        String email=this.email.getText().trim();
        if(maac.equals("") || ten.equals("") || phanquyen.equals("Phân quyền") || username.equals("") || pass.equals("") || gioitinh.equals("Chọn giới tính") || sdt.equals("") || email.equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin!");
            return;
        }
        if(phanquyen.equals("Quản lý")){
        try {
            con=ConDB.ketnoiDB();
           String sql="update quanly set username=N'"+ username + "', pass='"+pass+"', maphanquyen='"+phanquyen2+"', hoten=N'"+ten+"', gioitinh=N'"+gioitinh+"', sdt='"+sdt+"', email='"+email+"' where maquanly='"+maac+"'";
            Statement st= con.createStatement();
            st.executeUpdate(sql);
            con.close();
            JOptionPane.showMessageDialog(this, "Sửa tài khoản thành công!");
            load_taikhoan();
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
        else if(phanquyen.equals("Nhân viên")){
              try {
            con=ConDB.ketnoiDB();
           String sql="update nhanvien set username=N'"+ username + "', pass='"+pass+"', maphanquyen='"+phanquyen2+"', hoten=N'"+ten+"', gioitinh=N'"+gioitinh+"', sdt='"+sdt+"', email='"+email+"' where manhanvien='"+maac+"'";
            Statement st= con.createStatement();
            st.executeUpdate(sql);
            con.close();
            JOptionPane.showMessageDialog(this, "Sửa tài khoản thành công!");
            load_taikhoan();
        } catch (Exception e) {
            e.printStackTrace();
        }
        } 
    }//GEN-LAST:event_suaActionPerformed

    private void xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xoaActionPerformed
        String id=mataikhoan.getText().trim();
        String phanquyen=this.phanquyen.getSelectedItem().toString();
         int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa tài khoản này này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
         if (confirm == JOptionPane.YES_OPTION) {
         try {
            con= ConDB.ketnoiDB();
            if(phanquyen.equals("Quản lý")){
                String sql="delete quanly where maquanly='"+id+"'";
            Statement st=con.createStatement();
            int s=st.executeUpdate(sql);
            if (s > 0) {
        JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công");
    } else {
        JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản với id: " + id);
    }
            con.close();
            load_taikhoan();
            }
            else{
                 String sql="delete nhanvien where manhanvien='"+id+"'";
            Statement st=con.createStatement();
            int s=st.executeUpdate(sql);
            if (s > 0) {
        JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công");
    } else {
        JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản với id: " + id);
    }
             con.close();
            load_taikhoan();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         }else {
            JOptionPane.showMessageDialog(this, "Hủy thao tác xóa");
         }                                      
    }//GEN-LAST:event_xoaActionPerformed

    private void nhaplaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhaplaiActionPerformed
        mataikhoan.setText("");
        ten.setText("");
        phanquyen.setSelectedItem("Phân quyền");
        gioitinh.setSelectedItem("Chọn giới tính");
        username.setText("");
        pass.setText("");
        phone.setText("");
        email.setText("");
        mataikhoan.setEnabled(true);
        them.setEnabled(true);
        phanquyen.setEnabled(true);
    }//GEN-LAST:event_nhaplaiActionPerformed

    private void tablestaikhoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablestaikhoanMouseClicked
        int i=tablestaikhoan.getSelectedRow();
        DefaultTableModel tb= (DefaultTableModel) tablestaikhoan.getModel();
        mataikhoan.setText(tb.getValueAt(i, 0).toString());
        ten.setText(tb.getValueAt(i, 1).toString());
        String pq=tb.getValueAt(i, 5).toString();
        String pq2=ncc2.get(pq);
        phanquyen.setSelectedItem(pq2);
        username.setText(tb.getValueAt(i, 3).toString());
        pass.setText(tb.getValueAt(i, 4).toString());
        gioitinh.setSelectedItem(tb.getValueAt(i, 2).toString());
        phone.setText(tb.getValueAt(i, 6).toString());
        email.setText(tb.getValueAt(i, 7).toString());
        mataikhoan.setEnabled(false);
        them.setEnabled(false);
        phanquyen.setEnabled(false);
    }//GEN-LAST:event_tablestaikhoanMouseClicked

    private void txttiemkiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttiemkiemKeyReleased
        String tk=txttiemkiem.getText().trim();
          try {
            con=ConDB.ketnoiDB();
            Statement st=con.createStatement();
            Statement st2= con.createStatement();
            
            
            String sql="Select * from quanly where hoten like N'%"+tk+"%'";
            ResultSet rs= st.executeQuery(sql);
            tablestaikhoan.removeAll();
            String[] tdb={"Mã tài khoản", "Họ và Tên", "Giới tính", "Tên đăng nhập", "Passwords", "Mã Phân quyền", "Điện thoại", "Email"};
            DefaultTableModel model= new DefaultTableModel(tdb, 0);
            while(rs.next()){
                Vector v= new Vector();
                v.add(rs.getString("maquanly"));
                v.add(rs.getString("hoten"));
                v.add(rs.getString("gioitinh"));
                v.add(rs.getString("username"));
                v.add(rs.getString("pass"));
                v.add(rs.getString("maphanquyen"));
                v.add(rs.getString("sdt"));
                v.add(rs.getString("email"));
                model.addRow(v);
            }
            String sql2="Select * from nhanvien where hoten like N'%"+tk+"%'";
            ResultSet rs2= st2.executeQuery(sql2);
            while(rs2.next()){
                Vector v2= new Vector();
                v2.add(rs2.getString("manhanvien"));
                v2.add(rs2.getString("hoten"));
                v2.add(rs2.getString("gioitinh"));
                v2.add(rs2.getString("username"));
                v2.add(rs2.getString("pass"));
                v2.add(rs2.getString("maphanquyen"));
                v2.add(rs2.getString("sdt"));
                v2.add(rs2.getString("email"));
                model.addRow(v2);
            }
            tablestaikhoan.setModel(model);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txttiemkiemKeyReleased

    private void txttiemkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttiemkiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttiemkiemActionPerformed

    private void phoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phoneKeyTyped
          char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
        evt.consume();
        }
    }//GEN-LAST:event_phoneKeyTyped

    private void phoneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_phoneFocusLost
        String sdt=phone.getText();
        String regex;
        regex="^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-9]|9[0-9])[0-9]{7}$";
        if(!Pattern.matches(regex, sdt) && !sdt.equals("")){
            JOptionPane.showMessageDialog(this, "Nhập đúng số điện thoại Việt Nam");
            phone.setText("");
            return;
        }
    }//GEN-LAST:event_phoneFocusLost

    private void emailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailFocusLost
         String em=email.getText();
        String regex;
        regex="^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$";
        if(!Pattern.matches(regex, em) && !em.equals("")){
            JOptionPane.showMessageDialog(this, "Nhập đúng định dạng email");
            email.setText("");
            return;
        }
    }//GEN-LAST:event_emailFocusLost

    private void inActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inActionPerformed
        try {
            JFileChooser fc = new JFileChooser();
            int lc = fc.showOpenDialog(this);
            if (lc == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String tenfile = file.getName();
                if (tenfile.endsWith(".xlsx")) {    //endsWith chọn file có phần kết thúc ...
                    ReadExcel(file.getPath());
                } else {
                    JOptionPane.showMessageDialog(this, "Phải chọn file excel");
                }
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_inActionPerformed

    private void thoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thoatActionPerformed
        new Dashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_thoatActionPerformed

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
            java.util.logging.Logger.getLogger(frmQLTaikhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmQLTaikhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmQLTaikhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmQLTaikhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmQLTaikhoan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField email;
    private javax.swing.JComboBox<String> gioitinh;
    private javax.swing.JButton in;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField mataikhoan;
    private javax.swing.JButton nhaplai;
    private javax.swing.JTextField pass;
    private javax.swing.JComboBox<String> phanquyen;
    private javax.swing.JTextField phone;
    private javax.swing.JButton sua;
    private javax.swing.JTable tablestaikhoan;
    private javax.swing.JTextField ten;
    private javax.swing.JButton them;
    private javax.swing.JButton thoat;
    private javax.swing.JButton timkiem;
    private javax.swing.JTextField txttiemkiem;
    private javax.swing.JTextField username;
    private javax.swing.JButton xoa;
    // End of variables declaration//GEN-END:variables
}
