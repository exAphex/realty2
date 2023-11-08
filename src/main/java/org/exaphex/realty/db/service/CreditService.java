package org.exaphex.realty.db.service;

import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Credit;
import org.exaphex.realty.model.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreditService {

    public static List<Credit> getCredit(Unit u) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Credit> retCredits = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("select * from credits where unitid = ?");
            statement.setString(1, u.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Credit tmpCredit = new Credit(rs.getString("id"), rs.getString("unitid"), rs.getString("name"), rs.getString("description"), rs.getFloat("interestrate"), rs.getFloat("redemptionrate"), rs.getString("startdate"));
                retCredits.add(tmpCredit);
            }
        } catch (SQLException e) {
            // TODO: proper logging
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retCredits;
    }
}
