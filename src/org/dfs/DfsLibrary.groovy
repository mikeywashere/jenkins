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

    return contributors;
}

def String getContributorNames() {
    def contributorsJson = getContributors();
    def object = new groovy.json.JsonSlurperClassic().parseText(contributorsJson)

    def list = object.collect { "${it.name}" }
    list.unique()
    def text = list.join(",")
    text = text.replace(" ", "%20").replace(",", "%2c") // try encoding
    return text;
}

def findFiles(String fileSpec) {
    def value = sh (
        script: "find ./ -type f -name '${fileSpec}'",
        returnStdout: true
    ).trim()
    
    def values = value.tokenize("\r\n")
    return values;
}