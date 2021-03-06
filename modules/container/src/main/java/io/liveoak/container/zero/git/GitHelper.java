package io.liveoak.container.zero.git;

import java.io.File;

import io.liveoak.jgit.GitHandler;
import io.liveoak.spi.security.UserProfile;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

/**
 * @author Ken Finnigan
 */
public interface GitHelper {

    static Git initRepo(File directory) throws GitAPIException {
        return Git.init()
                .setDirectory(directory)
                .call();
    }

    static void addAllAndCommit(Git git, UserProfile user, String commitMsg) throws GitAPIException {
        AddCommand addCommand = git.add()
            .addFilepattern(".")
            .addFilepattern("application.json");

        CommitCommand commitCmd = git.commit();

        if (user != null && user.name() != null && user.email() != null) {
            commitCmd.setCommitter(user.name(), user.email());
        }
        if (commitMsg != null) {
            commitCmd.setMessage(commitMsg);
        }

        GitHandler.commit(() -> {
            addCommand.call();
            return commitCmd.call();
        });
    }
}
