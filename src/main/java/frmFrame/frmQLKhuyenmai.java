/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frmFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ADMIN
 */
public class frmQLKhuyenmai extends javax.swing.JFrame {

    /**
     * Creates new form frmQLKhuyenmai
     */
    public frmQLKhuyenmai() {
        initComponents();
        loadTable();
        loadMaSanPham();
    }
     private void loadTable() {
    try {
        Connection con = ConDB.ketnoiDB();
        Statement st = con.createStatement();

      
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

        
        String sql = "SELECT * FROM KhuyenMai";
        ResultSet rs = st.executeQuery(sql);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã Khuyến Mãi", "Tên Khuyến Mãi", "Mã Sản Phẩm", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Giảm Giá", "Giá Gốc", "Giá Sau Khuyến Mãi"}, 0);

        while (rs.next()) {
            Vector<Object> v = new Vector<>();
            v.add(rs.getString("MaKhuyenMai"));
            v.add(rs.getString("TenKhuyenMai"));
            v.add(rs.getString("MaSanPham"));
            v.add(rs.getDate("NgayBatDau").toString());
            v.add(rs.getDate("NgayKetThuc").toString());
            v.add(rs.getInt("GiamGia"));
            v.add(rs.getInt("GiaGoc"));

           
            java.sql.Date ngayBatDau = rs.getDate("NgayBatDau");
            java.sql.Date ngayKetThuc = rs.getDate("NgayKetThuc");
            if (!currentDate.before(ngayBatDau) && !currentDate.after(ngayKetThuc)) {
                v.add(rs.getInt("GiaSauGiam"));
            } else {
                v.add(rs.getInt("GiaGoc"));
            }

            model.addRow(v);
        }

