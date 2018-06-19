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

def String getContributors(String filter) {
    def contributors = sh (
        script: getContributorsScript(filter),
        returnStdout: true
    ).trim()

    if ( contributors == null || contributors.length() == 0 )
        return ""

    return contributors;
}

def String cleanupContributors(String contributors) {
    contributors = contributors.replace(',,', ',')
    if ( contributors.endsWith(',') )
        contributors = contributors.substring(0, contributors.length() - 1)
    if ( contributors[0] == ',' )
        contributors = contributors.substring(1)

    return contributors;
}

def String getContributorsByEmail() {
    def contributors = getContributors("%ce")

    if ( contributors == null || contributors.length() == 0 )
        return ""

    contributors = contributors.replace('noreply@github.com', '')

    return cleanupContributors(contributors);
}

def String getContributorsByName() {
    def contributors = getContributors("%cn")

    if ( contributors == null || contributors.length() == 0 )
        return ""

    contributors = contributors.replace('GitHub', '')

    return contributors;
}

def String getContributorsNames() {
    def emails = getContributors("%ce")
    def nameAndEmails = getContributors("%cn|%ce")

    def map = emails.collectEntries{ [(it.toLowerCase()):""] }
    nameAndEmails.each {
        def values = "${it}".split("|")
        def name = values[0]
        def email = values[1].toLowerCase()

        def entry = emails[email]
        if ( entry == null || entry == "" || entry.length() < name.length() ) {
            map[email] = name
        }
    }
}

