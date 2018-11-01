package ru.moolls.webchat.dao.jdbctemplate;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.moolls.webchat.dao.UserDao;
import ru.moolls.webchat.dao.domain.User;
import ru.moolls.webchat.dao.domain.UserRole;
import ru.moolls.webchat.dao.domain.UserStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Log4j
@Component
public class JdbcTemplateUserDao implements UserDao {

    private final String GET_USER_BY_ID_SQL = "SELECT * FROM CHAT_USER WHERE USER_ID = ?";

    private final String GET_USER_BY_NAME_AND_PASSWORD_SQL =
            "SELECT * FROM CHAT_USER WHERE USER_NAME = ? AND MD5_PASS = ?";

    private final String GET_USER_BY_NAME_SQL =
            "SELECT * FROM CHAT_USER WHERE USER_NAME = ?";

    private final String INSERT_NEW_USER_SQL =
            "INSERT INTO CHAT_USER (USER_ID, USER_NAME, MD5_PASS, STATUS, ROLE) \n" +
                    "VALUES (CHAT_USER_SEQ.nextval, ?, ?, ?, ?)";

    private final String UPDATE_USER_SQL =
            "UPDATE CHAT_USER SET USER_NAME = ?, MD5_PASS = ?, ROLE = ?, STATUS = ? WHERE USER_ID = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User getUserById(long userId) {
        log.info("Getting user by id= " + userId + " in DAO layer");
        User resultUser = null;
        try {
            resultUser = jdbcTemplate.queryForObject(GET_USER_BY_ID_SQL, new UserRowMapper(), userId);
        } catch (DataAccessException ex) {
            log.warn("User not found in DAO layer");
        }
        log.debug("User result: " + resultUser);
        return resultUser;
    }

    @Override
    public User getUserByNameAndPassword(String userName, String passMd5) {

        log.info("Getting user by name=" + userName + " and password in DAO layer");
        User resultUser = null;
        try {
            resultUser = jdbcTemplate.queryForObject(GET_USER_BY_NAME_AND_PASSWORD_SQL,
                    new UserRowMapper(), userName, passMd5);
        } catch (DataAccessException ex) {
            log.warn("User by name and passwords not found in DAO layer");
        }
        log.debug("User by name=" + userName + " and passwords=********; Result: " + resultUser);
        return resultUser;
    }

    @Override
    public User getUserByName(String userName) {
        log.info("Getting user by name=" + userName + " in DAO layer");
        User resultUser = null;
        try {
            resultUser = jdbcTemplate.queryForObject(GET_USER_BY_NAME_SQL, new UserRowMapper(), userName);
        } catch (DataAccessException ex) {
            log.warn("User by name " + userName + " not found in DAO layer");
        }
        log.debug("User by name=" + userName + "; Result: " + resultUser);
        return resultUser;
    }

    @Override
    public User insertUser(User user) {
        log.info("Insert new user=" + user.getUserName() + " in DAO layer");
        KeyHolder newUserIdKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new InsertNewUserPreparedStatementCreator(user), newUserIdKeyHolder);
        User resultUser = getUserById(newUserIdKeyHolder.getKey().longValue());
        log.debug("New user created; User: " + resultUser);
        return resultUser;
    }

    @Override
    public User updateUser(User user) {
        log.info("Update userId=" + user.getUserId() + " in DAO layer");
        log.debug("Updated user: " + user);
        KeyHolder updatedUserIdHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new UpdateUserPreparedStatementCreator(user), updatedUserIdHolder);
        User updatedUser = getUserById(user.getUserId());
        log.debug("Uses updated: " + updatedUser);
        return updatedUser;
    }


    class InsertNewUserPreparedStatementCreator implements PreparedStatementCreator {

        private User newUser;

        InsertNewUserPreparedStatementCreator(User newUser) {
            this.newUser = newUser;
        }

        @Override
        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            PreparedStatement ps = con.prepareStatement(INSERT_NEW_USER_SQL, new String[]{"USER_ID"});
            ps.setString(1, newUser.getUserName());
            ps.setString(2, newUser.getPassMd5());
            ps.setLong(3, newUser.getUserStatus().getUserStatusId());
            ps.setLong(4, newUser.getUserRole().getUserRoleId());

            return ps;
        }
    }

    class UpdateUserPreparedStatementCreator implements PreparedStatementCreator {

        private User updatableUser;

        UpdateUserPreparedStatementCreator(User updatableUser) {
            this.updatableUser = updatableUser;
        }

        @Override
        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            PreparedStatement ps = con.prepareStatement(UPDATE_USER_SQL);

            ps.setString(1, updatableUser.getUserName());
            ps.setString(2, updatableUser.getPassMd5());
            ps.setLong(3, updatableUser.getUserRole().getUserRoleId());
            ps.setLong(4, updatableUser.getUserStatus().getUserStatusId());
            ps.setLong(5, updatableUser.getUserId());

            return ps;
        }
    }

    class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getLong("USER_ID"));
            user.setUserName(rs.getString("USER_NAME"));
            user.setPassMd5(rs.getString("MD5_PASS"));
            user.setRegTimestamp(rs.getTimestamp("REG_TIMESTAMP"));

            UserRole userRole = new UserRole();
            UserStatus userStatus = new UserStatus();
            userRole.setUserRoleId(rs.getLong("ROLE"));
            userStatus.setUserStatusId(rs.getLong("STATUS"));

            user.setUserRole(userRole);
            user.setUserStatus(userStatus);
            return user;
        }
    }

}
