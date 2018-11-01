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
import ru.moolls.webchat.dao.EventDao;
import ru.moolls.webchat.dao.domain.Event;
import ru.moolls.webchat.dao.domain.EventType;
import ru.moolls.webchat.dao.domain.User;
import ru.moolls.webchat.dao.domain.enums.EventTypeEnum;

import java.sql.*;
import java.util.List;

@Component
@Log4j
public class JdbcTemplateEventDao implements EventDao {

    private final String GET_LAST_EVENTS_SQL =
            "SELECT * FROM " +
                    "(SELECT CHAT_EVENT.*, USER_FROM.USER_ID UF_ID, USER_FROM.USER_NAME UF_NAME, USER_TO.USER_ID UT_ID, USER_TO.USER_NAME UT_NAME FROM CHAT_EVENT " +
                    "LEFT JOIN CHAT_USER USER_FROM on CHAT_EVENT.FROM_USER = USER_FROM.USER_ID " +
                    "LEFT JOIN CHAT_USER USER_TO on CHAT_EVENT.TO_USER = USER_TO.USER_ID ORDER BY EVENT_ID DESC) " +
                    "WHERE ROWNUM <= ? ORDER BY EVENT_ID";

    private final String GET_EVENT_BY_ID_SQL =
            "SELECT CHAT_EVENT.*, USER_FROM.USER_ID UF_ID, USER_FROM.USER_NAME UF_NAME, USER_TO.USER_ID UT_ID, USER_TO.USER_NAME UT_NAME FROM CHAT_EVENT " +
                    "LEFT JOIN CHAT_USER USER_FROM on CHAT_EVENT.FROM_USER = USER_FROM.USER_ID " +
                    "LEFT JOIN CHAT_USER USER_TO on CHAT_EVENT.TO_USER = USER_TO.USER_ID WHERE EVENT_ID = ?";


    private final String GET_LAST_EVENTS_BY_TYPE_SQL =
            "SELECT * FROM " +
                    "(SELECT CHAT_EVENT.*, USER_FROM.USER_ID UF_ID, USER_FROM.USER_NAME UF_NAME, USER_TO.USER_ID UT_ID, USER_TO.USER_NAME UT_NAME FROM CHAT_EVENT " +
                    "LEFT JOIN CHAT_USER USER_FROM on CHAT_EVENT.FROM_USER = USER_FROM.USER_ID " +
                    "LEFT JOIN CHAT_USER USER_TO on CHAT_EVENT.TO_USER = USER_TO.USER_ID WHERE EVENT_TYPE = ?) " +
                    "WHERE ROWNUM <= ?";


    private final String PUT_EVENT_SQL =
            "INSERT INTO CHAT_EVENT (EVENT_ID, EVENT_TYPE, FROM_USER, TO_USER, MESSAGE, IP)" +
                    "VALUES (CHAT_EVENT_SEQ.nextval, ?, ?, ?, ?, ?)";

    private final String GET_LAST_EVENT_BY_USER_SQL =
            "SELECT * FROM (SELECT * FROM CHAT_EVENT WHERE TO_USER = ? ORDER BY EVENT_ID DESC) WHERE ROWNUM <= 1";


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> getLastEvents(int count) {
        log.info("Getting last events in DAO layer");
        List<Event> eventList = jdbcTemplate.query(GET_LAST_EVENTS_SQL, new EventRowMapper(), count);
        log.debug("Last events result: " + eventList);
        return eventList;
    }


    @Override
    public List<Event> getLastEventsByType(EventType eventType, int count) {
        log.info("Getting last events by type=" + eventType + " in DAO layer");
        long eventTypeId = eventType.getEventTypeId();
        List<Event> eventListByType = jdbcTemplate.query(GET_LAST_EVENTS_BY_TYPE_SQL,
                new EventRowMapper(), eventTypeId, count);
        log.debug("Last events by type:" + eventType + " result: " + eventListByType);
        return eventListByType;
    }

    @Override
    public Event getEventById(long eventId) {

        log.info("Starting getting event by ID=" + eventId + " process in DAO layer");
        Event event = null;
        try {
            event = jdbcTemplate.queryForObject(GET_EVENT_BY_ID_SQL, new EventRowMapper(), eventId);
        } catch (DataAccessException ex) {
            log.warn("Event by eventId not found in DAO layer");
        }
        log.debug("Result event by ID=" + eventId + ": " + event);
        return event;
    }


    @Override
    public Event putEvent(Event event) {
        log.info("Put event in DAO layer");
        log.debug("Event: " + event.toString());
        KeyHolder newEventIdKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new EventPreparedStatementCreator(event), newEventIdKeyHolder);
        Event newEvent = getEventById(newEventIdKeyHolder.getKey().longValue());
        log.debug("Created event: " + newEvent);
        return newEvent;
    }

    @Override
    public Event getLastEventOfUser(User user) {
        log.info("Starting getting last event by userId=" + user.getUserId() + " process in DAO layer");
        Event event = null;
        try {
            event = jdbcTemplate.queryForObject(GET_LAST_EVENT_BY_USER_SQL, new EventRowMapper(), user.getUserId());
        } catch (DataAccessException ex) {
            log.warn("Last event by userId not found in DAO layer");
        }
        log.debug("Last event by userId=" + user.getUserId() + ": " + event);
        log.info("Ending getting last event by userId=" + user.getUserId() + " process in DAO layer");
        return event;
    }


    class EventRowMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event event = new Event();
            event.setEventId(rs.getLong("EVENT_ID"));
            event.setMessage(rs.getString("MESSAGE"));
            event.setTimestamp(rs.getTimestamp("TIMESTAMP"));
            event.setIp(rs.getString("IP"));
            event.setEventType(EventTypeEnum.getValueById(rs.getLong("EVENT_TYPE")));

            if (rs.getLong("UF_ID") != 0) {
                User fromUser = new User();
                fromUser.setUserId(rs.getLong("UF_ID"));
                fromUser.setUserName(rs.getString("UF_NAME"));
                event.setFromUser(fromUser);
            }

            if (rs.getLong("UT_ID") != 0) {
                User toUser = new User();
                toUser.setUserId(rs.getLong("UT_ID"));
                toUser.setUserName(rs.getString("UT_NAME"));
                event.setToUser(toUser);
            }
            return event;
        }
    }

    class EventPreparedStatementCreator implements PreparedStatementCreator {

        private Event event;

        private EventPreparedStatementCreator(Event event) {
            this.event = event;
        }

        @Override
        public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException {

            PreparedStatement ps = connection.prepareStatement(PUT_EVENT_SQL, new String[]{"EVENT_ID"});

            ps.setLong(1, event.getEventType().getEventTypeId());

            if (event.getFromUser() != null) {
                ps.setLong(2, event.getFromUser().getUserId());
            } else {
                ps.setNull(2, Types.NULL);
            }

            if (event.getToUser() != null) {
                ps.setLong(3, event.getToUser().getUserId());
            } else {
                ps.setNull(3, Types.NULL);
            }

            ps.setString(4, event.getMessage());
            ps.setString(5, event.getIp());

            return ps;
        }
    }
}
