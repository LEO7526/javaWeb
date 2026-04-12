$ErrorActionPreference = 'Stop'

$javaHome = 'C:\Program Files\Java\jdk-21.0.6'
$mavenHome = 'C:\Users\s1812\.maven\maven-3.9.14'
$glassFishHome = 'C:\Users\s1812\Downloads\javaWeb\glassfish-8.0.1\glassfish8'
$projectRoot = 'C:\Users\s1812\Downloads\javaWeb\ws4-1_240434797'
$asadmin = Join-Path $glassFishHome 'bin\asadmin.bat'

$env:JAVA_HOME = $javaHome
$env:MAVEN_HOME = $mavenHome
$env:Path = "$javaHome\bin;$mavenHome\bin;$env:Path"

Set-Location $projectRoot

if (-not (Test-Path $asadmin)) {
    throw "GlassFish asadmin not found: $asadmin"
}

$domains = & $asadmin list-domains 2>&1
if ($domains -notmatch 'domain1 running') {
    & $asadmin start-domain domain1
}

mvn clean package

$warPath = Join-Path $projectRoot 'target\ws4_240434797-1.0-SNAPSHOT.war'
if (-not (Test-Path $warPath)) {
    throw "WAR not found: $warPath"
}

& $asadmin deploy --force=true $warPath

Write-Host ''
Write-Host 'Done.'
Write-Host 'App URL: http://localhost:8080/ws4_240434797-1.0-SNAPSHOT/'