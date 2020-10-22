package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.answer.query.VoteCountQuery;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.vote.Vote;
import ch.heigvd.amt.stack.domain.vote.VoteId;
import ch.heigvd.amt.stack.domain.vote.VoteRepository;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Default
public class JdbcVoteRepository extends JdbcRepository<Vote, VoteId> implements VoteRepository {
    @Resource(name = "database")
    private DataSource dataSource;

    private DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public int count(VoteCountQuery query) {
        setup(dataSource);
        boolean table= query.isUpvote();
        var search= "SELECT COUNT (*) FROM Vote WHERE isUpvote = ? AND idxAnswer = ?;";
        try{
            var statement = getDataSource().getConnection().prepareStatement(search);
            statement.setBoolean(1, table);
            statement.setString(2, query.getForAnswer().toString());
            var rs = statement.executeQuery();
            return rs.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Query didn't work");
        }
        return 0;
    }

    @Override
    public void save(Vote vote) {
        setup(dataSource);
        var insert = "INSERT INTO Vote (idxAnswer, idxCredential, isUpvote) VALUES (?, ?, ?) ON CONFLICT DO UPDATE SET isUpvote = ?;";
        try {
            var statement = getDataSource().getConnection().prepareStatement(insert);
            var isUpvote = vote.isUpvote() ? "TRUE" : "FALSE";
            statement.setString(1, vote.getId().getAnswer().toString());
            statement.setString(2, vote.getId().getVoter().toString());
            statement.setString(3, isUpvote);
            statement.setString(4, isUpvote);
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not add vote " + vote.getId());
        }
    }

    @Override
    public void remove(VoteId voteId) {
        setup(dataSource);
        var delete = "DELETE FROM Vote WHERE idxAnswer = ? AND idxCredential = ?;";
        try {
            var statement = getDataSource().getConnection().prepareStatement(delete);
            statement.setString(1, voteId.getAnswer().toString());
            statement.setString(2, voteId.getVoter().toString());
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not remove vote " + voteId);
        }
    }

    @Override
    public Optional<Vote> findById(VoteId voteId) {
        setup(dataSource);
        var select = "SELECT * FROM Vote WHERE idxAnswer = ? AND idxCredential = ?;";
        try {
            var statement = getDataSource().getConnection().prepareStatement(select);
            statement.setString(1, voteId.getAnswer().toString());
            statement.setString(2, voteId.getVoter().toString());
            var rs = statement.executeQuery();

            if (rs.next()) {
                return Optional.of(Vote.builder()
                        .id(VoteId.builder()
                                .answer(AnswerId.from(rs.getString("idxAnswer")))
                                .voter(CredentialId.from(rs.getString("idxCredential")))
                                .build())
                        .isUpvote(rs.getBoolean("isUpvote"))
                        .build());
            } else {
                return Optional.empty();
            }

        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not find vote " + voteId);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Vote> findAll() {
        setup(dataSource);

        var select = "SELECT * FROM Vote;";
        Collection<Vote> result = new ArrayList<>();
        try {
            var statement = getDataSource().getConnection().prepareStatement(select);
            var rs = statement.executeQuery();
            while (rs.next()) {
                var vote = Vote.builder()
                        .id(VoteId.builder()
                                .answer(AnswerId.from(rs.getString("idxAnswer")))
                                .voter(CredentialId.from(rs.getString("idxCredential")))
                                .build())
                        .isUpvote(rs.getBoolean("isUpvote"))
                        .build();
                result.add(vote);
            }
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not findAll()");
            return Collections.emptyList();
        }
        return result;
    }
}