        table.setModel(model);
        con.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu!");
    }
}
     
     
      private void ReadExcel(String path) {
    try {
        FileInputStream fis = new FileInputStream(path);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0); 
        Iterator<Row> itr = sheet.iterator();

        while (itr.hasNext()) { 
            Row row = itr.next(); 

            String MaKhuyenMai = getCellValueAsString(row.getCell(0));
            String TenKhuyenMai = getCellValueAsString(row.getCell(1));
            String NgayBatDau = getCellValueAsString(row.getCell(2));
            String NgayKetThuc = getCellValueAsString(row.getCell(3));
            String MaSanPham = getCellValueAsString(row.getCell(4));
            String giamGiaStr = getCellValueAsString(row.getCell(5));
            String giaSauGiamStr = getCellValueAsString(row.getCell(6));
            String giaGocStr = getCellValueAsString(row.getCell(7));
            int GiamGia = giamGiaStr.isEmpty() ? 0 : Integer.parseInt(giamGiaStr);
            int GiaSauGiam = giaSauGiamStr.isEmpty() ? 0 : Integer.parseInt(giaSauGiamStr);
            int GiaGoc = giaGocStr.isEmpty() ? 0 : Integer.parseInt(giaGocStr);
            themkhuyenmai(MaKhuyenMai, TenKhuyenMai, NgayBatDau, NgayKetThuc, MaSanPham, GiamGia, GiaSauGiam, GiaGoc);
        }

        wb.close();
        fis.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

      private String getCellValueAsString(Cell cell) {
    if (cell == null) {
        return "";
    }
    switch (cell.getCellType()) {
        case STRING:
            return cell.getStringCellValue();
        case NUMERIC:
            return String.valueOf((int) cell.getNumericCellValue());
        case BOOLEAN:
            return String.valueOf(cell.getBooleanCellValue());
        default:
            return "";
    }
}
    private void themkhuyenmai(String MaKhuyenMai, String TenKhuyenMai, String NgayBatDau, String NgayKetThuc, String MaSanPham , int GiamGia ,int GiaSauGiam, int GiaGoc){
        try {
            Connection con = ConDB.ketnoiDB();
            String sql="insert into KhuyenMai values(?,?,?,?,?,?,?,?)";
            PreparedStatement st=con.prepareStatement(sql);
            st.setString(1, MaKhuyenMai);
            st.setString(2, TenKhuyenMai);
            st.setString(3, NgayBatDau);
            st.setString(4, NgayKetThuc);
            st.setString(5, MaSanPham);
            st.setInt(6, GiamGia);
            st.setInt(7, GiaSauGiam);
            st.setInt(8, GiaGoc);
            st.executeUpdate();
            con.close();
            loadTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    
    private void loadMaSanPham() {
        try {
            Connection con = ConDB.ketnoiDB();
            Statement st = con.createStatement();
            String sql = "SELECT masp FROM sanpham";
            ResultSet rs = st.executeQuery(sql);

            masp.removeAllItems();
            masp.addItem("Chọn");

            while (rs.next()) {
                masp.addItem(rs.getString("masp"));
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải mã sản phẩm!");
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

        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        masp = new javax.swing.JComboBox<>();
        ngbd = new com.toedter.calendar.JDateChooser();
        ngkt = new com.toedter.calendar.JDateChooser();
        ptgiam = new javax.swing.JTextField();
        makm = new javax.swing.JTextField();
        tenkm = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        them = new javax.swing.JButton();
        sua = new javax.swing.JButton();
        xoa = new javax.swing.JButton();
        in = new javax.swing.JButton();
        thoat = new javax.swing.JButton();
        nhaplai = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txttk = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        jLabel9.setText("jLabel9");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUẢN LÝ CHƯƠNG TRÌNH KHUYẾN MÃI");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin khuyến mãi", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Mã khuyến mãi :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Tên khuyến mãi :");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Ngày bắt đầu :");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Ngày kết thúc :");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Mã sản phẩm :");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Phần trăm giảm  giá :");

        masp.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        masp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn", " " }));
        masp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maspActionPerformed(evt);
            }
        });

        ptgiam.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        makm.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        makm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makmActionPerformed(evt);
            }
        });

        tenkm.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tenkm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tenkmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(masp, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tenkm))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(makm, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ngbd, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                    .addComponent(ngkt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ptgiam))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(makm, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel4)
                        .addComponent(ngbd, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(tenkm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ngkt, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)))
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(masp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(ptgiam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(90, 90, 90))
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

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Tìm kiến khuyến mãi :");

        txttk.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txttk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttkActionPerformed(evt);
            }
        });
        txttk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txttkKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txttk)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txttk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        table.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 984, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1024, 768));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themActionPerformed
   String maKM = makm.getText().trim();
   String tenKM = tenkm.getText().trim();
   String maSP = masp.getSelectedItem().toString();
   String phanTramGiam = ptgiam.getText().trim();
   Date ngayBD = new Date(ngbd.getDate().getTime());
   Date ngayKT = new Date(ngkt.getDate().getTime());
   java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis()); 

   if (maKM.isEmpty() || tenKM.isEmpty() || maSP.equals("Chọn") || phanTramGiam.isEmpty() || ngayBD == null || ngayKT == null) {
       JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin khuyến mãi.");
       return;
   }

   try {
       Connection conn = ConDB.ketnoiDB();

      
       String sqlGetPrice = "SELECT giaban FROM sanpham WHERE masp = ?";
       PreparedStatement pstGetPrice = conn.prepareStatement(sqlGetPrice);
       pstGetPrice.setString(1, maSP);
       ResultSet rs = pstGetPrice.executeQuery();
       int giaGoc = 0;
       if (rs.next()) {
           giaGoc = rs.getInt("giaban");
       }

       
       int giamGia = Integer.parseInt(phanTramGiam);
       int giaSauGiam = (int) (giaGoc * (1 - (float) giamGia / 100));
       
       String sqlInsertKM = "INSERT INTO KhuyenMai (MaKhuyenMai, TenKhuyenMai, MaSanPham, GiamGia, NgayBatDau, NgayKetThuc, GiaSauGiam, GiaGoc) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
       PreparedStatement pstInsertKM = conn.prepareStatement(sqlInsertKM);
       pstInsertKM.setString(1, maKM);
       pstInsertKM.setString(2, tenKM);
       pstInsertKM.setString(3, maSP);
       pstInsertKM.setInt(4, giamGia);
       pstInsertKM.setDate(5, ngayBD);
       pstInsertKM.setDate(6, ngayKT);
       pstInsertKM.setInt(7, giaSauGiam);
       pstInsertKM.setInt(8, giaGoc);
       pstInsertKM.executeUpdate();

      
       if (!currentDate.before(ngayBD) && !currentDate.after(ngayKT)) {
           String sqlUpdatePrice = "UPDATE sanpham SET giaban = ? WHERE masp = ?";
           PreparedStatement pstUpdatePrice = conn.prepareStatement(sqlUpdatePrice);
           pstUpdatePrice.setInt(1, giaSauGiam); 
           pstUpdatePrice.setString(2, maSP);
           pstUpdatePrice.executeUpdate();
       }

       JOptionPane.showMessageDialog(null, "Thêm khuyến mãi thành công!");
       loadTable(); 
       conn.close();
   } catch (Exception e) {
       e.printStackTrace();
       JOptionPane.showMessageDialog(null, "Lỗi khi thêm khuyến mãi!");
   }
    }//GEN-LAST:event_themActionPerformed

    private void suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suaActionPerformed
    String maKM = makm.getText().trim();
    String tenKM = tenkm.getText().trim();
    String maSP = masp.getSelectedItem().toString();
    String phanTramGiam = ptgiam.getText().trim();
    Date ngayBD = new Date(ngbd.getDate().getTime());
    Date ngayKT = new Date(ngkt.getDate().getTime());
    java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis()); 

    if (maKM.isEmpty() || tenKM.isEmpty() || maSP.equals("Chọn") || phanTramGiam.isEmpty() || ngayBD == null || ngayKT == null) {
        JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin khuyến mãi.");
        return;
    }

    try {
        Connection conn = ConDB.ketnoiDB();

        
        String sqlGetOriginalPrice = "SELECT GiaGoc FROM KhuyenMai WHERE MaKhuyenMai = ?";
        PreparedStatement pstGetOriginalPrice = conn.prepareStatement(sqlGetOriginalPrice);
        pstGetOriginalPrice.setString(1, maKM);
        ResultSet rs = pstGetOriginalPrice.executeQuery();

        int giaGoc;
        if (rs.next()) {
           
            giaGoc = rs.getInt("GiaGoc");
        } else {
           
            String sqlGetPrice = "SELECT giaban FROM sanpham WHERE masp = ?";
            PreparedStatement pstGetPrice = conn.prepareStatement(sqlGetPrice);
            pstGetPrice.setString(1, maSP);
            ResultSet rsPrice = pstGetPrice.executeQuery();
            if (rsPrice.next()) {
                giaGoc = rsPrice.getInt("giaban");
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy sản phẩm!");
                return;
            }
        }

      
        int giamGia = Integer.parseInt(phanTramGiam);
        int giaSauGiam = (int) (giaGoc * (1 - (float) giamGia / 100));

        
        String sqlUpdateKM = "UPDATE KhuyenMai SET TenKhuyenMai=?, MaSanPham=?, GiamGia=?, NgayBatDau=?, NgayKetThuc=?, GiaSauGiam=?, GiaGoc=? WHERE MaKhuyenMai=?";
        PreparedStatement pstUpdateKM = conn.prepareStatement(sqlUpdateKM);
        pstUpdateKM.setString(1, tenKM);
        pstUpdateKM.setString(2, maSP);
        pstUpdateKM.setInt(3, giamGia);
        pstUpdateKM.setDate(4, ngayBD);
        pstUpdateKM.setDate(5, ngayKT);
        pstUpdateKM.setInt(6, giaSauGiam);
        pstUpdateKM.setInt(7, giaGoc);  
        pstUpdateKM.setString(8, maKM);
        pstUpdateKM.executeUpdate();

        
        String sqlUpdatePrice;
        PreparedStatement pstUpdatePrice;
        if (!currentDate.before(ngayBD) && !currentDate.after(ngayKT)) {
            
            sqlUpdatePrice = "UPDATE sanpham SET giaban = ? WHERE masp = ?";
            pstUpdatePrice = conn.prepareStatement(sqlUpdatePrice);
            pstUpdatePrice.setInt(1, giaSauGiam);
        } else {
          
            sqlUpdatePrice = "UPDATE sanpham SET giaban = ? WHERE masp = ?";
            pstUpdatePrice = conn.prepareStatement(sqlUpdatePrice);
            pstUpdatePrice.setInt(1, giaGoc);
        }
        pstUpdatePrice.setString(2, maSP);
        pstUpdatePrice.executeUpdate();

        JOptionPane.showMessageDialog(null, "Cập nhật khuyến mãi thành công!");
        loadTable();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật khuyến mãi!");
    }
    }//GEN-LAST:event_suaActionPerformed

    private void xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xoaActionPerformed
     String maKM = makm.getText().trim();

    if (maKM.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Vui lòng nhập mã khuyến mãi cần xóa.");
        return;
    }

    Connection conn = ConDB.ketnoiDB();
    try {
       
        String sqlGetOriginalPrice = "SELECT MaSanPham, GiaGoc FROM KhuyenMai WHERE MaKhuyenMai = ?";
        PreparedStatement pstGetOriginalPrice = conn.prepareStatement(sqlGetOriginalPrice);
        pstGetOriginalPrice.setString(1, maKM);
        ResultSet rs = pstGetOriginalPrice.executeQuery();

        String maSP = null;
        int giaGoc = 0;
        if (rs.next()) {
            maSP = rs.getString("MaSanPham");
            giaGoc = rs.getInt("GiaGoc");
        }

        if (maSP != null) {
           
            String sqlUpdatePrice = "UPDATE sanpham SET giaban = ? WHERE masp = ?";
            PreparedStatement pstUpdatePrice = conn.prepareStatement(sqlUpdatePrice);
            pstUpdatePrice.setInt(1, giaGoc);
            pstUpdatePrice.setString(2, maSP);
            pstUpdatePrice.executeUpdate();

          
            String sql = "DELETE FROM KhuyenMai WHERE MaKhuyenMai=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, maKM);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Xóa khuyến mãi thành công và khôi phục giá sản phẩm!");
            loadTable();
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sản phẩm tương ứng với khuyến mãi!");
        }
        
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi xóa khuyến mãi!");
    }
    }//GEN-LAST:event_xoaActionPerformed

    private void nhaplaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhaplaiActionPerformed
        makm.setText("");
        tenkm.setText("");
        masp.setSelectedItem("Chọn");
        ptgiam.setText("");
        in.setEnabled(true);
        them.setEnabled(true);
        makm.setEnabled(true);
        ngbd.setDate(null);
        ngkt.setDate(null);
    }//GEN-LAST:event_nhaplaiActionPerformed

    private void maspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maspActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maspActionPerformed

    private void makmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_makmActionPerformed

    private void tenkmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tenkmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tenkmActionPerformed

    private void txttkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttkActionPerformed

    private void thoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thoatActionPerformed
        // TODO add your handling code here:
       new Dashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_thoatActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        // TODO add your handling code here:
    int i = table.getSelectedRow(); 
    DefaultTableModel model = (DefaultTableModel) table.getModel();

    
    makm.setText(model.getValueAt(i, 0).toString()); 
    tenkm.setText(model.getValueAt(i, 1).toString()); 
    masp.setSelectedItem(model.getValueAt(i, 2).toString()); 

   
    try {
        java.util.Date ngayBatDau = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(i, 3).toString());
        ngbd.setDate(ngayBatDau); 
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    try {
        java.util.Date ngayKetThuc = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(i, 4).toString());
        ngkt.setDate(ngayKetThuc); 
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    ptgiam.setText(model.getValueAt(i, 5).toString()); 

    
    makm.setEnabled(false); 
    them.setEnabled(false); 
    in.setEnabled(false); 
    }//GEN-LAST:event_tableMouseClicked

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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            JFileChooser fc = new JFileChooser();
            int lc = fc.showOpenDialog(this);
            if (lc == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String tenfile = file.getAbsolutePath();
                if (tenfile.endsWith(".xlsx")) {    
                    ReadExcel(file.getPath());
                    JOptionPane.showMessageDialog(this, "import thành công file excel");
                } else {
                    JOptionPane.showMessageDialog(this, "Phải chọn file excel");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi import file Excel: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txttkKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttkKeyReleased
        // TODO add your handling code here:
        String tk=txttk.getText().trim();
          try {
            Connection con=ConDB.ketnoiDB();
            Statement st=con.createStatement();
            Statement st2= con.createStatement();
            
            
            String sql="Select * from KhuyenMai where TenKhuyenMai like N'%"+tk+"%'";
            ResultSet rs= st.executeQuery(sql);
            table.removeAll();
            String[] tdb={"Mã Khuyến Mãi", "Tên Khuyến Mãi", "Mã Sản Phẩm", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Giảm Giá", "Giá Gốc", "Giá Sau Khuyến Mãi"};
            DefaultTableModel model= new DefaultTableModel(tdb, 0);
            while(rs.next()){
                Vector<Object> v = new Vector<>();
                v.add(rs.getString("MaKhuyenMai"));
                v.add(rs.getString("TenKhuyenMai"));
                v.add(rs.getString("MaSanPham"));
                v.add(rs.getDate("NgayBatDau").toString());
                v.add(rs.getDate("NgayKetThuc").toString());
                v.add(rs.getInt("GiamGia"));
                v.add(rs.getInt("GiaGoc"));
                model.addRow(v);
            }
            
            table.setModel(model);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txttkKeyReleased

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
            java.util.logging.Logger.getLogger(frmQLKhuyenmai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmQLKhuyenmai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmQLKhuyenmai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmQLKhuyenmai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmQLKhuyenmai().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton in;
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
    private javax.swing.JTextField makm;
    private javax.swing.JComboBox<String> masp;
    private com.toedter.calendar.JDateChooser ngbd;
    private com.toedter.calendar.JDateChooser ngkt;
    private javax.swing.JButton nhaplai;
    private javax.swing.JTextField ptgiam;
    private javax.swing.JButton sua;
    private javax.swing.JTable table;
    private javax.swing.JTextField tenkm;
    private javax.swing.JButton them;
    private javax.swing.JButton thoat;
    private javax.swing.JTextField txttk;
    private javax.swing.JButton xoa;
    // End of variables declaration//GEN-END:variables

   
}
