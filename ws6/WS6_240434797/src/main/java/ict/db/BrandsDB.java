package ict.db;

import ict.bean.Brand;
import ict.bean.Phone;
import java.sql.*;
import java.util.ArrayList;

public class BrandsDB {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public BrandsDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public ArrayList<Brand> getAllBrands() {
        ArrayList<Brand> brands = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT brand FROM PHONE")) {
            while (rs.next()) {
                brands.add(new Brand(rs.getString("brand")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return brands;
    }

    public ArrayList<Phone> getPhonesByBrand(String brand) {
        ArrayList<Phone> phones = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT name, img, price FROM PHONE WHERE brand=?")) {
            ps.setString(1, brand);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    phones.add(new Phone(rs.getString("name"), rs.getString("img"), rs.getDouble("price")));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return phones;
    }
}
