#!/usr/bin/groovy
package org.dfs;

enum OperatingSystem {
    Windows,
    Linux
}

class Constants {
    static final dollar = "\$"
    static final doubleQuote = '"'
    static final singleQuote = "'"
    static final backSlash = "\\"
    static final release = "Release"
    static final debug = "Debug"
}

class OperatingSystemConstants {
    static final osName = "os.name"
    static final windows = "windows"
    static final linux = "linux"
}

def OperatingSystem getOperatingSystem() {
    return System.properties[OperatingSystemConstants.osName].toLowerCase().contains(OperatingSystemConstants.windows)
        ? OperatingSystem.Windows
        : OperatingSystem.Linux;
}

def String getContributorsScript(String filter) {
    return getOperatingSystem() == OperatingSystem.Linux ? "git log --pretty='" + filter + "' | sort | uniq | tr '\\n' ','" : Constants.dollar + "data = (git log --pretty='%ce' | sort | Get-Unique); " + Constants.dollar + "data -join ';'";
}

def String getAllContributors(String filter) {
    return sh (
        script: getContributorsScript(filter),
        returnStdout: true
    ).trim()
}

def String getContributorsEmail() {
    def contributors = getAllContributors("%ce")

    contributors = contributors.replace('noreply@github.com', '')
    contributors = contributors.replace(',,', ',')

    return contributors;
}

def String getContributorsName() {
    def contributors = getAllContributors("%cn")

    contributors = contributors.replace('GitHub', '')
    contributors = contributors.replace(',,', ',')

    return contributors;
}

