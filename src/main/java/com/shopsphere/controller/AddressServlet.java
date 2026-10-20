
package com.shopsphere.controller;

import java.io.IOException;
import java.util.List;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.model.Address;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AddressServlet extends HttpServlet {

    private AddressDAO addressDAO;

    @Override
    public void init() {
        addressDAO = new AddressDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        try {
            List<Address> addresses = addressDAO.getAddressesByUserId(userId);
            request.setAttribute("addresses", addresses);

            request.getRequestDispatcher("/addresses.jsp")
                   .forward(request, response);
        } catch (RuntimeException e) {
            getServletContext().log("Unable to load addresses", e);
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load saved addresses.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String addressLine = request.getParameter("addressLine");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String pincode = request.getParameter("pincode");
        String addressType = request.getParameter("addressType");

        if (isBlank(addressLine) || addressLine.length() > 255
                || isBlank(city) || city.length() > 80
                || isBlank(state) || state.length() > 80
                || pincode == null || !pincode.matches("[0-9]{6}")
                || !("HOME".equals(addressType)
                        || "WORK".equals(addressType)
                        || "OTHER".equals(addressType))) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Enter a valid address, city, state, six-digit pincode, and address type.");
            return;
        }

        Address address = new Address();
        address.setUserId(userId);
        address.setAddressLine(addressLine.trim());
        address.setCity(city.trim());
        address.setState(state.trim());
        address.setPincode(pincode);
        address.setAddressType(addressType);

        try {
            boolean saved = addressDAO.addAddress(address);

            if (saved) {
                response.sendRedirect(request.getContextPath()
                        + "/addresses?saved=true");
            } else {
                response.sendRedirect(request.getContextPath()
                        + "/addresses?saved=false");
            }
        } catch (RuntimeException e) {
            getServletContext().log("Unable to save address", e);
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to save the address.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
