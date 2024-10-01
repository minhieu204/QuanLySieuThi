/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frmFrame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Oulyne
 */
public class NhanvienDAO {
    public static void logLogin(String employeeId) {
    String sql = "INSERT INTO lslamviec (manhanvien, logintime, ngay, workstatus) VALUES (?, ?, ?, ?)";

    try (Connection con = ConDB.ketnoiDB();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        // Gán giá trị vào các tham số
        stmt.setString(1, employeeId); // Mã nhân viên (dạng chuỗi VARCHAR)
        stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // Thời gian đăng nhập hiện tại
        stmt.setDate(3, java.sql.Date.valueOf(LocalDate.now())); // Ngày làm việc hiện tại
        stmt.setString(4, "IN_PROGRESS"); // Trạng thái công việc (đang trong quá trình làm việc)

        
        stmt.executeUpdate();

        System.out.println("Đăng nhập thành công cho nhân viên ID: " + employeeId);

    } catch (SQLException e) {
        System.err.println("Đã xảy ra lỗi khi đăng nhập: " + e.getMessage());
        e.printStackTrace();
    }
}
    public static void logLogout(String employeeId) {
    String sql = "UPDATE lslamviec SET logouttime = ?, workstatus = 'COMPLETED', " +
                 "giolamviec = DATEDIFF(MINUTE, logintime, ?) / 60.0 " +
                 "WHERE manhanvien = ? AND workstatus = 'IN_PROGRESS'";

    try (Connection con = ConDB.ketnoiDB();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        // Gán thời gian hiện tại khi đăng xuất
        Timestamp logoutTime = Timestamp.valueOf(LocalDateTime.now());
        stmt.setTimestamp(1, logoutTime);
        stmt.setTimestamp(2, logoutTime);
        stmt.setString(3, employeeId); // Mã nhân viên

        // Thực thi câu lệnh cập nhật
        int rowsUpdated = stmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("Đăng xuất thành công cho nhân viên ID: " + employeeId);
        } else {
            System.out.println("Không tìm thấy phiên làm việc đang hoạt động cho nhân viên ID: " + employeeId);
        }

    } catch (SQLException e) {
        System.err.println("Đã xảy ra lỗi khi đăng xuất: " + e.getMessage());
        e.printStackTrace();
    }
}
}
