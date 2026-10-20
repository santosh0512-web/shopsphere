
package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.Address;

public class AddressDAO {

    public boolean addAddress(Address address) {
        String sql = "INSERT INTO addresses " +
                "(user_id, address_line, city, state, pincode, address_type) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, address.getUserId());
            ps.setString(2, address.getAddressLine());
            ps.setString(3, address.getCity());
            ps.setString(4, address.getState());
            ps.setString(5, address.getPincode());
            ps.setString(6, address.getAddressType());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save address.", e);
        }
    }

    public List<Address> getAddressesByUserId(int userId) {
        List<Address> addresses = new ArrayList<>();

        String sql = "SELECT address_id, user_id, address_line, " +
                "city, state, pincode, address_type " +
                "FROM addresses WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Address address = new Address();

                    address.setAddressId(rs.getInt("address_id"));
                    address.setUserId(rs.getInt("user_id"));
                    address.setAddressLine(rs.getString("address_line"));
                    address.setCity(rs.getString("city"));
                    address.setState(rs.getString("state"));
                    address.setPincode(rs.getString("pincode"));
                    address.setAddressType(rs.getString("address_type"));

                    addresses.add(address);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to fetch saved addresses.", e);
        }

        return addresses;
    }
}
