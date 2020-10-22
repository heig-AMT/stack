package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.domain.answer.Answer;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import ch.heigvd.amt.stack.domain.vote.Vote;
import ch.heigvd.amt.stack.domain.vote.VoteId;
import ch.heigvd.amt.stack.domain.vote.VoteRepository;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.addAll;

@ApplicationScoped
@Default
public class JdbcVoteRepository extends JdbcRepository<Vote, VoteId> implements VoteRepository {
    @Resource(name = "database")
    private DataSource dataSource;

    private DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void save(Vote vote) {
        setup(dataSource);
        String table = (vote.isUpOrDown() ? "UpVote" : "DownVote");
        var insert = "INSERT INTO " + table + " (idxAnswer, idxCredential) VALUES (?, ?);";
        try {
            var statement = getDataSource().getConnection().prepareStatement(insert);
            statement.setString(1, vote.getAnswer().toString());
            statement.setString(2, vote.getCredential().toString());
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not add vote " + vote.getId());
        }
    }

    @Override
    public void remove(VoteId voteId) {
        if(!findById(voteId).isEmpty()){
            remove(findById(voteId).get());
        }
    }


    public void remove(Vote vote) {
        setup(dataSource);
        String table = (vote.isUpOrDown() ? "UpVote" : "DownVote");
        var insert = "DELETE From " + table + " WHERE idxAnswer=? AND idxCredential=?;";
        try {
            var statement = getDataSource().getConnection().prepareStatement(insert);
            statement.setString(1, vote.getAnswer().toString());
            statement.setString(2, vote.getCredential().toString());
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not remove vote " + vote.getId());
        }
    }

    @Override
    public Optional<Vote> findById(VoteId voteId) {
        setup(dataSource);

        var insert = "SELECT From ? WHERE idxAnswer=? AND idxCredential=?;";
        try {
            var statement = getDataSource().getConnection().prepareStatement(insert);
            statement.setString(1, "UpVote");
            statement.setString(2, voteId.getAnswer().toString());
            statement.setString(3, voteId.getCredential().toString());
            var rs = statement.executeQuery();

            if (rs.next()) {
                return Optional.of(Vote.builder()
                        .id(new VoteId(AnswerId.from(rs.getString("idxAnswer"))
                                , CredentialId.from(rs.getString("idxCredential"))))
                        .build());
            } else {
                statement = getDataSource().getConnection().prepareStatement(insert);
                statement.setString(1, "DownVote");
                statement.setString(2, voteId.getAnswer().toString());
                statement.setString(3, voteId.getCredential().toString());
                rs = statement.executeQuery();

                if (rs.next()) {
                    return Optional.of(Vote.builder()
                            .id(new VoteId(AnswerId.from(rs.getString("idxAnswer"))
                                    , CredentialId.from(rs.getString("idxCredential"))))
                            .build());
                }
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
        return Stream.concat(findAllFromOneTable("UpVote").stream(),findAllFromOneTable("DownVote").stream()).collect(Collectors.toList());
    }

    private Collection<Vote> findAllFromOneTable(String table) {
        Collection<Vote>result =new ArrayList();
        var select = "SELECT * From "+table;

        try {
            var statement = getDataSource().getConnection().prepareStatement(select);
            var rs = statement.executeQuery();

            while (rs.next()) {
                Vote newA = (Vote.builder()
                        .id(new VoteId(AnswerId.from(rs.getString("idxAnswer"))
                                , CredentialId.from(rs.getString("idxCredential"))))
                        .build());
                result.add(newA);
            }
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not findAll from table "+table);
            return Collections.emptyList();
        }
        return result;
    }
}
