Set-PSDebug -Trace 1
$ErrorActionPreference = 'Stop'
mvn org.jacoco:jacoco-maven-plugin:prepare-agent verify org.jacoco:jacoco-maven-plugin:report -Dheadless
Invoke-Expression target/site/jacoco/index.html