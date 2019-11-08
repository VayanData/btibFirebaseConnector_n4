@echo off
setlocal EnableDelayedExpansion

GOTO:REM
---------------------------------------------------------------------------------------------------
Write the vendor.gradle file at the root of the repository.
The version number must be passed as first argument and will be printed as is.
---------------------------------------------------------------------------------------------------
:REM

cd "%~dp0\.."

echo /* > vendor.gradle
echo  * Copyright 2018 BTIB, All Rights Reserved. > vendor.gradle
echo  */ > vendor.gradle
echo /* Vendor Configuration File */ > vendor.gradle
echo group = "BTIB" >> vendor.gradle
echo. >> vendor.gradle
echo def moduleVersion = '%1' >> vendor.gradle
echo def moduleVersionPatch = [:] >> vendor.gradle
echo version = "${moduleVersion}${moduleVersionPatch.get(project.name, '')}" >> vendor.gradle
