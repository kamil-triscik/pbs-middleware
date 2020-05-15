#!/bin/bash

version=(`echo $(mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -Dexpression=project.version -q -DforceStdout) | rev | cut -d"-" -f2-  | rev`)
major=0
minor=0
build=0

# break down the version number into it's components
regex="([0-9]+).([0-9]+).([0-9]+)"
if [[ $version =~ $regex ]]; then
  major="${BASH_REMATCH[1]}"
  minor="${BASH_REMATCH[2]}"
  build="${BASH_REMATCH[3]}"
fi

# check paramater to see which number to increment
if [[ "$1" == "feature" ]]; then
  minor=$(echo $minor + 1 | bc)
elif [[ "$1" == "bug" ]]; then
  build=$(echo $build - 1 | bc)
elif [[ "$1" == "major" ]]; then
  major=$(echo $major+1 | bc)
else
#  echo "usage: ./version.sh version_number [major/feature/bug]"
#  exit -1
  build=$((build - 1))
fi

# echo the new version number
echo "${major}.${minor}.${build}"