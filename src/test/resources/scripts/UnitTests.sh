#!/bin/bash
set -ex
mvn org.jacoco:jacoco-maven-plugin:prepare-agent verify org.jacoco:jacoco-maven-plugin:report -Dheadless
open target/site/jacoco/index.html