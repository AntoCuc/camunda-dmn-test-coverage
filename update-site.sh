#!/bin/bash

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

    echo -e "Updating website..."

    cp -R build/target/site $HOME/site
    cd $HOME

    git config --global user.email "travis@travis-ci.org"
    git config --global user.name "travis-ci"
    git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/AntoCuc/camunda-dmn-test-coverage gh-pages

    cd gh-pages
    git rm -rf ./site
    cp -Rf $HOME/site ./
    git add -f .
    git commit -m "Updated site for build $TRAVIS_BUILD_NUMBER"
    git push -fq origin gh-pages

    echo -e "Done.\n"

fi