#!/usr/bin/groovy
package org.dfs;

class Constants {
    static final dollar = "\$"
    static final doubleQuote = '"'
    static final singleQuote = "'"
    static final backSlash = "\\"
    static final release = "Release"
    static final debug = "Debug"
}

def String getContributorsScript() {
    return "git log --pretty='{\"name\": \"%cn\", \"email\": \"%ce\"},' | sort | uniq | grep -v -E '@github.com';"
}

def String getContributors() {
    def contributors = sh (
        script: getContributorsScript(),
        returnStdout: true
    ).trim()

    if ( contributors == null || contributors.length() == 0 )
        return "{}"

    contributors = "[" + contributors + "]"

    echo contributors

    return contributors;
}

def Object getContributorNames() {
    def jsonSlurper = new JsonSlurper()
    def contributorsJson = getContributors();

    def object = jsonSlurper.parseText(contributorsJson)
    return object;
}

