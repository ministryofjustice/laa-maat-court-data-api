package gov.uk.courtdata.util;

import lombok.Getter;

public enum GitProperty {
  GIT_BRANCH("git.branch"),
  GIT_COMMIT_ID("git.commit.id"),
  GIT_COMMIT_ID_ABBREV("git.commit.id.abbrev"),
  GIT_COMMIT_USER_NAME("git.commit.user.name"),
  GIT_COMMIT_USER_EMAIL("git.commit.user.email"),
  GIT_COMMIT_MESSAGE_SHORT("git.commit.message.short"),
  GIT_COMMIT_MESSAGE_FULL("git.commit.message.full"),
  GIT_COMMIT_TIME("git.commit.time"),
  GIT_COMMIT_ID_DESCRIBE("git.commit.id.describe"),
  GIT_REMOTE_ORIGIN_URL("git.remote.origin.url"),
  GIT_TAGS("git.tags"),
  GIT_CLOSEST_TAG_NAME("git.closest.tag.name"),
  GIT_CLOSEST_TAG_COMMIT_COUNT("git.closest.tag.commit.count"),
  GIT_TOTAL_COMMIT_COUNT("git.total.commit.count"),
  GIT_DIRTY("git.dirty"),
  GIT_BUILD_USER_NAME("git.build.user.name"),
  GIT_BUILD_USER_EMAIL("git.build.user.email"),
  GIT_BUILD_VERSION("git.build.version"),
  GIT_BUILD_HOST("git.build.host");

  @Getter
  private final String gitPropertyKey;

  GitProperty(String gitPropertyKey) {
    this.gitPropertyKey = gitPropertyKey;
  }
}
