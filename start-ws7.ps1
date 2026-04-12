param(
    [string]$ProjectRoot = 'C:\Users\s1812\Downloads\javaWeb\ws7_240434797',
    [string]$JavaHome = 'C:\Program Files\Java\jdk-21.0.6',
    [string]$MavenHome = 'C:\Users\s1812\.maven\maven-3.9.14',
    [string]$GlassFishHome = 'C:\Users\s1812\Downloads\javaWeb\glassfish-8.0.1\glassfish8',
    [string]$Domain = 'domain1',
    [string]$ContextRoot = 'ws7'
)

$ErrorActionPreference = 'Stop'

if (-not (Test-Path $ProjectRoot)) {
    throw "Project folder not found: $ProjectRoot"
}

$asadmin = Join-Path $GlassFishHome 'bin\asadmin.bat'
if (-not (Test-Path $asadmin)) {
    throw "GlassFish asadmin not found: $asadmin"
}

$env:JAVA_HOME = $JavaHome
$env:MAVEN_HOME = $MavenHome
$env:Path = "$JavaHome\bin;$MavenHome\bin;$env:Path"

Set-Location $ProjectRoot

$domains = & $asadmin list-domains 2>&1
if ($domains -notmatch "$Domain running") {
    & $asadmin start-domain $Domain
}

mvn clean package

$warFiles = Get-ChildItem (Join-Path $ProjectRoot 'target') -Filter '*.war' -File | Sort-Object LastWriteTime -Descending
if (-not $warFiles) {
    throw 'No WAR found under target folder.'
}

$warPath = $warFiles[0].FullName
$appName = [System.IO.Path]::GetFileNameWithoutExtension($warPath)

& $asadmin undeploy $appName 2>$null
& $asadmin deploy --force=true --name $appName --contextroot $ContextRoot $warPath

Write-Host ''
Write-Host 'Done.'
Write-Host "WAR: $warPath"
Write-Host "App Name: $appName"
Write-Host "App URL: http://localhost:8080/$ContextRoot/"
