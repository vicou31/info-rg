#!/bin/sh
# This script provides functions to .gitlab-ci.yml such as logging in to a GitLab registry or tagging Docker images

# Log in to the GitLab registry
docker_login() {
  # The user is called "gitlab-ci-token"  ci_registry is registry.gitlab.com
  command docker login -u gitlab-ci-token -p HCt17cPAE1hJf_UDmwPs $CI_REGISTRY
}

# Executes a Docker command after making sure we're logged in to our Docker registry
docker() {
  docker_login
  # $@ contains the argument list
  command docker "$@"
}

# Gets the image of this repository by its old tag (first parameter), gives it a new tag (second parameter) and pushes it back to the registry
docker_retag_and_push() {
  oldTag="$1"
  newTag="$2"
  echo "Pulling docker image with tag $oldTag"
  docker pull $CI_REGISTRY_IMAGE:$oldTag >/dev/null

  echo "Tagging docker image with new tag $newTag (old tag was $oldTag)"
  docker tag $CI_REGISTRY_IMAGE:$oldTag $CI_REGISTRY_IMAGE:$newTag

  echo "Pushing docker image with tag $newTag"
  docker push $CI_REGISTRY_IMAGE:$newTag
}
