package awwcrud.services;


import awwcrud.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jeff on 7/13/16.
 */
public class UserService {

    Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts the user into the database. Returns the User object with an updated id
     * @param user
     * @return User object with an updated ID
     * @throws SQLException
     */
    public User add(User user) throws SQLException {
        PreparedStatement insert = connection.prepareStatement("INSERT INTO users (name) VALUES(?)");
        insert.setString(1, user.getName());
        insert.executeUpdate();

        ResultSet rs = insert.getGeneratedKeys();
        rs.next();
        user.setId(rs.getInt("id"));

        return user;
    }

    /**
     * Removes a user from the database
     * @param user
     * @throws SQLException
     */
    public void remove(User user) throws SQLException {
        PreparedStatement delete = connection.prepareStatement("DELETE FROM users WHERE id = ?");
        delete.setInt(1, user.getId());
        delete.executeUpdate();
    }

    /**
     * Retrieves a User object of the first one retrieved by username
     * @param username
     * @return User
     * @throws SQLException
     */
    public User getByUsername(String username) throws SQLException {
        PreparedStatement query = connection.prepareStatement("SELECT * FROM users WHERE name = ?");
        query.setString(1, username);
        ResultSet rs = query.executeQuery();

        rs.next();
        return createUserFromCurrentResultSet(rs);
    }

    /**
     * Returns a User object of the first one retrieved with given ID
     * @param id
     * @return User
     * @throws SQLException
     */
    public User getByID(int id) throws SQLException {
        PreparedStatement query = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
        query.setInt(1, id);
        ResultSet rs = query.executeQuery();

        rs.next(); //Sets the pointer
        return createUserFromCurrentResultSet(rs);
    }

    /**
     * Creates a User object with the resultset at the given pointer.  This requires that rs.next() be called
     * before this method is called.  This method does not loop loop through the resultset
     * @param rs
     * @return User or null if not found
     * @throws SQLException
     */
    private User createUserFromCurrentResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));

        if(user.getId() < 1){
            return null;
        }
        return user;
    }

    /**
     * Takes a populated user object and updates the database
     * @param user
     * @throws SQLException If there are connection issues
     */
    public void update(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET name=? WHERE id = ?");
        statement.setString(1, user.getName());
        statement.setInt(2, user.getId());
        statement.executeUpdate();
    }

}
