package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.answer.query.VoteCountQuery;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.vote.Vote;
import ch.heigvd.amt.stack.domain.vote.VoteId;
import ch.heigvd.amt.stack.domain.vote.VoteRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.database.dsl.PrepareStatementScope;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
@Default
public class JdbcVoteRepository extends JdbcRepository<Vote, VoteId> implements VoteRepository {

    private static Vote parseVote(ResultSet resultSet) throws SQLException {
        return Vote.builder()
                .id(VoteId.builder()
                        .answer(AnswerId.from(resultSet.getString("idxAnswer")))
                        .voter(CredentialId.from(resultSet.getString("idxCredential")))
                        .build())
                .isUpvote(resultSet.getBoolean("isUpvote"))
                .build();
    }

    @Override
    public int count(VoteCountQuery query) {
        setup(dataSource);
        boolean table = query.isUpvote();
        var search = "SELECT COUNT (*) FROM Vote WHERE isUpvote = ? AND idxAnswer = ?;";
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(search);
            statement.setBoolean(1, table);
            statement.setString(2, query.getForAnswer().toString());
            var rs = statement.executeQuery();
            if (!rs.next()) {
                throw new SQLException("ResultSet should not be empty.");
            }
            return rs.getInt(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Query didn't work");
        }
        return 0;
    }

    @Override
    public void save(Vote vote) {
        setup(dataSource);
        var insert = "INSERT INTO Vote (idxAnswer, idxCredential, isUpvote) VALUES (?, ?, ?) ON CONFLICT (idxAnswer, idxCredential) DO UPDATE SET isUpvote = ?;";
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(insert);
            statement.setString(1, vote.getId().getAnswer().toString());
            statement.setString(2, vote.getId().getVoter().toString());
            statement.setBoolean(3, vote.isUpvote());
            statement.setBoolean(4, vote.isUpvote());

            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not add vote " + vote.getId() + ": " + ex.toString());
        }
    }

    @Override
    public void remove(VoteId voteId) {
        setup(dataSource);
        var delete = "DELETE FROM Vote WHERE idxAnswer = ? AND idxCredential = ?;";
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(delete);
            statement.setString(1, voteId.getAnswer().toString());
            statement.setString(2, voteId.getVoter().toString());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not remove vote " + voteId);
        }
    }

    @Override
    public Optional<Vote> findById(VoteId voteId) {
        setup(dataSource);
        return findFor(dataSource,
                JdbcVoteRepository::parseVote,
                "SELECT * FROM Vote WHERE idxAnswer = ? AND idxCredential = ?;",
                (ps) -> {
                    ps.setString(1, voteId.getAnswer().toString());
                    ps.setString(2, voteId.getVoter().toString());
                }).findFirst();
    }

    @Override
    public Collection<Vote> findAll() {
        setup(dataSource);
        return findFor(dataSource,
                JdbcVoteRepository::parseVote,
                "SELECT * FROM Vote;",
                PrepareStatementScope.none()).collect(Collectors.toList());
    }
}
