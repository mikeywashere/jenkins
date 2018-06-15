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

def String getContributorsScript() {
    return getOperatingSystem() == OperatingSystem.Windows ? "git log --pretty='%ce' | sort | uniq | tr '\\n' ','" : Constants.dollar + "data = (git log --pretty='%ce' | sort | Get-Unique); " + Constants.dollar + "data -join ';'";
}

def String getContributors() {
    return sh (
        script: getContributorsScript(),
        returnStdout: true
    ).trim()
}
